<%@ page import="java.util.List, java.util.HashMap, java.util.Map" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservar Veículo</title>
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
		<%
		
		String tiposVeiculo = (String) request.getAttribute("tiposVeiculo");
		String[] categorias = null;
		if (tiposVeiculo != null) {
			categorias = tiposVeiculo.split(" ");
		}
		
		String descontosAtivosCliente = (String) request.getAttribute("descontosAtivosCliente");
		String[] descontos = null;
		if (descontosAtivosCliente != null) {
			descontos = descontosAtivosCliente.split(" ");
		}
		%>
		
	    <h1>Reservar Veículo</h1>
	    <form action="ReservarVeiculo21" method="get">
	         <input type="hidden" name="numeroFiscal" value="${numeroFiscal}" />
	        <input type="hidden" name="localidade" value="${localidade}" />
	
	        <label for="dataInicio">Data Início:</label>
	        <input type="datetime-local" name="dataInicio" id="dataInicio" required />
	
	        <label for="dataFim">Data Fim:</label>
	        <input type="datetime-local" name="dataFim" id="dataFim" required />
	        
	       	<label for="categoria">Tipo de Veículo:</label>
			<select name="categoria" id="categoria" required>
				<%
					for (String categoria : categorias) {
				%>
					<option value="<%= categoria %>"><%= categoria %></option>
				<%
					}
				%>
			</select>		
	
	        <label for="desconto">Desconto:</label>
	        <select name="desconto" id="desconto">
	            <%
					for (String desconto : descontos) {
				%>
					<option value="<%= desconto %>"><%= desconto %></option>
				<%
					}
				%>
	        </select>
	
	        <button type="submit">Reservar</button><br>
	        <button onclick="location.href='index.jsp';">Voltar</button>
	    </form>
	    <%
	    String custo = (String) request.getAttribute("custo");
		if (custo != null) {
		%>
		<h1><%= custo %></h1>
		<%
		}
		%>
	    </div>
</body>
</html>
