<?php
session_start();
require_once 'db.php';

$conn = db_connect();
$search = $_GET['search'] ?? '';
$category_id = $_GET['category_id'] ?? null;

$query = "
    SELECT p.id, p.title, p.created_at, p.updated_at, 
           u.username as author, c.name as category_name
    FROM Page p
    LEFT JOIN User u ON p.author_id = u.id
    LEFT JOIN Category c ON p.category_id = c.id
    WHERE p.title LIKE ?
";

$params = ["%$search%"];
$param_types = "s";

if ($category_id && is_numeric($category_id)) {
    $query .= " AND p.category_id = ?";
    $params[] = $category_id;
    $param_types .= "i";
}

$query .= " ORDER BY p.updated_at DESC";
$stmt = $conn->prepare($query);

if ($param_types === "s") {
    $stmt->bind_param($param_types, $params[0]);
} else {
    $stmt->bind_param($param_types, ...$params);
}

$stmt->execute();
$pages = $stmt->get_result();

$categories_stmt = $conn->prepare("SELECT id, name FROM Category ORDER BY name");
$categories_stmt->execute();
$categories = $categories_stmt->get_result();
?>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Wiki SMI - Todas as Páginas</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        :root {
            --primary: #2e7d32;
            --bg: #f5f6f7;
            --text: #212529;
            --muted: #6c757d;
            --link: #0056b3;
            --sidebar-width: 220px;
        }

        * {
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, sans-serif;
        }

        body {
            margin: 0;
            display: flex;
            background-color: var(--bg);
            color: var(--text);
        }

        aside {
            width: var(--sidebar-width);
            background-color: var(--primary);
            color: white;
            padding: 30px 20px;
            min-height: 100vh;
            position: fixed;
            top: 0;
            left: 0;
        }

        aside h1 {
            font-size: 1.5rem;
            margin-bottom: 30px;
        }

        aside nav a {
            display: block;
            color: white;
            text-decoration: none;
            padding: 10px 0;
            font-weight: 500;
        }

        aside nav a:hover {
            text-decoration: underline;
        }

        .main {
            margin-left: var(--sidebar-width);
            padding: 40px;
            width: 100%;
        }

        .main h2 {
            margin-bottom: 20px;
        }

        .search-bar {
            display: flex;
            gap: 10px;
            margin-bottom: 30px;
            flex-wrap: wrap;
        }

        .search-bar input,
        .search-bar select,
        .search-bar button {
            padding: 10px;
            font-size: 1rem;
            border-radius: 4px;
            border: 1px solid #ccc;
        }

        .search-bar button {
            background-color: var(--primary);
            color: white;
            border: none;
            cursor: pointer;
        }

        section.pages {
            display: flex;
            flex-direction: column;
            gap: 25px;
        }

        article.page {
            padding-bottom: 10px;
            border-bottom: 1px solid #ccc;
        }

        article.page h3 {
            font-size: 1.2rem;
            margin: 0;
        }

        article.page a {
            color: var(--link);
            text-decoration: none;
        }

        article.page a:hover {
            text-decoration: underline;
        }

        .category-badge {
            background-color: var(--primary);
            color: white;
            padding: 2px 6px;
            font-size: 0.75rem;
            border-radius: 4px;
            margin-left: 6px;
        }

        .meta {
            font-size: 0.85rem;
            color: var(--muted);
        }

        .no-results {
            margin-top: 40px;
            font-size: 1rem;
            color: var(--muted);
        }

        .btn-create {
            margin-top: 40px;
            display: inline-block;
            background-color: var(--primary);
            color: white;
            padding: 10px 20px;
            border-radius: 6px;
            text-decoration: none;
        }

        footer {
            margin-left: var(--sidebar-width);
            background-color: var(--primary);
            color: white;
            text-align: center;
            padding: 15px;
            font-size: 14px;
        }

        @media (max-width: 768px) {
            aside {
                position: static;
                width: 100%;
            }

            .main {
                margin-left: 0;
                padding: 20px;
            }

            footer {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>

<aside>
    <h1>Wiki SMI</h1>
    <nav>
        <a href="pages.php">📄 Todas as Páginas</a>
        <?php if (isset($_SESSION['user_id'])): ?>
            <a href="create_page.php">➕ Criar Página</a>
        <?php endif; ?>
    </nav>
</aside>

<main class="main">
    <h2>Lista de Páginas</h2>

    <form class="search-bar" method="get">
        <input type="text" name="search" placeholder="Pesquisar..." value="<?= htmlspecialchars($search) ?>">
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
        <section class="pages">
            <?php while ($page = $pages->fetch_assoc()): ?>
                <article class="page">
                    <h3>
                        <a href="view_page.php?id=<?= $page['id'] ?>"><?= htmlspecialchars($page['title']) ?></a>
                        <?php if ($page['category_name']): ?>
                            <span class="category-badge"><?= htmlspecialchars($page['category_name']) ?></span>
                        <?php endif; ?>
                    </h3>
                    <div class="meta">
                        Criado por <?= htmlspecialchars($page['author']) ?> em <?= date('d/m/Y H:i', strtotime($page['created_at'])) ?>
                        <?php if ($page['created_at'] != $page['updated_at']): ?>
                            | Atualizado em <?= date('d/m/Y H:i', strtotime($page['updated_at'])) ?>
                        <?php endif; ?>
                    </div>
                </article>
            <?php endwhile; ?>
        </section>
    <?php else: ?>
        <div class="no-results">
            Nenhuma página encontrada.
            <?php if ($search || $category_id): ?>
                <p><a href="pages.php">Limpar filtros</a></p>
            <?php endif; ?>
        </div>
    <?php endif; ?>

    <?php if (isset($_SESSION['user_id'])): ?>
        <a href="create_page.php" class="btn-create">Criar Nova Página</a>
    <?php endif; ?>
</main>

<footer>
    &copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador
</footer>

</body>
</html>
