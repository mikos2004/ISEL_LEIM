<?php
session_start();
require_once 'db.php';

// Verifica se o usuário está logado
if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$conn = db_connect();
$error = '';
$success = '';

// Obter categorias para o dropdown
$categories = [];
$category_stmt = $conn->prepare("SELECT id, name FROM Category");
$category_stmt->execute();
$category_result = $category_stmt->get_result();
while ($row = $category_result->fetch_assoc()) {
    $categories[] = $row;
}
$category_stmt->close();

// Processar o formulário de criação de página
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $title = trim($_POST['title']);
    $content = trim($_POST['content']);
    $category_id = $_POST['category_id'] ?? null;
    $author_id = $_SESSION['user_id'];

    // Validação básica
    if (empty($title)) {
        $error = "O título da página é obrigatório.";
    } elseif (empty($content)) {
        $error = "O conteúdo da página é obrigatório.";
    } else {
        // Inserir a nova página
        try {
            $conn->begin_transaction();

            // Inserir a página principal
            $stmt = $conn->prepare("INSERT INTO Page (title, content, author_id, category_id) VALUES (?, ?, ?, ?)");
            $stmt->bind_param("ssii", $title, $content, $author_id, $category_id);
            $stmt->execute();
            $page_id = $conn->insert_id;

            // Criar registro no histórico de versões
            $history_stmt = $conn->prepare("INSERT INTO PageHistory (page_id, content, author_id) VALUES (?, ?, ?)");
            $history_stmt->bind_param("isi", $page_id, $content, $author_id);
            $history_stmt->execute();

            // Definir permissões padrão (o autor recebe permissão de administração)
            $permission_stmt = $conn->prepare("INSERT INTO Permission (user_id, page_id, permission_level) VALUES (?, ?, 'administração')");
            $permission_stmt->bind_param("ii", $author_id, $page_id);
            $permission_stmt->execute();

            $conn->commit();
            $success = "Página criada com sucesso!";
            
            // Redirecionar para a página criada
            header("Location: view_page.php?id=" . $page_id);
            exit();
        } catch (Exception $e) {
            $conn->rollback();
            $error = "Erro ao criar página: " . $e->getMessage();
        }
    }
}
?>

<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Criar Nova Página - Wiki SMI</title>
    <style>
    * { box-sizing: border-box; font-family: Arial, sans-serif; margin: 0; padding:0 }
    body { 
        background: #f0f2f5; 
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }

    header {
        background-color: #4CAF50;
        padding: 20px;
        color: white;
        text-align: center;
    }

    .container {
        max-width: 1000px;
        width: 95%; /* Adicionei para melhor responsividade */
        margin: 20px auto;
        padding: 20px;
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    h1 {
        color: #333;
        margin-bottom: 20px;
    }

    .form-group {
        margin-bottom: 15px;
    }

    label {
        display: block;
        margin-bottom: 5px;
        font-weight: bold;
    }

    input[type="text"],
    select,
    textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    textarea {
        min-height: 300px;
        resize: vertical;
    }

    button {
        background-color: #4CAF50;
        color: white;
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    button:hover {
        background-color: #45a049;
    }

    .error {
        color: red;
        margin-bottom: 15px;
    }

    .success {
        color: green;
        margin-bottom: 15px;
    }

    .navigation {
        margin-top: 20px;
    }

    .navigation a {
        color: #4CAF50;
        text-decoration: none;
    }

    .navigation a:hover {
        text-decoration: underline;
    }

    footer {
        background-color: #4CAF50;
        color: white;
        text-align: center;
        padding: 15px 0;
        font-size: 14px;
        margin-top: auto;
    }
    #format-buttons {
    margin-bottom: 10px;
    }

    #format-buttons button {
        width: 40px;
        height: 40px;
        font-size: 14px;
        margin-right: 5px;
        padding: 5px 10px;
        font-weight: bold;
        border: 1px solid #ccc;
        border-radius: 4px;
        background-color: #4CAF50;
        cursor: pointer;
    }

    #format-buttons button:hover {
        background-color: #45a049;
    }
    </style>
</head>

<body>
    <header>
        <h1>Wiki SMI</h1>
    </header>

    <div class="container">
        <h1>Criar Nova Página</h1>

        <?php if (!empty($error)): ?>
        <div class="error"><?= htmlspecialchars($error) ?></div>
        <?php endif; ?>

        <?php if (!empty($success)): ?>
        <div class="success"><?= htmlspecialchars($success) ?></div>
        <?php endif; ?>

        <form method="POST">
            <div class="form-group">
                <label for="title">Título da Página</label>
                <input type="text" id="title" name="title" required>
            </div>

            <div class="form-group">
                <label for="category_id">Categoria (opcional)</label>
                <select id="category_id" name="category_id">
                    <option value="">-- Sem Categoria --</option>
                    <?php foreach ($categories as $category): ?>
                    <option value="<?= htmlspecialchars($category['id']) ?>"><?= htmlspecialchars($category['name']) ?>
                    </option>
                    <?php endforeach; ?>
                </select>
            </div>

            <div class="form-group">
                <label for="content">Conteúdo</label>
                <div class="form-group">
                    <label>Formatação Rápida</label>
                    <div id="format-buttons">
                        <button type="button" onclick="insertTag('b')"><b>B</b></button>
                        <button type="button" onclick="insertTag('i')"><i>I</i></button>
                        <button type="button" onclick="insertTag('u')"><u>U</u></button>
                        <button type="button" onclick="insertTag('sub')">X<sub>2</sub></button>
                        <button type="button" onclick="insertTag('sup')">X<sup>2</sup></button>
                        <button type="button" onclick="insertTag('code')">&lt;/&gt;</button>
                        <button type="button" onclick="insertTag('h1')">H1</button>
                        <button type="button" onclick="insertTag('h2')">H2</button>
                        <button type="button" onclick="insertTag('p')">P</button>
                    </div>
                </div>
                <textarea id="content" name="content" required></textarea>
            </div>

            <button type="submit">Criar Página</button>
        </form>

        <div class="navigation">
            <a href="home.php">← Voltar para a página inicial</a>
        </div>
    </div>

    <footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

</body>
<script>
function insertTag(tag) {
    const textarea = document.getElementById("content");
    const start = textarea.selectionStart;
    const end = textarea.selectionEnd;
    const selectedText = textarea.value.substring(start, end);
    const before = textarea.value.substring(0, start);
    const after = textarea.value.substring(end);

    const openTag = `<${tag}>`;
    const closeTag = `</${tag}>`;

    textarea.value = before + openTag + selectedText + closeTag + after;

    const cursorPos = start + openTag.length + selectedText.length + closeTag.length;
    textarea.focus();
    textarea.setSelectionRange(cursorPos, cursorPos);
}
</script>
</html>