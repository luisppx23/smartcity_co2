<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Cidadão</title>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700;800&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/emissoes-cidadao.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js"></script>
</head>
<body class="dashboard-body">
<jsp:include page="../navbar.jsp"/>

<div class="dashboard-wrapper dashboard-wrapper-cidadao-style">

    <section class="dashboard-page-header">
        <h1>Dashboard</h1>
        <p>Veja as suas emissões</p>
    </section>

    <section class="dashboard-sections">

        <c:if test="${empty listaVeiculos}">
            <div class="empty-state-box">
                Ainda não existem veículos associados à sua conta.
            </div>
        </c:if>

        <c:if test="${not empty listaVeiculos}">

            <div class="dashboard-row row-2 dashboard-top-summary">
                <div class="info-card destaque-card destaque-verde">
                    <div class="card-label">Km médios mensais</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${mediaKmsMensalCidadao}" minFractionDigits="1" maxFractionDigits="1"/> km
                    </div>
                    <div class="card-subtext">Média mensal de quilómetros</div>
                </div>

                <div class="info-card destaque-card destaque-verde-escuro">
                    <div class="card-label">CO₂ médio mensal</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${mediaCo2MensalCidadao}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">Média mensal de emissões</div>
                </div>
            </div>

            <div class="dashboard-row row-3 dashboard-secondary-summary">
                <div class="info-card">
                    <div class="card-label">Km deste mês</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${kmsMesAtual}" minFractionDigits="1" maxFractionDigits="1"/> km
                    </div>
                    <div class="card-subtext">Total registado no mês atual</div>
                </div>

                <div class="info-card">
                    <div class="card-label">CO₂ total deste mês</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${co2MesAtual}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">Emissões do mês atual</div>
                </div>

                <div class="info-card">
                    <div class="card-label">CO₂ do mesmo mês do ano passado</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${co2MesmoMesAnoPassado}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">Comparação com o período homólogo</div>
                </div>
            </div>

            <div class="dashboard-row row-vehicles">
                <c:forEach var="veiculo" items="${listaVeiculos}">
                    <div class="fuel-card fuel-cidadao">
                        <div class="fuel-title">
                                ${veiculo.marca} ${veiculo.modelo}
                        </div>

                        <div class="fuel-info">
                            <span>Matrícula</span>
                            <strong>${matriculaPorVeiculo[veiculo.id]}</strong>
                        </div>

                        <div class="fuel-info">
                            <span>Combustível</span>
                            <strong>${veiculo.tipoDeCombustivel}</strong>
                        </div>

                        <div class="fuel-info">
                            <span>Km total</span>
                            <strong>
                                <fmt:formatNumber value="${totalKmsPorVeiculo[veiculo.id]}" minFractionDigits="1" maxFractionDigits="1"/> km
                            </strong>
                        </div>

                        <div class="fuel-info">
                            <span>CO₂ total</span>
                            <strong>
                                <fmt:formatNumber value="${totalCo2PorVeiculo[veiculo.id]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </strong>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="emissoes-metrics">
                <div class="emissoes-metric-card">
                    <span class="emissoes-metric-icon"><i class="bi bi-speedometer2"></i></span>
                    <span class="emissoes-metric-label">Total KMs</span>
                    <span class="emissoes-metric-value">
                        <fmt:formatNumber value="${totalKmsGeral}" minFractionDigits="0" maxFractionDigits="0"/> km
                    </span>
                </div>

                <div class="emissoes-metric-card">
                    <span class="emissoes-metric-icon emissoes-metric-icon--gold"><i class="bi bi-cloud-haze2"></i></span>
                    <span class="emissoes-metric-label">CO₂ Total</span>
                    <span class="emissoes-metric-value">
                        <fmt:formatNumber value="${totalCo2Geral}" minFractionDigits="1" maxFractionDigits="1"/> kg
                    </span>
                </div>

                <div class="emissoes-metric-card">
                    <span class="emissoes-metric-icon emissoes-metric-icon--rank"><i class="bi bi-trophy"></i></span>
                    <span class="emissoes-metric-label">Ranking Poluição</span>
                    <span class="emissoes-metric-value">#${posicaoRankingPoluicao} / ${numeroTotalCidadaos}</span>
                </div>
            </div>

            <div class="dashboard-row row-vehicles dashboard-recent-summary">
                <c:forEach var="veiculo" items="${listaVeiculos}">
                    <div class="info-card">
                        <div class="card-label">${veiculo.marca} ${veiculo.modelo}</div>
                        <div class="card-subtext">Resumo do mês mais recente</div>

                        <div class="fuel-info">
                            <span>Km</span>
                            <strong>
                                <fmt:formatNumber value="${kmsUltimoMes[veiculo.id] != null ? kmsUltimoMes[veiculo.id] : 0}" pattern="#,##0"/>
                            </strong>
                        </div>

                        <div class="fuel-info">
                            <span>CO₂</span>
                            <strong>
                                <fmt:formatNumber value="${co2UltimoMes[veiculo.id] != null ? co2UltimoMes[veiculo.id] : 0}" pattern="#,##0.0"/> kg
                            </strong>
                        </div>

                        <div class="fuel-info">
                            <span>Taxa</span>
                            <strong>
                                €<fmt:formatNumber value="${taxaUltimoMes[veiculo.id] != null ? taxaUltimoMes[veiculo.id] : 0}" pattern="#,##0.00"/>
                            </strong>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="dashboard-row row-2 dashboard-charts-row">
                <div class="info-card chart-panel">
                    <div class="card-label">CO₂ por Veículo</div>
                    <div class="card-subtext">Total de emissões acumuladas por veículo</div>

                    <div class="vehicle-legend-list">
                        <c:forEach var="veiculo" items="${listaVeiculos}" varStatus="st">
                            <div class="vehicle-legend-item">
                                <span class="vehicle-legend-dot" style="background:${coresVeiculos[st.index % 6]};"></span>
                                <span>${matriculaPorVeiculo[veiculo.id]} — ${veiculo.marca} ${veiculo.modelo}</span>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="chart-box chart-box-sm">
                        <canvas id="chartVeiculos"></canvas>
                    </div>
                </div>

                <div class="info-card chart-panel">
                    <div class="card-label">CO₂ por Combustível</div>
                    <div class="card-subtext">Distribuição de emissões por tipo de combustível</div>

                    <div class="chart-box chart-box-md">
                        <canvas id="chartCombustivel"></canvas>
                    </div>
                </div>
            </div>

            <div class="dashboard-row dashboard-evolution-row">
                <div class="info-card evolution-panel">
                    <div class="card-label">Evolução Mensal</div>
                    <div class="card-subtext">Emissões CO₂ ou quilómetros ao longo do tempo</div>

                    <div class="evolution-toolbar">
                        <select id="tipoEvolucao" class="dashboard-select">
                            <option value="co2">Emissões CO₂</option>
                            <option value="km">Quilómetros</option>
                        </select>

                        <select id="veiculoEvolucao" class="dashboard-select">
                            <option value="total">Ver Total</option>
                            <c:forEach var="veiculo" items="${listaVeiculos}">
                                <option value="${veiculo.id}">
                                        ${matriculaPorVeiculo[veiculo.id]} — ${veiculo.marca} ${veiculo.modelo}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="chart-box chart-box-lg">
                        <canvas id="chartEvolucao"></canvas>
                    </div>

                    <div id="metaInfo" class="meta-info-panel">
                        <div class="dashboard-row row-3">
                            <div class="info-card">
                                <div class="card-label">Meta de CO₂ Mensal</div>
                                <div class="card-value">150 kg</div>
                                <div class="card-subtext">Objetivo sustentável</div>
                            </div>

                            <div class="info-card">
                                <div class="card-label">Km para Meta</div>
                                <div class="card-value" id="kmParaMeta">0 km</div>
                                <div class="card-subtext">Máximo recomendado</div>
                            </div>

                            <div class="info-card">
                                <div class="card-label">Taxa Estimada na Meta</div>
                                <div class="card-value" id="taxaMeta">€0.00</div>
                                <div class="card-subtext">Custo mensal</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </c:if>

        <div class="smart-card">
            <h3 class="dashboard-card-title">Percentagem por tipo de combustível</h3>
            <div class="table-responsive">
                <table class="smart-table">
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
                    <tfoot>
                    <tr>
                        <td><strong>Total</strong></td>
                        <td><strong><fmt:formatNumber value="${totalKmsGeral}" pattern="#,##0.0"/> km</strong></td>
                        <td></td>
                        <td><strong><fmt:formatNumber value="${totalCo2Geral}" pattern="#,##0.00"/> kg</strong></td>
                        <td></td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </div>

        <div class="emissoes-grid-2">
            <div class="smart-card">
                <h3 class="dashboard-card-title">Posição no Ranking</h3>

                <div class="emissoes-ranking-row">
                    <span class="emissoes-ranking-label">Ranking poluição</span>
                    <span class="emissoes-ranking-value">#${posicaoRankingPoluicao} / ${numeroTotalCidadaos}</span>
                </div>

                <div class="emissoes-ranking-row">
                    <span class="emissoes-ranking-label">CO₂ total</span>
                    <span class="emissoes-ranking-value">
                        <fmt:formatNumber value="${totalCo2Geral}" minFractionDigits="1" maxFractionDigits="1"/> kg
                    </span>
                </div>

                <c:if test="${numeroTotalCidadaos > 0}">
                    <c:set var="pctMelhor" value="${((numeroTotalCidadaos - posicaoRankingPoluicao) * 100) / numeroTotalCidadaos}" />

                    <c:choose>
                        <c:when test="${pctMelhor >= 90}">
                            <c:set var="tituloGamif" value="🌿 Guardião"/>
                            <c:set var="classeGamif" value="emissoes-gamif-guardiao"/>
                        </c:when>
                        <c:when test="${pctMelhor >= 75}">
                            <c:set var="tituloGamif" value="🥇 Ouro"/>
                            <c:set var="classeGamif" value="emissoes-gamif-ouro"/>
                        </c:when>
                        <c:when test="${pctMelhor >= 50}">
                            <c:set var="tituloGamif" value="🥈 Prata"/>
                            <c:set var="classeGamif" value="emissoes-gamif-prata"/>
                        </c:when>
                        <c:when test="${pctMelhor >= 25}">
                            <c:set var="tituloGamif" value="🥉 Bronze"/>
                            <c:set var="classeGamif" value="emissoes-gamif-bronze"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="tituloGamif" value="🌱 Iniciante"/>
                            <c:set var="classeGamif" value="emissoes-gamif-iniciante"/>
                        </c:otherwise>
                    </c:choose>

                    <div class="emissoes-ranking-progress">
                        <p class="emissoes-ranking-label">Melhor que</p>
                        <div class="emissoes-progress-track">
                            <div class="emissoes-progress-fill" style="width: <fmt:formatNumber value="${pctMelhor}" minFractionDigits="0" maxFractionDigits="0"/>%;"></div>
                        </div>

                        <div class="emissoes-gamif-footer">
                            <p class="emissoes-ranking-pct">
                                <fmt:formatNumber value="${pctMelhor}" minFractionDigits="0" maxFractionDigits="0"/>% do município
                            </p>
                            <span class="emissoes-gamif-titulo ${classeGamif}">${tituloGamif}</span>
                        </div>
                    </div>
                </c:if>
            </div>

            <div class="smart-card">
                <h3 class="dashboard-card-title">Curiosidades Ambientais</h3>

                <div class="curiosidades-list">
                    <div class="curiosidade-item">
                        <i class="bi bi-tree curiosidade-icon"></i>
                        <p class="curiosidade-texto">
                            Uma árvore absorve em média 22kg de CO₂ por ano. Você emitiu o equivalente a
                            <strong><fmt:formatNumber value="${totalCo2Geral / 22}" pattern="#,##0.0"/></strong> árvores!
                        </p>
                    </div>

                    <div class="curiosidade-item">
                        <i class="bi bi-bicycle curiosidade-icon"></i>
                        <p class="curiosidade-texto">
                            Ao caminhar ou andar de bicicleta 10km por semana, você pode economizar até €10/mês em taxas.
                        </p>
                    </div>

                    <div class="curiosidade-item">
                        <i class="bi bi-ev-station curiosidade-icon"></i>
                        <p class="curiosidade-texto">
                            Carros elétricos emitem 0g CO₂ durante a utilização, reduzindo drasticamente as suas taxas.
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div class="dashboard-actions">
            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                Voltar à Home
            </a>
        </div>
    </section>
