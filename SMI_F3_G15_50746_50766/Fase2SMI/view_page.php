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

$history->data_seek(0); // Voltar ao início do resultset
$first_version = $history->fetch_assoc();
$history->data_seek(0); // Reseta o ponteiro para poder usar o resultset novamente

// Verificar permissões do usuário
$can_edit = false;
$can_delete = false;

if (isset($_SESSION['user_id'])) {
    $user_id = $_SESSION['user_id'];
    
    $role_stmt = $conn->prepare("SELECT role FROM User WHERE id = ?");
    $role_stmt->bind_param("i", $user_id);
    $role_stmt->execute();
    $role_result = $role_stmt->get_result();
    
    if ($role_result->num_rows > 0) {
        $user_data = $role_result->fetch_assoc();
        $user_role = $user_data['role'];
        
        // Admin tem todas as permissões
        if ($user_role == 'admin') {
            $can_edit = true;
            $can_delete = true;
        } 
        // Editor pode editar, mas só deleta se for o autor
        else if ($user_role == 'editor') {
            $can_edit = true;
            $can_delete = ($page['author_id'] == $user_id);
        } 
        // Viewer só pode editar/apagar se for o autor
        else if ($page['author_id'] == $user_id) {
            $can_edit = true;
            $can_delete = true;
        }
    }
    $role_stmt->close();
}

?>

<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?= htmlspecialchars($page['title']) ?> - Wiki SMI</title>
    <style>
    * {
        box-sizing: border-box;
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0
    }

    body {
        background: #f0f2f5;
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }

    .main-container {
        display: flex;
        max-width: 1200px;
        margin: 20px auto;
        width: 95%;
        flex: 1;
        gap: 30px;
    }

    .sidebar {
        width: 250px;
        position: sticky;
        top: 20px;
        height: fit-content;
        max-height: calc(100vh - 40px);
        overflow-y: auto;
    }

    .sidebar h3 {
        margin-top: 0;
        color: #4CAF50;
        padding-bottom: 10px;
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
        transition: all 0.2s;
    }

    .sidebar a:hover {
        color: #4CAF50;
        padding-left: 5px;
    }

    .sidebar .h2 {
        padding-left: 10px;
        font-weight: bold;
    }

    .sidebar .h3 {
        padding-left: 20px;
        font-size: 0.9em;
    }

    .content-area {
        flex: 1;
    }

    .page-meta {
        color: #666;
        margin-bottom: 20px;
        font-size: 0.9em;
    }

    .page-content {
        line-height: 1.6;
        margin-top: 20px;
    }

    .actions {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
        flex-wrap: wrap;
    }

    .btn {
        display: inline-flex;
        align-items: center;
        padding: 8px 15px;
        border-radius: 4px;
        text-decoration: none;
        font-size: 0.9em;
        transition: all 0.2s;
        border: none;
        outline: none;
    }

    .btn i {
        margin-right: 5px;
    }

    .btn-back {
        background-color: #6c757d;
        color: white;
    }

    .btn-back:hover {
        background-color: #5a6268;
    }

    .btn-edit {
        background-color: #2196F3;
        color: white;
    }

    .btn-edit:hover {
        background-color: #0b7dda;
    }

    .btn-delete {
        background-color: #f44336;
        color: white;
    }

    .btn-delete:hover {
        background-color: #d32f2f;
    }

    .btn-history {
        background-color: #607d8b;
        color: white;
    }

    .btn-history:hover {
        background-color: #546e7a;
    }

    .category-badge {
        display: inline-block;
        background: #4CAF50;
        color: white;
        padding: 3px 8px;
        border-radius: 4px;
        font-size: 0.8em;
        margin-left: 5px;
    }

    .btn-public {
        background-color: #4CAF50;
        color: white;
    }

    .btn-public:hover {
        background-color: #3d8b40;
    }

    .btn-private {
        background-color: #ff9800;
        color: white;
    }

    .btn-private:hover {
        background-color: #e68a00;
    }

    footer {
        background-color: #4CAF50;
        color: white;
        text-align: center;
        padding: 15px 0;
        font-size: 14px;
        margin-top: auto;
    }

    code {
        font-family: Consolas, "courier new";
        color: crimson;
        background-color: #f1f1f1;
        padding: 2px;
        font-size: 105%;
    }
    </style>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>

