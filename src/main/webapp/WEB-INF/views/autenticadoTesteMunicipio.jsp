<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <style>
        .content-wrapper {
            background-color: lightblue;
            min-height: calc(100vh - 60px);
            padding: 20px 0;
        }
    </style>
</head>
<body>
<div class="content-wrapper">
    <div class="container">
        <div class="card mt-4">
            <div class="card-header">
                <h2>Autenticado Municipio</h2>
                <p>Bem vindo, ${user.nome}</p>
                <p>Username: ${user.username}</p>
                <p>Email: ${user.email}</p>
                <p>Data de Criação de conta: ${user.data_registo}</p>
                <p>Ativo: ${user.ativo}</p>
                <p>Tipo: ${user.tipo}</p>
                <p>Objetivo CO2/mês/hab: ${user.objetivo_co2_mes_hab}</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>