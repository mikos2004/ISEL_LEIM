<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registar Condutor</title>
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
    input, button {
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
        <h1>Registar Condutor</h1>
        
        <form action="CriarCondutor13" method="post">
            <label for="numeroCarta">Digite o seu número da carta de condução:</label>
            <input type="text" name="numeroCarta" id="numeroCarta" required />

            <label for="nome">Digite o seu Nome:</label>
            <input type="text" name="nome" id="nome" required />

            <label for="data">Data:</label><br>
            <input type="date" id="data" name="data" required><br>

            <button type="submit">Registar</button><br>
            <button onclick="location.href='index.jsp';">Voltar</button>
        </form>
        
        <c:if test="${not empty error}">
            <p>${error}</p>
        </c:if>

        <c:if test="${not empty success}">
            <h2>${success}</h2>
        </c:if>
    </div>
</body>
</html>
