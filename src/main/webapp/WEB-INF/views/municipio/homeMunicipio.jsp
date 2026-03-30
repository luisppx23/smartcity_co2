<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home Municipio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/form-pagesm.css">
</head>
<body class="form-page-body">
<jsp:include page="../navbarm.jsp"/>
<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card profile-card">
        <div class="form-card-header">
            <div class="form-card-logo">🏛️</div>
            <h1 class="form-card-title">Autenticado Município</h1>
            <h2 class="form-card-subtitle">Bem-vindo, ${user.nome}</h2>
            <p class="form-card-description">
                Consulte abaixo os dados da conta autenticada.
            </p>
        </div>

        <div class="profile-info-list">
            <div class="profile-info-row">
                <span class="profile-label">Username</span>
                <span class="profile-value">${user.username}</span>
            </div>

            <div class="profile-info-row">
                <span class="profile-label">Email</span>
                <span class="profile-value">${user.email}</span>
            </div>

            <div class="profile-info-row">
                <span class="profile-label">Data de Criação de Conta</span>
                <span class="profile-value">${user.data_registo}</span>
            </div>

            <div class="profile-info-row">
                <span class="profile-label">Ativo</span>
                <span class="profile-value">${user.ativo}</span>
            </div>

            <div class="profile-info-row">
                <span class="profile-label">Tipo</span>
                <span class="profile-value">${user.tipo}</span>
            </div>

            <div class="profile-info-row">
                <span class="profile-label">Objetivo CO2/mês/hab</span>
                <span class="profile-value">${user.objetivo_co2_mes_hab}</span>
            </div>
        </div>

        <div class="smart-form-actions profile-actions">
            <a href="/municipio/dashboardMunicipio" class="smart-btn smart-btn-primary">
                Dashboard
            </a>
        </div>
    </div>
</div>
</body>
</html>