<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Reputação e Descontos</title>
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
            max-width: 600px;
        }
        .container h1 {
            margin-bottom: 20px;
            color: #333;
        }
        .output {
            background-color: #f8f9fa;
            padding: 15px;
            margin-top: 20px;
            border-radius: 5px;
            text-align: left;
            white-space: pre-wrap;
            font-family: monospace;
            color: #212529;
        }
        .error {
            color: red;
            margin-top: 15px;
        }
        button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        a {
            text-decoration: none;
            color: white;
        }
        form {
            margin-bottom: 20px;
        }
        input {
            padding: 10px;
            margin: 5px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: 100%;
            max-width: 500px;
        }
        input[type="date"] {
            padding: 8px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Consultar Reputação e Descontos</h1>

        <!-- Formulário para inserir NIF -->
        <form action="ConsultarReputacao23" method="post">
            <label for="nif">NIF do Cliente:</label>
            <input type="text" id="nif" name="nif" placeholder="Insira o NIF" required>
            <button type="submit">Consultar</button>
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
