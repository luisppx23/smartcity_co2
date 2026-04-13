<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Município – Smart City</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<jsp:include page="navbarm.jsp"/>

<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">
        Dashboard do Município
        <c:if test="${not empty municipio.nome}"> — <c:out value="${municipio.nome}"/></c:if>
    </h1>
    <p class="mun-hero-subtitle">Visão geral das emissões e dos veículos registados em ${municipio.nome}</p>

    <div class="report-actions">
        <button onclick="window.print()" class="btn-print-report">
            <i class="bi bi-printer"></i>
            <span>Gerar Relatório</span>
        </button>
    </div>

</section>

<main class="mun-page-content">

    <c:if test="${not empty erro}">
        <div class="empty-state-box">
                ${erro}
        </div>
    </c:if>

    <c:if test="${empty erro}">
        <div class="dashboard-sections">

            <!-- 1ª LINHA -->
            <div class="dashboard-row row-2">

                <div class="info-card">
                    <div class="card-label">Emissões Mensais</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${mediaGlobalEmissoesMensais}" minFractionDigits="2"
                                          maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">Média mensal de CO₂</div>
                </div>

                <div class="info-card">
                    <div class="card-label">N.º de Veículos</div>
                    <div class="card-value">
                            ${quantidadeVeiculosTotais}
                    </div>
                    <div class="card-subtext">Total registado</div>
                </div>

            </div>

            <!-- 2ª LINHA -->
            <div class="dashboard-row row-2">

                <div class="info-card">
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

                <div class="info-card">
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

            <!-- GRÁFICO EVOLUÇÃO -->
            <div class="mun-card">
                <h3 class="mun-card-title">
                    <i class="bi bi-graph-up-arrow"></i> Evolução Mensal das Emissões CO₂
                </h3>
                <p class="mun-card-description">
                    Evolução das emissões totais ao longo dos meses.
                </p>
                <div class="emissoes-chart-wrap-lg">
                    <canvas id="evolucaoMensalChart"></canvas>
                </div>
            </div>

            <!-- GRÁFICOS 2 COLUNAS -->
            <div class="mun-grid-2">
                <div class="mun-card">
                    <h3 class="mun-card-title">
                        <i class="bi bi-car-front-fill"></i> Quantidade de Veículos
                    </h3>
                    <p class="mun-card-description">
                        Distribuição da frota por tipo de combustível.
                    </p>
                    <div class="emissoes-chart-wrap-md">
                        <canvas id="frotaPorTipoChart"></canvas>
                    </div>
                </div>

                <div class="mun-card">
                    <h3 class="mun-card-title">
                        <i class="bi bi-pie-chart-fill"></i> Emissões CO₂ por Combustível
                    </h3>
                    <p class="mun-card-description">
                        Distribuição das emissões totais por tipo de combustível.
                    </p>
                    <div class="emissoes-chart-wrap-md">
                        <canvas id="co2PorTipoChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- GRÁFICO KMS -->
            <div class="mun-card">
                <h3 class="mun-card-title">
                    <i class="bi bi-signpost-2-fill"></i> Quilómetros Totais por Combustível
                </h3>
                <p class="mun-card-description">
                    Total de quilómetros percorridos por tipo de veículo.
                </p>
                <div class="emissoes-chart-wrap-lg">
                    <canvas id="kmsPorTipoChart"></canvas>
                </div>
            </div>

            <!-- 3ª LINHA - CARDS POR TIPO DE COMBUSTÍVEL -->
            <div class="dashboard-row row-5">

                <div class="fuel-card fuel-gasolina">
                    <div class="fuel-title">Gasolina</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['GASOLINA'] != null ? quantidadeVeiculosPorCombustivel['GASOLINA'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['GASOLINA']}"
                                                  minFractionDigits="2" maxFractionDigits="2"/> kg</strong>
                    </div>
                </div>

                <div class="fuel-card fuel-diesel">
                    <div class="fuel-title">Diesel</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['DIESEL'] != null ? quantidadeVeiculosPorCombustivel['DIESEL'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['DIESEL']}" minFractionDigits="2"
                                                  maxFractionDigits="2"/> kg</strong>
                    </div>
                </div>

                <div class="fuel-card fuel-hibrido">
                    <div class="fuel-title">Híbrido</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['HIBRIDO'] != null ? quantidadeVeiculosPorCombustivel['HIBRIDO'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['HIBRIDO']}" minFractionDigits="2"
                                                  maxFractionDigits="2"/> kg</strong>
                    </div>
                </div>

                <div class="fuel-card fuel-gpl">
                    <div class="fuel-title">GPL</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['GPL'] != null ? quantidadeVeiculosPorCombustivel['GPL'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['GPL']}" minFractionDigits="2"
                                                  maxFractionDigits="2"/> kg</strong>
                    </div>
                </div>

                <div class="fuel-card fuel-eletrico">
                    <div class="fuel-title">Elétrico</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['ELETRICO'] != null ? quantidadeVeiculosPorCombustivel['ELETRICO'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['ELETRICO']}"
                                                  minFractionDigits="2" maxFractionDigits="2"/> kg</strong>
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
</main>

