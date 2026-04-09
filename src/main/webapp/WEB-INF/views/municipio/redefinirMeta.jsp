<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Redefinir Meta – Portal do Município</title>
    <link rel="stylesheet" href="/styles/homem.css" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
    <link rel="stylesheet" href="/styles/base-municipio.css" />
    <link rel="stylesheet" href="/styles/municipio/navbarm.css" />
    <link rel="stylesheet" href="/styles/municipio/dashboardm.css" />
</head>
<body>
<jsp:include page="navbarm.jsp"/>
<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">Redefinir Metas de CO₂
        <c:if test="${not empty municipio.nome}"> — <c:out value="${municipio.nome}"/></c:if>
    </h1>
    <p class="mun-hero-subtitle">Configure a meta do seu munícipio.</p>
</section>

<main class="mun-page-content">

    <%-- Mensagem de sucesso --%>
    <c:if test="${not empty mensagem}">
        <div class="mun-alert mun-alert-success mun-section">
            <i class="bi bi-check-circle"></i> ${mensagem}
        </div>
    </c:if>

    <%-- Mensagem de erro --%>
    <c:if test="${not empty erro}">
        <div class="mun-alert mun-alert-danger mun-section">
            <i class="bi bi-exclamation-circle"></i> ${erro}
        </div>
    </c:if>

    <%-- Contexto — o que é esta taxa --%>
    <div class="mun-card mun-section">
        <h3 class="mun-card-title">O que é o Objectivo CO₂?</h3>
        <p class="mun-card-description">
            O objectivo de CO₂ define o limite mensal de emissões por habitante, em quilogramas.
            Este valor é usado para avaliar se cada cidadão está dentro da meta ambiental do município.
            Um registo mensal com emissões abaixo deste valor é considerado "objectivo atingido".
        </p>

        <div class="mun-combustivel-list">
            <div class="mun-combustivel-row">
                <span class="mun-combustivel-label">Município</span>
                <span class="mun-combustivel-value">
                    <c:out value="${municipio.nome}" default="—"/>
                </span>
            </div>
            <div class="mun-combustivel-row">
                <span class="mun-combustivel-label">Objectivo actual</span>
                <span class="mun-combustivel-value">
                    <c:choose>
                        <c:when test="${not empty municipio}">
                            <fmt:formatNumber value="${municipio.objetivo_co2_mes_hab}"
                                              minFractionDigits="2" maxFractionDigits="2"/> kg / habitante / mês
                        </c:when>
                        <c:otherwise>—</c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="mun-combustivel-row">
                <span class="mun-combustivel-label">Contexto de referência</span>
                <span class="mun-combustivel-value">Média nacional: ~2.5 kg / habitante / mês</span>
            </div>
        </div>
    </div>

    <%-- Formulário de actualização --%>
    <div class="mun-card mun-section">
        <h3 class="mun-card-title">Actualizar Objectivo</h3>
        <p class="mun-card-description">
            Introduza o novo valor em kg de CO₂ por habitante por mês.
            O valor deve ser positivo e não pode exceder 1000 kg.
        </p>

        <form method="POST" action="<c:url value='/municipio/redefinirMetaAction'/>">

            <div class="mun-section">
                <label class="mun-metric-label" for="novoObjetivo">
                    Novo objectivo (kg CO₂ / habitante / mês)
                </label>
                <input type="number"
                       id="novoObjetivo"
                       name="novoObjetivo"
                       class="mun-input-clean"
                       step="0.1"
                       min="0.1"
                       max="1000"
                       placeholder="Ex: 2.50"
                       value="<c:if test='${not empty municipio}'><fmt:formatNumber value='${municipio.objetivo_co2_mes_hab}' minFractionDigits='2' maxFractionDigits='2'/></c:if>"
                       required />
            </div>

            <div class="mun-grid-2 mun-mt-20">
                <button type="submit" class="mun-btn-primary">
                    <i class="bi bi-check-circle"></i> Guardar Alteração
                </button>
                <a href="<c:url value='/municipio/dashboardMunicipio_b'/>" class="mun-btn-secondary">
                    <i class="bi bi-arrow-left"></i> Consultar Dashboard
                </a>
            </div>

        </form>
    </div>

    <%-- Impacto da alteração — contexto adicional --%>
    <div class="mun-card mun-section">
        <h3 class="mun-card-title">Impacto da Alteração</h3>
        <div class="mun-combustivel-list">
            <div class="mun-combustivel-row">
                <span class="mun-combustivel-label">Retroactividade</span>
                <span class="mun-combustivel-value">O novo valor aplica-se a todos os registos existentes</span>
            </div>
            <div class="mun-combustivel-row">
                <span class="mun-combustivel-label">Efeito nos relatórios</span>
                <span class="mun-combustivel-value">Os estados "atingido/não atingido" serão recalculados</span>
            </div>
            <div class="mun-combustivel-row">
                <span class="mun-combustivel-label">Permissão</span>
                <span class="mun-combustivel-value">Apenas o município autenticado pode alterar o seu objectivo</span>
            </div>
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