<?php
session_start();
require_once 'db.php';

if (!isset($_SESSION['user_id'])) {
    header("Location: login.php");
    exit();
}

$conn = db_connect();

// Verifica se o ID foi passado
if (!isset($_GET['id']) || !is_numeric($_GET['id'])) {
    die("ID da página inválido.");
}

$page_id = (int)$_GET['id'];
$user_id = $_SESSION['user_id'];

// Verifica se o utilizador é admin OU autor da página
$stmt = $conn->prepare("
    SELECT 
        p.author_id,
        u.role
    FROM Page p
    JOIN User u ON u.id = ?
    WHERE p.id = ?
");
$stmt->bind_param("ii", $user_id, $page_id);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 0) {
    die("Página não encontrada.");
}

$data = $result->fetch_assoc();
$is_author = ($data['author_id'] == $user_id);
$is_admin = ($data['role'] == 'admin');

if (!$is_author && !$is_admin) {
    die("Somente o autor da página ou administradores podem excluí-la.");
}
$stmt->close();

// Excluir a página
try {
    $conn->begin_transaction();

    // Exclui histórico
    $del_history = $conn->prepare("DELETE FROM PageHistory WHERE page_id = ?");
    $del_history->bind_param("i", $page_id);
    $del_history->execute();
    $del_history->close();

    // Exclui permissões (se existirem)
    $del_perms = $conn->prepare("DELETE FROM Permission WHERE page_id = ?");
    $del_perms->bind_param("i", $page_id);
    $del_perms->execute();
    $del_perms->close();

    // Exclui a página
    $del_page = $conn->prepare("DELETE FROM Page WHERE id = ?");
    $del_page->bind_param("i", $page_id);
    $del_page->execute();
    $del_page->close();

    $conn->commit();

    // Redireciona com sucesso
    header("Location: home.php?msg=Página excluída com sucesso");
    exit();

} catch (Exception $e) {
    $conn->rollback();
    die("Erro ao excluir a página: " . $e->getMessage());
}
?>