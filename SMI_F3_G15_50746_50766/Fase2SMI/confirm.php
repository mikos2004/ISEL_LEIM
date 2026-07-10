<?php
require_once 'db.php';
$conn = db_connect();

if(isset($_GET['token'])){
    $token = $_GET['token'];

    $stmnt = $conn->prepare("SELECT * FROM users WHERE token=? AND auth=0");
    $stmnt->bind_param("s", $token);
    $stmnt->execute();
    $result = $stmnt->get_result();

    if($result->num_rows == 1){
        $update = $conn->prepare("UPDATE users SET auth=1, token=NULL WHERE token=?");
        $update->bind_param("s", $token);
        $update->execute();
        echo "Email confirmado com sucesso! Pode fazer login agora.";
    } else {
        echo "Token inválido ou email já confirmado.";
    }
} else {
    echo "Token não fornecido.";
}
?>
