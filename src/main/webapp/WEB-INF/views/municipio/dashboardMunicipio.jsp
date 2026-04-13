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
    <h1 class="mun-hero-title">Dashboard do Município</h1>
    <p class="mun-hero-subtitle">Visão geral das emissões e dos veículos registados em ${municipio.nome}</p>

    <div class="report-actions">
        <button onclick="window.print()" class="btn-print-report">
            <i class="bi bi-printer"></i>
            <span>Gerar Relatório</span>
        </button>
    </div>
</section>

<main class="mun-page-content">

    <!-- Cabeçalho do Relatório (visível apenas na impressão) -->
    <div class="report-header" style="display: none;">
        <h2>Relatório de Emissões - ${municipio.nome}</h2>
        <p>Gerado em <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/></p>
    </div>

    <!-- Texto introdutório do relatório (visível apenas na impressão) -->
    <div class="report-intro" style="display: none;">
        <p>O presente relatório apresenta uma análise detalhada das emissões de CO₂ registadas no município de <strong>${municipio.nome}</strong>. Durante o período analisado, foram registados <strong>${quantidadeVeiculosTotais} veículos</strong> associados aos cidadãos do município, que emitiram um total de <strong><fmt:formatNumber value="${totalCo2Geral}" minFractionDigits="2" maxFractionDigits="2"/> kg de dióxido de carbono</strong>, tendo percorrido aproximadamente <strong><fmt:formatNumber value="${totalKmsGeral}" minFractionDigits="2" maxFractionDigits="2"/> quilómetros</strong>.</p>

        <p>A média mensal de emissões verificada no município situa-se nos <strong><fmt:formatNumber value="${mediaGlobalEmissoesMensais}" minFractionDigits="2" maxFractionDigits="2"/> kg</strong>, enquanto a meta ambiental definida para o concelho é de <strong><fmt:formatNumber value="${municipio.objetivo_co2_mes_hab}" minFractionDigits="2" maxFractionDigits="2"/> kg por habitante por mês</strong>. Os dados apresentados nas secções seguintes permitem uma consulta aprofundada da distribuição por tipo de combustível e da evolução temporal das emissões.</p>
    </div>

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
                        <fmt:formatNumber value="${mediaGlobalEmissoesMensais}" minFractionDigits="2" maxFractionDigits="2"/> kg
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

            <!-- 2ª LINHA - agora com 3 colunas (incluindo Total em Taxas) -->
            <div class="dashboard-row row-3">
                <div class="info-card">
                    <div class="card-label">Média Ano Anterior</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${mediaAnoAnterior}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">
                        <c:choose>
                            <c:when test="${anoAnterior > 0}">${anoAnterior}</c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="info-card">
                    <div class="card-label">Média Ano Atual</div>
                    <div class="card-value
                        <c:choose>
                            <c:when test="${mediaAnoAtual <= mediaAnoAnterior}">media-melber</c:when>
                            <c:otherwise>media-pior</c:otherwise>
                        </c:choose>
                    ">
                        <c:choose>
                            <c:when test="${mediaAnoAtual <= mediaAnoAnterior}">
                                <i class="bi bi-arrow-down-short" style="font-size: 1.5rem;"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-arrow-up-short" style="font-size: 1.5rem;"></i>
                            </c:otherwise>
                        </c:choose>
                        <fmt:formatNumber value="${mediaAnoAtual}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">
                        <c:choose>
                            <c:when test="${anoAtual > 0}">${anoAtual}</c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="info-card">
                    <div class="card-label">Total em Taxas</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${totalTaxaGeral != null ? totalTaxaGeral : 0}" minFractionDigits="2" maxFractionDigits="2"/> €
                    </div>
                    <div class="card-subtext">Taxas arrecadadas (todos os períodos)</div>
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

            <!-- GRÁFICOS KMS E TAXAS LADO A LADO -->
            <div class="mun-grid-2">
                <div class="mun-card">
                    <h3 class="mun-card-title">
                        <i class="bi bi-signpost-2-fill"></i> Quilómetros Totais por Combustível
                    </h3>
                    <p class="mun-card-description">
                        Total de quilómetros percorridos por tipo de veículo.
                    </p>
                    <div class="emissoes-chart-wrap-md">
                        <canvas id="kmsPorTipoChart"></canvas>
                    </div>
                </div>

                <div class="mun-card">
                    <h3 class="mun-card-title">
                        <i class="bi bi-currency-euro"></i> Taxas Totais por Combustível
                    </h3>
                    <p class="mun-card-description">
                        Total de taxas arrecadadas por tipo de veículo (€).
                    </p>
                    <div class="emissoes-chart-wrap-md">
                        <canvas id="taxaPorTipoChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- 3ª LINHA - CARDS POR TIPO DE COMBUSTÍVEL (com Total em Taxa adicionado) -->
            <div class="dashboard-row row-5">

                <div class="fuel-card fuel-gasolina">
                    <div class="fuel-title">Gasolina</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong>${quantidadeVeiculosPorCombustivel['GASOLINA'] != null ? quantidadeVeiculosPorCombustivel['GASOLINA'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['GASOLINA']}" minFractionDigits="2" maxFractionDigits="2"/> kg</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total em Taxa</span>
                        <strong><fmt:formatNumber value="${totalTaxaPorCombustivel['GASOLINA'] != null ? totalTaxaPorCombustivel['GASOLINA'] : 0}" minFractionDigits="2" maxFractionDigits="2"/> €</strong>
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
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['DIESEL']}" minFractionDigits="2" maxFractionDigits="2"/> kg</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total em Taxa</span>
                        <strong><fmt:formatNumber value="${totalTaxaPorCombustivel['DIESEL'] != null ? totalTaxaPorCombustivel['DIESEL'] : 0}" minFractionDigits="2" maxFractionDigits="2"/> €</strong>
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
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['HIBRIDO']}" minFractionDigits="2" maxFractionDigits="2"/> kg</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total em Taxa</span>
                        <strong><fmt:formatNumber value="${totalTaxaPorCombustivel['HIBRIDO'] != null ? totalTaxaPorCombustivel['HIBRIDO'] : 0}" minFractionDigits="2" maxFractionDigits="2"/> €</strong>
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
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['GPL']}" minFractionDigits="2" maxFractionDigits="2"/> kg</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total em Taxa</span>
                        <strong><fmt:formatNumber value="${totalTaxaPorCombustivel['GPL'] != null ? totalTaxaPorCombustivel['GPL'] : 0}" minFractionDigits="2" maxFractionDigits="2"/> €</strong>
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
                        <strong><fmt:formatNumber value="${emissaoMediaPorCombustivel['ELETRICO']}" minFractionDigits="2" maxFractionDigits="2"/> kg</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total em Taxa</span>
                        <strong><fmt:formatNumber value="${totalTaxaPorCombustivel['ELETRICO'] != null ? totalTaxaPorCombustivel['ELETRICO'] : 0}" minFractionDigits="2" maxFractionDigits="2"/> €</strong>
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

    <!-- Rodapé do Relatório (visível apenas na impressão) -->
    <div class="report-footer" style="display: none;">
        <p>Smart City CO₂ - Relatório de Emissões do Município - Documento gerado automaticamente</p>
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

        // Valores para o gráfico de taxas
        const taxaPorTipoValores = [
            ${totalTaxaPorCombustivel['GASOLINA'] != null ? totalTaxaPorCombustivel['GASOLINA'] : 0},
            ${totalTaxaPorCombustivel['DIESEL'] != null ? totalTaxaPorCombustivel['DIESEL'] : 0},
            ${totalTaxaPorCombustivel['HIBRIDO'] != null ? totalTaxaPorCombustivel['HIBRIDO'] : 0},
            ${totalTaxaPorCombustivel['GPL'] != null ? totalTaxaPorCombustivel['GPL'] : 0},
            ${totalTaxaPorCombustivel['ELETRICO'] != null ? totalTaxaPorCombustivel['ELETRICO'] : 0}
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
                font: { family: 'Outfit, sans-serif', size: 12 }
            },
            grid: {
                color: 'rgba(0, 31, 63, 0.08)',
                drawBorder: false
            },
            border: { display: false }
        };

        // Gráfico de evolução
        const ctxEvolucao = document.getElementById('evolucaoMensalChart');
        if (ctxEvolucao) {
            const mediaTotalEmissoes = evolucaoMensalValores.reduce((a, b) => a + b, 0) / evolucaoMensalValores.length;
            const metaTotal = ${municipio.objetivo_co2_mes_hab};
            new Chart(ctxEvolucao, {
                type: 'line',
                data: {
                    labels: mesesLabels,
                    datasets: [
                        {
                            label: 'Emissões Totais (kg)',
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
                        },
                        {
                            label: 'Média Total (kg)',
                            data: mesesLabels.map(function() { return mediaTotalEmissoes; }),
                            borderColor: '#D4A017',
                            backgroundColor: 'transparent',
                            borderWidth: 2,
                            borderDash: [5, 5],
                            fill: false,
                            pointRadius: 0
                        },
                        {
                            label: 'Meta Total (kg)',
                            data: mesesLabels.map(function() { return metaTotal; }),
                            borderColor: '#C0392B',
                            backgroundColor: 'transparent',
                            borderWidth: 2,
                            borderDash: [8, 4],
                            fill: false,
                            pointRadius: 0
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    interaction: { mode: 'index', intersect: false },
                    plugins: {
                        legend: { display: true, position: 'top', align: 'start' },
                        tooltip: { callbacks: { label: function(ctx) { return ctx.dataset.label + ': ' + ctx.raw.toFixed(2) + ' kg'; } } }
                    },
                    scales: {
                        x: axisCommon,
                        y: { ...axisCommon, beginAtZero: true, title: { display: true, text: 'Emissões Totais (kg)', color: '#001F3F' }, ticks: { callback: (v) => v.toFixed(0) + ' kg' } }
                    }
                }
            });
        }

        // Gráfico de quantidade de veículos (doughnut)
        const ctxFrota = document.getElementById('frotaPorTipoChart');
        if (ctxFrota) {
            new Chart(ctxFrota, {
                type: 'doughnut',
                data: { labels: combustivelLabels, datasets: [{ data: frotaValores, backgroundColor: cores, borderColor: '#FFFFFF', borderWidth: 2, hoverOffset: 8 }] },
                options: { responsive: true, maintainAspectRatio: false, cutout: '62%', plugins: { legend: { position: 'bottom' } } }
            });
        }

        // Gráfico de emissões CO₂ por combustível (pie)
        const ctxCo2 = document.getElementById('co2PorTipoChart');
        if (ctxCo2) {
            new Chart(ctxCo2, {
                type: 'pie',
                data: { labels: combustivelLabels, datasets: [{ data: co2PorTipoValores, backgroundColor: cores, borderColor: '#FFFFFF', borderWidth: 2, hoverOffset: 6 }] },
                options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'bottom' } } }
            });
        }

        // Gráfico de KMs por combustível (bar)
        const ctxKms = document.getElementById('kmsPorTipoChart');
        if (ctxKms) {
            new Chart(ctxKms, {
                type: 'bar',
                data: { labels: combustivelLabels, datasets: [{ label: 'Km totais', data: kmsPorTipoValores, backgroundColor: cores, borderRadius: 10, borderSkipped: false, maxBarThickness: 48 }] },
                options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: true, position: 'top', align: 'start' } }, scales: { x: axisCommon, y: { ...axisCommon, beginAtZero: true, ticks: { callback: (v) => v + ' km' } } } }
            });
        }

        // NOVO: Gráfico de taxas por combustível (bar)
        const ctxTaxa = document.getElementById('taxaPorTipoChart');
        if (ctxTaxa) {
            new Chart(ctxTaxa, {
                type: 'bar',
                data: { labels: combustivelLabels, datasets: [{ label: 'Taxa total (€)', data: taxaPorTipoValores, backgroundColor: cores, borderRadius: 10, borderSkipped: false, maxBarThickness: 48 }] },
                options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: true, position: 'top', align: 'start' } }, scales: { x: axisCommon, y: { ...axisCommon, beginAtZero: true, ticks: { callback: (v) => v + ' €' } } } }
            });
        }
    </script>
</c:if>

<script>
    // Alterar o título da página para o nome desejado no PDF
    document.title = "Relatório Municipal de Emissões - ${municipio.nome} - <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd-MM-yyyy"/>";

    const btnPrint = document.querySelector('.btn-print-report');
    if (btnPrint) {
        btnPrint.addEventListener('click', function() {
            const originalTitle = document.title;
            document.title = "Relatório Municipal de Emissões - ${municipio.nome} - <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd-MM-yyyy"/>";
            setTimeout(function() { document.title = originalTitle; }, 100);
        });
    }
</script>

</body>
</html>