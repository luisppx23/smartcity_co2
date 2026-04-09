<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Cidadão - Monitorização CO2</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/form-pages.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .chart-container {
            background: white;
            border-radius: 10px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
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
            .col-md-4 { flex: 0 0 33.333333%; max-width: 33.333333%; }
            .col-md-6 { flex: 0 0 50%; max-width: 50%; }
            .col-md-12 { flex: 0 0 100%; max-width: 100%; }
        }
        .empty-state-box {
            text-align: center;
            padding: 40px;
            color: #666;
            font-size: 16px;
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

        <c:choose>
            <c:when test="${empty listaRegistos}">
                <div class="empty-state-box">
                    Ainda não existem quilómetros registados na sua conta.
                </div>
            </c:when>
            <c:otherwise>
                <!-- Cards de Estatísticas -->
                <div class="row mb-4" style="display: flex; gap: 20px; justify-content: space-between;">
                    <div class="col-md-4" style="flex: 1;">
                        <div class="stats-card">
                            <h5>Total Emissões</h5>
                            <h2><fmt:formatNumber value="${totalCo2Geral}" pattern="#,##0.00"/> kg</h2>
                            <small>CO₂ emitido no período</small>
                        </div>
                    </div>
                    <div class="col-md-4" style="flex: 1;">
                        <div class="stats-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <h5>Média Mensal</h5>
                            <h2><fmt:formatNumber value="${totalCo2Geral / listaRegistos.size()}" pattern="#,##0.00"/> kg</h2>
                            <small>por registo</small>
                        </div>
                    </div>
                    <div class="col-md-4" style="flex: 1;">
                        <div class="stats-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <h5>Total KMs</h5>
                            <h2><fmt:formatNumber value="${totalKmsGeral}" pattern="#,##0.0"/> km</h2>
                            <small>quilómetros percorridos</small>
                        </div>
                    </div>
                </div>

                <!-- Gráfico de Linhas - Evolução Mensal -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="chart-container">
                            <h3>📊 Evolução Mensal de Emissões CO₂</h3>
                            <canvas id="emissoesChart" height="200"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Gráfico de Barras - Comparativo Veículos -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="chart-container">
                            <h3>🚗 Emissões por Veículo</h3>
                            <canvas id="veiculosChart" height="200"></canvas>
                        </div>
                    </div>

                    <!-- Gráfico de Pizza - Distribuição por Combustível -->
                    <div class="col-md-6">
                        <div class="chart-container">
                            <h3>🥧 Distribuição de Emissões por Combustível</h3>
                            <canvas id="distribuicaoChart" height="200"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Tabela de Percentagem por Combustível -->
                <div class="history-table-wrapper">
                    <h3 style="margin-bottom: 15px;">📊 Percentagem por tipo de combustível</h3>
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
                        <tbody>
                        <c:forEach var="combustivel" items="${combustiveisData}">
                            <tr>
                                <td>${combustivel.tipo}</td>
                                <td><fmt:formatNumber value="${combustivel.kms}" pattern="#,##0.0"/> km</td>
                                <td><fmt:formatNumber value="${combustivel.percentagemKms}" pattern="#,##0.00"/>%</td>
                                <td><fmt:formatNumber value="${combustivel.emissoes}" pattern="#,##0.00"/> kg</td>
                                <td><fmt:formatNumber value="${combustivel.percentagemCo2}" pattern="#,##0.00"/>%</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Tabela de Histórico de Registos -->
                <div class="history-table-wrapper">
                    <h3 style="margin-bottom: 15px;">📋 Histórico de Registos</h3>
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
                        <tbody>
                        <c:forEach var="registo" items="${listaRegistos}">
                            <c:set var="rowClass" value=""/>
                            <c:if test="${registo.emissoes > 500}">
                                <c:set var="rowClass" value="table-danger"/>
                            </c:if>
                            <c:if test="${registo.emissoes > 200 and registo.emissoes <= 500}">
                                <c:set var="rowClass" value="table-warning"/>
                            </c:if>
                            <tr class="${rowClass}">
                                <td>${registo.mes}</td>
                                <td>${registo.veiculo}</td>
                                <td>${registo.matricula}</td>
                                <td><fmt:formatNumber value="${registo.kms}" pattern="#,##0"/> km</td>
                                <td><strong><fmt:formatNumber value="${registo.emissoes}" pattern="#,##0.00"/> kg</strong></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Ranking de Poluição -->
                <div class="history-table-wrapper">
                    <h3 style="margin-bottom: 15px;">🏆 Ranking de Poluição</h3>
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
                            <td>${posicaoRankingPoluicao}º em ${numeroTotalCidadaos}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="smart-form-actions history-actions">
            <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao" class="smart-btn smart-btn-secondary">
                Voltar ao Dashboard
            </a>
        </div>
    </div>
</div>

<script>
    // Dados passados do servidor
    const meses = [
        <c:forEach var="mes" items="${meses}" varStatus="status">
        "${mes}"${!status.last ? ',' : ''}
        </c:forEach>
    ];

    const emissoesPorMes = [
        <c:forEach var="emissao" items="${emissoesPorMes}" varStatus="status">
        ${emissao}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    const veiculosData = [
        <c:forEach var="veiculo" items="${veiculosData}" varStatus="status">
        {marca: "${veiculo.marca}", modelo: "${veiculo.modelo}", emissoes: ${veiculo.emissoes}}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    const combustiveisData = [
        <c:forEach var="combustivel" items="${combustiveisData}" varStatus="status">
        {tipo: "${combustivel.tipo}", emissoes: ${combustivel.emissoes}, percentagem: ${combustivel.percentagemCo2}}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    // Gráfico de Evolução (Linhas)
    if (document.getElementById('emissoesChart')) {
        const ctx = document.getElementById('emissoesChart').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: meses,
                datasets: [{
                    label: 'Emissões CO₂ (kg)',
                    data: emissoesPorMes,
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
                            label: function(context) {
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
    if (document.getElementById('veiculosChart') && veiculosData.length > 0) {
        const ctx = document.getElementById('veiculosChart').getContext('2d');
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
    if (document.getElementById('distribuicaoChart') && combustiveisData.length > 0) {
        const ctx = document.getElementById('distribuicaoChart').getContext('2d');
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
</style>
</body>
</html>