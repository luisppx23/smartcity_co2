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
    <link rel="stylesheet" href="/styles/base-municipio.css">
    <link rel="stylesheet" href="/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="/styles/municipio/dashboardm.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

<jsp:include page="navbarm.jsp"/>

<main class="mun-page-content">

    <c:choose>
        <c:when test="${empty totalCo2Geral and empty totalKmsGeral}">
            <div class="mun-alert mun-alert-info">
                <i class="bi bi-info-circle"></i>
                Ainda não existem dados de emissões registados para este município.
            </div>
        </c:when>
        <c:otherwise>

            <!-- MÉTRICAS DE TOPO -->
            <div class="emissoes-metrics">
                <div class="emissoes-metric-card">
                    <span class="emissoes-metric-icon"><i class="bi bi-car-front"></i></span>
                    <span class="emissoes-metric-label">Total Veículos</span>
                    <span class="emissoes-metric-value">${quantidadeVeiculosTotais}</span>
                </div>
                <div class="emissoes-metric-card">
                    <span class="emissoes-metric-icon emissoes-metric-icon--gold"><i class="bi bi-cloud-haze2"></i></span>
                    <span class="emissoes-metric-label">CO₂ Total</span>
                    <span class="emissoes-metric-value">
                        <fmt:formatNumber value="${totalCo2Geral}" pattern="#,##0.0"/> kg
                    </span>
                </div>
                <div class="emissoes-metric-card">
                    <span class="emissoes-metric-icon emissoes-metric-icon--rank"><i class="bi bi-speedometer2"></i></span>
                    <span class="emissoes-metric-label">Total KMs</span>
                    <span class="emissoes-metric-value">
                        <fmt:formatNumber value="${totalKmsGeral}" pattern="#,##0.0"/> km
                    </span>
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

            <!-- CARDS POR TIPO DE COMBUSTÍVEL -->
            <div class="dashboard-section-title">
                <h2>📊 Estatísticas por Tipo de Combustível</h2>
            </div>
            <div class="dashboard-row row-5">
                <!-- Gasolina -->
                <div class="fuel-card fuel-gasolina">
                    <div class="fuel-title">Gasolina</div>
                    <div class="fuel-info">
                        <span>Veículos</span>
                        <strong>${quantidadeVeiculosPorCombustivel['GASOLINA'] != null ? quantidadeVeiculosPorCombustivel['GASOLINA'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong><fmt:formatNumber value="${totalKmsPorCombustivel['GASOLINA']}" pattern="#,##0.0"/> km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total CO₂</span>
                        <strong><fmt:formatNumber value="${totalCo2PorCombustivel['GASOLINA']}" pattern="#,##0.0"/> kg</strong>
                    </div>
                </div>

                <!-- Diesel -->
                <div class="fuel-card fuel-diesel">
                    <div class="fuel-title">Diesel</div>
                    <div class="fuel-info">
                        <span>Veículos</span>
                        <strong>${quantidadeVeiculosPorCombustivel['DIESEL'] != null ? quantidadeVeiculosPorCombustivel['DIESEL'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong><fmt:formatNumber value="${totalKmsPorCombustivel['DIESEL']}" pattern="#,##0.0"/> km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total CO₂</span>
                        <strong><fmt:formatNumber value="${totalCo2PorCombustivel['DIESEL']}" pattern="#,##0.0"/> kg</strong>
                    </div>
                </div>

                <!-- Híbrido -->
                <div class="fuel-card fuel-hibrido">
                    <div class="fuel-title">Híbrido</div>
                    <div class="fuel-info">
                        <span>Veículos</span>
                        <strong>${quantidadeVeiculosPorCombustivel['HIBRIDO'] != null ? quantidadeVeiculosPorCombustivel['HIBRIDO'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong><fmt:formatNumber value="${totalKmsPorCombustivel['HIBRIDO']}" pattern="#,##0.0"/> km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total CO₂</span>
                        <strong><fmt:formatNumber value="${totalCo2PorCombustivel['HIBRIDO']}" pattern="#,##0.0"/> kg</strong>
                    </div>
                </div>

                <!-- GPL -->
                <div class="fuel-card fuel-gpl">
                    <div class="fuel-title">GPL</div>
                    <div class="fuel-info">
                        <span>Veículos</span>
                        <strong>${quantidadeVeiculosPorCombustivel['GPL'] != null ? quantidadeVeiculosPorCombustivel['GPL'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong><fmt:formatNumber value="${totalKmsPorCombustivel['GPL']}" pattern="#,##0.0"/> km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total CO₂</span>
                        <strong><fmt:formatNumber value="${totalCo2PorCombustivel['GPL']}" pattern="#,##0.0"/> kg</strong>
                    </div>
                </div>

                <!-- Elétrico -->
                <div class="fuel-card fuel-eletrico">
                    <div class="fuel-title">Elétrico</div>
                    <div class="fuel-info">
                        <span>Veículos</span>
                        <strong>${quantidadeVeiculosPorCombustivel['ELETRICO'] != null ? quantidadeVeiculosPorCombustivel['ELETRICO'] : 0}</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong><fmt:formatNumber value="${totalKmsPorCombustivel['ELETRICO']}" pattern="#,##0.0"/> km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total CO₂</span>
                        <strong><fmt:formatNumber value="${totalCo2PorCombustivel['ELETRICO']}" pattern="#,##0.0"/> kg</strong>
                    </div>
                </div>
            </div>

            <!-- BOTÃO VOLTAR -->
            <div class="dashboard-actions">
                <a href="${pageContext.request.contextPath}/municipio/dashboardMunicipio" class="smart-btn smart-btn-secondary">
                    <i class="bi bi-arrow-left"></i> Voltar ao Dashboard
                </a>
            </div>

        </c:otherwise>
    </c:choose>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Cores padrão para gráficos
    const CORES = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'];

    // Dados para gráficos
    var tipos = [];
    var quantidades = [];
    var emissoes = [];
    var kms = [];

    <c:forEach var="entry" items="${quantidadeVeiculosPorCombustivel}">
    tipos.push('${entry.key}');
    quantidades.push(${entry.value});
    </c:forEach>

    <c:forEach var="entry" items="${totalCo2PorCombustivel}">
    emissoes.push(${entry.value});
    </c:forEach>

    <c:forEach var="entry" items="${totalKmsPorCombustivel}">
    kms.push(${entry.value});
    </c:forEach>

    // Dados para evolução mensal
    var meses = [];
    var emissoesMensais = [];

    <c:forEach var="entry" items="${totalCo2PorMes}">
    meses.push('${entry.key}');
    emissoesMensais.push(${entry.value});
    </c:forEach>

    // Gráfico de evolução mensal (linhas)
    if (document.getElementById('evolucaoMensalChart') && meses.length > 0) {
        new Chart(document.getElementById('evolucaoMensalChart'), {
            type: 'line',
            data: {
                labels: meses,
                datasets: [{
                    label: 'Emissões CO₂ (kg)',
                    data: emissoesMensais,
                    borderColor: '#001F3F',
                    backgroundColor: 'rgba(0, 31, 63, 0.1)',
                    tension: 0.4,
                    fill: true,
                    pointRadius: 4,
                    pointHoverRadius: 6,
                    pointBackgroundColor: '#001F3F'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.raw.toFixed(2) + ' kg CO₂';
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        title: { display: true, text: 'Emissões (kg CO₂)', font: { family: 'Outfit', size: 12 } },
                        beginAtZero: true,
                        ticks: { callback: function(v) { return v.toFixed(0); } }
                    },
                    x: {
                        title: { display: true, text: 'Mês/Ano', font: { family: 'Outfit', size: 12 } }
                    }
                }
            }
        });
    }

    // Gráfico de quantidade por tipo (pizza)
    if (document.getElementById('frotaPorTipoChart') && tipos.length > 0) {
        new Chart(document.getElementById('frotaPorTipoChart'), {
            type: 'pie',
            data: {
                labels: tipos,
                datasets: [{
                    data: quantidades,
                    backgroundColor: CORES.slice(0, tipos.length),
                    borderWidth: 0,
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: { position: 'bottom', labels: { font: { size: 11, family: 'Outfit' }, boxWidth: 10, padding: 12 } },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = total > 0 ? ((context.raw / total) * 100).toFixed(1) : 0;
                                return `${context.label}: ${context.raw} veículos (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    }

    // Gráfico de emissões por tipo (pizza)
    if (document.getElementById('co2PorTipoChart') && tipos.length > 0) {
        new Chart(document.getElementById('co2PorTipoChart'), {
            type: 'pie',
            data: {
                labels: tipos,
                datasets: [{
                    data: emissoes,
                    backgroundColor: CORES.slice(0, tipos.length),
                    borderWidth: 0,
                    hoverOffset: 4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: { position: 'bottom', labels: { font: { size: 11, family: 'Outfit' }, boxWidth: 10, padding: 12 } },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = total > 0 ? ((context.raw / total) * 100).toFixed(1) : 0;
                                return `${context.label}: ${context.raw.toFixed(2)} kg (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    }

    // Gráfico de KMs por tipo (barras)
    if (document.getElementById('kmsPorTipoChart') && tipos.length > 0) {
        new Chart(document.getElementById('kmsPorTipoChart'), {
            type: 'bar',
            data: {
                labels: tipos,
                datasets: [{
                    label: 'Quilómetros Totais (km)',
                    data: kms,
                    backgroundColor: 'rgba(0, 31, 63, 0.7)',
                    borderRadius: 8,
                    barPercentage: 0.65
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.raw.toFixed(2) + ' km';
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        title: { display: true, text: 'Quilómetros (km)', font: { family: 'Outfit', size: 12 } },
                        ticks: { callback: function(v) { return v.toFixed(0); } }
                    },
                    x: {
                        title: { display: true, text: 'Tipo de Combustível', font: { family: 'Outfit', size: 12 } }
                    }
                }
            }
        });
    }
</script>

<style>
    /* Estilos adicionais para consistência com o template do cidadão */
    .emissoes-metrics {
        display: flex;
        gap: 1.5rem;
        margin-bottom: 2rem;
        flex-wrap: wrap;
    }

    .emissoes-metric-card {
        background: var(--mun-card-bg);
        backdrop-filter: var(--mun-card-blur);
        border: var(--mun-card-border);
        border-radius: var(--mun-card-radius);
        padding: 1.5rem;
        flex: 1;
        min-width: 180px;
        text-align: center;
        transition: transform 0.2s;
    }

    .emissoes-metric-card:hover {
        transform: translateY(-3px);
    }

    .emissoes-metric-icon {
        font-size: 2rem;
        display: block;
        margin-bottom: 0.5rem;
    }

    .emissoes-metric-icon--gold {
        color: #D4AF37;
    }

    .emissoes-metric-icon--rank {
        color: #f59e0b;
    }

    .emissoes-metric-label {
        font-size: 0.8rem;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        color: var(--mun-text-muted);
        margin-bottom: 0.5rem;
    }

    .emissoes-metric-value {
        font-size: 2rem;
        font-weight: 700;
        color: var(--mun-navy);
    }

    .emissoes-chart-wrap-lg {
        position: relative;
        width: 100%;
        min-height: 320px;
    }

    .emissoes-chart-wrap-md {
        position: relative;
        width: 100%;
        min-height: 280px;
    }

    .dashboard-section-title {
        margin: 2rem 0 1rem;
    }

    .dashboard-section-title h2 {
        font-size: 1.3rem;
        font-weight: 600;
        color: var(--mun-navy);
    }

    .dashboard-row {
        display: grid;
        gap: 1.5rem;
        margin-bottom: 2rem;
    }

    .row-5 {
        grid-template-columns: repeat(5, 1fr);
    }

    .fuel-card {
        background: var(--mun-card-bg);
        backdrop-filter: var(--mun-card-blur);
        border: var(--mun-card-border);
        border-radius: var(--mun-card-radius);
        padding: 1.5rem;
        text-align: center;
        transition: transform 0.2s;
        border-top: 4px solid;
    }

    .fuel-card:hover {
        transform: translateY(-3px);
    }

    .fuel-gasolina { border-top-color: #FF6384; }
    .fuel-diesel { border-top-color: #36A2EB; }
    .fuel-hibrido { border-top-color: #FFCE56; }
    .fuel-gpl { border-top-color: #4BC0C0; }
    .fuel-eletrico { border-top-color: #9966FF; }

    .fuel-title {
        font-size: 1.1rem;
        font-weight: 700;
        margin-bottom: 1rem;
    }

    .fuel-info {
        margin: 0.75rem 0;
    }

    .fuel-info span {
        font-size: 0.75rem;
        color: var(--mun-text-muted);
        display: block;
    }

    .fuel-info strong {
        font-size: 1.2rem;
        font-weight: 700;
        color: var(--mun-navy);
    }

    .dashboard-actions {
        text-align: center;
        margin-top: 2rem;
    }

    .smart-btn-secondary {
        background: rgba(0, 31, 63, 0.08);
        border: 1px solid rgba(0, 31, 63, 0.2);
        border-radius: 12px;
        padding: 0.75rem 1.5rem;
        font-weight: 600;
        color: var(--mun-navy);
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
        transition: all 0.2s;
    }

    .smart-btn-secondary:hover {
        background: rgba(0, 31, 63, 0.15);
        text-decoration: none;
        color: var(--mun-navy);
    }

    @media (max-width: 1200px) {
        .row-5 {
            grid-template-columns: repeat(3, 1fr);
        }
    }

    @media (max-width: 768px) {
        .row-5 {
            grid-template-columns: 1fr;
        }

        .emissoes-metrics {
            flex-direction: column;
        }

        .mun-grid-2 {
            grid-template-columns: 1fr;
        }
    }
</style>

</body>
</html>