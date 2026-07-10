<%@ page import="pt.isel.leim.sbd2.Auxiliar" import="java.util.HashMap" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Atribuir Veículo</title>
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
            width: 90%;
            max-width: 900px;
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
        select, button {
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
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Atribuir Veículo</h1>
        <form action="AtribuirReserva41" method="post">
        
        
        <%
        String id = (String) request.getParameter("reserva");
        %>
        	<input type="hidden" name="id" value="<%= id %>" />
     
            <label for="veiculo">Veículo:</label>
            <select name="veiculo" id="veiculo" required>
                <%
                    String infosList = (String) request.getAttribute("carros");
	                String[][] veiculos = null;
	                if (infosList != null) {
	                	veiculos = Auxiliar.separarString(infosList);
	                }
                
                    if (veiculos != null) {
                        for (String[] veiculo : veiculos) {
                %>
                    <option value="<%= veiculo[0] %>"><%= veiculo[0] %></option>
                <%
                        }
                    }
                %>
            </select>

            <label for="condutor">Condutor:</label>
            <select name="condutor" id="condutor" required>
                <%
                    infosList = (String) request.getAttribute("condutores");
	                String[][] condutores = null;
	                if (infosList != null) {
	                	condutores = Auxiliar.separarString(infosList);
	                }
                
                    if (condutores != null) {
                        for (String[] condutor : condutores) {
                %>
                    <option value="<%= condutor[0] %>"><%= condutor[1] %></option>
                <%
                        }
                    }
                %>
            </select>

            <button type="submit">Atribuir</button>
        </form>
        <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
            <p><%= message %></p>
        <%
            }
        %>
        <button onclick="location.href='index.jsp';">Voltar</button>
    </div>
</body>
</html>
