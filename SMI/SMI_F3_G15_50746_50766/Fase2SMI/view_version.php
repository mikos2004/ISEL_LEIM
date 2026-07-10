<?php
session_start();
require_once 'db.php';

if (!isset($_GET['id']) || !is_numeric($_GET['id'])) {
    header("Location: home.php");
    exit();
}

$version_id = (int)$_GET['id'];
$conn = db_connect();

// Procurar a versão da página (PageHistory) com dados da página e utilizador autor da versão
$stmt = $conn->prepare("
    SELECT ph.*, p.title AS page_title, u.username
    FROM PageHistory ph
    JOIN Page p ON ph.page_id = p.id
    JOIN User u ON ph.author_id = u.id
    WHERE ph.id = ?
");
$stmt->bind_param("i", $version_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    header("Location: home.php?error=Versão não encontrada");
    exit();
}

$version = $result->fetch_assoc();
$stmt->close();

// Procurar histórico de versões da mesma página, ordenado da mais recente para a mais antiga
$history_stmt = $conn->prepare("
    SELECT ph.*, u.username 
    FROM PageHistory ph
    JOIN User u ON ph.author_id = u.id
    WHERE ph.page_id = ?
    ORDER BY ph.created_at DESC
");
$history_stmt->bind_param("i", $version['page_id']);
$history_stmt->execute();
$history = $history_stmt->get_result();
$history_stmt->close();

// Verificar permissões do utilizador 
$can_edit = false;
$can_delete = false;
if (isset($_SESSION['user_id'])) {
    $user_id = $_SESSION['user_id'];
    // Se utilizador for autor da página (não da versão), pode editar/excluir
    $page_stmt = $conn->prepare("SELECT author_id FROM Page WHERE id = ?");
    $page_stmt->bind_param("i", $version['page_id']);
    $page_stmt->execute();
    $page_res = $page_stmt->get_result();
    if ($page_res->num_rows > 0) {
        $page_data = $page_res->fetch_assoc();
        if ($page_data['author_id'] == $user_id) {
            $can_edit = true;
            $can_delete = true;
        }
    }
    $page_stmt->close();
}
?>

<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8" />
    <title>Versão da página: <?= htmlspecialchars($version['page_title']) ?></title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: Arial, sans-serif;
            background: #f9f9f9;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }


        main {
            max-width: 900px;
            width: 100%;
            margin: 20px auto;
            padding: 0 20px;
            flex: 1;
        }


        .version-meta {
            margin-bottom: 15px;
            color: #555;
            font-size: 0.9em;
        }

        pre.content {
            background: white;
            padding: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
            white-space: pre-wrap;
            word-wrap: break-word;
            margin-bottom: 40px;
        }

        .history-container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .history-item {
            border-bottom: 1px solid #eee;
            padding: 10px 0;
        }

        .history-meta {
            font-size: 0.8em;
            color: #666;
        }

        .diff {
            background: #f4f4f4;
            padding: 10px;
            margin-top: 5px;
            border-radius: 4px;
            font-family: monospace;
            white-space: pre-wrap;
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

        <h1>Versão da página: <?= htmlspecialchars($version['page_title']) ?></h1>
        <div class="version-meta">
            <strong>Editado por:</strong> <?= htmlspecialchars($version['username']) ?><br>
            <strong>Data da edição:</strong> <?= date('d/m/Y H:i', strtotime($version['created_at'])) ?>
        </div>

        <pre class="content"><?= htmlspecialchars($version['content']) ?></pre>

        <div class="history-container" id="history">
            <h2>Histórico de Alterações</h2>

            <?php while ($hist_version = $history->fetch_assoc()): ?>
                <div class="history-item">
                    <div class="history-meta">
                        Versão de <?= date('d/m/Y H:i', strtotime($hist_version['created_at'])) ?>
                        por <?= htmlspecialchars($hist_version['username']) ?>
                    </div>
                    <div class="diff">
                        <?= nl2br(htmlspecialchars(substr($hist_version['content'], 0, 200))) ?>...
                    </div>
                    <?php if ($hist_version['id'] == $version['id']): ?>
                        <em>Versão atual</em>
                    <?php else: ?>
                        <a href="view_version.php?id=<?= $hist_version['id'] ?>">Ver versão completa</a>
                    <?php endif; ?>
                </div>
            <?php endwhile; ?>
        </div>
    </main>

    <footer>
        &copy; <?= date('Y') ?> Wiki SMI - Miguel Alcobia e Tomás Salvador
    </footer>
</body>

</html>