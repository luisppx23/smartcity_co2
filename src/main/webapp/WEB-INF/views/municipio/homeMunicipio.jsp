<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Município</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>
<body>
<jsp:include page="../navbarm.jsp"/>

<section class="municipio-hero">
    <div class="municipio-hero-content">
        <h1>Painel do Município</h1>
        <p>Aceda rapidamente às funcionalidades principais.</p>
    </div>
</section>

<main class="municipio-dashboard">
    <section class="municipio-card municipio-main-card">
        <h2>Gestão do Município</h2>
        <p class="municipio-section-subtitle">
            Selecione uma das áreas abaixo para gerir informação, relatórios e dados do município.
        </p>

        <div class="municipio-actions-card">
<%--            <a href="${pageContext.request.contextPath}/municipio/redefinirMeta" class="municipio-action-card">--%>
<%--                <div class="municipio-action-icon">🎯</div>--%>
<%--                <h3>Redefinir Meta</h3>--%>
<%--                <p>Atualizar objetivos e metas do município.</p>--%>
<%--                <span class="municipio-action-button">Abrir</span>--%>
<%--            </a>--%>

            <a href="${pageContext.request.contextPath}/municipio/redefinirTaxa" class="municipio-action-card">
                <div class="municipio-action-icon">💶</div>
                <h3>Redefinir Taxa</h3>
                <p>Alterar a taxa aplicada no sistema.</p>
                <span class="municipio-action-button">Abrir</span>
            </a>

            <a href="${pageContext.request.contextPath}/municipio/dashboardMunicipio_b" class="municipio-action-card">
                <div class="municipio-action-icon">📊</div>
                <h3>Dashboard Município</h3>
                <p>Consultar estatísticas e indicadores gerais.</p>
                <span class="municipio-action-button">Abrir</span>
            </a>

<%--            <a href="${pageContext.request.contextPath}/municipio/relatoriosMunicipio" class="municipio-action-card">--%>
<%--                <div class="municipio-action-icon">📄</div>--%>
<%--                <h3>Relatórios</h3>--%>
<%--                <p>Analisar relatórios e informação detalhada.</p>--%>
<%--                <span class="municipio-action-button">Abrir</span>--%>
<%--            </a>--%>

            <a href="${pageContext.request.contextPath}/municipio/listaCidadaos" class="municipio-action-card">
                <div class="municipio-action-icon">👥</div>
                <h3>Lista de Cidadãos</h3>
                <p>Gerir e consultar os cidadãos do município.</p>
                <span class="municipio-action-button">Abrir</span>
            </a>

            <a href="${pageContext.request.contextPath}/municipio/listaVeiculos" class="municipio-action-card">
                <div class="municipio-action-icon">🚗</div>
                <h3>Lista de Veículos</h3>
                <p>Ver veículos associados aos cidadãos.</p>
                <span class="municipio-action-button">Abrir</span>
            </a>
        </div>
    </section>
</main>

</body>
</html>