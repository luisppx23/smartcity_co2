<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Municipio</title>
</head>
<body>
<div class="container">
    <div class="card mt-4">
        <div class="mt-3 text-center">
            <a href="/municipio/redefinirTaxa">Redefinir Taxa</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/municipio/homeMunicipio">Home Municipio</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/municipio/relatoriosMunicipio">Relatorios Municipio</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/municipio/listaCidadaos">Lista de Cidadãos</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/municipio/listaVeiculos">Lista de Veículos Associados aos Cidadãos do Município</a>
        </div>
    </div>
</div>
</body>
</html>