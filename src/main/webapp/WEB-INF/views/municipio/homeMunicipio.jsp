<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard Município</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>
<body>
<jsp:include page="navbarm.jsp"/>

<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">Home do Município</h1>
    <p class="mun-hero-subtitle">Gestão de dados em tempo real do município de ${municipio.nome}</p>
</section>

<main class="mun-page-content">

    <c:if test="${not empty erro}">
        <div class="mun-alert mun-alert-danger mun-section">
            <i class="bi bi-exclamation-circle"></i> ${erro}
        </div>
    </c:if>

    </div>

    <%-- ACESSOS RÁPIDOS --%>
    <div class="mun-quick-grid mun-section">
        <a href="<c:url value='/municipio/relatoriosMunicipio'/>" class="mun-quick-card">
            <div class="mun-quick-icon"><i class="bi bi-file-earmark-bar-graph"></i></div>
            <div class="mun-quick-title">Relatórios Mensais</div>
            <div class="mun-quick-desc">Relatórios detalhados de emissões, KMs e tendências por período.</div>
        </a>
        <a href="<c:url value='/municipio/redefinirTaxa'/>" class="mun-quick-card">
            <div class="mun-quick-icon"><i class="bi bi-sliders"></i></div>
            <div class="mun-quick-title">Redefinir Taxa</div>
            <div class="mun-quick-desc">Ajuste os parâmetros de cálculo de taxas de emissão de CO₂.</div>
        </a>
        <a href="<c:url value='/municipio/redefinirMeta'/>" class="mun-quick-card">
            <div class="mun-quick-icon"><i class="bi bi-sliders"></i></div>
            <div class="mun-quick-title">Redefinir Metas</div>
            <div class="mun-quick-desc">Ajuste os parâmetros de metas relativos às emissões de CO₂.</div>
        </a>
        <a href="<c:url value='/municipio/listaCidadaos'/>" class="mun-quick-card">
            <div class="mun-quick-icon"><i class="bi bi-people"></i></div>
            <div class="mun-quick-title">Lista de Cidadãos</div>
            <div class="mun-quick-desc">Consulte os cidadãos registados e os seus dados de emissões.</div>
        </a>
        <a href="<c:url value='/municipio/listaVeiculos'/>" class="mun-quick-card">
            <div class="mun-quick-icon"><i class="bi bi-car-front"></i></div>
            <div class="mun-quick-title">Lista de Veículos</div>
            <div class="mun-quick-desc">Veículos associados aos cidadãos do município.</div>
        </a>
    </div>
    <%-- COMBUSTÍVEL + GAMIFICAÇÃO --%>
    <div class="mun-grid-2">

        <%-- CO₂ por combustível --%>
        <div class="mun-card">
            <h3 class="mun-card-title">CO₂ por Combustível</h3>
            <div class="mun-combustivel-list">
                <c:forEach var="entry" items="${totalCo2PorCombustivel}">
                    <div class="mun-combustivel-row">
                        <span class="mun-combustivel-label">${entry.key}</span>
                        <span class="mun-combustivel-value">
                            <fmt:formatNumber value="${entry.value}" minFractionDigits="1" maxFractionDigits="1"/> kg
                            <span class="mun-combustivel-pct">(<fmt:formatNumber value="${percentagemCo2PorCombustivel[entry.key]}" minFractionDigits="0" maxFractionDigits="0"/>%)</span>
                        </span>
                    </div>
                </c:forEach>
            </div>
            <div class="mun-mt-16">
                <a href="<c:url value='/municipio/relatoriosMunicipio'/>" class="mun-btn-primary mun-full-width">
                    <i class="bi bi-bar-chart-line"></i> Ver Relatório Completo
                </a>
            </div>
        </div>
    </div>
    </section>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>