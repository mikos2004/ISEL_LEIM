<%@ page import="pt.isel.leim.sbd2.Auxiliar" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultar Dados do Veículo</title>
<style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 300vh;
        }
        .container {
            background: #ffffff;
            padding: 20px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 8000px;
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
        <h1>Consultar Dados do Veículo</h1>

        <!-- Formulário para inserir matrícula -->
        <form action="LevantarVeiculo31" method="post">
            <label for="numeroCartaConducao">Numero da Carta de Conducao:</label>
            <input type="text" id="numeroCartaConducao" name="numeroCartaConducao" placeholder="Insira o numero da Carta de Conducao" required>
            <button type="submit">Consultar</button>
        </form>

        <%
        String infosList = (String) request.getAttribute("consultaOutput");
		
        // Processar a string e separar os valores num array
        String[][] veiculos = null;
        if (infosList != null) {
        	veiculos = Auxiliar.separarString(infosList);
        %>
        
        
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
        <%  
        }
        %>
		<%
          String numeroCartaConducao = (String) request.getAttribute("numeroCartaConducao");
          if (numeroCartaConducao != null) {
          %>
          <form action="EscolherAluguer31" method="post">
          	<input type="hidden" name="numeroCartaConducao" value="<%= numeroCartaConducao %>">
            	<label for="aluguer">Aluguer:</label>
            	<input type="text" id="aluguer" name="aluguer" placeholder="Escolha a linha do aluguer:" required>
            	<button type="submit">Selecionar</button>
          </form>
          <%
          }
          %>
         
          <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
            <p><%= message %></p>
        <%
            }
        %>
        <!-- Exibir erro, se houver -->
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        
        <!-- Botão para voltar -->
        <button onclick="location.href='index.jsp';">Voltar</button>
    </div>
</body>
</html>