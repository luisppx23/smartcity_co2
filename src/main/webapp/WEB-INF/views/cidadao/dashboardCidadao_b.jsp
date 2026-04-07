<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Cidadão - Monitorização CO2</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .chart-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
            text-align: center;
        }

        h3 {
            color: #333;
            margin-bottom: 20px;
        }

        .form-page-wrapper {
            padding-top: 20px;
        }

        .form-card {
            max-width: 1400px;
            margin: 0 auto;
        }

        .history-table-wrapper {
            overflow-x: auto;
            margin-bottom: 20px;
        }

        .history-table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .history-table th,
        .history-table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #eee;
        }

        .history-table th {
            background-color: #f8f9fa;
            font-weight: 600;
            color: #333;
        }

        .history-table tr:hover {
            background-color: #f5f5f5;
        }

        .table-danger {
            background-color: #f8d7da !important;
        }

        .table-warning {
            background-color: #fff3cd !important;
        }

        .row {
            display: flex;
            flex-wrap: wrap;
            margin-right: -15px;
            margin-left: -15px;
        }

        .col-md-4, .col-md-6, .col-md-12 {
            position: relative;
            width: 100%;
            padding-right: 15px;
            padding-left: 15px;
        }

        @media (min-width: 768px) {
            .col-md-4 {
                flex: 0 0 33.333333%;
                max-width: 33.333333%;
            }

            .col-md-6 {
                flex: 0 0 50%;
                max-width: 50%;
            }

            .col-md-12 {
                flex: 0 0 100%;
                max-width: 100%;
            }
        }

        .empty-state-box {
            text-align: center;
            padding: 40px;
            color: #666;
            font-size: 16px;
        }

        .loading-spinner {
            text-align: center;
            padding: 40px;
            color: #666;
        }
    </style>
</head>
<body class="form-page-body">
<jsp:include page="../navbar.jsp"/>

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card history-card" style="max-width: 1400px;">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>📊</span>
            </div>
            <h1 class="form-card-title">Portal Smart City</h1>
            <h2 class="form-card-subtitle">Dashboard de Emissões de CO₂</h2>
            <p class="form-card-description">
                Visualize as suas emissões de CO₂ de forma gráfica e detalhada.
            </p>
        </div>

        <div id="dashboardContent">
            <div class="loading-spinner">
                Carregando dados...
            </div>
        </div>

        <div class="smart-form-actions history-actions">
            <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao" class="smart-btn smart-btn-secondary">
                Voltar ao Dashboard
            </a>
        </div>
    </div>
</div>

