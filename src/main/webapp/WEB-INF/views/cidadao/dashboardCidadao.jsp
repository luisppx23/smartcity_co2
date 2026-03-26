<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
</head>
<body>
<div class="container">
    <div class="card mt-4">
        <div class="mt-3 text-center">
            <a href="/cidadao/registoVeiculo">Registar Veiculo</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/cidadao/homeCidadao">Home</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/cidadao/simularTaxa">Simular Taxa</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/cidadao/listaVeiculos">Lista de Veículos Associados ao Cidadão</a>
        </div>


    </div>
</div>
</body>
</html>