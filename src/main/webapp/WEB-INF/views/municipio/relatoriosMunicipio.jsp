<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Relatórios Município</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>

<jsp:include page="navbarm.jsp"/>
<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">Relatórios do Município</h1>
    <p class="mun-hero-subtitle">Histórico agregado de emissões, KMs e tendências do município de ${municipio.nome}</p>
</section>

<main class="mun-page-content">

<%-- ── RESUMO MENSAL ── --%>
<div class="mun-card mun-section">
    <h3 class="mun-card-title">Resumo Mensal</h3>
    <div class="table-responsive">
        <table class="mun-table">
            <thead>
            <tr>
                <th>Mês</th>
                <th>Total KMs</th>
                <th>Total CO₂</th>
                <th>Habitantes</th>
                <th>Média CO₂/Hab.</th>
                <th>Objectivo</th>
                <th>Estado</th>
                <th>vs Mês Anterior</th>
                <th>vs Ano Anterior</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="mesAno" items="${totalKmsPorMes.keySet()}">
                <tr>
                    <td><strong>${mesAno}</strong></td>
                    <td><fmt:formatNumber value="${totalKmsPorMes[mesAno]}" minFractionDigits="1" maxFractionDigits="1"/> km</td>
                    <td><fmt:formatNumber value="${totalCo2PorMes[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                    <td>${numeroHabitantes}</td>
                    <td><fmt:formatNumber value="${mediaCo2PorHabitantePorMes[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                    <td><fmt:formatNumber value="${municipio.objetivo_co2_mes_hab}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                    <td>
                        <c:choose>
                            <c:when test="${objetivoAtingidoPorMes[mesAno]}">
                                            <span class="mun-badge mun-badge-success">
                                                <i class="bi bi-check-circle"></i> Atingido
                                            </span>
                            </c:when>
                            <c:otherwise>
                                            <span class="mun-badge mun-badge-danger">
                                                <i class="bi bi-x-circle"></i> Não atingido
                                            </span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty variacaoMesAnterior[mesAno]}">
                                <c:choose>
                                    <c:when test="${corComparacaoMesAnterior[mesAno] == 'green'}">
                                                    <span class="mun-variacao-negativa">
                                                        <i class="bi bi-arrow-down"></i>
                                                        <fmt:formatNumber value="${variacaoMesAnterior[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                    </c:when>
                                    <c:when test="${corComparacaoMesAnterior[mesAno] == 'red'}">
                                                    <span class="mun-variacao-positiva">
                                                        <i class="bi bi-arrow-up"></i>
                                                        <fmt:formatNumber value="${variacaoMesAnterior[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="mun-variacao-neutra">—</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <span class="mun-variacao-neutra">—</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty variacaoAnoAnteriorPorMes[mesAno]}">
                                <c:choose>
                                    <c:when test="${corComparacaoAnoAnteriorPorMes[mesAno] == 'green'}">
                                                    <span class="mun-variacao-negativa">
                                                        <i class="bi bi-arrow-down"></i>
                                                        <fmt:formatNumber value="${variacaoAnoAnteriorPorMes[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                    </c:when>
                                    <c:when test="${corComparacaoAnoAnteriorPorMes[mesAno] == 'red'}">
                                                    <span class="mun-variacao-positiva">
                                                        <i class="bi bi-arrow-up"></i>
                                                        <fmt:formatNumber value="${variacaoAnoAnteriorPorMes[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="mun-variacao-neutra">—</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <span class="mun-variacao-neutra">—</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%-- ── EVOLUÇÃO MENSAL ── --%>
<div class="mun-card mun-section">
    <h3 class="mun-card-title">Evolução das Emissões Mensais</h3>
    <div class="table-responsive">
        <table class="mun-table">
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
                    <td><fmt:formatNumber value="${evolucaoEmissoesMensais[mesAno]}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%-- ── COMBUSTÍVEL ── --%>
<div class="mun-card mun-section">
    <h3 class="mun-card-title">Percentagem por Tipo de Combustível</h3>
    <div class="table-responsive">
        <table class="mun-table">
            <thead>
            <tr>
                <th>Combustível</th>
                <th>Total KMs</th>
                <th>% KMs</th>
                <th>Total CO₂</th>
                <th>% CO₂</th>
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

<%-- ── EMISSÕES MÉDIAS POR COMBUSTÍVEL ── --%>
<div class="mun-card mun-section">
    <h3 class="mun-card-title">Emissões Médias por Tipo de Combustível</h3>
    <div class="table-responsive">
        <table class="mun-table">
            <thead>
            <tr>
                <th>Combustível</th>
                <th>Emissão Média</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="combustivel" items="${emissaoMediaPorCombustivel.keySet()}">
                <tr>
                    <td>${combustivel}</td>
                    <td><fmt:formatNumber value="${emissaoMediaPorCombustivel[combustivel]}" minFractionDigits="2" maxFractionDigits="2"/> kg</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>


<%-- VOLTAR --%>
<div class="mun-card">
    <a href="<c:url value='/municipio/homeMunicipio'/>" class="mun-btn-secondary">
        <i class="bi bi-arrow-left"></i> Voltar ao Home
    </a>
</div>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>