<script>
    // Função para carregar os dados do dashboard via API
    function carregarDadosDashboard() {
        console.log("Carregando dados...");

        fetch(`/api/charts/cidadao/dados-dashboard`)
            .then(response => {
                console.log("Status:", response.status);
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
        // Evitar divisão por zero se a lista estiver vazia
        const mediaMensal = (data.listaRegistos && data.listaRegistos.length > 0)
            ? (data.totalCo2Geral / data.listaRegistos.length)
            : 0;

        const container = document.getElementById('dashboardContent');

        // IMPORTANTE: O uso de \${ no innerHTML é para escapar o motor do JSP
        container.innerHTML = `
            <div class="row mb-4" style="display: flex; gap: 20px; justify-content: space-between;">
                <div class="col-md-4" style="flex: 1;">
                    <div class="stats-card">
                        <h5>Total Emissões</h5>
                        <h2>\${data.totalCo2Geral.toFixed(2)} kg</h2>
                        <small>CO₂ emitido no período</small>
                    </div>
                </div>
                <div class="col-md-4" style="flex: 1;">
                    <div class="stats-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                        <h5>Média Mensal</h5>
                        <h2>\${mediaMensal.toFixed(2)} kg</h2>
                        <small>por registo</small>
                    </div>
                </div>
                <div class="col-md-4" style="flex: 1;">
                    <div class="stats-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                        <h5>Total KMs</h5>
                        <h2>\${data.totalKmsGeral.toFixed(1)} km</h2>
                        <small>quilómetros percorridos</small>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12">
                    <div class="chart-container">
                        <h3>📊 Evolução Mensal de Emissões CO₂</h3>
                        <canvas id="emissoesChart" height="200"></canvas>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-6">
                    <div class="chart-container">
                        <h3>🚗 Emissões por Veículo</h3>
                        <canvas id="veiculosChart" height="200"></canvas>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="chart-container">
                        <h3>🥧 Distribuição de Emissões por Combustível</h3>
                        <canvas id="distribuicaoChart" height="200"></canvas>
                    </div>
                </div>
            </div>

            <div class="history-table-wrapper">
                <h3>📊 Percentagem por tipo de combustível</h3>
                <table class="history-table" id="combustiveisTable">
                    <thead>
                        <tr>
                            <th>Combustível</th>
                            <th>Total Kms</th>
                            <th>% dos Kms</th>
                            <th>Total CO2 (kg)</th>
                            <th>% do CO2</th>
                         </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>

            <div class="history-table-wrapper">
                <h3>📋 Histórico de Registos</h3>
                <table class="history-table" id="detalhesTable">
                    <thead>
                         <tr>
                            <th>Mês/Ano</th>
                            <th>Veículo</th>
                            <th>Matrícula</th>
                            <th>KMs Percorridos</th>
                            <th>Emissões CO₂ (kg)</th>
                         </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>

            <div class="history-table-wrapper">
                <h3>🏆 Ranking de Poluição</h3>
                <table class="history-table">
                    <thead>
                         <tr>
                            <th>Métrica</th>
                            <th>Valor</th>
                         </tr>
                    </thead>
                    <tbody>
                         <tr>
                            <td>Posição no ranking dos mais poluidores</td>
                            <td>\${data.posicaoRankingPoluicao}&ordm; em \${data.numeroTotalCidadaos}</td>
                         </tr>
                    </tbody>
                </table>
            </div>
        `;

        // Criar os gráficos APÓS o HTML estar no DOM
        setTimeout(() => {
            criarGraficoEvolucao(data.meses, data.emissoesPorMes);
            criarGraficoVeiculos(data.veiculosData);
            criarGraficoDistribuicao(data.combustiveisData);
            preencherTabelaCombustiveis(data);
            preencherTabelaRegistos(data.listaRegistos);
        }, 100);
    }

    // Gráfico de Evolução (Linhas)
    function criarGraficoEvolucao(meses, emissoes) {
        const canvas = document.getElementById('emissoesChart');
        if (!canvas) {
            console.error("Canvas emissoesChart não encontrado");
            return;
        }

        const ctx = canvas.getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: meses,
                datasets: [{
                    label: 'Emissões CO₂ (kg)',
                    data: emissoes,
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return context.raw.toFixed(2) + ' kg CO₂';
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        title: {
                            display: true,
                            text: 'Emissões (kg CO₂)'
                        },
                        beginAtZero: true
                    }
                }
            }
        });
    }

    // Gráfico de Veículos (Barras)
    function criarGraficoVeiculos(veiculosData) {
        const canvas = document.getElementById('veiculosChart');
        if (!canvas) {
            console.error("Canvas veiculosChart não encontrado");
            return;
        }

        const ctx = canvas.getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: veiculosData.map(v => `${v.marca} ${v.modelo}`),
                datasets: [{
                    label: 'Emissões Totais (kg CO₂)',
                    data: veiculosData.map(v => v.emissoes),
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
                        title: {
                            display: true,
                            text: 'Emissões (kg CO₂)'
                        }
                    }
                }
            }
        });
    }

    // Gráfico de Distribuição por Combustível (Pizza)
    function criarGraficoDistribuicao(combustiveisData) {
        const canvas = document.getElementById('distribuicaoChart');
        if (!canvas) {
            console.error("Canvas distribuicaoChart não encontrado");
            return;
        }

        const ctx = canvas.getContext('2d');
        const cores = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'];

        new Chart(ctx, {
            type: 'pie',
            data: {
                labels: combustiveisData.map(c => c.tipo),
                datasets: [{
                    data: combustiveisData.map(c => c.emissoes),
                    backgroundColor: cores.slice(0, combustiveisData.length),
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function (context) {
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

    function preencherTabelaCombustiveis(data) {
        const tbody = document.querySelector('#combustiveisTable tbody');
        if (!tbody) return;

        tbody.innerHTML = '';

        for (const combustivel of data.combustiveisData) {
            const row = tbody.insertRow();
            row.insertCell(0).innerText = combustivel.tipo;
            row.insertCell(1).innerText = combustivel.kms.toFixed(1) + ' km';
            row.insertCell(2).innerText = combustivel.percentagemKms.toFixed(2) + '%';
            row.insertCell(3).innerText = combustivel.emissoes.toFixed(2) + ' kg';
            row.insertCell(4).innerText = combustivel.percentagemCo2.toFixed(2) + '%';
        }
    }

    function preencherTabelaRegistos(registos) {
        const tbody = document.querySelector('#detalhesTable tbody');
        if (!tbody) return;

        tbody.innerHTML = '';

        if (!registos || registos.length === 0) {
            const row = tbody.insertRow();
            row.insertCell(0).colSpan = 5;
            row.insertCell(0).innerText = 'Nenhum registo encontrado.';
            return;
        }

        registos.forEach(item => {
            const row = tbody.insertRow();
            row.insertCell(0).innerText = item.mes;
            row.insertCell(1).innerText = item.veiculo;
            row.insertCell(2).innerText = item.matricula;
            row.insertCell(3).innerText = item.kms.toFixed(0) + ' km';
            row.insertCell(4).innerHTML = `<strong>${item.emissoes.toFixed(2)} kg</strong>`;

            if (item.emissoes > 500) {
                row.classList.add('table-danger');
            } else if (item.emissoes > 200) {
                row.classList.add('table-warning');
            }
        });
    }

    // Inicializar quando a página carregar
    document.addEventListener('DOMContentLoaded', function () {
        console.log("Página carregada, a iniciar...");
        carregarDadosDashboard();
    });
</script>

<style>
    .table-danger {
        background-color: #f8d7da !important;
    }

    .table-warning {
        background-color: #fff3cd !important;
    }

    .history-table tbody tr.table-danger td {
        background-color: #f8d7da;
    }

    .history-table tbody tr.table-warning td {
        background-color: #fff3cd;
    }

    .row {
        display: flex;
        flex-wrap: wrap;
        margin-right: -15px;
        margin-left: -15px;
    }

    .col-md-4, .col-md-6, .col-md-12 {
        position: relative;
        width: 100%;
        padding-right: 15px;
        padding-left: 15px;
    }

    @media (min-width: 768px) {
        .col-md-4 {
            flex: 0 0 33.333333%;
            max-width: 33.333333%;
        }

        .col-md-6 {
            flex: 0 0 50%;
            max-width: 50%;
        }

        .col-md-12 {
            flex: 0 0 100%;
            max-width: 100%;
        }
    }

    .empty-state-box {
        text-align: center;
        padding: 40px;
        color: #666;
        font-size: 16px;
    }

    .loading-spinner {
        text-align: center;
        padding: 40px;
        color: #666;
    }
</style>
</body>
</html>