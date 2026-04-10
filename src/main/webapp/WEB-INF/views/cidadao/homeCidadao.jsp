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
    <style>
        .home-hero {
            text-align: center;
            padding: 48px 20px 32px;
        }
        .home-hero h1 {
            font-size: 2.2rem;
            font-weight: 700;
            color: #D4AF37;
            margin-bottom: 8px;
        }
        .home-hero p {
            font-size: 1rem;
            color: #4a5a4e;
        }
        .home-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }
        .home-card {
            background: rgba(245, 235, 210, 0.55);
            border-radius: 18px;
            padding: 28px 24px;
            border: 1px solid rgba(212, 175, 55, 0.25);
            backdrop-filter: blur(6px);
            -webkit-backdrop-filter: blur(6px);
        }
        .home-card h3 {
            font-size: 1.25rem;
            font-weight: 700;
            color: #1c3a28;
            margin-bottom: 8px;
        }
        .home-card p {
            font-size: 0.92rem;
            color: #4a5a4e;
            margin-bottom: 18px;
            line-height: 1.45;
        }
        .home-btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: #1f5a3d;
            color: #ffffff;
            text-decoration: none;
            border-radius: 10px;
            padding: 10px 18px;
            font-weight: 700;
            font-size: 0.92rem;
            transition: background 0.2s;
        }
        .home-btn:hover {
            background: #17442e;
            color: #ffffff;
            text-decoration: none;
        }
        .quick-links-card {
            background: rgba(245, 235, 210, 0.45);
            border-radius: 18px;
            padding: 20px 24px;
            border: 1px solid rgba(212, 175, 55, 0.25);
            backdrop-filter: blur(6px);
            margin-bottom: 20px;
        }
        .quick-links-card h3 {
            font-size: 1rem;
            font-weight: 700;
            color: #1c3a28;
            margin-bottom: 14px;
        }
        .quick-links-row {
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
        }
        .quick-links-row a {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            color: #1f5a3d;
            font-size: 0.9rem;
            font-weight: 600;
            text-decoration: none;
        }
        .quick-links-row a:hover { text-decoration: underline; }
        .gamif-card {
            background: rgba(245, 235, 210, 0.35);
            border-radius: 18px;
            padding: 20px 24px;
            border: 1px solid rgba(212, 175, 55, 0.20);
            backdrop-filter: blur(6px);
        }
        @media (max-width: 768px) {
            .home-grid { grid-template-columns: 1fr; }
            .quick-links-row { flex-direction: column; gap: 10px; }
        }
    </style>
</head>
<body class="form-page-body">

<jsp:include page="../navbar.jsp"/>
<div class="navbar-spacer"></div>

<script>
    (function() {
        var btn = document.getElementById('avatarBtn');
        var dd  = document.getElementById('profileDropdown');
        if (btn && dd) {
            btn.addEventListener('click', function(e) { e.stopPropagation(); dd.classList.toggle('open'); });
            document.addEventListener('click', function() { dd.classList.remove('open'); });
            dd.addEventListener('click', function(e) { e.stopPropagation(); });
        }
    })();
</script>

<div class="home-hero">
    <h1>Bem-vindo, ${user.firstName}!</h1>
    <p>Mobilidade mais inteligente para uma cidade mais sustentável.</p>
</div>

<div class="dashboard-container">

    <div class="home-grid">
        <div class="home-card">
            <h3>Registar Veículo</h3>
            <p>Adicione um novo veículo à sua conta.</p>
            <a href="${pageContext.request.contextPath}/cidadao/registoVeiculo" class="home-btn">
                <i class="bi bi-car-front"></i> Registar Veículo
            </a>
        </div>

        <div class="home-card">
            <h3>Simular Taxa</h3>
            <p>Consulte uma simulação de taxa associada.</p>
            <a href="${pageContext.request.contextPath}/cidadao/simularTaxa" class="home-btn">
                <i class="bi bi-calculator"></i> Simular Taxa
            </a>
        </div>

        <div class="home-card">
            <h3>Registar KMs</h3>
            <p>Introduza novos registos de quilometragem do seu veículo.</p>
            <a href="${pageContext.request.contextPath}/cidadao/registoKms" class="home-btn">
                <i class="bi bi-pencil"></i> Registar KMs
            </a>
        </div>

        <div class="home-card">
            <h3>Ver Registos de KMs</h3>
            <p>Consulte o histórico dos seus registos de quilometragem.</p>
            <a href="${pageContext.request.contextPath}/cidadao/verRegistosKms" class="home-btn">
                <i class="bi bi-clock-history"></i> Ver Registos de KMs
            </a>
        </div>
    </div>

    <div class="quick-links-card">
        <h3>Acessos Rápidos</h3>
        <div class="quick-links-row">
            <a href="${pageContext.request.contextPath}/cidadao/emissoes">
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
                    <c:otherwise>
                        <span class="dashboard-gamif-label">Iniciante</span>
                    </c:otherwise>
                </c:choose>
                <span class="dashboard-gamif-count">Árvores acumuladas: 100+</span>
            </div>
        </div>
    </div>

</div>

</body>
</html>

.dashboard-card, .dashboard-card-blue, .dashboard-card-beige {
background: rgba(245, 235, 210, 0.35) !important;
box-shadow: none !important;
border: 1px solid rgba(212, 175, 55, 0.20) !important;
}