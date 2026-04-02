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

        <div class="dashboard-actions">
            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                Voltar à Home
            </a>
        </div>
    </section>
</div>

</body>
</html>