<body>
    <link rel="stylesheet" href="styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <header>
    <div class="header-content">
            <a href="pages.php" class="back-button" title="Voltar">
    <i class="fas fa-arrow-left"></i>
</a>

        <h1>Wiki SMI</h1>
    </div>
    </header>

    <div class="main-container">
        <aside class="sidebar">
            <h3>Índice</h3>
            <ul id="toc"></ul>
        </aside>

        <main class="content-area">
            <div class="actions">
                <?php if ($can_edit): ?>
                <a href="edit_page.php?id=<?= $page_id ?>" class="btn btn-edit"><i class="fas fa-edit"></i> Editar</a>
                <?php endif; ?>
                <?php if ($can_delete): ?>
                <button id="openDeleteModal" class="btn btn-delete">
                    <i class="fas fa-trash"></i> Excluir
                </button>
                <?php endif; ?>
                <a href="view_version.php?id=<?= $first_version['id'] ?>" class="btn btn-history"><i class="fas fa-history"></i> Histórico</a>
                
                <?php if ($can_edit): ?>
                <button id="toggleStatusBtn" class="btn <?= $page['status'] === 'público' ? 'btn-public' : 'btn-private' ?>">
                    <i class="fas fa-eye<?= $page['status'] === 'privado' ? '-slash' : '' ?>"></i>
                    <?= $page['status'] === 'público' ? 'Público' : 'Privado' ?>
                </button>
                <?php endif; ?>
            </div>

            <h1 id="page-title"><?= htmlspecialchars($page['title']) ?></h1>

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

            <div class="page-content">
                <?= $page['content'] ?>
            </div>
        </main>
    </div>
    <div id="deleteModal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background-color:rgba(0,0,0,0.6); z-index:9999; justify-content:center; align-items:center;">
        <div style="background:#fff; padding:20px; border-radius:8px; width:90%; max-width:400px; text-align:center;">
            <h2>Confirmar Exclusão</h2>
            <p>Tem certeza que deseja excluir esta página?</p>
            <div style="margin-top:20px; display:flex; justify-content:space-around;">
                <button id="cancelDelete" style="padding:8px 15px; background-color:#6c757d; color:white; border:none; border-radius:4px;">Cancelar</button>
                <a href="delete_page.php?id=<?= $page_id ?>" id="confirmDelete" style="padding:8px 15px; background-color:#f44336; color:white; border:none; border-radius:4px; text-decoration:none;">Excluir</a>
            </div>
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

        document.querySelectorAll('#toc a').forEach(anchor => {
            anchor.addEventListener('click', function(e) {
                e.preventDefault();
                const targetId = this.getAttribute('href');
                const targetElement = document.querySelector(targetId);
                
                if (targetElement) {
                    window.scrollTo({
                        top: targetElement.offsetTop - 20,
                        behavior: 'smooth'
                    });
                }
            });
        });
    });

    document.getElementById('toggleStatusBtn')?.addEventListener('click', async function() {
        const btn = this;
        const pageId = <?= $page_id ?>;
        const isPublic = btn.classList.contains('btn-public');
        const newStatus = isPublic ? 'privado' : 'público';

        try {
            const response = await fetch('toggle_page_status.php', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `id=${pageId}&status=${newStatus}`
            });

            const data = await response.json();

            if (!response.ok || !data.success) {
                throw new Error(data.message || 'Erro desconhecido');
            }
            btn.classList.toggle('btn-public');
            btn.classList.toggle('btn-private');

            const icon = btn.querySelector('i');
            icon.classList.toggle('fa-eye');
            icon.classList.toggle('fa-eye-slash');

            btn.innerHTML = `<i class="fas ${isPublic ? 'fa-eye-slash' : 'fa-eye'}"></i> ${newStatus}`;

        } catch (error) {
            console.error('Erro:', error);
            alert(`Falha ao atualizar: ${error.message}`);
        }
    });

        document.getElementById('openDeleteModal')?.addEventListener('click', () => {
        document.getElementById('deleteModal').style.display = 'flex';
    });

    document.getElementById('cancelDelete')?.addEventListener('click', () => {
        document.getElementById('deleteModal').style.display = 'none';
    });
    </script>
</body>

</html>