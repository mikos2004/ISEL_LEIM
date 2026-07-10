<?php
session_start();
require_once 'db.php';

$conn = db_connect();
$search = $_GET['search'] ?? '';
$category_id = $_GET['category_id'] ?? null;

// Obter informações do utilizador logado
$user_id = $_SESSION['user_id'] ?? null;
$user_role = null;

if ($user_id) {
    $user_stmt = $conn->prepare("SELECT role FROM User WHERE id = ?");
    $user_stmt->bind_param("i", $user_id);
    $user_stmt->execute();
    $user_result = $user_stmt->get_result();
    if ($user_result->num_rows > 0) {
        $user_data = $user_result->fetch_assoc();
        $user_role = $user_data['role'];
    }
    $user_stmt->close();
}

// Construir a query base
$query = "
    SELECT p.id, p.title, p.created_at, p.updated_at, p.status, p.author_id,
           u.username as author, c.name as category_name
    FROM Page p
    LEFT JOIN User u ON p.author_id = u.id
    LEFT JOIN Category c ON p.category_id = c.id
    WHERE p.title LIKE ?
    AND (
        p.status = 'público' 
        OR p.author_id = ? 
        OR ? = 'admin'
    )
";

$params = ["%$search%", $user_id, $user_role];
$param_types = "sis";

// Adicionar filtro por categoria se especificado
if ($category_id && is_numeric($category_id)) {
    $query .= " AND p.category_id = ?";
    $params[] = $category_id;
    $param_types .= "i";
}
$query .= " ORDER BY p.updated_at DESC";

// Preparar e executar a query
$stmt = $conn->prepare($query);
$stmt->bind_param($param_types, ...$params);
$stmt->execute();
$pages = $stmt->get_result();

// Obter categorias para o filtro
$categories_stmt = $conn->prepare("SELECT id, name FROM Category ORDER BY name");
$categories_stmt->execute();
$categories = $categories_stmt->get_result();
?>


<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Páginas - Wiki SMI</title>
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

    .container {
        max-width: 1000px;
        margin: 40px auto;
        padding: 0 20px;
        width: 100%;
    }

    .search-box {
        margin: 20px 0;
        display: flex;
        gap: 10px;
    }

    .search-box input {
        flex: 1;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .search-box select {
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .search-box button {
        padding: 10px 15px;
        background: #4CAF50;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .page-list {
        list-style: none;
        padding: 0;
    }

    .page-item {
        padding: 15px 0;
        border-bottom: 1px solid #eee;
    }

    .page-item:last-child {
        border-bottom: none;
    }

    .page-title {
        font-size: 1.2em;
        margin-bottom: 5px;
    }

    .page-title a {
        color: #2a6496;
        text-decoration: none;
    }

    .page-title a:hover {
        text-decoration: underline;
    }

    .page-meta {
        color: #666;
        font-size: 0.9em;
    }

    .category-badge {
        display: inline-block;
        background: #4CAF50;
        color: white;
        padding: 2px 6px;
        border-radius: 4px;
        font-size: 0.8em;
        margin-left: 5px;
    }

    .no-pages {
        color: #666;
        text-align: center;
        padding: 40px 0;
    }

    .actions {
        margin-top: 20px;
    }

    .btn {
        display: inline-block;
        padding: 8px 15px;
        background: #4CAF50;
        color: white;
        text-decoration: none;
        border-radius: 4px;
    }



    .private-badge {
        display: inline-block;
        background: #ff9800;
        color: white;
        padding: 2px 6px;
        border-radius: 4px;
        font-size: 0.8em;
        margin-left: 5px;
    }
    </style>
</head>

<body>
    <link rel="stylesheet" href="styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <header>
    <div class="header-content">
            <a href="home.php" class="back-button" title="Voltar">
    <i class="fas fa-arrow-left"></i>
</a>

        <h1>Wiki SMI</h1>
    </div>
    </header>

    <div class="container">
        <h1>Todas as Páginas</h1>

        <form method="get" class="search-box">
            <input type="text" name="search" placeholder="Pesquisar páginas..."
                value="<?= htmlspecialchars($search) ?>">
            <select name="category_id">
                <option value="">Todas categorias</option>
                <?php while ($cat = $categories->fetch_assoc()): ?>
                <option value="<?= $cat['id'] ?>" <?= ($category_id == $cat['id']) ? 'selected' : '' ?>>
                    <?= htmlspecialchars($cat['name']) ?>
                </option>
                <?php endwhile; ?>
            </select>
            <button type="submit">Procurar</button>
        </form>

        <?php if ($pages->num_rows > 0): ?>
        <ul class="page-list">
            <?php while ($page = $pages->fetch_assoc()): ?>
            <li class="page-item">
                <div class="page-title">
                    <a href="view_page.php?id=<?= $page['id'] ?>"><?= htmlspecialchars($page['title']) ?></a>
                    <?php if ($page['status'] === 'privado'): ?>
                    <span class="private-badge">Privado</span>
                    <?php endif; ?>
                    <?php if ($page['category_name']): ?>
                    <span class="category-badge"><?= htmlspecialchars($page['category_name']) ?></span>
                    <?php endif; ?>
                </div>
                <div class="page-meta">
                    Criado por <?= htmlspecialchars($page['author']) ?> em
                    <?= date('d/m/Y H:i', strtotime($page['created_at'])) ?>
                    <?php if ($page['created_at'] != $page['updated_at']): ?>
                    | Última atualização em <?= date('d/m/Y H/i', strtotime($page['updated_at'])) ?>
                    <?php endif; ?>
                </div>
            </li>
            <?php endwhile; ?>
        </ul>
        <?php else: ?>
        <div class="no-pages">
            Nenhuma página encontrada.
            <?php if ($search || $category_id): ?>
            <p><a href="pages.php">Limpar filtros</a></p>
            <?php endif; ?>
        </div>
        <?php endif; ?>
    </div>

    <footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>
</body>

</html>