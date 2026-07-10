<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu Gerente</title>
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
        <h1>Menu Gerente</h1>
        <a href="Gerente51.jsp" class="menu-option">Histórico de um Veiculo</a>
        <a href="MenorLucro52" class="menu-option">Ranking das marcas com menor lucro</a>
        <a href="MelhorAvaliacao53.jsp" class="menu-option">Ranking dos modelos com melhor avaliação</a>
        <a href="MenosKm54.jsp" class="menu-option">Ranking dos veiculos com menor Km</a>
        <a href="MelhorReputacao55.jsp" class="menu-option">Ranking dos clientes ordenados por reputação</a>
        <a href="index.jsp" class="menu-option back">Voltar ao Menu Principal</a>
    </div>
</body>
</html>