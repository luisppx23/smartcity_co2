<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Cidadão</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
</head>
<body class="dashboard-body">
<jsp:include page="../navbar.jsp"/>

<div class="dashboard-wrapper">

    <section class="dashboard-hero cidadao-hero">
        <h1>Dashboard do Cidadão</h1>
        <p>Consulte rapidamente os seus quilómetros e emissões de CO₂.</p>
    </section>

    <section class="dashboard-sections">

        <c:if test="${empty listaVeiculos}">
            <div class="empty-state-box">
                Ainda não existem veículos associados à sua conta.
            </div>
        </c:if>

        <c:if test="${not empty listaVeiculos}">

            <!-- 1ª linha -->
            <div class="dashboard-row row-2">
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

            <!-- 2ª linha -->
            <div class="dashboard-row row-3">
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

            <!-- 3ª linha -->
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

        </c:if>

        <%-- GAMIFICAÇÃO --%>
        <div class="smart-card">
            <div class="dashboard-gamif">

                <div class="dashboard-gamif-bar-wrap">
                    <div class="dashboard-gamif-track">
                        <c:choose>
                            <c:when test="${not empty posicaoRankingPoluicao and not empty numeroTotalCidadaos and numeroTotalCidadaos > 0}">
                                <c:set var="pctMelhor" value="${((numeroTotalCidadaos - posicaoRankingPoluicao) * 100) / numeroTotalCidadaos}"/>
                                <div class="dashboard-gamif-fill" style="width: <fmt:formatNumber value="${pctMelhor}" minFractionDigits="0" maxFractionDigits="0"/>%;"></div>
                            </c:when>
                            <c:otherwise>
                                <div class="dashboard-gamif-fill"></div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="dashboard-gamif-milestones">
                        <div class="dashboard-gamif-step done">
                            <div class="dashboard-gamif-dot"></div>
                            <span>Initial</span>
                        </div>
                        <div class="dashboard-gamif-step ${not empty pctMelhor and pctMelhor >= 25 ? 'done' : ''}">
                            <div class="dashboard-gamif-dot"></div>
                            <span>Bronze</span>
                        </div>
                        <div class="dashboard-gamif-step ${not empty pctMelhor and pctMelhor >= 50 ? 'done' : ''}">
                            <div class="dashboard-gamif-dot"></div>
                            <span>Silver</span>
                        </div>
                        <div class="dashboard-gamif-step ${not empty pctMelhor and pctMelhor >= 75 ? (pctMelhor < 90 ? 'active' : 'done') : 'active'}">
                            <div class="dashboard-gamif-dot"></div>
                            <span>Gold</span>
                        </div>
                    </div>
                </div>

                <div class="dashboard-gamif-right">
                    <svg class="dashboard-gamif-tree" viewBox="0 0 40 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <ellipse cx="20" cy="18" rx="14" ry="14" fill="#D4AF37" opacity="0.92"/>
                        <ellipse cx="20" cy="22" rx="10" ry="10" fill="#c49b28" opacity="0.70"/>
                        <rect x="17" y="32" width="6" height="12" rx="2" fill="#8B6914"/>
                        <ellipse cx="20" cy="18" rx="9" ry="9" fill="#e8cc6a" opacity="0.45"/>
                    </svg>
                    <c:choose>
                        <c:when test="${not empty pctMelhor and pctMelhor >= 90}">
                            <span class="dashboard-gamif-label">Guardião</span>
                        </c:when>
                        <c:when test="${not empty pctMelhor and pctMelhor >= 75}">
                            <span class="dashboard-gamif-label">Ouro</span>
                        </c:when>
                        <c:when test="${not empty pctMelhor and pctMelhor >= 50}">
                            <span class="dashboard-gamif-label">Prata</span>
                        </c:when>
                        <c:when test="${not empty pctMelhor and pctMelhor >= 25}">
                            <span class="dashboard-gamif-label">Bronze</span>
                        </c:when>
                        <c:when test="${not empty pctMelhor}">
                            <span class="dashboard-gamif-label">Iniciante</span>
                        </c:when>
                        <c:otherwise>
                            <span class="dashboard-gamif-label">Guardião</span>
                        </c:otherwise>
                    </c:choose>
                    <span class="dashboard-gamif-count">Árvores acumuladas: 100+</span>
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

</body>
</html>