<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Histórico de Quilómetros</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/form-pages.css">
</head>
<body class="form-page-body">
<jsp:include page="../navbar.jsp"/>

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card history-card">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>🚗</span>
            </div>
            <h1 class="form-card-title">Portal Smart City</h1>
            <h2 class="form-card-subtitle">Histórico de Quilómetros</h2>
            <p class="form-card-description">
                Consulte os quilómetros registados na sua conta ao longo do tempo.
            </p>
        </div>

        <c:if test="${empty listaRegistos}">
            <div class="empty-state-box">
                Ainda não existem quilómetros registados na sua conta.
            </div>
        </c:if>

        <c:if test="${not empty listaRegistos}">
            <!-- TABELA 1: HISTÓRICO COMPLETO -->
            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Histórico de registos</h3>
                <table class="history-table">
                    <thead>
                    <th>Data</th>
                    <th>Veículo</th>
                    <th>Matrícula</th>
                    <th>Combustível</th>
                    <th>Kms</th>
                    <th>Emissão g/km</th>
                    <th>Emissão total (kg)</th>
                    </thead>
                    <tbody>
                    <c:forEach var="registo" items="${listaRegistos}">
                        <td>
                            <fmt:formatDate value="${registo.mes_ano}" pattern="dd/MM/yyyy"/>
                        </td>
                        <td>
                                ${registo.ownership.veiculo.marca} ${registo.ownership.veiculo.modelo}
                        </td>
                        <td>
                                ${registo.ownership.matricula}
                        </td>
                        <td>
                                ${registo.ownership.veiculo.tipoDeCombustivel}
                        </td>
                        <td>
                            <fmt:formatNumber value="${registo.kms_mes}" minFractionDigits="1" maxFractionDigits="1"/> km
                        </td>
                        <td>
                            <fmt:formatNumber value="${registo.emissaoGPorKm}" minFractionDigits="2" maxFractionDigits="2"/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${registo.emissaoEfetivaKg}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <br>

            <!-- TABELA 2: TOTAIS POR MATRÍCULA (USANDO LISTA VEICULOS + MAPAS) -->
            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Totais por matrícula</h3>
                <table class="history-table">
                    <thead>
                    <th>Veículo</th>
                    <th>Matrícula</th>
                    <th>Combustível</th>
                    <th>Total Kms</th>
                    <th>Total CO2 (kg)</th>
                    </thead>
                    <tbody>
                    <c:forEach var="veiculo" items="${listaVeiculos}">
                        <tr>
                            <td>
                                    ${veiculo.marca} ${veiculo.modelo}
                            </td>
                            <td>
                                    ${matriculaPorVeiculo[veiculo.id]}
                            </td>
                            <td>
                                    ${veiculo.tipoDeCombustivel}
                            </td>
                            <td>
                                <fmt:formatNumber value="${totalKmsPorVeiculo[veiculo.id]}" minFractionDigits="1" maxFractionDigits="1"/> km
                            </td>
                            <td>
                                <fmt:formatNumber value="${totalCo2PorVeiculo[veiculo.id]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <br>
            <!-- TABELA 3: PERCENTAGEM POR TIPO DE COMBUSTÍVEL -->
            <br>

            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Percentagem por tipo de combustível</h3>
                <table class="history-table">
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
                            <td>
                                <fmt:formatNumber value="${totalKmsPorCombustivel[combustivel]}" minFractionDigits="1" maxFractionDigits="1"/> km
                            </td>
                            <td>
                                <fmt:formatNumber value="${percentagemKmsPorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/>%
                            </td>
                            <td>
                                <fmt:formatNumber value="${totalCo2PorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </td>
                            <td>
                                <fmt:formatNumber value="${percentagemCo2PorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/>%
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <br>

            <!-- RESUMO GLOBAL -->
            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Resumo global</h3>
                <table class="history-table">
                    <thead>
                    <th>Total Global de Quilómetros</th>
                    <th>Total Global de CO2</th>
                    </thead>
                    <tbody>
                    <tr>
                        <td>
                            <fmt:formatNumber value="${totalKmsGeral}" minFractionDigits="1" maxFractionDigits="1"/> km
                        </td>
                        <td>
                            <fmt:formatNumber value="${totalCo2Geral}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <br>
            <!-- TABELA 4: RANKING DE POLUIÇÃO -->
            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Ranking de poluição</h3>
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
        </c:if>

        <div class="smart-form-actions history-actions">
            <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao" class="smart-btn smart-btn-secondary">
                Voltar ao Dashboard
            </a>
        </div>
    </div>
</div>
</body>
</html>