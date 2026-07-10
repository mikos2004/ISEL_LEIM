<%@ page import="pt.isel.leim.sbd2.Auxiliar" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Localizar Veiculo</title>
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
            width: 100%;
            max-width: 800px;
        }
        .container h1 {
            margin-bottom: 20px;
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
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
    </style>
</head>
<body>
    <div class="container"> 
        <h1>Localizar Veículo</h1>

        <!-- Formulário para inserir matrícula -->
        <form action="LocalizarVeiculo42" method="post">
            <label for="matricula">Matrícula do Veículo:</label>
            <input type="text" id="matricula" name="matricula" placeholder="Insira a matrícula" required>
            <button type="submit">Consultar</button>
        </form>
        
        <%
        String infosList = (String) request.getAttribute("consultaOutput");
		
        // Processar a string e separar os valores num array
        String[][] veiculos = null;
        if (infosList != null) {
        	veiculos = Auxiliar.separarString(infosList);
        }
        %>
         <c:if test="${not empty veiculos}">
            <table>
                <thead>
                    <tr>
                        <th>Matrícula</th>
                        <th>Localidade</th>
                        <th>Piso</th>
                        <th>Fila</th>
                        <th>Posição</th>
                        <th>Morada</th>
                        <th>Latitude</th>
                        <th>Longitude</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Iterar sobre os veículos -->
                    <%
                    if (veiculos != null) {
                        for (String[] veiculo : veiculos) {
                    %>
                        <tr>
                            <% for (String campo : veiculo) { %>
                                <td><%= campo %></td>
                            <% } %>
                        </tr>
                    <%
                        }
                    }
                    %>
                </tbody>
            </table>
        </c:if>

        <!-- Exibir erro, se houver -->
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <!-- Botão para voltar -->
        <button onclick="location.href='index.jsp';">Voltar</button>
    </div>
    
</body>
</html>