<?php
session_start();
require_once 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$conn = db_connect();
$user_id = $_SESSION['user_id'];
$success = '';
$error = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $current_password = $_POST['current_password'] ?? '';
    $new_password = $_POST['new_password'] ?? '';
    $confirm_password = $_POST['confirm_password'] ?? '';

    if (empty($current_password) || empty($new_password) || empty($confirm_password)) {
        $error = "Todos os campos são obrigatórios.";
    } elseif ($new_password !== $confirm_password) {
        $error = "A nova palavra-passe e a confirmação não coincidem.";
    } elseif (strlen($new_password) < 6) {
        $error = "A nova palavra-passe deve ter pelo menos 6 caracteres.";
    } else {
        $stmt = $conn->prepare("SELECT password_hash FROM User WHERE id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $stmt->bind_result($password_hash);
        $stmt->fetch();
        $stmt->close();

        if (!password_verify($current_password, $password_hash)) {
            $error = "A palavra-passe atual está incorreta.";
        } else {
            $new_hash = password_hash($new_password, PASSWORD_DEFAULT);
            $stmt = $conn->prepare("UPDATE User SET password_hash = ? WHERE id = ?");
            $stmt->bind_param("si", $new_hash, $user_id);
            if ($stmt->execute()) {
                $success = "Palavra-passe atualizada com sucesso!";
            } else {
                $error = "Erro ao atualizar a palavra-passe.";
            }
            $stmt->close();
        }
    }
}
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Alterar Palavra-passe</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            background: #f0f2f5;
            font-family: Arial, sans-serif;
            margin: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        header, footer {
            background-color: #4CAF50;
            color: white;
            text-align: center;
            padding: 15px 0;
        }

        .container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 40px 20px;
        }

        .form-box {
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
        }

        form input {
            width: 100%;
            padding: 10px;
            margin: 10px 0 20px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        form button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            border: none;
            border-radius: 6px;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        form button:hover {
            background-color: #45a049;
        }

        .message {
            text-align: center;
            color: red;
            margin-bottom: 15px;
        }

        .message.success {
            color: green;
        }
    </style>
</head>
<body>

<header>Alterar Palavra-passe</header>

<div class="container">
    <div class="form-box">
        <a href="home.php" class="btn">← Voltar</a>
        <h2>Atualizar Palavra-passe</h2>
        <?php if ($error): ?>
            <div class="message"><?= htmlspecialchars($error) ?></div>
        <?php elseif ($success): ?>
            <div class="message success"><?= htmlspecialchars($success) ?></div>
        <?php endif; ?>

        <form method="POST">
            <input type="password" name="current_password" placeholder="Palavra-passe atual" required>
            <input type="password" name="new_password" placeholder="Nova palavra-passe" required>
            <input type="password" name="confirm_password" placeholder="Confirmar nova palavra-passe" required>
            <button type="submit">Alterar</button>
        </form>
    </div>
</div>

<footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

</body>
</html>
