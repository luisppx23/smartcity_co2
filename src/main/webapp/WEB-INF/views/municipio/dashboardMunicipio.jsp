<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Município – Smart City</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body class="dashboard-body">

<jsp:include page="navbarm.jsp"/>

<div class="dashboard-wrapper">

    <div class="dashboard-hero">
        <div>
            <h1>Dashboard do Município</h1>
            <c:choose>
                <c:when test="${not empty municipio}">
                    <p>Visão geral das emissões e veículos registados de ${municipio.nome}</p>
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
                        <fmt:formatNumber value="${mediaGlobalEmissoesMensais}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">Média mensal de CO₂</div>
                </div>

                <!-- 2 - Nº de Veículos -->
                <div class="info-card">
                    <div class="card-label">N.º de Veículos</div>
                    <div class="card-value">
                            ${quantidadeVeiculosTotais}
                    </div>
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

            </div>

            <!-- 2ª LINHA -->
            <div class="dashboard-row row-2">

                <!-- 4 - Média Ano Anterior -->
                <div class="info-card destaque-card destaque-azul">
                    <div class="card-label">Média Ano Anterior</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${mediaAnoAnterior}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">
                        <c:choose>
                            <c:when test="${anoAnterior > 0}">
                                ${anoAnterior}
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- 5 - Média Ano Atual -->
                <c:set var="classeAnoAtual" value="destaque-laranja"/>
                <c:if test="${mediaAnoAtual > mediaAnoAnterior}">
                    <c:set var="classeAnoAtual" value="destaque-vermelho"/>
                </c:if>

                <div class="info-card destaque-card ${classeAnoAtual}">
                    <div class="card-label">Média Ano Atual</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${mediaAnoAtual}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">
                        <c:choose>
                            <c:when test="${anoAtual > 0}">
                                ${anoAtual}
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </div>
                </div>

            </div>

            <!-- GRÁFICO: Evolução Mensal das Emissões -->
            <div class="mun-card">
                <h3 class="mun-card-title">📈 Evolução Mensal das Emissões CO₂</h3>
                <p class="mun-card-description">Evolução das emissões totais ao longo dos meses.</p>
                <div class="emissoes-chart-wrap-lg">
                    <canvas id="evolucaoMensalChart"></canvas>
                </div>
            </div>

            <!-- GRÁFICOS: Frota por Tipo de Veículo -->
            <div class="mun-grid-2">
                <div class="mun-card">
                    <h3 class="mun-card-title">🚗 Quantidade de Veículos</h3>
                    <p class="mun-card-description">Distribuição da frota por tipo de combustível.</p>
                    <div class="emissoes-chart-wrap-md">
                        <canvas id="frotaPorTipoChart"></canvas>
                    </div>
                </div>

                <div class="mun-card">
                    <h3 class="mun-card-title">📊 Emissões CO₂ por Combustível</h3>
                    <p class="mun-card-description">Distribuição das emissões totais por tipo.</p>
                    <div class="emissoes-chart-wrap-md">
                        <canvas id="co2PorTipoChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- GRÁFICO: Quilómetros por Tipo -->
            <div class="mun-card">
                <h3 class="mun-card-title">🛣️ Quilómetros Totais por Combustível</h3>
                <p class="mun-card-description">Total de quilómetros percorridos por tipo de veículo.</p>
                <div class="emissoes-chart-wrap-lg">
                    <canvas id="kmsPorTipoChart"></canvas>
                </div>
            </div>

            <!-- 3ª LINHA -->
            <div class="dashboard-row row-5">

                <!-- Gasolina -->
                <div class="fuel-card fuel-gasolina">
                    <div class="fuel-title">Gasolina</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['GASOLINA']}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <fmt:formatNumber value="${emissaoMediaPorCombustivel['GASOLINA']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </strong>
                    </div>
                </div>

                <!-- Híbrido -->
                <div class="fuel-card fuel-hibrido">
                    <div class="fuel-title">Híbrido</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['HIBRIDO']}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <fmt:formatNumber value="${emissaoMediaPorCombustivel['HIBRIDO']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </strong>
                    </div>
                </div>

                <!-- GPL -->
                <div class="fuel-card fuel-gpl">
                    <div class="fuel-title">GPL</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['GPL']}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <fmt:formatNumber value="${emissaoMediaPorCombustivel['GPL']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </strong>
                    </div>
                </div>

                <!-- Diesel -->
                <div class="fuel-card fuel-diesel">
                    <div class="fuel-title">Diesel</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['DIESEL']}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <fmt:formatNumber value="${emissaoMediaPorCombustivel['DIESEL']}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </strong>
                    </div>
                </div>

                <!-- Elétrico -->
                <div class="fuel-card fuel-eletrico">
                    <div class="fuel-title">Elétrico</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['ELETRICO']}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong>
                            <fmt:formatNumber value="${emissaoMediaPorCombustivel['ELETRICO']}" minFractionDigits="2" maxFractionDigits="2"/> kg
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

