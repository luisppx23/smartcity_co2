<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Histórico de Quilómetros</title>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700;800&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/emissoes-cidadao.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/cidadao-pages.css">
</head>

<body class="dashboard-body">
<jsp:include page="../navbar.jsp"/>

<div class="dashboard-wrapper dashboard-wrapper-cidadao-style page-internal-cidadao">
    <section class="dashboard-page-header">
        <h1>Histórico de Quilómetros</h1>
        <p>Consulte os registos, totais e estatísticas da sua conta</p>
    </section>

    <section class="dashboard-sections">

        <c:if test="${empty listaRegistos}">
            <div class="empty-state-box">
                Ainda não existem quilómetros registados na sua conta.
            </div>
        </c:if>

        <c:if test="${not empty listaRegistos}">
            <c:set var="totalTaxaGeral" value="0.0" />
            <c:forEach var="registo" items="${listaRegistos}">
                <c:if test="${not empty registo.taxa}">
                    <c:set var="totalTaxaGeral" value="${totalTaxaGeral + registo.taxa.valor}" />
                </c:if>
            </c:forEach>

            <!-- PAGINAÇÃO -->
            <c:set var="registosPorPagina" value="10" />
            <c:set var="totalRegistos" value="${fn:length(listaRegistos)}" />
            <c:set var="paginaAtual" value="${empty param.page ? 1 : param.page}" />

            <c:if test="${paginaAtual < 1}">
                <c:set var="paginaAtual" value="1" />
            </c:if>

            <c:set var="totalPaginas" value="${(totalRegistos + registosPorPagina - 1) / registosPorPagina}" />

            <c:if test="${paginaAtual > totalPaginas}">
                <c:set var="paginaAtual" value="${totalPaginas}" />
            </c:if>

            <c:set var="indiceInicio" value="${(paginaAtual - 1) * registosPorPagina}" />
            <c:set var="indiceFim" value="${indiceInicio + registosPorPagina - 1}" />

            <div class="history-summary-grid">
                <div class="info-card">
                    <div class="card-label">Total Global de Quilómetros</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${totalKmsGeral}" minFractionDigits="1" maxFractionDigits="1"/> km
                    </div>
                    <div class="card-subtext">Acumulado de todos os registos</div>
                </div>

                <div class="info-card">
                    <div class="card-label">Total Global de CO₂</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${totalCo2Geral}" minFractionDigits="2" maxFractionDigits="2"/> kg
                    </div>
                    <div class="card-subtext">Emissões efetivas acumuladas</div>
                </div>

                <div class="info-card">
                    <div class="card-label">Total Global de Taxas</div>
                    <div class="card-value">
                        <fmt:formatNumber value="${totalTaxaGeral}" minFractionDigits="2" maxFractionDigits="2"/> €
                    </div>
                    <div class="card-subtext">Soma das taxas associadas</div>
                </div>
            </div>

            <div class="smart-card">
                <div class="history-table-header">
                    <h3 class="history-block-title">Histórico de registos</h3>
                    <div class="history-table-meta">
                        A mostrar ${indiceInicio + 1}
                        -
                        <c:choose>
                            <c:when test="${indiceFim + 1 > totalRegistos}">
                                ${totalRegistos}
                            </c:when>
                            <c:otherwise>
                                ${indiceFim + 1}
                            </c:otherwise>
                        </c:choose>
                        de ${totalRegistos} registos
                    </div>
                </div>

                <div class="table-responsive history-table-wrapper">
                    <table class="smart-table history-table">
                        <thead>
                        <tr>
                            <th>Data</th>
                            <th>Veículo</th>
                            <th>Matrícula</th>
                            <th>Combustível</th>
                            <th>Kms</th>
                            <th>Emissão g/km</th>
                            <th>Emissão total (kg)</th>
                            <th>Taxa (€)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="registo" items="${listaRegistos}" begin="${indiceInicio}" end="${indiceFim}">
                            <tr>
                                <td><fmt:formatDate value="${registo.mes_ano}" pattern="dd/MM/yyyy"/></td>
                                <td>${registo.ownership.veiculo.marca} ${registo.ownership.veiculo.modelo}</td>
                                <td>${registo.ownership.matricula}</td>
                                <td>${registo.ownership.veiculo.tipoDeCombustivel}</td>
                                <td><fmt:formatNumber value="${registo.kms_mes}" minFractionDigits="1" maxFractionDigits="1"/> km</td>
                                <td><fmt:formatNumber value="${registo.emissaoGPorKm}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${registo.emissaoEfetivaKg}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty registo.taxa}">
                                            <fmt:formatNumber value="${registo.taxa.valor}" minFractionDigits="2" maxFractionDigits="2"/> €
                                        </c:when>
                                        <c:otherwise>--</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <c:if test="${totalPaginas > 1}">
                    <div class="pagination-smart">
                        <a href="?page=1"
                           class="pagination-smart-link ${paginaAtual == 1 ? 'pagination-smart-disabled' : ''}">
                            <i class="bi bi-chevron-double-left"></i>
                        </a>

                        <a href="?page=${paginaAtual - 1}"
                           class="pagination-smart-link ${paginaAtual == 1 ? 'pagination-smart-disabled' : ''}">
                            <i class="bi bi-chevron-left"></i>
                        </a>

                        <c:forEach var="p" begin="1" end="${totalPaginas}">
                            <c:choose>
                                <c:when test="${p == paginaAtual}">
                                    <span class="pagination-smart-current">${p}</span>
                                </c:when>
                                <c:otherwise>
                                    <a href="?page=${p}" class="pagination-smart-link">${p}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <a href="?page=${paginaAtual + 1}"
                           class="pagination-smart-link ${paginaAtual == totalPaginas ? 'pagination-smart-disabled' : ''}">
                            <i class="bi bi-chevron-right"></i>
                        </a>

                        <a href="?page=${totalPaginas}"
                           class="pagination-smart-link ${paginaAtual == totalPaginas ? 'pagination-smart-disabled' : ''}">
                            <i class="bi bi-chevron-double-right"></i>
                        </a>
                    </div>
                </c:if>
            </div>

            <div class="smart-card">
                <h3 class="history-block-title">Totais por matrícula</h3>
                <div class="table-responsive history-table-wrapper">
                    <table class="smart-table history-table">
                        <thead>
                        <tr>
                            <th>Veículo</th>
                            <th>Matrícula</th>
                            <th>Combustível</th>
                            <th>Total Kms</th>
                            <th>Total CO2 (kg)</th>
                            <th>Total Taxa (€)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="veiculo" items="${listaVeiculos}">
                            <tr>
                                <td>${veiculo.marca} ${veiculo.modelo}</td>
                                <td>${matriculaPorVeiculo[veiculo.id]}</td>
                                <td>${veiculo.tipoDeCombustivel}</td>
                                <td><fmt:formatNumber value="${totalKmsPorVeiculo[veiculo.id]}" minFractionDigits="1" maxFractionDigits="1"/> km</td>
                                <td><fmt:formatNumber value="${totalCo2PorVeiculo[veiculo.id]}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                                <td><fmt:formatNumber value="${totalTaxaPorVeiculo[veiculo.id]}" minFractionDigits="2" maxFractionDigits="2"/> €</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="smart-card">
                <h3 class="history-block-title">Percentagem por tipo de combustível</h3>
                <div class="table-responsive history-table-wrapper">
                    <table class="smart-table history-table">
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
                        <c:forEach var="combustivel" items="${totalKmsPorCombustivel.keySet()}">
                            <tr>
                                <td>${combustivel}</td>
                                <td><fmt:formatNumber value="${totalKmsPorCombustivel[combustivel]}" minFractionDigits="1" maxFractionDigits="1"/> km</td>
                                <td><fmt:formatNumber value="${percentagemKmsPorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/>%</td>
                                <td><fmt:formatNumber value="${totalCo2PorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                                <td><fmt:formatNumber value="${percentagemCo2PorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/>%</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="emissoes-grid-2">
                <div class="smart-card">
                    <h3 class="dashboard-card-title">Resumo global</h3>
                    <div class="emissoes-ranking-row">
                        <span class="emissoes-ranking-label">Quilómetros</span>
                        <span class="emissoes-ranking-value">
                            <fmt:formatNumber value="${totalKmsGeral}" minFractionDigits="1" maxFractionDigits="1"/> km
                        </span>
                    </div>
                    <div class="emissoes-ranking-row">
                        <span class="emissoes-ranking-label">CO₂</span>
                        <span class="emissoes-ranking-value">
                            <fmt:formatNumber value="${totalCo2Geral}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </span>
                    </div>
                    <div class="emissoes-ranking-row">
                        <span class="emissoes-ranking-label">Taxas</span>
                        <span class="emissoes-ranking-value">
                            <fmt:formatNumber value="${totalTaxaGeral}" minFractionDigits="2" maxFractionDigits="2"/> €
                        </span>
                    </div>
                </div>

                <div class="smart-card">
                    <h3 class="dashboard-card-title">Ranking de poluição</h3>
                    <div class="emissoes-ranking-row">
                        <span class="emissoes-ranking-label">Posição</span>
                        <span class="emissoes-ranking-value">${posicaoRankingPoluicao}º em ${numeroTotalCidadaos}</span>
                    </div>
                    <div class="emissoes-ranking-row">
                        <span class="emissoes-ranking-label">Interpretação</span>
                        <span class="emissoes-ranking-value">Ranking dos mais poluidores</span>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="dashboard-actions">
            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                Voltar à Home
            </a>
        </div>
    </section>
</div>
</body>
</html>