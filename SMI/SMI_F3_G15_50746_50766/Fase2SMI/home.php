<?php
session_start();
require_once 'db.php';

if (!isset($_SESSION['username'])) {
    header("Location: login.php");
    exit();
}

$conn = db_connect();

$username = htmlspecialchars($_SESSION['username']);
$user_id = $_SESSION['user_id'] ?? null;
$user_role = null;

// Obter role do utilizador
if ($user_id) {
    $stmt = $conn->prepare("SELECT role FROM User WHERE id = ?");
    $stmt->bind_param("i", $user_id);
    $stmt->execute();
    $result = $stmt->get_result();
    if ($result->num_rows > 0) {
        $user_role = $result->fetch_assoc()['role'];
    }
    $stmt->close();
}

// Procurar os 3 artigos mais recentes
$query = "
    SELECT p.id, p.title, p.created_at, p.updated_at, p.status, u.username AS author
    FROM Page p
    LEFT JOIN User u ON p.author_id = u.id
    WHERE p.status = 'público' OR p.author_id = ? OR ? = 'admin'
    ORDER BY p.updated_at DESC
    LIMIT 3
";
$stmt = $conn->prepare($query);
$stmt->bind_param("is", $user_id, $user_role);
$stmt->execute();
$destaques = $stmt->get_result();
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Wiki SMI - Página Inicial</title>
    <style>
        * {
            box-sizing: border-box;
        }
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }


        .main {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 40px;
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .sidebar, .content {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        .sidebar h2 {
            font-size: 1.5em;
            margin-bottom: 20px;
            color: #333;
        }

        .button {
            display: block;
            background-color: #4CAF50;
            color: white;
            text-align: center;
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
            transition: background-color 0.3s;
        }

        .button:hover {
            background-color: #45a049;
        }

        .content h2 {
            font-size: 1.6em;
            margin-bottom: 20px;
            color: #333;
        }

        .article {
            border-bottom: 1px solid #eee;
            padding: 15px 0;
        }

        .article:last-child {
            border-bottom: none;
        }

        .article h3 {
            margin: 0;
            font-size: 1.2em;
            color: #4CAF50;
        }

        .article p {
            margin: 8px 0 0;
            color: #666;
            font-size: 0.9em;
        }

        .article a {
            color: #4CAF50;
            text-decoration: none;
            font-weight: bold;
        }

        .article a:hover {
            text-decoration: underline;
        }

    </style>
</head>
<body>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<link rel="stylesheet" href="styles.css">
    <header>
    <div class="header-content">
        <h1>Wiki SMI</h1>
    </div>
    </header>

<div class="main">
    <div class="sidebar">
        <h2>Bem Vindo(a), <?= $username ?></h2>
        <a class="button" href="pages.php">Ver Páginas Wiki</a>
        <a class="button" href="create_page.php">Criar Nova Página</a>
        <a class="button" href="profile.php">Perfil</a>
        <a class="button" href="logout.php">Logout</a>
    </div>

    <div class="content">
        <h2>Destaques Recentes</h2>
        <?php if ($destaques->num_rows > 0): ?>
            <?php while ($page = $destaques->fetch_assoc()): ?>
                <div class="article">
                    <h3><a href="view_page.php?id=<?= $page['id'] ?>"><?= htmlspecialchars($page['title']) ?></a></h3>
                    <p>Por <?= htmlspecialchars($page['author']) ?> em <?= date('d/m/Y', strtotime($page['created_at'])) ?></p>
                </div>
            <?php endwhile; ?>
        <?php else: ?>
            <p>Nenhum artigo disponível.</p>
        <?php endif; ?>
    </div>
</div>

<footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

</body>
</html>
