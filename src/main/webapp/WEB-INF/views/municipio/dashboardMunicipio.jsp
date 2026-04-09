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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>
<body class="dashboard-body">

<jsp:include page="navbarm.jsp"/>
<div class="dashboard-wrapper">

    <div class="dashboard-hero">
        <div>
            <h1>Dashboard do Município</h1>
            <c:choose>
                <c:when test="${not empty municipio}">
                    <p>Visão geral das emissões e veículos registados de ${user.nome}</p>
                </c:when>
                <c:otherwise>
                    <p>Visão geral das emissões e veículos registados</p>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="dashboard-hero-icon">📅</div>
    </div>

    <c:if test="${not empty erro}">
        <div class="empty-state-box">
                ${erro}
        </div>
    </c:if>

    <c:if test="${empty erro}">
        <div class="dashboard-sections">

            <!-- 1ª LINHA -->
            <div class="dashboard-row row-3">

                <!-- 1 - Emissões Mensais -->
                <div class="info-card">
                    <div class="card-label">Emissões Mensais</div>
                    <div class="card-value">
                        <c:choose>
                            <c:when test="${not empty mediaGlobalEmissoesMensais}">
                                <fmt:formatNumber value="${mediaGlobalEmissoesMensais}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-subtext">Média mensal de CO₂</div>
                </div>

                <!-- 2 - Nº de Veículos -->
                <div class="info-card">
                    <div class="card-label">N.º de Veículos</div>
                    <div class="card-value">
                        <c:choose>
                            <c:when test="${not empty quantidadeVeiculosTotais}">
                                ${quantidadeVeiculosTotais}
                            </c:when>
                            <c:otherwise>
                                0
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-subtext">Total registado</div>
                </div>


                <!-- 3 - Média por Veículo -->
                <div class="info-card">
                    <div class="card-label">Média por Veículo</div>
                    <div class="card-value">
                        <c:choose>
                            <c:when test="${not empty mediaPorVeiculo}">
                                <fmt:formatNumber value="${mediaPorVeiculo}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-subtext">CO₂ por veículo</div>
                </div>

            </div>

            <!-- 2ª LINHA -->
            <div class="dashboard-row row-2">

                <!-- 4 - Média Ano Anterior -->
                <div class="info-card destaque-card destaque-azul">
                    <div class="card-label">Média Ano Anterior</div>
                    <div class="card-value">
                        <c:choose>
                            <c:when test="${not empty mediaAnoAnterior}">
                                <fmt:formatNumber value="${mediaAnoAnterior}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-subtext">
                        <c:choose>
                            <c:when test="${not empty anoAnterior}">
                                ${anoAnterior}
                            </c:when>
                            <c:otherwise>
                                -
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- 5 - Média Ano Atual -->
                <c:set var="classeAnoAtual" value="destaque-laranja"/>
                <c:if test="${not empty mediaAnoAtual and not empty mediaAnoAnterior and mediaAnoAtual > mediaAnoAnterior}">
                    <c:set var="classeAnoAtual" value="destaque-vermelho"/>
                </c:if>

                <div class="info-card destaque-card ${classeAnoAtual}">
                    <div class="card-label">Média Ano Atual</div>
                    <div class="card-value">
                        <c:choose>
                            <c:when test="${not empty mediaAnoAtual}">
                                <fmt:formatNumber value="${mediaAnoAtual}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </c:when>
                            <c:otherwise>
                                0.00 kg
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card-subtext">
                        <c:choose>
                            <c:when test="${not empty anoAtual}">
                                ${anoAtual}
                            </c:when>
                            <c:otherwise>
                                -
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>

            <!-- 3ª LINHA -->
            <div class="dashboard-row row-5">

                <!-- Gasolina -->
                <div class="fuel-card fuel-gasolina">
                    <div class="fuel-title">Gasolina</div>

                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty quantidadeVeiculosPorCombustivel and not empty quantidadeVeiculosPorCombustivel['GASOLINA']}">
                                    ${quantidadeVeiculosPorCombustivel['GASOLINA']}
                                </c:when>
                                <c:otherwise>
                                    0
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty emissaoMediaPorCombustivel and not empty emissaoMediaPorCombustivel['GASOLINA']}">
                                    <fmt:formatNumber value="${emissaoMediaPorCombustivel['GASOLINA']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                </c:when>
                                <c:otherwise>
                                    0.00 kg
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                </div>

                <!-- Híbrido -->
                <div class="fuel-card fuel-hibrido">
                    <div class="fuel-title">Híbrido</div>

                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty quantidadeVeiculosPorCombustivel and not empty quantidadeVeiculosPorCombustivel['HIBRIDO']}">
                                    ${quantidadeVeiculosPorCombustivel['HIBRIDO']}
                                </c:when>
                                <c:otherwise>
                                    0
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty emissaoMediaPorCombustivel and not empty emissaoMediaPorCombustivel['HIBRIDO']}">
                                    <fmt:formatNumber value="${emissaoMediaPorCombustivel['HIBRIDO']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                </c:when>
                                <c:otherwise>
                                    0.00 kg
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                </div>

                <!-- GPL -->
                <div class="fuel-card fuel-gpl">
                    <div class="fuel-title">GPL</div>

                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty quantidadeVeiculosPorCombustivel and not empty quantidadeVeiculosPorCombustivel['GPL']}">
                                    ${quantidadeVeiculosPorCombustivel['GPL']}
                                </c:when>
                                <c:otherwise>
                                    0
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty emissaoMediaPorCombustivel and not empty emissaoMediaPorCombustivel['GPL']}">
                                    <fmt:formatNumber value="${emissaoMediaPorCombustivel['GPL']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                </c:when>
                                <c:otherwise>
                                    0.00 kg
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                </div>

                <!-- Diesel -->
                <div class="fuel-card fuel-diesel">
                    <div class="fuel-title">Diesel</div>

                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty quantidadeVeiculosPorCombustivel and not empty quantidadeVeiculosPorCombustivel['DIESEL']}">
                                    ${quantidadeVeiculosPorCombustivel['DIESEL']}
                                </c:when>
                                <c:otherwise>
                                    0
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty emissaoMediaPorCombustivel and not empty emissaoMediaPorCombustivel['DIESEL']}">
                                    <fmt:formatNumber value="${emissaoMediaPorCombustivel['DIESEL']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                </c:when>
                                <c:otherwise>
                                    0.00 kg
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
                </div>
                <!-- Elétrico -->
                <div class="fuel-card fuel-eletrico">
                    <div class="fuel-title">Elétrico</div>

                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty quantidadeVeiculosPorCombustivel and not empty quantidadeVeiculosPorCombustivel['ELETRICO']}">
                                    ${quantidadeVeiculosPorCombustivel['ELETRICO']}
                                </c:when>
                                <c:otherwise>
                                    0
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty emissaoMediaPorCombustivel and not empty emissaoMediaPorCombustivel['ELETRICO']}">
                                    <fmt:formatNumber value="${emissaoMediaPorCombustivel['ELETRICO']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                </c:when>
                                <c:otherwise>
                                    0.00 kg
                                </c:otherwise>
                            </c:choose>
                        </strong>
                    </div>
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