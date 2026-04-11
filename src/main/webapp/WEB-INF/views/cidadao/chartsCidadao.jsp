<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard Cidadão – Smart City</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
    <link rel="stylesheet" href="/styles/base-cidadao.css" />
    <link rel="stylesheet" href="/styles/cidadao/navbar.css" />
    <link rel="stylesheet" href="/styles/cidadao/forms-cidadao.css" />
    <link rel="stylesheet" href="/styles/cidadao/emissoes-cidadao.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body>

<jsp:include page="../navbar.jsp"/>

<section class="dashboard-hero">
    <h1 class="dashboard-hero-title dashboard-hero-title-sm">Dashboard do Cidadão</h1>
    <p class="dashboard-hero-subtitle">Acompanhe as suas emissões, quilometragem e custos mensais.</p>
</section>

<main class="page-content">

    <c:choose>
        <c:when test="${empty listaRegistos}">
            <div class="smart-card">
                <div class="form-smart-alert form-smart-alert-info">
                    <i class="bi bi-info-circle"></i>
                    Ainda não existem quilómetros registados na sua conta.
                    <a href="<c:url value='/cidadao/registoVeiculo'/>">Registe o seu primeiro veículo</a>
                    e depois
                    <a href="<c:url value='/cidadao/registoKms'/>">introduza quilómetros</a>
                    para ver as suas emissões.
                </div>
            </div>
        </c:when>
        <c:otherwise>

            <%-- MÉTRICAS DE TOPO --%>
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

            <!-- Resumo Mensal -->
            <h3 class="dashboard-card-title" style="margin-bottom: 1rem;">Resumo Mensal</h3>

            <div style="display: flex; flex-wrap: wrap; gap: 1.5rem; margin-bottom: 2rem;">
                <c:forEach var="veiculo" items="${listaVeiculos}" varStatus="st">
                    <div class="smart-card" style="padding: 1.5rem; flex: 1; min-width: 250px;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                            <h4 style="font-size: 1.125rem; font-weight: 600; color: #1f2937; margin: 0;">${veiculo.marca} ${veiculo.modelo}</h4>
                            <span style="font-size: 1.5rem;">🚗</span>
                        </div>
                        <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 1rem; text-align: center;">
                            <div>
                                <div style="font-size: 1.5rem; font-weight: 700; color: #14532d; margin-bottom: 0.25rem;">
                                    <fmt:formatNumber value="${kmsUltimoMes[veiculo.id] != null ? kmsUltimoMes[veiculo.id] : 0}" pattern="#,##0"/>
                                </div>
                                <div style="font-size: 0.75rem; color: #6b7280;">Km</div>
                            </div>
                            <div>
                                <div style="font-size: 1.5rem; font-weight: 700; color: #14532d; margin-bottom: 0.25rem;">
                                    <fmt:formatNumber value="${co2UltimoMes[veiculo.id] != null ? co2UltimoMes[veiculo.id] : 0}" pattern="#,##0.0"/>
                                </div>
                                <div style="font-size: 0.75rem; color: #6b7280;">CO₂</div>
                            </div>
                            <div>
                                <div style="font-size: 1.5rem; font-weight: 700; color: #14532d; margin-bottom: 0.25rem;">
                                    €<fmt:formatNumber value="${taxaUltimoMes[veiculo.id] != null ? taxaUltimoMes[veiculo.id] : 0}" pattern="#,##0.00"/>
                                </div>
                                <div style="font-size: 0.75rem; color: #6b7280;">Taxa</div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <%-- GRÁFICO CO₂ POR VEÍCULO + DONUT POR COMBUSTÍVEL --%>
            <div class="emissoes-grid-2">
                <div class="smart-card emissoes-chart-card">
                    <h3 class="dashboard-card-title">CO₂ por Veículo (kg)</h3>
                    <p class="dashboard-card-description">Total de emissões acumuladas por veículo registado.</p>
                    <div class="emissoes-legend" id="legendVeiculos">
                        <c:forEach var="veiculo" items="${listaVeiculos}" varStatus="st">
                            <span class="emissoes-legend-item">
                                <span class="emissoes-legend-dot" style="background: ${coresVeiculos[st.index % 5]}"></span>
                                ${matriculaPorVeiculo[veiculo.id]} — ${veiculo.marca} ${veiculo.modelo}
                            </span>
                        </c:forEach>
                    </div>
                    <div class="emissoes-chart-wrap-lg">
                        <canvas id="chartVeiculos"></canvas>
                    </div>
                </div>

                <div class="smart-card emissoes-chart-card">
                    <h3 class="dashboard-card-title">Por Combustível</h3>
                    <p class="dashboard-card-description">Distribuição de CO₂ por tipo de combustível.</p>
                    <div class="emissoes-chart-wrap-md">
                        <canvas id="chartCombustivel"></canvas>
                    </div>
                </div>
            </div>

            <%-- EVOLUÇÃO MENSAL --%>
            <div class="smart-card emissoes-chart-card">
                <h3 class="dashboard-card-title">Evolução Mensal de Emissões CO₂</h3>
                <div class="chart-controls" style="display: flex; gap: 12px; margin-bottom: 20px; justify-content: flex-end;">
                    <select id="tipoEvolucao" class="chart-select" style="padding: 8px 16px; border-radius: 8px; border: 1px solid #ddd;">
                        <option value="co2">Emissões CO₂</option>
                        <option value="km">Quilómetros</option>
                    </select>
                    <select id="veiculoEvolucao" class="chart-select" style="padding: 8px 16px; border-radius: 8px; border: 1px solid #ddd;">
                        <option value="total">Ver Total</option>
                        <c:forEach var="veiculo" items="${listaVeiculos}">
                            <option value="${veiculo.id}">${veiculo.marca} ${veiculo.modelo}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="emissoes-chart-wrap-lg">
                    <canvas id="chartEvolucao"></canvas>
                </div>
                <div id="metaInfo" class="emissoes-ranking-progress" style="margin-top: 20px; display: none;">
                    <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px;">
                        <div class="info-card" style="padding: 16px; text-align: center;">
                            <div class="card-label">Meta de CO₂ Mensal</div>
                            <div class="card-value">150 kg</div>
                            <div class="card-subtext">Objetivo sustentável</div>
                        </div>
                        <div class="info-card" style="padding: 16px; text-align: center;" id="kmParaMetaCard">
                            <div class="card-label">Km para Meta</div>
                            <div class="card-value" id="kmParaMeta">0 km</div>
                            <div class="card-subtext">Máximo recomendado</div>
                        </div>
                        <div class="info-card" style="padding: 16px; text-align: center;" id="taxaMetaCard">
                            <div class="card-label">Taxa Estimada na Meta</div>
                            <div class="card-value" id="taxaMeta">€0.00</div>
                            <div class="card-subtext">Custo mensal</div>
                        </div>
                    </div>
                </div>
            </div>

            <%-- TABELA PERCENTAGEM POR COMBUSTÍVEL --%>
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
                            <td colspan="2"><strong>Total</strong></td>
                            <td><strong><fmt:formatNumber value="${totalKmsGeral}" pattern="#,##0.0"/> km</strong></td>
                            <td colspan="2"><strong><fmt:formatNumber value="${totalCo2Geral}" pattern="#,##0.00"/> kg</strong></td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>

            <%-- HISTÓRICO DE REGISTOS --%>
            <div class="smart-card">
                <h3 class="dashboard-card-title">Histórico de Registos</h3>
                <div class="table-responsive">
                    <table class="smart-table">
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
            </div>

            <%-- RANKING E CURIOSIDADES --%>
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
                            <c:when test="${pctMelhor >= 90}"><c:set var="tituloGamif" value="🌿 Guardião"/><c:set var="classeGamif" value="emissoes-gamif-guardiao"/></c:when>
                            <c:when test="${pctMelhor >= 75}"><c:set var="tituloGamif" value="🥇 Ouro"/><c:set var="classeGamif" value="emissoes-gamif-ouro"/></c:when>
                            <c:when test="${pctMelhor >= 50}"><c:set var="tituloGamif" value="🥈 Prata"/><c:set var="classeGamif" value="emissoes-gamif-prata"/></c:when>
                            <c:when test="${pctMelhor >= 25}"><c:set var="tituloGamif" value="🥉 Bronze"/><c:set var="classeGamif" value="emissoes-gamif-bronze"/></c:when>
                            <c:otherwise><c:set var="tituloGamif" value="🌱 Iniciante"/><c:set var="classeGamif" value="emissoes-gamif-iniciante"/></c:otherwise>
                        </c:choose>
                        <div class="emissoes-ranking-progress" style="margin-top: 20px;">
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
                    <div style="display: flex; flex-direction: column; gap: 16px;">
                        <div style="display: flex; gap: 12px; align-items: flex-start;">
                            <i class="bi bi-tree" style="font-size: 24px; color: #15803d;"></i>
                            <p style="margin: 0; font-size: 14px; color: #374151;">Uma árvore absorve em média 22kg de CO₂ por ano. Você emitiu o equivalente a <strong><fmt:formatNumber value="${totalCo2Geral / 22}" pattern="#,##0.0"/></strong> árvores!</p>
                        </div>
                        <div style="display: flex; gap: 12px; align-items: flex-start;">
                            <i class="bi bi-bicycle" style="font-size: 24px; color: #15803d;"></i>
                            <p style="margin: 0; font-size: 14px; color: #374151;">Ao caminhar ou andar de bicicleta 10km por semana, você pode economizar até €10/mês em taxas.</p>
                        </div>
                        <div style="display: flex; gap: 12px; align-items: flex-start;">
                            <i class="bi bi-ev-station" style="font-size: 24px; color: #15803d;"></i>
                            <p style="margin: 0; font-size: 14px; color: #374151;">Carros elétricos emitem 0g CO₂ durante a utilização, reduzindo drasticamente as suas taxas!</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="mun-mt-20" style="margin-top: 24px; text-align: center;">
                <a href="<c:url value='/cidadao/homeCidadao'/>" class="btn-secondary-smart">
                    <i class="bi bi-arrow-left"></i> Voltar ao Início
                </a>
            </div>

        </c:otherwise>
    </c:choose>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.js"></script>