</div>

<script>
    var CORES = ['#04523B','#D4AF37','#1f5a3d','#8B6914','#2b6a49','#c49b28'];

    var idsVeiculos = [];
    var labelsVeiculos = [];
    var valoresCo2Veiculos = [];
    var totalKmsPorVeiculoValores = [];

    <c:forEach var="veiculo" items="${listaVeiculos}">
    idsVeiculos.push('${veiculo.id}'.toString());
    labelsVeiculos.push('${matriculaPorVeiculo[veiculo.id]}');
    valoresCo2Veiculos.push(parseFloat('${totalCo2PorVeiculo[veiculo.id]}') || 0);
    totalKmsPorVeiculoValores.push(parseFloat('${totalKmsPorVeiculo[veiculo.id]}') || 0);
    </c:forEach>

    var labelsCombustivel = [];
    var valoresCombustivel = [];

    <c:forEach var="combustivel" items="${combustiveisData}">
    labelsCombustivel.push('${combustivel.tipo}');
    valoresCombustivel.push(parseFloat('${combustivel.emissoes}') || 0);
    </c:forEach>

    var meses = [
        <c:forEach var="mes" items="${meses}" varStatus="status">
        "${mes}"${!status.last ? ',' : ''}
        </c:forEach>
    ];

    var emissoesPorMes = [
        <c:forEach var="emissao" items="${emissoesPorMes}" varStatus="status">
        ${emissao}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    var kmsPorMes = [
        <c:forEach var="kms" items="${kmsPorMes}" varStatus="status">
        ${kms}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    if (document.getElementById('chartVeiculos') && labelsVeiculos.length > 0) {
        new Chart(document.getElementById('chartVeiculos'), {
            type: 'bar',
            data: {
                labels: labelsVeiculos,
                datasets: [{
                    label: 'CO₂ (kg)',
                    data: valoresCo2Veiculos,
                    backgroundColor: labelsVeiculos.map(function(_, i) {
                        return CORES[i % CORES.length];
                    }),
                    borderRadius: 4,
                    borderSkipped: false,
                    maxBarThickness: 34
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        backgroundColor: 'rgba(22, 50, 79, 0.95)',
                        titleColor: '#fff',
                        bodyColor: '#fff',
                        cornerRadius: 8,
                        padding: 8
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: {
                            color: '#6b7c70',
                            font: { size: 10 }
                        }
                    },
                    y: {
                        grid: {
                            color: 'rgba(22, 50, 79, 0.06)'
                        },
                        ticks: {
                            color: '#6b7c70',
                            font: { size: 10 },
                            callback: function(v) {
                                return v.toFixed(1) + ' kg';
                            }
                        }
                    }
                }
            }
        });
    }

    if (document.getElementById('chartCombustivel') && labelsCombustivel.length > 0) {
        new Chart(document.getElementById('chartCombustivel'), {
            type: 'doughnut',
            data: {
                labels: labelsCombustivel,
                datasets: [{
                    data: valoresCombustivel,
                    backgroundColor: labelsCombustivel.map(function(_, i) {
                        return CORES[i % CORES.length];
                    }),
                    borderWidth: 0,
                    hoverOffset: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '66%',
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            color: '#5f6f65',
                            usePointStyle: true,
                            pointStyle: 'circle',
                            padding: 10,
                            font: { size: 10 }
                        }
                    },
                    tooltip: {
                        backgroundColor: 'rgba(22, 50, 79, 0.95)',
                        titleColor: '#fff',
                        bodyColor: '#fff',
                        cornerRadius: 8,
                        padding: 8,
                        callbacks: {
                            label: function(ctx) {
                                return ' ' + ctx.label + ': ' + ctx.raw.toFixed(1) + ' kg';
                            }
                        }
                    }
                }
            }
        });
    }

    var evolucaoChart = null;
    var metaCO2Mensal = 150;
    var taxaPorKm = 0.25;

    function atualizarEvolucaoChart() {
        var tipoSelect = document.getElementById('tipoEvolucao');
        var veiculoSelect = document.getElementById('veiculoEvolucao');
        var canvas = document.getElementById('chartEvolucao');
        var metaInfo = document.getElementById('metaInfo');

        if (!tipoSelect || !veiculoSelect || !canvas) return;

        var tipo = tipoSelect.value;
        var veiculoId = veiculoSelect.value.toString();
        var dados = [];

        if (veiculoId === 'total') {
            dados = tipo === 'co2' ? emissoesPorMes : kmsPorMes;
        } else {
            var idx = idsVeiculos.indexOf(veiculoId);

            if (idx >= 0) {
                var somaTotalCo2 = valoresCo2Veiculos.reduce(function(a, b) {
                    return a + b;
                }, 0) || 1;

                var proporcao = valoresCo2Veiculos[idx] / somaTotalCo2;

                dados = (tipo === 'co2' ? emissoesPorMes : kmsPorMes).map(function(v) {
                    return v * proporcao;
                });
            } else {
                dados = tipo === 'co2' ? emissoesPorMes : kmsPorMes;
            }
        }

        var datasets = [{
            label: tipo === 'co2' ? 'Emissões CO₂ (kg)' : 'Quilómetros (km)',
            data: dados,
            borderColor: '#04523B',
            backgroundColor: 'rgba(4, 82, 59, 0.08)',
            borderWidth: 2,
            fill: true,
            tension: 0.35,
            pointRadius: 1.8,
            pointHoverRadius: 3
        }];

        if (tipo === 'co2') {
            datasets.push({
                label: 'Meta CO₂',
                data: meses.map(function() {
                    return metaCO2Mensal;
                }),
                borderColor: '#D4AF37',
                borderWidth: 1.5,
                borderDash: [5, 4],
                fill: false,
                pointRadius: 0
            });
        }

        if (evolucaoChart) {
            evolucaoChart.destroy();
        }

        evolucaoChart = new Chart(canvas, {
            type: 'line',
            data: {
                labels: meses,
                datasets: datasets
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
                        labels: {
                            color: '#5f6f65',
                            usePointStyle: true,
                            padding: 12,
                            font: { size: 10 }
                        }
                    },
                    tooltip: {
                        backgroundColor: 'rgba(22, 50, 79, 0.95)',
                        titleColor: '#fff',
                        bodyColor: '#fff',
                        cornerRadius: 8,
                        padding: 8,
                        callbacks: {
                            label: function(ctx) {
                                return ctx.dataset.label + ': ' + ctx.raw.toFixed(1) + (tipo === 'co2' ? ' kg' : ' km');
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: { display: false },
                        ticks: {
                            color: '#6b7c70',
                            font: { size: 9 }
                        }
                    },
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: 'rgba(22, 50, 79, 0.06)'
                        },
                        ticks: {
                            color: '#6b7c70',
                            font: { size: 9 },
                            callback: function(v) {
                                return v.toFixed(0);
                            }
                        }
                    }
                }
            }
        });

        if (tipo === 'co2') {
            metaInfo.style.display = 'block';

            if (veiculoId !== 'total') {
                var idxVeiculo = idsVeiculos.indexOf(veiculoId);

                if (idxVeiculo >= 0) {
                    var kmsVeiculo = totalKmsPorVeiculoValores[idxVeiculo] || 1;
                    var emPorKm = valoresCo2Veiculos[idxVeiculo] / kmsVeiculo;
                    var kmParaMeta = Math.round(metaCO2Mensal / (emPorKm || 0.2));

                    document.getElementById('kmParaMeta').innerText = kmParaMeta + ' km';
                    document.getElementById('taxaMeta').innerText = '€' + (kmParaMeta * taxaPorKm).toFixed(2);
                }
            } else {
                var totalCo2 = valoresCo2Veiculos.reduce(function(a, b) {
                    return a + b;
                }, 0);

                var totalKms = totalKmsPorVeiculoValores.reduce(function(a, b) {
                    return a + b;
                }, 0);

                var emissaoMediaPorKm = totalCo2 / (totalKms || 1);
                var kmParaMetaTotal = Math.round(metaCO2Mensal / (emissaoMediaPorKm || 0.2));

                document.getElementById('kmParaMeta').innerText = kmParaMetaTotal + ' km';
                document.getElementById('taxaMeta').innerText = '€' + (kmParaMetaTotal * taxaPorKm).toFixed(2);
            }
        } else {
            metaInfo.style.display = 'none';
        }
    }

    if (document.getElementById('tipoEvolucao')) {
        document.getElementById('tipoEvolucao').addEventListener('change', atualizarEvolucaoChart);
        document.getElementById('veiculoEvolucao').addEventListener('change', atualizarEvolucaoChart);
        atualizarEvolucaoChart();
    }
</script>

</body>
</html>