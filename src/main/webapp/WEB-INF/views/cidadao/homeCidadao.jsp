<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Home Cidadão</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/home.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/dashboard.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</head>
<body class="form-page-body">

<jsp:include page="../navbar.jsp"/>

<div class="home-hero">
    <h1>Bem-vindo, ${user.firstName}!</h1>
    <p>Mobilidade mais inteligente para uma cidade mais sustentável.</p>
</div>

<div class="dashboard-container">

    <div class="home-grid">
        <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo" class="home-card municipality-style-card">
            <div class="card-icon">
                <i class="bi bi-car-front"></i>
            </div>
            <h3>Registar Veículo</h3>
            <p>Adicione um novo veículo à sua conta.</p>
        </a>

        <a href="${pageContext.request.contextPath}/cidadao/simularTaxa" class="home-card municipality-style-card">
            <div class="card-icon">
                <i class="bi bi-calculator"></i>
            </div>
            <h3>Simular Taxa</h3>
            <p>Consulte uma simulação de taxa associada.</p>
        </a>

        <a href="${pageContext.request.contextPath}/cidadao/registoKms" class="home-card municipality-style-card">
            <div class="card-icon">
                <i class="bi bi-pencil"></i>
            </div>
            <h3>Registar KMs</h3>
            <p>Introduza novos registos de quilometragem do seu veículo.</p>
        </a>

        <a href="${pageContext.request.contextPath}/cidadao/historicoKms" class="home-card municipality-style-card">
            <div class="card-icon">
                <i class="bi bi-clock-history"></i>
            </div>
            <h3>Ver Registos de KMs</h3>
            <p>Consulte o histórico dos seus registos de quilometragem.</p>
        </a>
    </div>

    <div class="quick-links-card">
        <h3>Acessos Rápidos</h3>
        <div class="quick-links-row">
            <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao">
                <i class="bi bi-bar-chart-line"></i> As minhas emissões
            </a>
            <a href="${pageContext.request.contextPath}/cidadao/listaVeiculos">
                <i class="bi bi-car-front"></i> Os meus veículos
            </a>
            <a href="${pageContext.request.contextPath}/cidadao/simularTaxa">
                <i class="bi bi-calculator"></i> Simular taxa
            </a>
            <a href="${pageContext.request.contextPath}/cidadao/perfil">
                <i class="bi bi-person"></i> O meu perfil
            </a>
        </div>
    </div>

    <%-- GAMIFICAÇÃO --%>
    <div class="gamif-card">
        <div class="dashboard-gamif">
            <div class="dashboard-gamif-bar-wrap">
                <div class="dashboard-gamif-track">
                    <c:choose>
                        <c:when test="${not empty posicaoRankingPoluicao and not empty numeroTotalCidadaos and numeroTotalCidadaos > 0}">
                            <c:set var="pctMelhor" value="${((numeroTotalCidadaos - posicaoRankingPoluicao) * 100) / numeroTotalCidadaos}"/>
                            <div class="dashboard-gamif-fill" style="width: <fmt:formatNumber value="${pctMelhor}" minFractionDigits="0" maxFractionDigits="0"/>%;"></div>
                        </c:when>
                        <c:otherwise>
                            <div class="dashboard-gamif-fill" style="width: 0%;"></div>
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
                    <div class="dashboard-gamif-step ${not empty pctMelhor and pctMelhor >= 75 ? 'done' : 'active'}">
                        <div class="dashboard-gamif-dot"></div>
                        <span>Gold</span>
                    </div>
                </div>
            </div>

<%--            <div class="dashboard-gamif-right">--%>
<%--                <svg class="dashboard-gamif-tree" viewBox="0 0 40 48" fill="none" xmlns="http://www.w3.org/2000/svg">--%>
<%--                    <ellipse cx="20" cy="18" rx="14" ry="14" fill="#D4AF37" opacity="0.92"/>--%>
<%--                    <ellipse cx="20" cy="22" rx="10" ry="10" fill="#c49b28" opacity="0.70"/>--%>
<%--                    <rect x="17" y="32" width="6" height="12" rx="2" fill="#8B6914"/>--%>
<%--                    <ellipse cx="20" cy="18" rx="9" ry="9" fill="#e8cc6a" opacity="0.45"/>--%>
<%--                </svg>--%>

<%--                <c:choose>--%>
<%--                    <c:when test="${not empty pctMelhor and pctMelhor >= 90}">--%>
<%--                        <span class="dashboard-gamif-label">Guardião</span>--%>
<%--                    </c:when>--%>
<%--                    <c:when test="${not empty pctMelhor and pctMelhor >= 75}">--%>
<%--                        <span class="dashboard-gamif-label">Ouro</span>--%>
<%--                    </c:when>--%>
<%--                    <c:when test="${not empty pctMelhor and pctMelhor >= 50}">--%>
<%--                        <span class="dashboard-gamif-label">Prata</span>--%>
<%--                    </c:when>--%>
<%--                    <c:when test="${not empty pctMelhor and pctMelhor >= 25}">--%>
<%--                        <span class="dashboard-gamif-label">Bronze</span>--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
<%--                        <span class="dashboard-gamif-label">Iniciante</span>--%>
<%--                    </c:otherwise>--%>
<%--                </c:choose>--%>

<%--                <span class="dashboard-gamif-count">Árvores acumuladas: 100+</span>--%>
<%--            </div>--%>
        </div>
    </div>

</div>

</body>
</html>