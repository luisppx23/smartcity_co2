<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Perfil</title>

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
        <h1>Editar Perfil</h1>
        <p>Mantenha as suas informações atualizadas</p>
    </section>

    <section class="dashboard-sections">
        <div class="smart-card profile-edit-card">
            <h3 class="dashboard-card-title">Dados pessoais</h3>

            <form action="${pageContext.request.contextPath}/cidadao/perfil/salvar" method="post" enctype="multipart/form-data" class="profile-edit-form">
                <div class="profile-edit-group">
                    <label for="fotoFicheiro">Foto de Perfil</label>
                    <input type="file" id="fotoFicheiro" name="fotoFicheiro" class="profile-file-input" accept="image/*">
                </div>

                <div class="profile-edit-group">
                    <label for="firstName">Primeiro Nome</label>
                    <input type="text" id="firstName" name="firstName" class="smart-input-dashboard" value="${user.firstName}" required>
                </div>

                <div class="profile-edit-group">
                    <label for="lastName">Apelido</label>
                    <input type="text" id="lastName" name="lastName" class="smart-input-dashboard" value="${user.lastName}" required>
                </div>

                <div class="profile-edit-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" class="smart-input-dashboard" value="${user.email}" required>
                </div>

                <div class="profile-edit-group">
                    <label for="contacto">Contacto</label>
                    <input type="text" id="contacto" name="contacto" class="smart-input-dashboard" value="${user.contacto}">
                </div>

                <div class="profile-edit-group">
                    <label for="morada">Morada</label>
                    <input type="text" id="morada" name="morada" class="smart-input-dashboard" value="${user.morada}">
                </div>

                <div class="profile-edit-actions">
                    <button type="submit" class="smart-btn btn-smart-primary-custom">
                        Guardar Alterações
                    </button>
                    <a href="${pageContext.request.contextPath}/cidadao/perfil" class="smart-btn smart-btn-secondary">
                        Cancelar
                    </a>
                </div>
            </form>
        </div>
    </section>
</div>
</body>
</html>