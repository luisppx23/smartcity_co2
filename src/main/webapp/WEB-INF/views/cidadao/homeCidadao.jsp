<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Home Cidadão</title>

    <!-- Navbar -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">

    <!-- Estilo alinhado com município -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/home.css">

    <!-- Fonte (igual município) -->
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet">

    <!-- Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<jsp:include page="../navbar.jsp"/>

<div class="dashboard-container">

    <!-- HERO -->
    <div class="home-hero">
        <h1>Bem-vindo, ${user.firstName}!</h1>
        <p>Mobilidade mais inteligente para uma cidade mais sustentável.</p>
    </div>

    <!-- AÇÕES PRINCIPAIS -->
    <div class="home-grid">

        <a href="${pageContext.request.contextPath}/cidadao/registoVeiculo" class="home-card">
            <div class="card-icon">
                <i class="bi bi-car-front"></i>
            </div>
            <h3>Registar Veículo</h3>
            <p>Adicione um novo veículo à sua conta.</p>
        </a>

        <a href="${pageContext.request.contextPath}/cidadao/simularTaxa" class="home-card">
            <div class="card-icon">
                <i class="bi bi-calculator"></i>
            </div>
            <h3>Simular Taxa</h3>
            <p>Consulte uma simulação de taxa associada.</p>
        </a>

        <a href="${pageContext.request.contextPath}/cidadao/registoKms" class="home-card">
            <div class="card-icon">
                <i class="bi bi-pencil"></i>
            </div>
            <h3>Registar KMs</h3>
            <p>Introduza novos registos de quilometragem do seu veículo.</p>
        </a>

        <a href="${pageContext.request.contextPath}/cidadao/historicoKms" class="home-card">
            <div class="card-icon">
                <i class="bi bi-clock-history"></i>
            </div>
            <h3>Ver Registos de KMs</h3>
            <p>Consulte o histórico dos seus registos de quilometragem.</p>
        </a>

    </div>

    <!-- ACESSOS RÁPIDOS (estilo portal município) -->
    <div class="quick-links-card">

        <h3>Acessos Rápidos</h3>

        <div class="quick-links-row">

            <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao">
                <i class="bi bi-bar-chart-line"></i>
                <span>As minhas emissões</span>
            </a>

            <a href="${pageContext.request.contextPath}/cidadao/listaVeiculos">
                <i class="bi bi-car-front"></i>
                <span>Os meus veículos</span>
            </a>
            

            <a href="${pageContext.request.contextPath}/cidadao/perfil">
                <i class="bi bi-person"></i>
                <span>O meu perfil</span>
            </a>

        </div>
    </div>

</div>

</body>
</html>