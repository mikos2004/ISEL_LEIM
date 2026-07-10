<%@ page import="pt.isel.leim.sbd2.BD_backend, java.sql.Connection, java.util.Arrays" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registar Intervenção</title>
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
        <h1>Intervenção</h1>

        <form action="RegistarIntervencao43" method="post">
            <label for="matricula">Matrícula:</label><br>
            <input type="text" id="matricula" name="matricula" placeholder="Insira a matrícula do veículo" required><br>

            <label for="quilometragem">Quilometragem:</label><br>
            <input type="text" id="quilometragem" name="quilometragem" placeholder="Insira a quilometragem" required><br>

            <label for="descricao">Descrição:</label><br>
            <input type="text" id="descricao" name="descricao" placeholder="Insira a descrição" required><br>

            <label for="data">Data:</label><br>
            <input type="date" id="data" name="data" required><br>

            <button type="submit" name="submit">Registar</button>
        </form>

        
        <%
            String message = null;
            boolean isSuccess = false;
            Connection conn = null;

            if (request.getParameter("submit") != null) {
                String matricula = request.getParameter("matricula");
                String quilometragem = request.getParameter("quilometragem");
                String descricao = request.getParameter("descricao");
                String data = request.getParameter("data");

                String[] dadoIntervencao = { matricula, quilometragem, descricao, data };
                System.out.println("Dados da Intervenção: " + Arrays.toString(dadoIntervencao));

                try {
                	Class.forName("com.mysql.jdbc.Driver");
                    conn = BD_backend.getConnection(BD_backend.getUrl(), BD_backend.getUser(), BD_backend.getPassword());
                    isSuccess = BD_backend.adicionarIntervencaoWeb(conn, dadoIntervencao);
                    message = isSuccess ? "Intervenção adicionada com sucesso!" : "Falha ao adicionar a intervenção.";
                } catch (Exception e) {
                    message = "Erro: " + e.getMessage();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        %>
        

        <% if (message != null) { %>
            <p class="<%= isSuccess ? "success" : "error" %>"><%= message %></p>
        <% } %>
    </div>
</body>
</html>
