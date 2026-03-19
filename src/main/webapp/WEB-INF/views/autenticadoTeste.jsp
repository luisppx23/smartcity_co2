<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div class="container">
    <div class="card mt-4">
        <div class="card-header">
            <h2>Autenticado</h2>
            <p>Bem vindo, ${user.getNome()}</p>
            <p>Password: ${user.getPassword().size()}</p>
        </div>
    </div>
</div>
</body>
</html>