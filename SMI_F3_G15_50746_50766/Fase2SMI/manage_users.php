<?php
session_start();
require_once 'db.php';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'admin') {
    header("Location: login.php");
    exit();
}

$conn = db_connect();
$current_user_id = $_SESSION['user_id'];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['user_id'], $_POST['role'])) {
    $user_id = intval($_POST['user_id']);
    $role = $_POST['role'];

    $valid_roles = ['viewer', 'editor', 'admin'];
    if (!in_array($role, $valid_roles)) {
        echo json_encode(['success' => false, 'error' => 'Role inválido.']);
        exit();
    }

    $stmt = $conn->prepare("UPDATE User SET role = ? WHERE id = ?");
    $stmt->bind_param("si", $role, $user_id);
    if ($stmt->execute()) {
        echo json_encode(['success' => true]);
    } else {
        echo json_encode(['success' => false, 'error' => 'Erro ao atualizar.']);
    }
    exit();
}

// Encontrar todos os utilizadores, exceto o próprio
$stmt = $conn->prepare("SELECT id, username, email, role FROM User WHERE id != ? ORDER BY username ASC");
$stmt->bind_param("i", $current_user_id);
$stmt->execute();
$users = $stmt->get_result()->fetch_all(MYSQLI_ASSOC);
?>

<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8">
    <title>Gerir Utilizadores - Wiki SMI</title>
    <style>
    * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
    }
    
    body {
        font-family: Arial, sans-serif;
        background: #f0f2f5;
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }

    main {
        flex: 1;
        padding: 40px 20px;
        max-width: 1200px;
        width: 100%;
        margin: 0 auto;
    }

    h1 {
        text-align: center;
        margin-bottom: 30px;
        color: #333;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        background: white;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        border-radius: 8px;
        overflow: hidden;
    }

    th, td {
        padding: 15px;
        text-align: left;
        border-bottom: 1px solid #ddd;
    }

    th {
        background-color: #4CAF50;
        color: white;
    }

    select {
        padding: 6px 10px;
        border-radius: 4px;
        border: 1px solid #ccc;
        background-color: #fff;
        font-size: 14px;
    }

    select:focus {
        outline: none;
        border-color: #4CAF50;
    }

    </style>
</head>

<body>
    <link rel="stylesheet" href="styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <header>
    <div class="header-content">
            <a href="javascript:history.back()" class="back-button" title="Voltar">
                        <i class="fas fa-arrow-left"></i>
            </a>

        <h1>Wiki SMI</h1>
    </div>
    </header>

    <main>
        <h1>Gestão de Utilizadores</h1>
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($users as $u): ?>
                <tr data-user-id="<?= $u['id'] ?>">
                    <td><?= htmlspecialchars($u['username']) ?></td>
                    <td><?= htmlspecialchars($u['email']) ?></td>
                    <td>
                        <select onchange="updateRole(this)" data-user-id="<?= $u['id'] ?>">
                            <option value="viewer" <?= $u['role'] === 'viewer' ? 'selected' : '' ?>>viewer</option>
                            <option value="editor" <?= $u['role'] === 'editor' ? 'selected' : '' ?>>editor</option>
                            <option value="admin" <?= $u['role'] === 'admin' ? 'selected' : '' ?>>admin</option>
                        </select>
                    </td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    </main>

    <footer>
        &copy; <?= date('Y') ?> Wiki SMI - Miguel Alcobia e Tomás Salvador
    </footer>

    <script>
    function updateRole(select) {
        const userId = select.dataset.userId;
        const newRole = select.value;

        fetch("manage_users.php", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `user_id=${userId}&role=${encodeURIComponent(newRole)}`
            })
            .then(res => res.json())
            .then(data => {
                if (!data.success) {
                    alert("Erro: " + (data.error || 'Erro desconhecido.'));
                }
            })
            .catch(() => {
                alert("Erro de rede.");
            });
    }
    </script>
</body>

</html>