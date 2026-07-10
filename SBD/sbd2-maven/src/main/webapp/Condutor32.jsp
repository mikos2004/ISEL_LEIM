<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entregar Veículo</title>
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
        .message {
            margin-top: 20px;
            font-size: 16px;
            color: #555;
        }
        .success {
            color: #28a745;
        }
        .error {
            color: #d9534f;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Entregar Veículo</h1>
        <form action="EntregarVeiculo32" method="post">
            <label for="matricula">Matrícula:</label>
            <input type="text" id="matricula" name="matricula" required />

            <label for="parque">Parque de Levantamento:</label>
            <input type="text" id="parque" name="parque" required />

            <button type="submit">Entregar</button>
        </form>

        <!-- Exibir saída capturada -->
        <div class="output">
            <% 
                String consultaOutput = (String) request.getAttribute("consultaOutput");
                String error = (String) request.getAttribute("error");

                if (consultaOutput != null) { 
            %>
                <pre><%= consultaOutput %></pre>
            <% 
                } else if (error != null) { 
            %>
                <p class="error"><%= error %></p>
            <% 
                } 
            %>
        </div>
        <!-- Botão para voltar -->
        <button onclick="location.href='index.jsp';">Voltar</button>
    </div>
</body>
</html>
