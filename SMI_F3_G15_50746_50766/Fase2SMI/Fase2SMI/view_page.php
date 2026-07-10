<?php
session_start();
require_once 'db.php';

if (!isset($_GET['id'])) {
    header("Location: home.php");
    exit();
}

$page_id = (int)$_GET['id'];
$conn = db_connect();

// Obter a página principal
$page_stmt = $conn->prepare("
    SELECT p.*, u.username, c.name as category_name 
    FROM Page p
    LEFT JOIN User u ON p.author_id = u.id
    LEFT JOIN Category c ON p.category_id = c.id
    WHERE p.id = ?
");
$page_stmt->bind_param("i", $page_id);
$page_stmt->execute();
$page_result = $page_stmt->get_result();

if ($page_result->num_rows === 0) {
    header("Location: home.php?error=Página não encontrada");
    exit();
}

$page = $page_result->fetch_assoc();
$page_stmt->close();

// Obter histórico de versões
$history_stmt = $conn->prepare("
    SELECT ph.*, u.username 
    FROM PageHistory ph
    JOIN User u ON ph.author_id = u.id
    WHERE ph.page_id = ?
    ORDER BY ph.created_at DESC
");
$history_stmt->bind_param("i", $page_id);
$history_stmt->execute();
$history = $history_stmt->get_result();
$history_stmt->close();

// Verificar permissões do usuário
$can_edit = false;
$can_delete = false;

if (isset($_SESSION['user_id'])) {
    $user_id = $_SESSION['user_id'];
    
    // Verificar se é o autor ou tem permissões especiais
    if ($page['author_id'] == $user_id) {
        $can_edit = true;
        $can_delete = true;
    } else {
        $perm_stmt = $conn->prepare("
            SELECT permission_level FROM Permission 
            WHERE user_id = ? AND page_id = ?
        ");
        $perm_stmt->bind_param("ii", $user_id, $page_id);
        $perm_stmt->execute();
        $perm_result = $perm_stmt->get_result();
        
        if ($perm_result->num_rows > 0) {
            $perm = $perm_result->fetch_assoc();
            $can_edit = in_array($perm['permission_level'], ['edição', 'administração']);
            $can_delete = ($perm['permission_level'] === 'administração');
        }
        
        $perm_stmt->close();
    }
}

// Contar visualizações (opcional)
/*$view_stmt = $conn->prepare("
    INSERT INTO PageViews (page_id, viewed_at) 
    VALUES (?, NOW())
");
$view_stmt->bind_param("i", $page_id);
$view_stmt->execute();
$view_stmt->close();*/
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?= htmlspecialchars($page['title']) ?> - Wiki SMI</title>
    <style>
        * { box-sizing: border-box; font-family: Arial, sans-serif; margin: 0; padding:0 }
        body { 
            background: #f0f2f5; 
            display: flex;
            flex-direction: column;
            min-height: 100vh;
        }
        header { background-color: #4CAF50; padding: 20px; color: white; }
        .container { max-width: 1000px; margin: 20px auto; padding: 20px; background: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); flex: 1;  width: 95%; }
        h1 { color: #333; margin-bottom: 10px; }
        .page-meta { color: #666; margin-bottom: 20px; font-size: 0.9em; }
        .page-content { line-height: 1.6; margin-bottom: 30px; margin: 15px }
        .actions { margin-bottom: 20px; }
        .btn { display: inline-block; padding: 8px 15px; border-radius: 4px; text-decoration: none; margin-right: 10px; }
        .btn-edit { background-color: #2196F3; color: white; }
        .btn-delete { background-color: #f44336; color: white; }
        .btn-history { background-color: #607d8b; color: white; }
        .category-badge { display: inline-block; background: #4CAF50; color: white; padding: 3px 8px; border-radius: 4px; font-size: 0.8em; }
        .history-container { margin-top: 40px; }
        .history-item { border-bottom: 1px solid #eee; padding: 10px 0; }
        .history-meta { font-size: 0.8em; color: #666; }
        .diff { background: #f9f9f9; padding: 10px; border-radius: 4px; margin-top: 5px; }
        footer {
            background-color: #4CAF50;
            color: white;
            text-align: center;
            padding: 15px 0;
            font-size: 14px;
            margin-top: auto;
        }
        code {
            font-family: Consolas,"courier new";
            color: crimson;
            background-color: #f1f1f1;
            padding: 2px;
            font-size: 105%;
        }
        .page-layout {
            display: flex;
            gap: 20px;
        }

        .sidebar {
            width: 250px;
            position: sticky;
            top: 20px;
            align-self: flex-start;
            max-height: calc(100vh - 40px);
            overflow-y: auto;
        }

        .sidebar-content {
            background: white;
            padding: 15px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }

        .sidebar h3 {
            margin-top: 0;
            color: #4CAF50;
        }

        .sidebar ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .sidebar li {
            margin-bottom: 5px;
        }

        .sidebar a {
            color: #333;
            text-decoration: none;
            display: block;
            padding: 5px 0;
        }

        .sidebar a:hover {
            color: #4CAF50;
        }

        .sidebar .h2 {
            padding-left: 10px;
        }

        .sidebar .h3 {
            padding-left: 20px;
            font-size: 0.9em;
        }

        .main-content {
            flex: 1;
        }
    </style>
</head>
<body>
    <header>
        <div style="max-width: 1000px; margin: 0 auto;">
            <h1>Wiki SMI</h1>
        </div>
    </header>

    <div class="container">
        <div class="actions">
            <a href="home.php" class="btn">← Voltar</a>
            <?php if ($can_edit): ?>
                <a href="edit_page.php?id=<?= $page_id ?>" class="btn btn-edit">Editar</a>
            <?php endif; ?>
            <?php if ($can_delete): ?>
                <a href="delete_page.php?id=<?= $page_id ?>" class="btn btn-delete" onclick="return confirm('Tem certeza que deseja excluir esta página?')">Excluir</a>
            <?php endif; ?>
            <a href="#history" class="btn btn-history">Histórico</a>
        </div>

         <div class="page-layout">
                <aside class="sidebar" id="sidebar">
                    <div class="sidebar-content">
                        <h3>Índice</h3>
                        <ul id="toc"></ul>
                    </div>
                </aside>

                <main class="main-content">
                    <h1 id="page-title"><?= htmlspecialchars($page['title']) ?></h1>
                    
                    <div class="page-meta">
                        <div class="page-meta">
                            Criado por <?= htmlspecialchars($page['username']) ?> em 
                            <?= date('d/m/Y H:i', strtotime($page['created_at'])) ?>
                            
                            <?php if ($page['category_name']): ?>
                                <span class="category-badge"><?= htmlspecialchars($page['category_name']) ?></span>
                            <?php endif; ?>
                            
                            <?php if ($page['created_at'] != $page['updated_at']): ?>
                                <br>Última atualização em <?= date('d/m/Y H:i', strtotime($page['updated_at'])) ?>
                            <?php endif; ?>
                        </div>
                    </div>
                    
                    <div class="page-content">
                        <?= $page['content'] ?>
                    </div>

                    <div class="history-container" id="history">
                        <h2>Histórico de Alterações</h2>
                        
                        <?php while ($version = $history->fetch_assoc()): ?>
                            <div class="history-item">
                                <div class="history-meta">
                                    Versão de <?= date('d/m/Y H:i', strtotime($version['created_at'])) ?> 
                                    por <?= htmlspecialchars($version['username']) ?>
                                </div>
                                <div class="diff">
                                    <?= nl2br(htmlspecialchars(substr($version['content'], 0, 200))) ?>...
                                </div>
                                <a href="view_version.php?id=<?= $version['id'] ?>">Ver versão completa</a>
                            </div>
                        <?php endwhile; ?>
                    </div>
                </main>
            </div>
        
    </div>

    <footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Extrai os cabeçalhos do conteúdo
            const content = document.querySelector('.page-content');
            const headings = content.querySelectorAll('h1, h2, h3');
            const toc = document.getElementById('toc');
            
            // Cria o índice
            headings.forEach(heading => {
                // Cria um ID se não existir
                if (!heading.id) {
                    heading.id = heading.textContent.toLowerCase().replace(/\s+/g, '-').replace(/[^\w-]/g, '');
                }
                
                const li = document.createElement('li');
                li.className = heading.tagName.toLowerCase();
                
                const a = document.createElement('a');
                a.href = `#${heading.id}`;
                a.textContent = heading.textContent;
                
                li.appendChild(a);
                toc.appendChild(li);
            });

            // Ativa/desativa a sidebar em telas pequenas
            function handleResize() {
                if (window.innerWidth < 992) {
                    document.getElementById('sidebar').style.display = 'none';
                } else {
                    document.getElementById('sidebar').style.display = 'block';
                }
            }

            window.addEventListener('resize', handleResize);
            handleResize();
        });
    </script>
</body>
</html>