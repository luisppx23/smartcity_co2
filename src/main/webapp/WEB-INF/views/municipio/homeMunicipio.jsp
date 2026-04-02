<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Municipio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
</head>
<body>
<jsp:include page="../navbarm.jsp"/>
<div class="dashboard-theme-strip">
    <div class="dashboard-theme-content">
        <h2>Painel do Município</h2>
        <p>Aceda rapidamente às funcionalidades principais.</p>
    </div>
</div>

<div class="dashboard-container">
    <div class="dashboard-card dashboard-links-card">
        <h3>Gestão do Município</h3>

        <div class="dashboard-links">
            <a href="/municipio/redefinirTaxa">Redefinir Taxa</a>
            <a href="/municipio/dashboardMunicipio">Dashboard Município</a>
            <a href="/municipio/relatoriosMunicipio">Relatorios Município</a>
            <a href="/municipio/listaCidadaos">Lista de Cidadãos</a>
            <a href="/municipio/listaVeiculos">Lista de Veículos Associados aos Cidadãos do Município</a>
        </div>
    </div>
</div>
</body>
</html>