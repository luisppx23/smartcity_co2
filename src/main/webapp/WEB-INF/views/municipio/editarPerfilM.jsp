<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Perfil do Município – Smart City</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">
</head>
<body>

<jsp:include page="navbarm.jsp"/>

<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">Editar Perfil do Município</h1>
    <p class="mun-hero-subtitle">Actualize os dados da sua entidade</p>
</section>

<main class="mun-page-content">

    <div class="mun-card">
        <form action="${pageContext.request.contextPath}/municipio/perfil/salvar" method="post" enctype="multipart/form-data">

            <div class="mun-section">
                <label for="fotoFicheiro" class="mun-card-description"><strong>Foto do Município</strong></label>
                <input type="file" id="fotoFicheiro" name="fotoFicheiro" class="mun-input-clean" accept="image/*">
            </div>

            <div class="mun-section">
                <label for="nome" class="mun-card-description"><strong>Nome do Município</strong></label>
                <input type="text" id="nome" name="nome" class="mun-input-clean" value="${municipio.nome}" required>
            </div>

            <div class="mun-section">
                <label for="email" class="mun-card-description"><strong>Email</strong></label>
                <input type="email" id="email" name="email" class="mun-input-clean" value="${municipio.email}" required>
            </div>

            <div class="mun-section">
                <label for="nif" class="mun-card-description"><strong>NIF</strong></label>
                <input type="number" id="nif" name="nif" class="mun-input-clean" value="${municipio.nif}" required>
            </div>

            <div class="mun-section">
                <label for="objetivo" class="mun-card-description"><strong>Objectivo CO₂ (kg/hab/mês)</strong></label>
                <input type="number" id="objetivo" name="objetivo" class="mun-input-clean" step="0.1" value="${municipio.objetivo_co2_mes_hab}" required>
            </div>

            <div class="mun-grid-2" style="margin-top: 2rem;">
                <button type="submit" class="mun-btn-primary">
                    <i class="bi bi-save"></i> Guardar Alterações
                </button>
                <a href="${pageContext.request.contextPath}/municipio/perfil" class="mun-btn-secondary">
                    <i class="bi bi-arrow-left"></i> Cancelar
                </a>
            </div>

        </form>
    </div>

</main>

</body>
</html>