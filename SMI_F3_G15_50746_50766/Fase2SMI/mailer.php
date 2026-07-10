<?php

function enviar_email($para, $assunto, $mensagem) {
    // Configurações do seu servidor SMTP
    $smtp_host = 'smtp.seuprovedor.com';
    $smtp_usuario = 'seuemail@dominio.com';
    $smtp_senha = 'suasenha';
    $smtp_porta = 587;
    $smtp_secure = 'tls'; // ou 'ssl'
    
    // Cabeçalhos do email
    $headers = "From: noreply@seusite.com\r\n";
    $headers .= "Reply-To: contato@seusite.com\r\n";
    $headers .= "MIME-Version: 1.0\r\n";
    $headers .= "Content-Type: text/plain; charset=UTF-8\r\n";
    
    // Usando a função mail() básica (pode não funcionar bem em todos os servidores)
    // return mail($para, $assunto, $mensagem, $headers);
    
    // Método recomendado usando PHPMailer (mais confiável)
    require 'vendor/autoload.php'; // Se usar Composer
    
    $mail = new PHPMailer\PHPMailer\PHPMailer();
    
    try {
        // Configuração do servidor
        //$mail->isSMTP();
        //$mail->Host       = $smtp_host;
        //$mail->SMTPAuth   = true;
        //$mail->Username   = $smtp_usuario;
        //$mail->Password   = $smtp_senha;
        //$mail->SMTPSecure = $smtp_secure;
        //$mail->Port       = $smtp_porta;
        
        // Remetente e destinatário
        $mail->setFrom('noreply@seusite.com', 'Nome do Site');
        $mail->addAddress($para);
        
        // Conteúdo
        $mail->isHTML(false); // Email em texto puro
        $mail->Subject = $assunto;
        $mail->Body    = $mensagem;
        
        return $mail->send();
    } catch (Exception $e) {
        error_log("Erro ao enviar email: {$mail->ErrorInfo}");
        return false;
    }
}
?>