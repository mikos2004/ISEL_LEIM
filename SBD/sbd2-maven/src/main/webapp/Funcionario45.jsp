<%@ page import="pt.isel.leim.sbd2.Auxiliar" import="java.util.HashMap" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Avaliar Reputação</title>
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
        <h1>Avaliar Reputação</h1>

        <form action="AvaliarReputacao45" method="post">
            <label for="numFiscal">Número Fiscal:</label>
            <input type="text" id="numFiscal" name="numFiscal" placeholder="Insira o número fiscal" required>
            <button type="submit">Avaliar</button>
        </form>

        <!-- Exibir Reputação -->
        <c:if test="${not empty reputacao}">
            <h2>Reputação do Cliente: ${reputacao}</h2>
        </c:if>

        <!-- Exibir Descontos Ativos -->
        <c:if test="${not empty descontosAtivos}">
            <h3>Descontos Ativos</h3>
            
        </c:if>
		<%
        String DescontosList = (String) request.getAttribute("descontosAtivos");
		
		// Processar a string e separar os valores num array
        String[][] descontos = null;
        if (DescontosList != null) {
        	descontos = Auxiliar.separarString(DescontosList);
        }
        %>
        <c:if test="${not empty descontosAtivos}">
            <table>
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Descrição</th>
                        <th>Valor</th>
                        <th>Data de Inicio</th>
                        <th>Data de Fim</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Iterar sobre as reservas -->
                    <%
			            if (descontos != null) {
			        %>
						<%
				            for (String[] reserva : descontos) {
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
			            }%>
			       
                </tbody>
                
            </table>
        </c:if>
        
        <!-- Adicionar input "Escolha o desconto" e botão "Atribuir desconto" se a reputação permitir -->
        <c:if test="${not empty reputacao && (reputacao >= 3.0 || reputacao == 0.0)}">
            <form action="AvaliarReputacao45" method="post">
                <label for="desconto">Escolha o desconto:</label>
                <input type="text" id="desconto" name="desconto" placeholder="Insira o código do desconto" required>
                <input type="hidden" name="numFiscal" value="${param.numFiscal}">
                <button type="submit">Atribuir Desconto</button>
            </form>
        </c:if>
        <!-- Exibir mensagem -->
        <c:if test="${not empty message}">
            <div class="message ${message.contains('Erro') ? 'error' : ''}">
                ${message}
            </div>
        </c:if>
    </div>
</body>
</html>
