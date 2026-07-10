<?php
session_start();
require_once 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$conn = db_connect();
$user_id = $_SESSION['user_id'];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['field'], $_POST['value'])) {
    $allowed_fields = ['username', 'email'];
    $field = $_POST['field'];
    $value = trim($_POST['value']);

    if (in_array($field, $allowed_fields)) {
        $stmt = $conn->prepare("UPDATE User SET $field = ? WHERE id = ?");
        $stmt->bind_param("si", $value, $user_id);
        if ($stmt->execute()) {
            echo json_encode(['success' => true]);
        } else {
            echo json_encode(['success' => false, 'error' => 'Erro ao atualizar.']);
        }
        exit();
    }
}

// Obter os dados do utilizador
$stmt = $conn->prepare("SELECT username, email, role FROM User WHERE id = ?");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$user = $stmt->get_result()->fetch_assoc();
$_SESSION['role'] = $user['role'];
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Perfil - Wiki SMI</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: #f5f7fa;
            margin: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        header {
            background-color: #4CAF50;
            color: white;
            padding: 20px;
            text-align: center;
            font-size: 26px;
            font-weight: bold;
        }

        .container {
            flex: 1;
            display: flex;
            justify-content: center;
            padding: 50px 20px;
        }

        .profile-box {
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
            padding: 40px;
            width: 100%;
            max-width: 500px;
            transition: all 0.3s ease-in-out;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #333;
        }

        .field {
            margin-bottom: 25px;
        }

        .field label {
            font-weight: 600;
            margin-bottom: 6px;
            display: block;
            color: #444;
        }

        .display-value {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .text {
            font-size: 16px;
            color: #222;
        }

        .field input {
            width: 100%;
            padding: 10px;
            margin-top: 6px;
            border-radius: 6px;
            border: 1px solid #ccc;
            display: none;
            font-size: 15px;
        }

        .edit-icon {
            cursor: pointer;
            color: #4CAF50;
            font-size: 18px;
            margin-left: 10px;
        }

        .divider {
            height: 1px;
            background-color: #ddd;
            margin: 30px 0;
        }

        .button-group {
            display: flex;
            flex-direction: column;
            gap: 15px;
            margin-top: 30px;
        }

        .button-group button {
            padding: 12px;
            border: none;
            border-radius: 6px;
            background-color: #4CAF50;
            color: white;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .button-group button:hover {
            background-color: #3e9e44;
        }

        .header-bar {
            position: relative;
            text-align: center;
            margin-bottom: 30px;
        }

        .header-bar h2 {
            font-size: 24px;
            color: #333;
            margin: 0;
        }

        .btn-back {
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            text-decoration: none;
            color: #4CAF50;
            font-weight: bold;
            font-size: 16px;
            padding-left: 5px;
        }

        footer {
            background-color: #4CAF50;
            color: white;
            text-align: center;
            padding: 15px 0;
            font-size: 14px;
            margin-top: auto;
        }
    </style>
</head>
<body>

<header>Wiki SMI</header>

<div class="container">
    <div class="profile-box">
        <div class="header-bar">
            <a href="home.php" class="btn-back">←</a>
            <h2>Perfil</h2>
        </div>
        <div class="field" data-field="username">
            <label>Nome de Utilizador</label>
            <div class="display-value">
                <span class="text"><?= htmlspecialchars($user['username']) ?></span>
                <span class="edit-icon" onclick="enableEdit(this)">✏️</span>
            </div>
            <input type="text" value="<?= htmlspecialchars($user['username']) ?>" onblur="saveField(this)">
        </div>

        <div class="field" data-field="email">
            <label>Email</label>
            <div class="display-value">
                <span class="text"><?= htmlspecialchars($user['email']) ?></span>
                <span class="edit-icon" onclick="enableEdit(this)">✏️</span>
            </div>
            <input type="text" value="<?= htmlspecialchars($user['email']) ?>" onblur="saveField(this)">
        </div>

        <div class="field">
            <label>Role</label>
            <div class="display-value">
                <span class="text"><?= htmlspecialchars($user['role']) ?></span>
            </div>
        </div>

        <div class="divider"></div>

        <div class="button-group">
            <?php if ($_SESSION['role'] === 'admin'): ?>
                <button onclick="window.location.href='manage_users.php'">Gerir Utilizadores</button>
            <?php endif; ?>
            <button onclick="window.location.href='change_password.php'">Alterar Palavra-passe</button>
        </div>
    </div>
</div>

<footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

<script>
function enableEdit(icon) {
    const field = icon.closest('.field');
    field.querySelector('.display-value').style.display = 'none';
    const input = field.querySelector('input');
    input.style.display = 'block';
    input.focus();
}

function saveField(input) {
    const field = input.closest('.field');
    const fieldName = field.dataset.field;
    const newValue = input.value;

    fetch("profile.php", {
        method: "POST",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `field=${fieldName}&value=${encodeURIComponent(newValue)}`
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            field.querySelector('.text').textContent = newValue;
        } else {
            alert("Erro ao atualizar: " + (data.error || 'Desconhecido'));
        }
        field.querySelector('.display-value').style.display = 'flex';
        input.style.display = 'none';
    })
    .catch(() => {
        alert("Erro de rede.");
        field.querySelector('.display-value').style.display = 'flex';
        input.style.display = 'none';
    });
}
</script>

</body>
</html>
