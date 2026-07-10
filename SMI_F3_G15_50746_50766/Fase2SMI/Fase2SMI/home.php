<?php
session_start();

// Verifica se o user está logado
if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit();
}

$username = htmlspecialchars($_SESSION['username']);
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Wiki SMI - Página Inicial</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f0f2f5;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        header {
            background-color: #4CAF50;
            padding: 20px;
            text-align: center;
            color: white;
            font-size: 24px;
            font-weight: bold;
        }
        .container {
            flex: 1;
            padding: 30px;
            max-width: 800px;
            margin: 0 auto;
            text-align: center;
        }
        h2 {
            color: #333;
            margin-bottom: 20px;
        }
        a.button {
            display: inline-block;
            padding: 12px 20px;
            margin: 10px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-size: 16px;
        }
        a.button:hover {
            background-color: #45a049;
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

    <header>
        <div class="header-content">
            <h1>Wiki SMI</h1>
        </div>
    </header>

    <div class="container">
        <h2>Bem-vindo, <?= $username ?>!</h2>
        <p>Este é o sistema Wiki SMI. Escolha uma opção para começar:</p>

        <a class="button" href="pages.php">📚 Ver Páginas Wiki</a>
        <a class="button" href="create_page.php">✍️ Criar Nova Página</a>
        <a class="button" href="profile.php">Profile</a>
        <a class="button" href="logout.php">🚪 Logout</a>
    </div>

    <footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

</body>
</html>
