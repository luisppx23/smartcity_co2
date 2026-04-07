<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Município - Monitorização CO2</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .chart-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .dashboard-section-title {
            margin: 30px 0 20px 0;
            padding-bottom: 10px;
            border-bottom: 2px solid #e0e0e0;
        }
        .dashboard-section-title h2 {
            color: #333;
            font-size: 1.5rem;
        }
        .row {
            display: flex;
            flex-wrap: wrap;
            margin-right: -15px;
            margin-left: -15px;
        }
        .col-md-6, .col-md-12 {
            position: relative;
            width: 100%;
            padding-right: 15px;
            padding-left: 15px;
        }
        @media (min-width: 768px) {
            .col-md-6 { flex: 0 0 50%; max-width: 50%; }
            .col-md-12 { flex: 0 0 100%; max-width: 100%; }
        }
        .loading-spinner {
            text-align: center;
            padding: 40px;
            color: #666;
        }
        .stats-summary {
            display: flex;
            justify-content: space-between;
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-box {
            flex: 1;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px;
            padding: 20px;
            text-align: center;
        }
        .stat-box h3 {
            font-size: 2rem;
            margin: 10px 0;
        }
        .empty-state-box {
            text-align: center;
            padding: 40px;
            color: #666;
            font-size: 16px;
        }
    </style>
</head>
<body class="dashboard-body">

<jsp:include page="../navbarm.jsp"/>

<div class="dashboard-wrapper">

    <div class="dashboard-hero">
        <div>
            <h1>Dashboard de Emissões do Município</h1>
            <p>Análise gráfica das emissões e frota de veículos</p>
        </div>
        <div class="dashboard-hero-icon">📊</div>
    </div>

    <div id="dashboardContent">
        <div class="loading-spinner">
            Carregando dados...
        </div>
    </div>

    <div class="dashboard-actions">
        <a href="${pageContext.request.contextPath}/municipio/homeMunicipio" class="smart-btn smart-btn-secondary">
            Voltar ao Home
        </a>
    </div>
</div>

<script>
    function carregarDadosDashboard() {
        console.log("Carregando dados do município...");

        fetch('/api/charts/municipio/dados-dashboard')
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("Dados recebidos:", data);
                renderizarDashboard(data);
            })
            .catch(error => {
                console.error('Erro ao carregar dados:', error);
                document.getElementById('dashboardContent').innerHTML = `
                    <div class="empty-state-box">
                        Erro ao carregar os dados: ${error.message}<br>
                        Verifique se está autenticado.
                    </div>
                `;
            });
    }

    function renderizarDashboard(data) {
        const container = document.getElementById('dashboardContent');

        container.innerHTML = `
            <!-- Cards Resumo -->
            <div class="stats-summary">
                <div class="stat-box">
                    <h5>Total Veículos</h5>
                    <h3 id="totalVeiculos">-</h3>
                </div>
                <div class="stat-box" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                    <h5>Total CO₂</h5>
                    <h3 id="totalCO2">- kg</h3>
                </div>
                <div class="stat-box" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                    <h5>Total KMs</h5>
                    <h3 id="totalKMs">- km</h3>
                </div>
            </div>

            <!-- Gráfico 1: Evolução Anual das Emissões -->
            <div class="dashboard-section-title">
                <h2>📈 Evolução Anual das Emissões CO₂</h2>
            </div>
            <div class="chart-container">
                <canvas id="evolucaoAnualChart" height="200"></canvas>
            </div>

            <!-- Gráficos de Pizza -->
            <div class="dashboard-section-title">
                <h2>🚗 Análise da Frota por Tipo de Veículo</h2>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <div class="chart-container">
                        <h3>Quantidade de Veículos</h3>
                        <canvas id="frotaPorTipoChart" height="200"></canvas>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="chart-container">
                        <h3>Emissões Totais de CO₂</h3>
                        <canvas id="co2PorTipoChart" height="200"></canvas>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="chart-container">
                        <h3>Quilómetros Totais por Tipo</h3>
                        <canvas id="kmsPorTipoChart" height="200"></canvas>
                    </div>
                </div>
            </div>

            <!-- Cards por Tipo de Combustível -->
            <div class="dashboard-section-title">
                <h2>📊 Emissões Médias por Tipo de Combustível</h2>
            </div>
            <div class="dashboard-row row-5">
                <!-- Gasolina -->
                <div class="fuel-card fuel-gasolina">
                    <div class="fuel-title">Gasolina</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong id="gasolinaQtd">0</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong id="gasolinaKms">0 km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong id="gasolinaCo2">0 kg</strong>
                    </div>
                </div>

                <!-- Diesel -->
                <div class="fuel-card fuel-diesel">
                    <div class="fuel-title">Diesel</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong id="dieselQtd">0</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong id="dieselKms">0 km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong id="dieselCo2">0 kg</strong>
                    </div>
                </div>

                <!-- Híbrido -->
                <div class="fuel-card fuel-hibrido">
                    <div class="fuel-title">Híbrido</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong id="hibridoQtd">0</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong id="hibridoKms">0 km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong id="hibridoCo2">0 kg</strong>
                    </div>
                </div>

                <!-- GPL -->
                <div class="fuel-card fuel-gpl">
                    <div class="fuel-title">GPL</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong id="gplQtd">0</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong id="gplKms">0 km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong id="gplCo2">0 kg</strong>
                    </div>
                </div>

                <!-- Elétrico -->
                <div class="fuel-card fuel-eletrico">
                    <div class="fuel-title">Elétrico</div>
                    <div class="fuel-info">
                        <span>Veículos registados</span>
                        <strong id="eletricoQtd">0</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Total KMs</span>
                        <strong id="eletricoKms">0 km</strong>
                    </div>
                    <div class="fuel-info">
                        <span>Emissão média</span>
                        <strong id="eletricoCo2">0 kg</strong>
                    </div>
                </div>
            </div>
        </div>
        `;

        // Atualizar cards resumo
        calcularTotais(data);

        // Criar gráficos
        setTimeout(() => {
            // Gráfico de evolução anual
            if (data.anos && data.anos.length > 0 && data.emissoesAnuais && data.emissoesAnuais.length > 0) {
                criarGraficoEvolucaoAnual(data.anos, data.emissoesAnuais);
            }

            // Gráficos de pizza e barra
            if (data.frotaPorTipo && data.frotaPorTipo.length > 0) {
                criarGraficoPizza('frotaPorTipoChart', 'Quantidade de Veículos', data.frotaPorTipo, 'quantidade');
            }
            if (data.co2PorTipo && data.co2PorTipo.length > 0) {
                criarGraficoPizza('co2PorTipoChart', 'Emissões CO₂ (kg)', data.co2PorTipo, 'co2');
            }
            if (data.kmsPorTipo && data.kmsPorTipo.length > 0) {
                criarGraficoBarra('kmsPorTipoChart', 'Quilómetros por Tipo de Veículo', data.kmsPorTipo, 'kms');
            }

            // Preencher cards
            preencherCards(data);
        }, 100);
    }

    function calcularTotais(data) {
        let totalVeiculos = 0;
        let totalCO2 = 0;
        let totalKMs = 0;

        if (data.frotaPorTipo) {
            for (const item of data.frotaPorTipo) {
                totalVeiculos += item.quantidade;
            }
        }
        if (data.co2PorTipo) {
            for (const item of data.co2PorTipo) {
                totalCO2 += item.co2;
            }
        }
        if (data.kmsPorTipo) {
            for (const item of data.kmsPorTipo) {
                totalKMs += item.kms;
            }
        }

        document.getElementById('totalVeiculos').innerText = totalVeiculos;
        document.getElementById('totalCO2').innerText = totalCO2.toFixed(2) + ' kg';
        document.getElementById('totalKMs').innerText = totalKMs.toFixed(1) + ' km';
    }

    let evolucaoAnualChart = null;
    function criarGraficoEvolucaoAnual(anos, emissoes) {
        const canvas = document.getElementById('evolucaoAnualChart');
        if (!canvas) return;

        const ctx = canvas.getContext('2d');
        if (evolucaoAnualChart) evolucaoAnualChart.destroy();

        evolucaoAnualChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: anos,
                datasets: [{
                    label: 'Emissões Totais CO₂ (kg)',
                    data: emissoes,
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.4,
                    fill: true,
                    pointRadius: 5,
                    pointHoverRadius: 7,
                    pointBackgroundColor: 'rgb(75, 192, 192)'
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
                        title: { display: true, text: 'Emissões (kg CO₂)' },
                        beginAtZero: true
                    },
                    x: {
                        title: { display: true, text: 'Ano' }
                    }
                }
            }
        });
    }

    let pizzaCharts = {};
    function criarGraficoPizza(canvasId, label, data, valueKey) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return;

        const ctx = canvas.getContext('2d');
        if (pizzaCharts[canvasId]) pizzaCharts[canvasId].destroy();

        const cores = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'];

        pizzaCharts[canvasId] = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: data.map(item => item.tipo),
                datasets: [{
                    data: data.map(item => item[valueKey]),
                    backgroundColor: cores.slice(0, data.length),
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = total > 0 ? ((context.raw / total) * 100).toFixed(1) : 0;
                                const value = valueKey === 'quantidade' ? context.raw : context.raw.toFixed(2);
                                const unit = valueKey === 'quantidade' ? '' : (valueKey === 'co2' ? ' kg' : ' km');
                                return `${context.label}: ${value}${unit} (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    }

    let barChart = null;
    function criarGraficoBarra(canvasId, label, data, valueKey) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return;

        const ctx = canvas.getContext('2d');
        if (barChart) barChart.destroy();

        barChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map(item => item.tipo),
                datasets: [{
                    label: label,
                    data: data.map(item => item[valueKey]),
                    backgroundColor: 'rgba(54, 162, 235, 0.5)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        title: { display: true, text: 'Quilómetros (km)' }
                    }
                }
            }
        });
    }

    function preencherCards(data) {
        // Gasolina
        if (data.gasolinaData) {
            document.getElementById('gasolinaQtd').innerText = data.gasolinaData.quantidade || 0;
            document.getElementById('gasolinaKms').innerText = (data.gasolinaData.kms || 0).toFixed(1) + ' km';
            document.getElementById('gasolinaCo2').innerText = (data.gasolinaData.co2 || 0).toFixed(2) + ' kg';
        }

        // Diesel
        if (data.dieselData) {
            document.getElementById('dieselQtd').innerText = data.dieselData.quantidade || 0;
            document.getElementById('dieselKms').innerText = (data.dieselData.kms || 0).toFixed(1) + ' km';
            document.getElementById('dieselCo2').innerText = (data.dieselData.co2 || 0).toFixed(2) + ' kg';
        }

        // Híbrido
        if (data.hibridoData) {
            document.getElementById('hibridoQtd').innerText = data.hibridoData.quantidade || 0;
            document.getElementById('hibridoKms').innerText = (data.hibridoData.kms || 0).toFixed(1) + ' km';
            document.getElementById('hibridoCo2').innerText = (data.hibridoData.co2 || 0).toFixed(2) + ' kg';
        }

        // GPL
        if (data.gplData) {
            document.getElementById('gplQtd').innerText = data.gplData.quantidade || 0;
            document.getElementById('gplKms').innerText = (data.gplData.kms || 0).toFixed(1) + ' km';
            document.getElementById('gplCo2').innerText = (data.gplData.co2 || 0).toFixed(2) + ' kg';
        }

        // Elétrico
        if (data.eletricoData) {
            document.getElementById('eletricoQtd').innerText = data.eletricoData.quantidade || 0;
            document.getElementById('eletricoKms').innerText = (data.eletricoData.kms || 0).toFixed(1) + ' km';
            document.getElementById('eletricoCo2').innerText = '0 kg';
        }
    }

    // Inicializar
    document.addEventListener('DOMContentLoaded', function() {
        console.log("Página carregada, a carregar dados do município...");
        carregarDadosDashboard();
    });
</script>

</body>
</html>