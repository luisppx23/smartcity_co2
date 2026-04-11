<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>O Meu Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/form-pages.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body class="form-page-body">
<jsp:include page="../navbar.jsp"/>
<div class="form-page-wrapper">
    <div class="form-card profile-card">
        <div class="form-card-header">
            <div class="profile-image-section text-center mb-3">
                <c:choose>
                    <c:when test="${not empty user.fotoUrl}">
                        <img src="${user.fotoUrl}" class="rounded-circle" style="width: 120px; height: 120px; object-fit: cover; border: 3px solid #3f6f54;">
                    </c:when>
                    <c:otherwise>
                        <div style="width: 120px; height: 120px; background: #eee; border-radius: 50%; display: inline-block; line-height: 120px; font-size: 50px;">👤</div>
                    </c:otherwise>
                </c:choose>
            </div>
            <h1 class="form-card-title">Área do Cidadão</h1>
            <h2 class="form-card-subtitle">${user.firstName} ${user.lastName}</h2>
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
                <span class="profile-label">NIF</span>
                <span class="profile-value">${user.nif}</span>
            </div>
            <div class="profile-info-row">
                <span class="profile-label">Morada</span>
                <span class="profile-value">${user.morada}</span>
            </div>
        </div>

        <div class="smart-form-actions">
            <a href="${pageContext.request.contextPath}/cidadao/perfil/editar" class="smart-btn smart-btn-primary">Editar Perfil</a>
            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">Voltar</a>
        </div>

        <c:if test="${not empty param.sucesso}">
            <p style="color: green; text-align: center; margin-top: 10px;">Perfil atualizado com sucesso!</p>
        </c:if>
    </div>
</div>
</body>
</html>