<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
   <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu Funcionário</title>
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
        .menu-container {
            text-align: center;
            background: #ffffff;
            padding: 20px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }
        .menu-container h1 {
            margin-bottom: 20px;
            color: #333;
        }
        .menu-option {
            display: block;
            margin: 10px 0;
            padding: 10px 15px;
            background-color: #007BFF;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        .menu-option:hover {
            background-color: #0056b3;
        }
        .menu-option.back {
            background-color: #DC3545;
        }
        .menu-option.back:hover {
            background-color: #a71d2a;
        }
    </style>
</head>
<body>
    <div class="menu-container">
        <h1>Menu Funcionário</h1>
        <a href="EscolherReserva41" class="menu-option">Atribuir veículos a reservas.</a>
        <a href="Funcionario42.jsp" class="menu-option">Localizar Veículo</a>
        <a href="Funcionario43.jsp" class="menu-option">Registar intervenção de um Veiculo</a>
        <a href="Funcionario45.jsp" class="menu-option">Avaliar a reputação de um cliente</a>
        <a href="Funcionario46.jsp" class="menu-option">Identificar o condutor através da data</a>
        <a href="index.jsp" class="menu-option back">Voltar ao Menu Principal</a>
    </div>
</body>
</html>