<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>O Meu Perfil</title>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700;800&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/cidadao-pages.css">
</head>
<body class="dashboard-body">
<jsp:include page="../navbar.jsp"/>

<div class="dashboard-wrapper dashboard-wrapper-cidadao-style">
    <section class="dashboard-page-header">
        <h1>O Meu Perfil</h1>
        <p>Consulte e gira os seus dados pessoais</p>
    </section>

    <section class="dashboard-sections">
        <div class="profile-layout">
            <div class="info-card profile-avatar-card">
                <div class="profile-image-wrap">
                    <c:choose>
                        <c:when test="${not empty user.fotoUrl}">
                            <img src="${user.fotoUrl}" class="profile-avatar" alt="Foto de perfil">
                        </c:when>
                        <c:otherwise>
                            <div class="profile-avatar-placeholder">👤</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="profile-user-name">${user.firstName} ${user.lastName}</div>
                <p class="profile-user-subtitle">Área do Cidadão</p>
            </div>

            <div class="smart-card profile-info-card">
                <h3 class="dashboard-card-title">Informação da conta</h3>

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

                <div class="profile-actions">
                    <a href="${pageContext.request.contextPath}/cidadao/perfil/editar" class="smart-btn btn-smart-primary-custom">
                        Editar Perfil
                    </a>

                    <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                        Voltar
                    </a>

                    <form action="${pageContext.request.contextPath}/cidadao/perfil/apagar" method="post" class="inline-form" onsubmit="return confirm('Tem a certeza que deseja apagar permanentemente a sua conta? Esta ação é irreversível.');">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" class="smart-btn btn-danger-smart">
                            Apagar Conta
                        </button>
                    </form>
                </div>

                <c:if test="${not empty param.sucesso}">
                    <p class="success-message">Perfil atualizado com sucesso!</p>
                </c:if>
            </div>
        </div>
    </section>
</div>
</body>
</html>