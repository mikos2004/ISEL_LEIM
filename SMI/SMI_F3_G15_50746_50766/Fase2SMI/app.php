<?php

    session_start();
    if(!isset($_SESSION['user'])){
        header("Location: login.php");
        exit();
    }else{
        $user = $_SESSION['user'];
        echo $user['username'];
    }

    require_once 'db.php';
    $conn = db_connect();

    $sql = "SELECT * FROM users";
    $result = $conn->query($sql);

    $conn->close();
?>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>App</title>
</head>
<body>
    <h1>Users</h1>
    <h2>Current User: <?= $_SESSION['user']['username'] ?></h2>
    <ul>
        <?php while ($row = $result->fetch_assoc()):?>
            <li><?= $row["username"] ?></li>
        <?php endwhile;   ?>
    </ul>
    
    <input type="submit" value="Logout"><a href="logout.php">Logout</a>
</body>
</html>