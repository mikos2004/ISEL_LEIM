<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registar Cliente</title>
<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        margin: 0;
        padding: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 200vh;
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
        <h1>Registar Cliente</h1>
        
        <form action="CriarCliente11" method="post">
            <label for="numeroFiscal">Digite o seu Número Fiscal (NIF):</label>
            <input type="text" name="numeroFiscal" id="numeroFiscal" required />

            <label for="nome">Digite o seu Nome:</label>
            <input type="text" name="nome" id="nome" required />

            <label for="contacto">Digite o seu Contacto Telefónico:</label>
            <input type="text" name="contacto" id="contacto" required />

            <label for="morada">Digite a Morada no formato 'Rua, número':</label>
            <input type="text" name="morada" id="morada" required />

            <label for="distrito">Digite o seu Distrito:</label>
            <input type="text" name="distrito" id="distrito" required />

            <label for="concelho">Digite o seu Concelho:</label>
            <input type="text" name="concelho" id="concelho" required />

            <label for="freguesia">Digite a sua Freguesia:</label>
            <input type="text" name="freguesia" id="freguesia" required />

            <label for="linguas">Digite as Línguas separadas por vírgulas (Ex: Português, Inglês):</label>
            <input type="text" name="linguas" id="linguas" required />

            <label for="moeda">Escolha a Moeda:</label>
            <select name="moeda" id="moeda" required>
                <option value="USD">USD</option>
                <option value="EUR">EUR</option>
                <option value="BRL">BRL</option>
                <option value="GBP">GBP</option>
                <option value="JPY">JPY</option>
                <option value="AUD">AUD</option>
                <option value="CAD">CAD</option>
                <option value="CHF">CHF</option>
                <option value="CNY">CNY</option>
                <option value="INR">INR</option>
            </select>

            <label for="tipoCliente">Escolha o Tipo de Cliente:</label>
            <select name="tipoCliente" id="tipoCliente" required>
                <option value="Pessoa">Pessoa</option>
                <option value="Empresa">Empresa</option>
            </select>
            
            <label for="capitalSocial">Digite o CapitalSocial(Se for empresa):</label>
            <input type="text" name="capitalSocial" id="capitalSocial"  />

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