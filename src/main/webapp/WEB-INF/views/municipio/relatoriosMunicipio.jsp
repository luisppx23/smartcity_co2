<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Relatórios Município</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/form-pagesm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>
<body class="form-page-body">

<jsp:include page="../navbarm.jsp"/>

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card history-card">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>🏙️</span>
            </div>

            <h1 class="form-card-title">Portal Smart City</h1>
            <h2 class="form-card-subtitle">Histórico do Município</h2>

            <c:choose>
                <c:when test="${not empty municipio}">
                    <p class="form-card-description">
                        Consulte os dados agregados do município ${municipio.nome}.
                    </p>
                </c:when>
                <c:otherwise>
                    <p class="form-card-description">
                        Consulte os dados agregados do município.
                    </p>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${not empty erro}">
            <div class="empty-state-box">
                    ${erro}
            </div>
        </c:if>

        <c:if test="${empty erro and empty listaRegistos}">
            <div class="empty-state-box">
                Ainda não existem registos associados a este município.
            </div>
        </c:if>

        <c:if test="${empty erro and not empty listaRegistos}">

            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Resumo geral</h3>

                <table class="history-table">
                    <tbody>
                    <tr>
                        <th>Total de habitantes</th>
                        <td>${numeroHabitantes}</td>
                    </tr>
                    <tr>
                        <th>Quantidade total de veículos</th>
                        <td>${quantidadeVeiculosTotais}</td>
                    </tr>
                    <tr>
                        <th>Total global de quilómetros</th>
                        <td>
                            <fmt:formatNumber value="${totalKmsGeral}" minFractionDigits="1" maxFractionDigits="1"/> km
                        </td>
                    </tr>
                    <tr>
                        <th>Total global de CO2</th>
                        <td>
                            <fmt:formatNumber value="${totalCo2Geral}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </td>
                    </tr>
                    <tr>
                        <th>Média global de emissões mensais</th>
                        <td>
                            <fmt:formatNumber value="${mediaGlobalEmissoesMensais}" minFractionDigits="2" maxFractionDigits="2"/> kg
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <br>

            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Resumo mensal</h3>

                <table class="history-table">
                    <thead>
                    <tr>
                        <th>Mês</th>
                        <th>Total de Quilómetros</th>
                        <th>Total de CO2</th>
                        <th>Número de Habitantes</th>
                        <th>Média CO2 por Habitante</th>
                        <th>Objetivo CO2 por Habitante</th>
                        <th>Estado do Objetivo</th>
                        <th>Comparação com mês anterior</th>
                        <th>Comparação com ano anterior</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="mesAno" items="${totalKmsPorMes.keySet()}">
                        <tr>
                            <td>${mesAno}</td>
                            <td>
                                <fmt:formatNumber value="${totalKmsPorMes[mesAno]}" minFractionDigits="1" maxFractionDigits="1"/> km
                            </td>
                            <td>
                                <fmt:formatNumber value="${totalCo2PorMes[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </td>
                            <td>${numeroHabitantes}</td>
                            <td>
                                <fmt:formatNumber value="${mediaCo2PorHabitantePorMes[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </td>
                            <td>
                                <fmt:formatNumber value="${municipio.objetivo_co2_mes_hab}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${objetivoAtingidoPorMes[mesAno]}">
                                        Objetivo atingido
                                    </c:when>
                                    <c:otherwise>
                                        Objetivo não atingido
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty variacaoMesAnterior[mesAno]}">
                                        <span style="color:${corComparacaoMesAnterior[mesAno]}; font-weight:bold;">
                                            <fmt:formatNumber value="${variacaoMesAnterior[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${not empty variacaoAnoAnteriorPorMes[mesAno]}">
                                        <span style="color:${corComparacaoAnoAnteriorPorMes[mesAno]}; font-weight:bold;">
                                            <fmt:formatNumber value="${variacaoAnoAnteriorPorMes[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <br>

            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Evolução das emissões mensais</h3>

                <table class="history-table">
                    <thead>
                    <tr>
                        <th>Mês</th>
                        <th>Emissões Totais</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="mesAno" items="${evolucaoEmissoesMensais.keySet()}">
                        <tr>
                            <td>${mesAno}</td>
                            <td>
                                <fmt:formatNumber value="${evolucaoEmissoesMensais[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

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

            <div class="history-table-wrapper">
                <h3 style="margin-bottom: 15px;">Emissões médias por tipo de combustível</h3>

                <table class="history-table">
                    <thead>
                    <tr>
                        <th>Combustível</th>
                        <th>Emissão média</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="combustivel" items="${emissaoMediaPorCombustivel.keySet()}">
                        <tr>
                            <td>${combustivel}</td>
                            <td>
                                <fmt:formatNumber value="${emissaoMediaPorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </c:if>

        <div class="smart-form-actions history-actions">
            <a href="${pageContext.request.contextPath}/municipio/dashboardMunicipio" class="smart-btn smart-btn-secondary">
                Voltar ao Dashboard
            </a>
        </div>
    </div>
</div>

</body>
</html>