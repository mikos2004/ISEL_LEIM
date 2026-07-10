<?php
session_start();
require_once 'db.php';
$conn = db_connect();

// Mensagens de erro
$msg = '';

if($_SERVER['REQUEST_METHOD'] == 'POST'){
    // Verificar CAPTCHA primeiro
    if(empty($_POST['captcha']) || empty($_SESSION['captcha']) || 
       strtolower(trim($_POST['captcha'])) !== strtolower($_SESSION['captcha'])){
        $msg = "Código CAPTCHA incorreto. Por favor, tente novamente.";
        echo strtolower($_SESSION['captcha']);
        unset($_SESSION['captcha']); // Força gerar novo CAPTCHA
    } else {
        // CAPTCHA válido, processar o registro
        $email = $_POST['email'];
        $username = $_POST['username'];
        $password = $_POST['password'];
        $confirm_password = $_POST['confirm_password'];

        if(!empty($email) && !empty($username) && !empty($password) && !empty($confirm_password)){
            if($password === $confirm_password){
                // Verifica se o usuário já existe
                $stmnt = $conn->prepare("SELECT * FROM User WHERE email=?");
                $stmnt->bind_param("s", $email);
                $stmnt->execute();
                $result = $stmnt->get_result();
                
                if($result->num_rows == 0){
                    // Hash da senha
                    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

                    // Insere o novo usuário
                    $stmnt = $conn->prepare("INSERT INTO User (username, password_hash, email) VALUES (?,?,?)");
                    $stmnt->bind_param("sss", $username, $hashed_password, $email);
                    if($stmnt->execute()){
                        // Envia email de confirmação com PHPMailer
                        $msg = "Registro efetuado! Verifique seu email para confirmar.";
                        // Limpa o CAPTCHA da sessão após sucesso
                        unset($_SESSION['captcha']);
                        header("Location: login.php");
                        exit();
                    } else {
                        $msg = "Erro ao registrar usuário.";
                    }
                } else {
                    $msg = "Usuário já existe.";
                }
            } else {
                $msg = "As senhas não coincidem.";
            }
        } else {
            $msg = "Por favor, preencha todos os campos.";
        }
    }
}
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Wiki SMI - Registo</title>
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
        h2 {
            margin-bottom: 10px; color: #333; 
        }
        form { 
            display: flex; flex-direction: column;
        }
        input[type="email"],
        input[type="text"],
        input[type="password"] {
            padding: 10px;
            margin: 6px 0;
            border: 1px solid #ddd;
            border-radius: 8px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 12px;
            border: none;
            border-radius: 8px;
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
        .captcha-container {
            margin: 15px 0;
        }
        .captcha-image {
            border: 1px solid #ddd;
            padding: 5px;
            background: #f9f9f9;
            margin-bottom: 5px;
            border-radius: 8px;
        }
        .refresh-captcha {
            cursor: pointer;
            color: blue;
            font-size: 12px;
            display: block;
        }
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
            <h2>Criar Nova Conta</h2>
            <form method="POST" action="">
                <input type="email" name="email" placeholder="Email" required>
                <input type="text" name="username" placeholder="Nome de utilizador" required>
                <input type="password" name="password" placeholder="Password" required>
                <input type="password" name="confirm_password" placeholder="Confirmar Password" required>

                <div class="captcha-container">
                    <label>Digite o código abaixo:</label><br>
                    <img src="captcha.php" alt="CAPTCHA" class="captcha-image" id="captcha-image">
                    <span class="refresh-captcha" onclick="refreshCaptcha()">↻ Atualizar Código</span><br>
                    <input type="text" name="captcha" placeholder="Código CAPTCHA" required>
                </div>

                <input type="submit" value="Registrar">
            </form>

            <?php if (!empty($msg)): ?>
                <p class="error"><?= htmlspecialchars($msg) ?></p>
            <?php endif; ?>

            <a class="link" href="login.php">Já tem conta? Faça login</a>
        </div>
    </div>

    <footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

    <script>
        function refreshCaptcha() {
            var captchaImage = document.getElementById('captcha-image');
            captchaImage.src = 'captcha.php?' + new Date().getTime();
        }
    </script>
</body>
</html>
