<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Home Cidadão</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/form-pages.css">
</head>
<body class="form-page-body">

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card profile-card">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>👤</span>
            </div>
            <h1 class="form-card-title">Área do Cidadão</h1>
            <h2 class="form-card-subtitle">
                Bem-vindo, ${user.firstName} ${user.lastName}
            </h2>
            <p class="form-card-description">
                Consulte aqui os dados associados à sua conta.
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
                <span class="profile-label">Contacto</span>
                <span class="profile-value">${user.contacto}</span>
            </div>

            <div class="profile-info-row">
                <span class="profile-label">Morada</span>
                <span class="profile-value">${user.morada}</span>
            </div>

            <div class="profile-info-row">
                <span class="profile-label">Data de Registo</span>
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
        </div>

        <div class="smart-form-actions profile-actions">
            <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao" class="smart-btn smart-btn-primary">
                Ir para o Dashboard
            </a>
        </div>
    </div>
</div>

</body>
</html>