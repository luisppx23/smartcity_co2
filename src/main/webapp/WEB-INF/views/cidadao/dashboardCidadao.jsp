<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Cidadão</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/dashboard.css">
</head>
<body>

<jsp:include page="../navbar.jsp"/>

<div class="dashboard-theme-strip">
    <div class="dashboard-theme-content">
        <h2>Transformando quilómetros em consciência.</h2>
        <p>Mobilidade mais inteligente para uma cidade mais sustentável.</p>
    </div>
</div>

<div class="dashboard-container">

    <div class="dashboard-grid">
        <div class="dashboard-card dashboard-card-blue">
            <h3>Registar Veículo</h3>
            <p>Adicione um novo veículo à sua conta.</p>
            <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo" class="dashboard-button">
                Registar Veículo
            </a>
        </div>

        <div class="dashboard-card dashboard-card-beige">
            <h3>Simular Taxa</h3>
            <p>Consulte uma simulação da taxa associada.</p>
            <a href="${pageContext.request.contextPath}/cidadao/simularTaxa" class="dashboard-button">
                Simular Taxa
            </a>
        </div>
    </div>

    <div class="dashboard-grid">
        <div class="dashboard-card">
            <h3>Registar KMs</h3>
            <p>Introduza novos registos de quilometragem do seu veículo.</p>
            <a href="${pageContext.request.contextPath}/cidadao/registoKms" class="dashboard-button">
                Registar KMs
            </a>
        </div>

        <div class="dashboard-card">
            <h3>Ver Registos de KMs</h3>
            <p>Consulte o histórico dos seus registos de quilometragem.</p>
            <a href="${pageContext.request.contextPath}/cidadao/verRegistosKms" class="dashboard-button">
                Ver Registos de KMs
            </a>
        </div>
    </div>

    <div class="dashboard-card dashboard-links-card">
        <h3>Acessos Rápidos</h3>
        <div class="dashboard-links">
            <a href="${pageContext.request.contextPath}/cidadao/listaVeiculos">Lista de Veículos Associados ao Cidadão</a>
        </div>
    </div>

</div>

</body>
</html>