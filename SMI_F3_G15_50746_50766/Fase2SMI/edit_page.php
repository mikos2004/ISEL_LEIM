<?php
session_start();
require_once 'db.php';

// Verifica se o utilizador está logado
if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

if (!isset($_GET['id'])) {
    header("Location: home.php");
    exit();
}

$page_id = (int)$_GET['id'];
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

// Obter dados da página existente
$page_stmt = $conn->prepare("SELECT * FROM Page WHERE id = ?");
$page_stmt->bind_param("i", $page_id);
$page_stmt->execute();
$page_result = $page_stmt->get_result();

if ($page_result->num_rows === 0) {
    header("Location: home.php?error=Página não encontrada");
    exit();
}

$page = $page_result->fetch_assoc();
$page_stmt->close();

// Verificar se o utilizador tem permissão para editar
$user_id = $_SESSION['user_id'];
$can_edit = false;

// Primeiro verifica se é admin/editor
$role_stmt = $conn->prepare("SELECT role FROM User WHERE id = ?");
$role_stmt->bind_param("i", $user_id);
$role_stmt->execute();
$role_result = $role_stmt->get_result();

if ($role_result->num_rows > 0) {
    $user_data = $role_result->fetch_assoc();
    $user_role = $user_data['role'];
    
    // Admins e editores podem editar qualquer página
    if ($user_role == 'admin' || $user_role == 'editor') {
        $can_edit = true;
    }
}
$role_stmt->close();

// Se ainda não tem permissão, verifica se é o autor
if (!$can_edit && $page['author_id'] == $user_id) {
    $can_edit = true;
}

// Se ainda não tem permissão, verifica permissões específicas
if (!$can_edit) {
    $perm_stmt = $conn->prepare("SELECT permission_level FROM Permission WHERE user_id = ? AND page_id = ?");
    $perm_stmt->bind_param("ii", $user_id, $page_id);
    $perm_stmt->execute();
    $perm_result = $perm_stmt->get_result();
    
    if ($perm_result->num_rows > 0) {
        $perm = $perm_result->fetch_assoc();
        $can_edit = in_array($perm['permission_level'], ['edição', 'administração']);
    }
    
    $perm_stmt->close();
}

if (!$can_edit) {
    header("Location: view_page.php?id=" . $page_id . "&error=Sem permissão para editar");
    exit();
}

// Processar o formulário de atualização de página
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $title = trim($_POST['title']);
    $content = trim($_POST['content']);
    $category_id = $_POST['category_id'] ?? null;

    // Validação básica
    if (empty($title)) {
        $error = "O título da página é obrigatório.";
    } elseif (empty($content)) {
        $error = "O conteúdo da página é obrigatório.";
    } else {
        // Atualizar a página
        try {
            $conn->begin_transaction();

            // Atualizar a página principal
            $stmt = $conn->prepare("UPDATE Page SET title = ?, content = ?, category_id = ?, updated_at = NOW() WHERE id = ?");
            $stmt->bind_param("ssii", $title, $content, $category_id, $page_id);
            $stmt->execute();

            // Criar registro no histórico de versões
            $history_stmt = $conn->prepare("INSERT INTO PageHistory (page_id, content, author_id) VALUES (?, ?, ?)");
            $history_stmt->bind_param("isi", $page_id, $content, $user_id);
            $history_stmt->execute();

            $conn->commit();
            $success = "Página atualizada com sucesso!";
            
            // Redirecionar para a página atualizada
            header("Location: view_page.php?id=" . $page_id);
            exit();
        } catch (Exception $e) {
            $conn->rollback();
            $error = "Erro ao atualizar página: " . $e->getMessage();
        }
    }
}
?>