<script>
    var CORES = ['#04523B','#D4AF37','#1f5a3d','#8B6914','#2b6a49','#c49b28'];

    // Dados dos veículos
    var labelsVeiculos = [], valoresCo2Veiculos = [];
    <c:forEach var="veiculo" items="${listaVeiculos}" varStatus="st">
    labelsVeiculos.push('${matriculaPorVeiculo[veiculo.id]}');
    valoresCo2Veiculos.push(parseFloat('${totalCo2PorVeiculo[veiculo.id]}') || 0);
    </c:forEach>

    // Dados por combustível
    var labelsCombustivel = [], valoresCombustivel = [];
    <c:forEach var="combustivel" items="${combustiveisData}">
    labelsCombustivel.push('${combustivel.tipo}');
    valoresCombustivel.push(parseFloat('${combustivel.emissoes}') || 0);
    </c:forEach>

    // Dados evolução mensal
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

    // Total KMs por veículo para cálculos
    var totalKmsPorVeiculoValores = [
        <c:forEach var="veiculo" items="${listaVeiculos}" varStatus="status">
        ${totalKmsPorVeiculo[veiculo.id]}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    // Gráfico de barras - CO₂ por veículo
    if (document.getElementById('chartVeiculos') && labelsVeiculos.length > 0) {
        new Chart(document.getElementById('chartVeiculos'), {
            type: 'bar',
            data: { labels: labelsVeiculos, datasets: [{ label: 'CO₂ (kg)', data: valoresCo2Veiculos, backgroundColor: labelsVeiculos.map(function(_, i) { return CORES[i % CORES.length]; }), borderRadius: 6, borderSkipped: false }] },
            options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { display: false } }, scales: { x: { ticks: { font: { size: 12, family: 'Outfit' }, color: '#5f5f5f' }, grid: { display: false } }, y: { ticks: { font: { size: 12, family: 'Outfit' }, color: '#5f5f5f', callback: function(v) { return v.toFixed(1) + ' kg'; } }, grid: { color: 'rgba(0,0,0,0.05)' } } } }
        });
    }

    // Gráfico Donut - CO₂ por combustível
    if (document.getElementById('chartCombustivel') && labelsCombustivel.length > 0) {
        new Chart(document.getElementById('chartCombustivel'), {
            type: 'doughnut',
            data: { labels: labelsCombustivel, datasets: [{ data: valoresCombustivel, backgroundColor: labelsCombustivel.map(function(_, i) { return CORES[i % CORES.length]; }), borderWidth: 0, hoverOffset: 4 }] },
            options: { responsive: true, maintainAspectRatio: false, cutout: '62%', plugins: { legend: { position: 'bottom', labels: { font: { size: 12, family: 'Outfit' }, color: '#5f5f5f', boxWidth: 10, boxHeight: 10, padding: 16 } }, tooltip: { callbacks: { label: function(ctx) { return ' ' + ctx.label + ': ' + ctx.raw.toFixed(1) + ' kg'; } } } } }
        });
    }

    // Gráfico de evolução
    var evolucaoChart = null;
    var metaCO2Mensal = 150;
    var taxaPorKm = 0.25;

    function atualizarEvolucaoChart() {
        var tipo = document.getElementById('tipoEvolucao').value;
        var veiculoId = document.getElementById('veiculoEvolucao').value;
        var dados = [];

        if (veiculoId === 'total') {
            dados = tipo === 'co2' ? emissoesPorMes : kmsPorMes;
        } else {
            var idx = labelsVeiculos.indexOf(veiculoId);
            if (idx >= 0) {
                var proporcao = valoresCo2Veiculos[idx] / (valoresCo2Veiculos.reduce(function(a,b) { return a + b; }, 0) || 1);
                dados = (tipo === 'co2' ? emissoesPorMes : kmsPorMes).map(function(v) { return v * proporcao; });
            } else {
                dados = tipo === 'co2' ? emissoesPorMes : kmsPorMes;
            }
        }

        var datasets = [{
            label: tipo === 'co2' ? 'Emissões CO₂ (kg)' : 'Quilómetros (km)',
            data: dados,
            borderColor: '#04523B',
            backgroundColor: 'rgba(4, 82, 59, 0.1)',
            borderWidth: 3,
            fill: true,
            tension: 0.4
        }];

        if (tipo === 'co2') {
            datasets.push({
                label: 'Meta CO₂',
                data: meses.map(function() { return metaCO2Mensal; }),
                borderColor: '#D4AF37',
                borderWidth: 2,
                borderDash: [5, 5],
                fill: false,
                pointRadius: 0
            });
        }

        if (evolucaoChart) evolucaoChart.destroy();

        evolucaoChart = new Chart(document.getElementById('chartEvolucao'), {
            type: 'line',
            data: { labels: meses, datasets: datasets },
            options: { responsive: true, maintainAspectRatio: false, plugins: { tooltip: { callbacks: { label: function(ctx) { return ctx.dataset.label + ': ' + ctx.raw.toFixed(1) + (tipo === 'co2' ? ' kg' : ' km'); } } } }, scales: { y: { beginAtZero: true, ticks: { callback: function(v) { return v.toFixed(0); } } } } }
        });

        var metaInfo = document.getElementById('metaInfo');
        if (tipo === 'co2') {
            metaInfo.style.display = 'block';
            if (veiculoId !== 'total') {
                var idx = labelsVeiculos.indexOf(veiculoId);
                if (idx >= 0) {
                    var emPorKm = valoresCo2Veiculos[idx] / (totalKmsPorVeiculoValores && totalKmsPorVeiculoValores[idx] ? totalKmsPorVeiculoValores[idx] : 1);
                    var kmParaMeta = Math.round(metaCO2Mensal / (emPorKm || 0.2));
                    document.getElementById('kmParaMeta').innerText = kmParaMeta + ' km';
                    document.getElementById('taxaMeta').innerText = '€' + (kmParaMeta * taxaPorKm).toFixed(2);
                }
            }
        } else {
            metaInfo.style.display = 'none';
        }
    }

    // Inicializar eventos e gráfico
    if (document.getElementById('tipoEvolucao')) {
        document.getElementById('tipoEvolucao').addEventListener('change', atualizarEvolucaoChart);
        document.getElementById('veiculoEvolucao').addEventListener('change', atualizarEvolucaoChart);
        atualizarEvolucaoChart();
    }
</script>
</body>
</html>