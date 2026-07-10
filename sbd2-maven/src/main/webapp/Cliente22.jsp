<%@ page import="pt.isel.leim.sbd2.Auxiliar" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Reservas</title>
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
            width: 100%;
            max-width: 1100px;
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
        <h1>Consultar Reservas</h1>

        <!-- Formulário para inserir NIF -->
        <form action="ConsultarReservas22" method="post">
            <label for="nif">NIF do Cliente:</label>
            <input type="text" id="nif" name="nif" placeholder="Insira o NIF" required>
            <button type="submit">Consultar</button>
        </form>

        <!-- Exibir tabela com as reservas somente após consulta -->
        <%
        String reservasList = (String) request.getAttribute("reservasList");
		
        // Processar a string e separar os valores num array
        String[][] reservas = null;
        if (reservasList != null) {
        	reservas = Auxiliar.separarString(reservasList);
        }
        %>
        <c:if test="${not empty reservas}">
            <table>
                <thead>
                    <tr>
                        <th>Reserva ID</th>
                        <th>Número Fiscal</th>
                        <th>Tipo Carro</th>
                        <th>Parque Levantamento</th>
                        <th>Data Início</th>
                        <th>Data Fim</th>
                        <th>Duração (Horas)</th>
                        <th>Código Desconto</th>
                        <th>Custo Final</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Iterar sobre as reservas -->
                    <%
			            if (reservas != null) {
			        %>
	                    <%
				            for (String[] reserva : reservas) {
				        %>
				        	<tr>
					        	<%
					            for (String campo : reserva) {
						        %>
						            <td><%= campo %></td>
						        <%
						            }
						        %>
	                        </tr>
				        <%
				            }
				        %>
			       <%
			            }
			       %>
                </tbody>
            </table>
        </c:if>
		
		<%
        String alugueresList = (String) request.getAttribute("alugueresList");
		
        // Processar a string e separar os valores num array
        String[][] alugueres = null;
        if (alugueresList != null) {
        	alugueres = Auxiliar.separarString(alugueresList);
        }
        %>
        <c:if test="${not empty reservas}">
            <table>
                <thead>
                    <tr>
                        <th>Inicio</th>
                        <th>Fim</th>
                        <th>Duracao</th>
                        <th>CustoFinal</th>
                        <th>Avaliação</th>
                        <th>Comentario</th>
                        <th>Nif</th>
                        <th>NºCarta de Condução</th>
                        <th>Código</th>
                        <th>Matricula</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Iterar sobre as reservas -->
                    <%
			            if (alugueres != null) {
			        %>
	                    <%
				            for (String[] aluguer : alugueres) {
				        %>
				        	<tr>
					        	<%
					            for (String campo : aluguer) {
						        %>
						            <td><%= campo %></td>
						        <%
						            }
						        %>
	                        </tr>
				        <%
				            }
				        %>
			       <%
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
