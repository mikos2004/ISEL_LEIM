<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registar Veículo</title>
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
        <h1>Registar Veículo</h1>
        
        <form action="CriarVeiculo15" method="get">
            <label for="matricula">Digite a Matrícula:</label>
            <input type="text" name="matricula" id="matricula" required />

            <label for="marca">Digite a Marca:</label>
            <input type="text" name="marca" id="marca" required />

            <label for="modelo">Digite o Modelo:</label>
            <input type="text" name="modelo" id="modelo" required />

            <label for="cor">Digite a Cor do Veículo:</label>
            <input type="text" name="cor" id="cor" required />

            <label for="tipo">Escolha o Tipo de Veículo:</label>
            <select name="tipo" id="tipo" required>
                <option value="Familiar">Familiar</option>
                <option value="Comercial">Comercial</option>
                <option value="Motociclo">Motociclo</option>
            </select>

            <label for="numeroLugares">Digite o NºLugares:</label>
            <input type="text" name="numeroLugares" id="numeroLugares" required />
            
            <label for="numeroPortas">Digite o NºPortas:</label>
            <input type="text" name="numeroPortas" id="numeroPortas" required />
            
            <label for="capacidadeCarga">Digite a Capacidade de Carga:</label>
            <input type="text" name="capacidadeCarga" id="capacidadeCarga" required />
            
            <label for="tipoMotor">Digite o Motor:</label>
            <input type="text" name="tipoMotor" id="tipoMotor" required />
            
            <label for="potencia">Digite a Potencia:</label>
            <input type="text" name="potencia" id="potencia" required />
            
            <label for="localidade">Digite a Localidade:</label>
            <input type="text" name="localidade" id="localidade" required />

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
