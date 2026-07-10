<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Atualizar Dados do Condutor</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
    .container {
        background: #ffffff;
        padding: 20px 40px;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        text-align: center;
        width: 100%;
        max-width: 500px;
    }
    .container h1 {
        margin-bottom: 20px;
        color: #333;
    }
    form {
        display: flex;
        flex-direction: column;
        align-items: stretch;
    }
    label {
        font-weight: bold;
        margin-bottom: 5px;
        text-align: left;
    }
    input, select, button {
        width: 100%;
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 14px;
    }
    button {
        background-color: #007BFF;
        color: #ffffff;
        font-size: 16px;
        border: none;
        cursor: pointer;
        transition: background-color 0.3s;
    }
    button:hover {
        background-color: #0056b3;
    }
    h2 {
        color: #28a745;
    }
    p {
        color: red;
    }
</style>
</head>
<body>
    <div class="container">
        <h1>Atualizar Dados do Condutor</h1>
        
        <form action="AtualizarCondutor14" method="post">
            <!-- Input para o número da carta -->
            <label for="numeroCarta">Digite o Número da Carta de Condução:</label>
            <input type="text" name="numeroCarta" id="numeroCarta" required />
            
            <!-- Input para o novo valor -->
            <label for="data">Digite a Nova Data:</label><br>
            <input type="date" id="data" name="data" required><br>
            
            <!-- Botão para submeter -->
            <button type="submit">Atualizar</button><br>
            <button onclick="location.href='index.jsp';">Voltar</button>
        </form>
        
        <!-- Mensagens de Erro ou Sucesso -->
        <c:if test="${not empty error}">
            <p>${error}</p>
        </c:if>

        <c:if test="${not empty success}">
            <h2>${success}</h2>
        </c:if>
    </div>
</body>
</html>