<!DOCTYPE html>
<html lang="pt">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Página - Wiki SMI</title>
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
        width: 95%;
        margin: 20px auto;
        padding: 20px;
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    h1 {
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

    .modal {
        display: none;
        position: fixed;
        z-index: 1000;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow-y: auto;
        background-color: rgba(0, 0, 0, 0.6);
    }

    .modal-content {
        background-color: #fff;
        margin: 40px auto;
        padding: 20px;
        border-radius: 8px;
        width: 90%;
        max-width: 600px;
    }

    .close {
        color: #aaa;
        float: right;
        font-size: 28px;
        font-weight: bold;
        cursor: pointer;
    }

    .close:hover {
        color: black;
    }

    .form-group {
        margin-bottom: 15px;
    }

    .form-group label {
        display: block;
        margin-bottom: 5px;
    }

    .form-group input {
        width: 100%;
        padding: 8px;
        border: 1px solid #ddd;
        border-radius: 4px;
    }

    .modal-footer {
        margin-top: 20px;
        text-align: right;
    }

    .modal-footer button {
        padding: 8px 15px;
        margin-left: 10px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .btn-primary {
        background-color: #4CAF50;
        color: white;
    }

    .btn-secondary {
        background-color: #f44336;
        color: white;
    }

    .image-preview {
        max-width: 100%;
        max-height: 200px;
        margin-top: 10px;
        display: none;
    }

    .insert-image-btn {
        background-color: #4CAF50;
        color: white;
        border: none;
        padding: 8px 12px;
        border-radius: 4px;
        cursor: pointer;
        margin-bottom: 10px;
    }

    .insert-image-btn i {
        margin-right: 5px;
    }

    .image-card {
        display: inline-block;
        border: 1px solid #ddd;
        border-radius: 6px;
        padding: 10px;
        background: #f9f9f9;
        text-align: center;
        margin: 10px 0;
        max-width: 100%;
    }

    .image-card img {
        max-width: 100%;
        height: auto;
        border-radius: 4px;
    }

    .image-card .caption {
        margin-top: 8px;
        font-size: 14px;
        color: #555;
    }

    .video-card {
        width: 100%;
        background: transparent;
        border: none;
        padding: 0;
        margin: 0 0 20px 0;
    }

    .video-card video {
        width: 100%;
        height: auto;
        display: block;
        border-radius: 6px 6px 0 0;
        background-color: #000;
    }

    .video-card .caption {
        background: #f5f5f5;
        padding: 8px 12px;
        border: 1px solid #ddd;
        border-top: none;
        border-radius: 0 0 6px 6px;
        font-size: 0.9em;
        text-align: center;
    }

    /* Botão de vídeo */
    .insert-video-btn {
        background-color: #4CAF50;
        color: white;
        border: none;
        padding: 8px 12px;
        border-radius: 4px;
        cursor: pointer;
        margin-bottom: 10px;
        margin-left: 5px;
    }

    .insert-video-btn i {
        margin-right: 5px;
    }
    </style>
    
</head>

<body>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
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
        <h1>Editar Página</h1>

        <?php if (!empty($error)): ?>
        <div class="error"><?= htmlspecialchars($error) ?></div>
        <?php endif; ?>

        <?php if (!empty($success)): ?>
        <div class="success"><?= htmlspecialchars($success) ?></div>
        <?php endif; ?>

        <form method="POST">
            <div class="form-group">
                <label for="title">Título da Página</label>
                <input type="text" id="title" name="title" value="<?= htmlspecialchars($page['title']) ?>" required>
            </div>

            <div class="form-group">
                <label for="category_id">Categoria (opcional)</label>
                <select id="category_id" name="category_id">
                    <option value="">-- Sem Categoria --</option>
                    <?php foreach ($categories as $category): ?>
                    <option value="<?= htmlspecialchars($category['id']) ?>" <?= $category['id'] == $page['category_id'] ? 'selected' : '' ?>>
                        <?= htmlspecialchars($category['name']) ?>
                    </option>
                    <?php endforeach; ?>
                </select>
            </div>

            <div class="form-group">
                <label for="content">Conteúdo</label> 
                <button type="button" onclick="insertTag('b')"><b>B</b></button>
                <button type="button" onclick="insertTag('i')"><i>I</i></button>
                <button type="button" onclick="insertTag('u')"><u>U</u></button>
                <button type="button" onclick="insertTag('sub')">X<sub>2</sub></button>
                <button type="button" onclick="insertTag('sup')">X<sup>2</sup></button>
                <button type="button" onclick="insertTag('code')">&lt;/&gt;</button>
                <button type="button" onclick="insertTag('h1')">H1</button>
                <button type="button" onclick="insertTag('h2')">H2</button>
                <button type="button" onclick="insertTag('p')">P</button>
                <button type="button" class="insert-image-btn" onclick="openImageModal()">
                    <i class="fas fa-image"></i>
                </button>
                <button type="button" class="insert-video-btn" onclick="openVideoModal()">
                    <i class="fas fa-video"></i>
                </button>
                <textarea id="content" name="content" required><?= htmlspecialchars($page['content']) ?></textarea>
            </div>

            <button type="submit">Atualizar Página</button>
        </form>

    </div>

    <div id="imageModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeImageModal()">&times;</span>
            <h2>Inserir Imagem</h2>

            <div class="form-group">
                <label for="imageFile">Imagem:</label>
                <input type="file" id="imageFile" accept="image/*">
                <img id="imagePreview" class="image-preview" alt="Pré-visualização">
            </div>

            <div class="form-group">
                <label for="imageAlt">Legenda (alt):</label>
                <input type="text" id="imageAlt" placeholder="Descrição da imagem">
            </div>

            <div class="form-group">
                <label for="imageWidth">Largura (px):</label>
                <input type="number" id="imageWidth" placeholder="Auto">
            </div>

            <div class="form-group">
                <label for="imageHeight">Altura (px):</label>
                <input type="number" id="imageHeight" placeholder="Auto">
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-secondary" onclick="closeImageModal()">Cancelar</button>
                <button type="button" class="btn-primary" onclick="insertImage()">Inserir</button>
            </div>
        </div>
    </div>

    <div id="videoModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeVideoModal()">&times;</span>
            <h2>Inserir Vídeo</h2>

            <div class="form-group">
                <label for="youtubeURL">Link do YouTube:</label>
                <input type="text" id="youtubeURL" placeholder="https://www.youtube.com/watch?v=ID_DO_VIDEO">
            </div>

            <div class="form-group">
                <label for="videoAlt">Legenda (descrição):</label>
                <input type="text" id="videoAlt" placeholder="Descrição do vídeo">
            </div>

            <div class="form-group">
                <label for="videoWidth">Largura (px):</label>
                <input type="number" id="videoWidth" placeholder="Auto">
            </div>

            <div class="form-group">
                <label for="videoHeight">Altura (px):</label>
                <input type="number" id="videoHeight" placeholder="Auto">
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-secondary" onclick="closeVideoModal()">Cancelar</button>
                <button type="button" class="btn-primary" onclick="insertVideo()">Inserir</button>
            </div>
        </div>
    </div>

    <footer>&copy; 2025 Wiki SMI - Miguel Alcobia e Tomás Salvador</footer>

    <script>
    function openImageModal() {
        document.getElementById('imageModal').style.display = 'block';
    }

    function closeImageModal() {
        document.getElementById('imageModal').style.display = 'none';
        // Limpar campos
        document.getElementById('imageFile').value = '';
        document.getElementById('imageAlt').value = '';
        document.getElementById('imageWidth').value = '';
        document.getElementById('imageHeight').value = '';
        document.getElementById('imagePreview').style.display = 'none';
    }

    // Pré-visualização da imagem
    document.getElementById('imageFile').addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(event) {
                const preview = document.getElementById('imagePreview');
                preview.src = event.target.result;
                preview.style.display = 'block';
            };
            reader.readAsDataURL(file);
        }
    });

    // Inserir imagem no conteúdo
    function insertImage() {
        const fileInput = document.getElementById('imageFile');
        const altText = document.getElementById('imageAlt').value || '';
        const width = document.getElementById('imageWidth').value;
        const height = document.getElementById('imageHeight').value;

        if (fileInput.files.length === 0) {
            alert('Por favor, selecione uma imagem');
            return;
        }

        const file = fileInput.files[0];
        const reader = new FileReader();

        reader.onload = function(event) {
            const base64 = event.target.result;
            const contentTextarea = document.getElementById('content');
            const cursorPos = contentTextarea.selectionStart;

            // Criar HTML do card com imagem e legenda
            let imgHTML = `<div class="image-card">\n`;
            imgHTML += `  <img src="${base64}" alt="${altText}"`;
            if (width) imgHTML += ` width="${width}"`;
            if (height) imgHTML += ` height="${height}"`;
            imgHTML += `>\n`;
            if (altText) {
                imgHTML += `  <p class="caption">${altText}</p>\n`;
            }
            imgHTML += `</div>\n`;

            // Inserir no conteúdo
            const content = contentTextarea.value;
            contentTextarea.value = content.substring(0, cursorPos) + imgHTML + content.substring(cursorPos);

            closeImageModal();
        };

        reader.readAsDataURL(file);
    }

    // Fechar modal ao clicar fora
    window.onclick = function(event) {
        const modal = document.getElementById('imageModal');
        if (event.target === modal) {
            closeImageModal();
        }
    };

    // VIDEO
    // Funções para manipulação do modal de vídeo
    function openVideoModal() {
        document.getElementById('videoModal').style.display = 'block';
    }

    function closeVideoModal() {
        document.getElementById('videoModal').style.display = 'none';
        // Limpar campos
        document.getElementById('youtubeURL').value = '';
        document.getElementById('videoAlt').value = '';
        document.getElementById('videoWidth').value = '';
        document.getElementById('videoHeight').value = '';
    }

    // Inserir vídeo no conteúdo
    function insertVideo() {
        const youtubeURL = document.getElementById('youtubeURL').value.trim();
        const altText = document.getElementById('videoAlt').value || '';
        const width = document.getElementById('videoWidth').value;
        const height = document.getElementById('videoHeight').value;

        if (!youtubeURL) {
            alert('Por favor, insira o link do vídeo do YouTube.');
            return;
        }

        // Extrair o ID do vídeo do YouTube
        const match = youtubeURL.match(/(?:youtu\.be\/|youtube\.com\/(?:watch\?v=|embed\/))([\w\-]+)/);
        if (!match || !match[1]) {
            alert('Link do YouTube inválido.');
            return;
        }
        const videoId = match[1];

        const widthAttr = width ? `width="${width}"` : '';
        const heightAttr = height ? `height="${height}"` : '';
        const iframeHTML = `
<div class="video-card">
    <iframe src="https://www.youtube.com/embed/${videoId}" ${widthAttr} ${heightAttr} 
        frameborder="0" allowfullscreen></iframe>
    <div class="caption">${altText}</div>
</div>
`;

        // Inserir no conteúdo
        const textarea = document.getElementById('content');
        const cursorPos = textarea.selectionStart;
        const content = textarea.value;
        textarea.value = content.substring(0, cursorPos) + iframeHTML + content.substring(cursorPos);

        closeVideoModal();
    }

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
</body>
</html>