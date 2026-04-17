<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Perfil – Smart City</title>

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
        <div class="smart-card" style="padding: 24px; width: 100%; box-sizing: border-box;">
            <h3 class="dashboard-card-title">Dados pessoais</h3>

            <form action="${pageContext.request.contextPath}/cidadao/perfil/salvar" method="post" enctype="multipart/form-data" style="display: flex; flex-direction: column; gap: 20px;">
                <div style="display: flex; flex-direction: column; gap: 8px;">
                    <label for="fotoFicheiro" style="font-weight: 700; color: #6B7A8D; font-size: 0.85rem; text-transform: uppercase;">Foto de Perfil</label>
                    <input type="file" id="fotoFicheiro" name="fotoFicheiro" accept="image/*" style="width: 100%; padding: 12px; border: 1px solid rgba(0, 31, 63, 0.2); border-radius: 12px; font-family: 'Outfit', sans-serif; font-size: 0.9rem; box-sizing: border-box;">
                </div>

                <div style="display: flex; flex-direction: column; gap: 8px;">
                    <label for="firstName" style="font-weight: 700; color: #6B7A8D; font-size: 0.85rem; text-transform: uppercase;">Primeiro Nome</label>
                    <input type="text" id="firstName" name="firstName" value="${user.firstName}" required style="width: 100%; padding: 12px; border: 1px solid rgba(0, 31, 63, 0.2); border-radius: 12px; font-family: 'Outfit', sans-serif; font-size: 0.9rem; box-sizing: border-box;">
                </div>

                <div style="display: flex; flex-direction: column; gap: 8px;">
                    <label for="lastName" style="font-weight: 700; color: #6B7A8D; font-size: 0.85rem; text-transform: uppercase;">Apelido</label>
                    <input type="text" id="lastName" name="lastName" value="${user.lastName}" required style="width: 100%; padding: 12px; border: 1px solid rgba(0, 31, 63, 0.2); border-radius: 12px; font-family: 'Outfit', sans-serif; font-size: 0.9rem; box-sizing: border-box;">
                </div>

                <div style="display: flex; flex-direction: column; gap: 8px;">
                    <label for="email" style="font-weight: 700; color: #6B7A8D; font-size: 0.85rem; text-transform: uppercase;">Email</label>
                    <input type="email" id="email" name="email" value="${user.email}" required style="width: 100%; padding: 12px; border: 1px solid rgba(0, 31, 63, 0.2); border-radius: 12px; font-family: 'Outfit', sans-serif; font-size: 0.9rem; box-sizing: border-box;">
                </div>

                <div style="display: flex; flex-direction: column; gap: 8px;">
                    <label for="contacto" style="font-weight: 700; color: #6B7A8D; font-size: 0.85rem; text-transform: uppercase;">Contacto</label>
                    <input type="text" id="contacto" name="contacto" value="${user.contacto}" style="width: 100%; padding: 12px; border: 1px solid rgba(0, 31, 63, 0.2); border-radius: 12px; font-family: 'Outfit', sans-serif; font-size: 0.9rem; box-sizing: border-box;">
                </div>

                <div style="display: flex; flex-direction: column; gap: 8px;">
                    <label for="morada" style="font-weight: 700; color: #6B7A8D; font-size: 0.85rem; text-transform: uppercase;">Morada</label>
                    <input type="text" id="morada" name="morada" value="${user.morada}" style="width: 100%; padding: 12px; border: 1px solid rgba(0, 31, 63, 0.2); border-radius: 12px; font-family: 'Outfit', sans-serif; font-size: 0.9rem; box-sizing: border-box;">
                </div>

                <div style="display: flex; justify-content: center; gap: 16px; margin-top: 20px; flex-wrap: wrap;">
                    <button type="submit" class="smart-btn btn-smart-primary-custom" style="display: inline-flex; align-items: center; gap: 8px;">
                        <i class="bi bi-save"></i> Guardar Alterações
                    </button>
                    <a href="${pageContext.request.contextPath}/cidadao/perfil" class="smart-btn smart-btn-secondary" style="display: inline-flex; align-items: center; gap: 8px;">
                        <i class="bi bi-arrow-left"></i> Cancelar
                    </a>
                </div>
            </form>
        </div>
    </section>
</div>

</body>
</html>