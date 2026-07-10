<?php
session_start();
require_once 'db.php'; // Conexão com a BD
$conn = db_connect();

$msg = '';

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $password = $_POST['password'];

    // Verifica se ambos foram preenchidos
    if (!empty($email) && !empty($password)) {
        $stmnt = $conn->prepare("SELECT * FROM User WHERE email=?");
        $stmnt->bind_param("s", $email);
        $stmnt->execute();
        $result = $stmnt->get_result();

        if ($result->num_rows == 1) {
            $user = $result->fetch_assoc();
            // Verifica a senha
            if (password_verify($password, $user['password_hash'])) {
                // Autenticação com sucesso
                $_SESSION['username'] = $user['username'];
                $_SESSION['user_id'] = $user['id'];

                header("Location: home.php");
                exit();
            } else {
                $msg = "Senha incorreta.";
            }
        } else {
            $msg = "Usuário não encontrado.";
        }
    } else {
        $msg = "Por favor, preencha todos os campos.";
    }
}
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Wiki SMI - Login</title>
    <style>
        * { box-sizing: border-box; font-family: Arial, sans-serif; margin: 0; padding: 0; }

        body {
            background: #f0f2f5;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }

        .container {
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .box {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }

        h2 { margin-bottom: 20px; color: #333; }

        form { display: flex; flex-direction: column; }

        input[type="text"],
        input[type="password"] {
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ddd;
            border-radius: 8px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 8px;
            margin-top: 10px;
            cursor: pointer;
            font-size: 16px;
        }

        input[type="submit"]:hover { background-color: #45a049; }

        .link {
            margin-top: 15px;
            display: block;
            color: #4CAF50;
            text-decoration: none;
        }

        .link:hover { text-decoration: underline; }

        .error {
            color: red;
            margin-top: 10px;
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

    <div class="container">
        <div class="box">
            <h2>Entrar na Sua Conta</h2>
            <form method="POST">
                <input type="text" name="email" placeholder="Email" required>
                <input type="password" name="password" placeholder="Password" required>
                <input type="submit" value="Entrar">
            </form>

            <?php if (isset($msg)): ?>
                <p class="error"><?= htmlspecialchars($msg) ?></p>
            <?php endif; ?>

            <a class="link" href="register.php">Não tem conta? Registre-se</a>
        </div>
    </div>

    <footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

</body>
</html>
