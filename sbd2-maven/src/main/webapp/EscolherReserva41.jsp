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
            max-width: 900px;
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
        <h1> Atribuir Reserva</h1>

        
        
        <%
        String reservasList = (String) request.getAttribute("consultaOutput");
		
        // Processar a string e separar os valores num array
        String[][] reservas = null;
        if (reservasList != null) {
        	reservas = Auxiliar.separarString(reservasList);
        }
        %>
         
            <table>
                <thead>
                    <tr>
                        <th>ReservaId</th>
                        <th>NIF</th>
                        <th>Categoria</th>
                        <th>Parque</th>
                        <th>Inicio</th>
                        <th>Fim</th>
                        <th>Duração</th>
                        <th>CodigoDesconto</th>
                        <th>CustoFinal</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Iterar sobre os veículos -->
                    <%
                    if (reservas != null) {
                        for (String[] reserva : reservas) {
                    %>
                        <tr>
                            <% for (String campo : reserva) { %>
                                <td><%= campo %></td>
                            <% } %>
                        </tr>
                    <%
                        }
                    }
                    %>
                </tbody>
            </table>
        <form action="EscolherCarroCondutor41" method="get">
            <label for="reserva">Reserva ID:</label>
            <input type="text" id="reserva" name="reserva" placeholder="Insira o Id" required>
            <button type="submit">Avançar</button>
        </form>

        <!-- Exibir erro, se houver -->
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <!-- Botão para voltar -->
        <button onclick="location.href='index.jsp';">Voltar</button>
    </div>
    
</body>
</html>