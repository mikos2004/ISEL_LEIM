<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menu Principal</title>
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
        .menu-option.exit {
            background-color: #DC3545;
        }
        .menu-option.exit:hover {
            background-color: #a71d2a;
        }
    </style>
</head>
<body>
    <div class="menu-container">
        <h1>Menu Principal</h1>
        <a href="administrador.jsp" class="menu-option">Administrador</a>
        <a href="cliente.jsp" class="menu-option">Cliente</a>
        <a href="condutor.jsp" class="menu-option">Condutor</a>
        <a href="funcionario.jsp" class="menu-option">Funcionário</a>
        <a href="gerente.jsp" class="menu-option">Gerente</a>
    </div>
</body>
</html>
