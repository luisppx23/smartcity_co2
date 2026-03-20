<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body background-color="orange">
<div class="container">
    <div class="card mt-4">
        <div class="card-header">
            <h2>Autenticado Cidadao</h2>
            <p>Bem vindo, ${user.firstName} ${user.lastName}</p>
            <p>Username: ${user.username}</p>
            <p>Email: ${user.email}</p>
            <p>Data de Criação de conta: ${user.data_registo}</p>
            <p>Ativo: ${user.ativo}</p>
            <p>Tipo: ${user.tipo}</p>

            <a href="/logout" class="btn btn-danger">Logout</a>



        </div>
    </div>
</div>
</body>
</html>