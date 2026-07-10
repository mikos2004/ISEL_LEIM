<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
session_start();
require_once 'db.php';

// Verifica se todos os parâmetros necessários foram recebidos
if (!isset($_SESSION['user_id'], $_POST['id'], $_POST['status'])) {
    header('Content-Type: application/json');
    echo json_encode(['success' => false, 'message' => 'Parâmetros inválidos']);
    exit();
}

$page_id = (int)$_POST['id'];
$new_status = $_POST['status'] === 'público' ? 'público' : 'privado'; 

try {
    $conn = db_connect();
    
    // Verifica permissões
    $stmt = $conn->prepare("SELECT p.author_id, u.role 
                           FROM Page p
                           JOIN User u ON u.id = ?
                           WHERE p.id = ?");
    $stmt->bind_param("ii", $_SESSION['user_id'], $page_id);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows === 0) {
        throw new Exception('Página ou usuário não encontrado');
    }
    
    $data = $result->fetch_assoc();
    $is_author = ($data['author_id'] == $_SESSION['user_id']);
    $is_admin_editor = in_array($data['role'], ['admin', 'editor']);
    
    if (!$is_author && !$is_admin_editor) {
        throw new Exception('Sem permissão para editar esta página');
    }
    
    // Atualiza status
    $update = $conn->prepare("UPDATE Page SET status = ? WHERE id = ?");
    $update->bind_param("si", $new_status, $page_id);
    
    if (!$update->execute()) {
        throw new Exception('Erro ao atualizar no banco de dados');
    }
    
    header('Content-Type: application/json');
    echo json_encode(['success' => true, 'message' => 'Status atualizado']);
    
} catch (Exception $e) {
    header('Content-Type: application/json');
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => $e->getMessage()]);
} finally {
    if (isset($conn)) $conn->close();
}
?>