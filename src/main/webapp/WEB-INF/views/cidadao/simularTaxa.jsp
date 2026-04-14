<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simular Taxa</title>

    <!-- Fonte igual ao Município -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet" />

    <!-- Ícones -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

    <!-- Navbar -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">

    <!-- CSS da página -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/simulartaxa.css">
</head>
<body class="dashboard-body">

<jsp:include page="../navbar.jsp"/>

<div class="dashboard-wrapper dashboard-wrapper-cidadao-style page-internal-cidadao">
    <section class="dashboard-page-header">
        <h1>Simular Taxa</h1>
        <p>Simule o valor da taxa com base nos quilómetros percorridos</p>
    </section>

    <section class="dashboard-sections">
        <c:choose>
            <c:when test="${empty cidadao.listaDeVeiculos}">
                <div class="simulacao-empty-state">
                    Ainda não tem veículos associados à sua conta. Registe primeiro um veículo para poder simular a taxa.
                </div>

                <div class="dashboard-actions">
                    <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo" class="smart-btn btn-smart-primary-custom">
                        <i class="bi bi-plus-lg"></i>
                        Registar Veículo
                    </a>
                    <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                        <i class="bi bi-arrow-left"></i>
                        Voltar
                    </a>
                </div>
            </c:when>

            <c:otherwise>
                <div class="simulacao-layout">
                    <div class="smart-card simulacao-form-card">
                        <div class="simulacao-intro-badge">
                            <i class="bi bi-calculator"></i>
                            <span>Simulação rápida</span>
                        </div>

                        <h3 class="dashboard-card-title">Nova simulação</h3>

                        <form action="${pageContext.request.contextPath}/cidadao/simularTaxa" method="POST" class="simulacao-form-grid">
                            <div class="simulacao-form-group">
                                <label for="veiculoId">Veículo</label>
                                <select id="veiculoId" name="veiculoId" class="smart-input-dashboard" required>
                                    <option value="">Selecione um veículo</option>
                                    <c:forEach var="ownership" items="${cidadao.listaDeVeiculos}">
                                        <option value="${ownership.veiculo.id}"
                                            ${ownershipSelecionado != null && ownershipSelecionado.id == ownership.id ? 'selected' : ''}>
                                                ${ownership.matricula} - ${ownership.veiculo.marca} ${ownership.veiculo.modelo}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="simulacao-field-help">
                                    Escolha o veículo para o qual pretende estimar o valor da taxa.
                                </small>
                            </div>

                            <div class="simulacao-form-group">
                                <label for="kms">Quilómetros (km)</label>
                                <input
                                        type="number"
                                        step="0.1"
                                        id="kms"
                                        name="kms"
                                        class="smart-input-dashboard"
                                        value="${kmsSimulacao > 0 ? kmsSimulacao : ''}"
                                        placeholder="Ex: 150.5"
                                        required>
                                <small class="simulacao-field-help">
                                    Introduza o total de quilómetros a considerar na simulação.
                                </small>
                            </div>

                            <div class="simulacao-form-actions">
                                <button type="submit" class="smart-btn btn-smart-primary-custom">
                                    <i class="bi bi-calculator"></i>
                                    Calcular Taxa
                                </button>
                                <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                                    <i class="bi bi-arrow-left"></i>
                                    Voltar
                                </a>
                            </div>
                        </form>
                    </div>

                    <div class="smart-card simulacao-side-card">
                        <h3 class="simulacao-side-title">Como funciona</h3>

                        <div class="simulacao-tips-list">
                            <div class="simulacao-tip-item">
                                <span class="simulacao-tip-icon"><i class="bi bi-car-front"></i></span>
                                <p class="simulacao-tip-text">
                                    A simulação considera o veículo selecionado e as respetivas características ambientais.
                                </p>
                            </div>

                            <div class="simulacao-tip-item">
                                <span class="simulacao-tip-icon"><i class="bi bi-speedometer2"></i></span>
                                <p class="simulacao-tip-text">
                                    Quanto maior o número de quilómetros percorridos, maior tende a ser o valor estimado.
                                </p>
                            </div>

                            <div class="simulacao-tip-item">
                                <span class="simulacao-tip-icon"><i class="bi bi-graph-up"></i></span>
                                <p class="simulacao-tip-text">
                                    Esta página serve para prever custos antes de submeter um registo real.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>

                <c:if test="${valorTaxa != null}">
                    <div class="simulacao-result-card-white">
                        <h3 class="simulacao-result-title">Resultado da Simulação</h3>

                        <div class="simulacao-result-list">
                            <div class="simulacao-result-row">
                                <span class="simulacao-result-label">Veículo</span>
                                <span class="simulacao-result-value">
                                    ${ownershipSelecionado.matricula} - ${ownershipSelecionado.veiculo.marca} ${ownershipSelecionado.veiculo.modelo}
                                </span>
                            </div>

                            <div class="simulacao-result-row">
                                <span class="simulacao-result-label">Quilómetros</span>
                                <span class="simulacao-result-value">
                                    <fmt:formatNumber value="${kmsSimulacao}" minFractionDigits="1" maxFractionDigits="1"/> km
                                </span>
                            </div>

                            <div class="simulacao-result-row">
                                <span class="simulacao-result-label">Emissão estimada</span>
                                <span class="simulacao-result-value">
                                    <fmt:formatNumber value="${emissaoGPorKm}" minFractionDigits="2" maxFractionDigits="2"/> g/km
                                </span>
                            </div>

                            <div class="simulacao-result-row simulacao-result-row-main">
                                <span class="simulacao-result-label">Valor da Taxa Estimada</span>
                                <span class="simulacao-result-value simulacao-result-main">
                                    <fmt:formatNumber value="${valorTaxa}" minFractionDigits="2" maxFractionDigits="2"/> €
                                </span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty erro}">
                    <div class="simulacao-error-card">
                        <p class="simulacao-error-text">${erro}</p>
                    </div>
                </c:if>
            </c:otherwise>
        </c:choose>
    </section>
</div>

</body>
</html>