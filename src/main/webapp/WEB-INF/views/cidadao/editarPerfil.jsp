<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Editar Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/form-pages.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
</head>
<body class="form-page-body">
<div class="form-page-wrapper">
    <div class="form-card profile-card">
        <div class="form-card-header">
            <h1 class="form-card-title">Editar Meus Dados</h1>
            <p class="form-card-description">Mantenha as suas informações atualizadas.</p>
        </div>

        <form action="${pageContext.request.contextPath}/cidadao/perfil/salvar" method="post" enctype="multipart/form-data" class="smart-form">

            <div class="smart-form-group">
                <label>Foto de Perfil</label>
                <input type="file" name="fotoFicheiro" class="form-control" accept="image/*">
            </div>

            <div class="smart-form-group">
                <label>Primeiro Nome</label>
                <input type="text" name="firstName" class="smart-input" value="${user.firstName}" required>
            </div>

            <div class="smart-form-group">
                <label>Apelido</label>
                <input type="text" name="lastName" class="smart-input" value="${user.lastName}" required>
            </div>

            <div class="smart-form-group">
                <label>Email</label>
                <input type="email" name="email" class="smart-input" value="${user.email}" required>
            </div>

            <div class="smart-form-group">
                <label>Contacto</label>
                <input type="text" name="contacto" class="smart-input" value="${user.contacto}">
            </div>

            <div class="smart-form-group">
                <label>Morada</label>
                <input type="text" name="morada" class="smart-input" value="${user.morada}">
            </div>

            <div class="smart-form-actions">
                <button type="submit" class="smart-btn smart-btn-primary">Guardar Alterações</button>
                <a href="${pageContext.request.contextPath}/cidadao/perfil" class="smart-btn smart-btn-secondary">Cancelar</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>