<c:if test="${empty erro}">
    <script>
        const mesesLabels = [
            <c:forEach var="mes" items="${mesesOrdenados}" varStatus="status">
            "${mes}"<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        const evolucaoMensalValores = [
            <c:forEach var="mes" items="${mesesOrdenados}" varStatus="status">
            ${evolucaoEmissoesMensais[mes] != null ? evolucaoEmissoesMensais[mes] : 0}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        const combustivelLabels = [
            "GASOLINA", "DIESEL", "HIBRIDO", "GPL", "ELETRICO"
        ];

        const frotaValores = [
            ${quantidadeVeiculosPorCombustivel['GASOLINA'] != null ? quantidadeVeiculosPorCombustivel['GASOLINA'] : 0},
            ${quantidadeVeiculosPorCombustivel['DIESEL'] != null ? quantidadeVeiculosPorCombustivel['DIESEL'] : 0},
            ${quantidadeVeiculosPorCombustivel['HIBRIDO'] != null ? quantidadeVeiculosPorCombustivel['HIBRIDO'] : 0},
            ${quantidadeVeiculosPorCombustivel['GPL'] != null ? quantidadeVeiculosPorCombustivel['GPL'] : 0},
            ${quantidadeVeiculosPorCombustivel['ELETRICO'] != null ? quantidadeVeiculosPorCombustivel['ELETRICO'] : 0}
        ];

        const co2PorTipoValores = [
            ${totalCo2PorCombustivel['GASOLINA'] != null ? totalCo2PorCombustivel['GASOLINA'] : 0},
            ${totalCo2PorCombustivel['DIESEL'] != null ? totalCo2PorCombustivel['DIESEL'] : 0},
            ${totalCo2PorCombustivel['HIBRIDO'] != null ? totalCo2PorCombustivel['HIBRIDO'] : 0},
            ${totalCo2PorCombustivel['GPL'] != null ? totalCo2PorCombustivel['GPL'] : 0},
            ${totalCo2PorCombustivel['ELETRICO'] != null ? totalCo2PorCombustivel['ELETRICO'] : 0}
        ];

        const kmsPorTipoValores = [
            ${totalKmsPorCombustivel['GASOLINA'] != null ? totalKmsPorCombustivel['GASOLINA'] : 0},
            ${totalKmsPorCombustivel['DIESEL'] != null ? totalKmsPorCombustivel['DIESEL'] : 0},
            ${totalKmsPorCombustivel['HIBRIDO'] != null ? totalKmsPorCombustivel['HIBRIDO'] : 0},
            ${totalKmsPorCombustivel['GPL'] != null ? totalKmsPorCombustivel['GPL'] : 0},
            ${totalKmsPorCombustivel['ELETRICO'] != null ? totalKmsPorCombustivel['ELETRICO'] : 0}
        ];

        const cores = [
            "#FF6384", // gasolina
            "#36A2EB", // diesel
            "#FFCE56", // hibrido
            "#4BC0C0", // gpl
            "#9966FF"  // eletrico
        ];

        const ctxEvolucao = document.getElementById('evolucaoMensalChart');
        if (ctxEvolucao) {
            new Chart(ctxEvolucao, {
                type: 'line',
                data: {
                    labels: mesesLabels,
                    datasets: [{
                        label: 'Emissões CO₂ (kg)',
                        data: evolucaoMensalValores,
                        borderColor: '#1d4ed8',
                        backgroundColor: 'rgba(29, 78, 216, 0.12)',
                        fill: true,
                        tension: 0.3
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
        }

        const ctxFrota = document.getElementById('frotaPorTipoChart');
        if (ctxFrota) {
            new Chart(ctxFrota, {
                type: 'doughnut',
                data: {
                    labels: combustivelLabels,
                    datasets: [{
                        data: frotaValores,
                        backgroundColor: cores
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
        }

        const ctxCo2 = document.getElementById('co2PorTipoChart');
        if (ctxCo2) {
            new Chart(ctxCo2, {
                type: 'pie',
                data: {
                    labels: combustivelLabels,
                    datasets: [{
                        data: co2PorTipoValores,
                        backgroundColor: cores
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
        }

        const ctxKms = document.getElementById('kmsPorTipoChart');
        if (ctxKms) {
            new Chart(ctxKms, {
                type: 'bar',
                data: {
                    labels: combustivelLabels,
                    datasets: [{
                        label: 'Km totais',
                        data: kmsPorTipoValores,
                        backgroundColor: cores
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false
                }
            });
        }

        console.log("mesesLabels:", mesesLabels);
        console.log("evolucaoMensalValores:", evolucaoMensalValores);
        console.log("frotaValores:", frotaValores);
        console.log("co2PorTipoValores:", co2PorTipoValores);
        console.log("kmsPorTipoValores:", kmsPorTipoValores);
    </script>
</c:if>

</body>
</html>