<c:if test="${empty erro}">
    <script>
        const mesesLabels = [
            <c:forEach var="mes" items="${mesesOrdenados}" varStatus="status">
            "${mes}"<c:if test="${!status.last}">, </c:if>
            </c:forEach>
        ];

        const evolucaoMensalValores = [
            <c:forEach var="mes" items="${mesesOrdenados}" varStatus="status">
            ${evolucaoEmissoesMensais[mes] != null ? evolucaoEmissoesMensais[mes] : 0}<c:if test="${!status.last}">, </c:if>
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
            '#D4A017', // gasolina
            '#1A5276', // diesel
            '#1A7A4A', // hibrido
            '#6B5CA5', // gpl
            '#0F766E'  // eletrico
        ];

        Chart.defaults.font.family = 'Outfit, sans-serif';
        Chart.defaults.color = '#6B7A8D';
        Chart.defaults.plugins.legend.labels.usePointStyle = true;
        Chart.defaults.plugins.legend.labels.boxWidth = 10;
        Chart.defaults.plugins.legend.labels.boxHeight = 10;
        Chart.defaults.plugins.legend.labels.padding = 18;
        Chart.defaults.plugins.tooltip.backgroundColor = 'rgba(0, 31, 63, 0.92)';
        Chart.defaults.plugins.tooltip.titleColor = '#FFFFFF';
        Chart.defaults.plugins.tooltip.bodyColor = '#E8F0FB';
        Chart.defaults.plugins.tooltip.padding = 12;
        Chart.defaults.plugins.tooltip.cornerRadius = 12;
        Chart.defaults.plugins.tooltip.displayColors = true;

        const axisCommon = {
            ticks: {
                color: '#6B7A8D',
                font: {
                    family: 'Outfit, sans-serif',
                    size: 12
                }
            },
            grid: {
                color: 'rgba(0, 31, 63, 0.08)',
                drawBorder: false
            },
            border: {
                display: false
            }
        };

        const ctxEvolucao = document.getElementById('evolucaoMensalChart');
        if (ctxEvolucao) {
            new Chart(ctxEvolucao, {
                type: 'line',
                data: {
                    labels: mesesLabels,
                    datasets: [{
                        label: 'Emissões CO₂ (kg)',
                        data: evolucaoMensalValores,
                        borderColor: '#001F3F',
                        backgroundColor: 'rgba(26, 82, 118, 0.10)',
                        pointBackgroundColor: '#1A5276',
                        pointBorderColor: '#FFFFFF',
                        pointBorderWidth: 2,
                        pointRadius: 4,
                        pointHoverRadius: 6,
                        fill: true,
                        tension: 0.35
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    interaction: {
                        mode: 'index',
                        intersect: false
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            align: 'start'
                        }
                    },
                    scales: {
                        x: axisCommon,
                        y: {
                            ...axisCommon,
                            beginAtZero: true,
                            ticks: {
                                color: '#6B7A8D',
                                callback: function (value) {
                                    return value + ' kg';
                                }
                            }
                        }
                    }
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
                        backgroundColor: cores,
                        borderColor: '#FFFFFF',
                        borderWidth: 2,
                        hoverOffset: 8
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    cutout: '62%',
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
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
                        backgroundColor: cores,
                        borderColor: '#FFFFFF',
                        borderWidth: 2,
                        hoverOffset: 6
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
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
                        backgroundColor: cores,
                        borderRadius: 10,
                        borderSkipped: false,
                        maxBarThickness: 48
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                            align: 'start'
                        }
                    },
                    scales: {
                        x: axisCommon,
                        y: {
                            ...axisCommon,
                            beginAtZero: true,
                            ticks: {
                                color: '#6B7A8D',
                                callback: function (value) {
                                    return value + ' km';
                                }
                            }
                        }
                    }
                }
            });
        }
    </script>
</c:if>

</body>
</html>
