<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Município</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">
</head>
<body class="dashboard-body">

<jsp:include page="../navbarm.jsp"/>

<div class="dashboard-wrapper">

    <div class="dashboard-hero">
        <div>
            <h1>Dashboard do Município</h1>
            <p>
                <c:choose>
                    <c:when test="${not empty municipio}">
                        Visão geral das emissões e veículos registados de ${municipio.nome}
                    </c:when>
                    <c:otherwise>
                        Visão geral das emissões e veículos registados
                    </c:otherwise>
                </c:choose>
            </p>
        </div>
        <div class="dashboard-hero-icon">📅</div>
    </div>

    <c:if test="${not empty erro}">
        <div class="empty-state-box">
                ${erro}
        </div>
    </c:if>

    <c:if test="${empty erro}">
        <div class="cards-grid">

            <!-- 1 - Emissões Mensais -->
            <div class="info-card">
                <div class="card-label">Emissões Mensais</div>
                <div class="card-value">
                    <fmt:formatNumber value="${mediaGlobalEmissoesMensais}" minFractionDigits="2" maxFractionDigits="2"/> kg
                </div>
                <div class="card-subtext">Média mensal de CO₂</div>
            </div>

            <!-- 2 - Nº de Veículos -->
            <div class="info-card">
                <div class="card-label">Nº de Veículos</div>
                <div class="card-value">${quantidadeVeiculosTotais}</div>
                <div class="card-subtext">Total registado</div>
            </div>

            <!-- 3 - Média por Veículo -->
            <div class="info-card">
                <div class="card-label">Média por Veículo</div>
                <div class="card-value">
                    <fmt:formatNumber value="${mediaPorVeiculo}" minFractionDigits="2" maxFractionDigits="2"/> kg
                </div>
                <div class="card-subtext">CO₂ por veículo</div>
            </div>

            <!-- 4 - Mês Atual -->
            <div class="info-card destaque-card destaque-verde">
                <div class="card-label">Mês Mais Recente</div>
                <div class="card-value">
                    <fmt:formatNumber value="${mesAtualCo2}" minFractionDigits="2" maxFractionDigits="2"/> kg
                </div>
                <div class="card-subtext">${mesAtualLabel}</div>
            </div>

            <!-- 5 - Média Ano Anterior -->
            <div class="info-card destaque-card destaque-azul">
                <div class="card-label">Média Ano Anterior</div>
                <div class="card-value">
                    <fmt:formatNumber value="${mediaAnoAnterior}" minFractionDigits="2" maxFractionDigits="2"/> kg
                </div>
                <div class="card-subtext">${anoAnterior}</div>
            </div>

            <!-- 6 - Média Ano Atual -->
            <div class="info-card destaque-card destaque-roxo">
                <div class="card-label">Média Ano Atual</div>
                <div class="card-value">
                    <fmt:formatNumber value="${mediaAnoAtual}" minFractionDigits="2" maxFractionDigits="2"/> kg
                </div>
                <div class="card-subtext">${anoAtual}</div>
            </div>

            <!-- 7 - Gasolina -->
            <div class="fuel-card fuel-gasolina">
                <div class="fuel-title">Gasolina</div>
                <div class="fuel-line">
                    <span>Emissão média</span>
                    <strong>
                        <c:choose>
                            <c:when test="${not empty emissaoMediaPorCombustivel['GASOLINA']}">
                                <fmt:formatNumber value="${emissaoMediaPorCombustivel['GASOLINA']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </div>
            </div>

            <!-- 8 - Diesel -->
            <div class="fuel-card fuel-diesel">
                <div class="fuel-title">Diesel</div>
                <div class="fuel-line">
                    <span>Emissão média</span>
                    <strong>
                        <c:choose>
                            <c:when test="${not empty emissaoMediaPorCombustivel['DIESEL']}">
                                <fmt:formatNumber value="${emissaoMediaPorCombustivel['DIESEL']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </div>
            </div>

            <!-- 9 - Híbrido -->
            <div class="fuel-card fuel-hibrido">
                <div class="fuel-title">Híbrido</div>
                <div class="fuel-line">
                    <span>Emissão média</span>
                    <strong>
                        <c:choose>
                            <c:when test="${not empty emissaoMediaPorCombustivel['HIBRIDO']}">
                                <fmt:formatNumber value="${emissaoMediaPorCombustivel['HIBRIDO']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </div>
            </div>

            <!-- 10 - GPL -->
            <div class="fuel-card fuel-gpl">
                <div class="fuel-title">GPL</div>
                <div class="fuel-line">
                    <span>Emissão média</span>
                    <strong>
                        <c:choose>
                            <c:when test="${not empty emissaoMediaPorCombustivel['GPL']}">
                                <fmt:formatNumber value="${emissaoMediaPorCombustivel['GPL']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </div>
            </div>

        </div>
    </c:if>

    <div class="dashboard-actions">
        <a href="${pageContext.request.contextPath}/municipio/homeMunicipio" class="smart-btn smart-btn-secondary">
            Voltar ao Home
        </a>
    </div>
</div>

</body>
</html>