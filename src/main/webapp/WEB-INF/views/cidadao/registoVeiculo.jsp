<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registo de Veículo</title>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700;800&display=swap" rel="stylesheet" />

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/cidadao-pages.css">
</head>
<body class="dashboard-body">
<jsp:include page="../navbar.jsp"/>

<div class="dashboard-wrapper dashboard-wrapper-cidadao-style page-internal-cidadao">
    <section class="dashboard-page-header">
        <h1>Registo de Veículo</h1>
        <p>Associe o seu veículo à sua conta de cidadão</p>
    </section>

    <section class="dashboard-sections">
        <div class="vehicle-register-layout">
            <div class="smart-card vehicle-form-card">
                <div class="vehicle-intro-badge">
                    <i class="bi bi-car-front"></i>
                    <span>Novo veículo</span>
                </div>

                <h3 class="dashboard-card-title">Adicionar veículo</h3>

                <div class="vehicle-highlight-box">
                    <div class="vehicle-highlight-item">
                        <span class="vehicle-highlight-label">Objetivo</span>
                        <span class="vehicle-highlight-value">Associar à sua conta</span>
                    </div>

                    <div class="vehicle-highlight-item">
                        <span class="vehicle-highlight-label">Vantagem</span>
                        <span class="vehicle-highlight-value">Consumo aplicado automaticamente</span>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/auth/cidadao/adicionarVeiculoAction" method="POST" class="vehicle-form-grid">
                    <div class="vehicle-form-group">
                        <label for="matricula">Matrícula</label>
                        <input
                                type="text"
                                id="matricula"
                                name="matricula"
                                class="smart-input-dashboard input-uppercase"
                                placeholder="AA-00-AA"
                                required>
                        <small class="vehicle-field-help">
                            Introduza a matrícula no formato habitual. O texto será apresentado em maiúsculas.
                        </small>
                    </div>

                    <div class="vehicle-form-group">
                        <label for="modeloReferencia">Selecione o Modelo</label>
                        <select id="modeloReferencia" name="modeloReferencia" class="smart-input-dashboard" required>
                            <option value="">Selecione o seu veículo</option>
                            <c:forEach var="v" items="${veiculosBase}">
                                <option value="${v.marca}:${v.modelo}">
                                        ${v.marca} ${v.modelo} - ${v.tipoDeCombustivel}
                                </option>
                            </c:forEach>
                        </select>
                        <small class="vehicle-field-help">
                            Os dados de consumo e combustível serão associados automaticamente ao modelo escolhido.
                        </small>
                    </div>

                    <div class="vehicle-form-group">
                        <label for="anoRegisto">Ano de Registo</label>
                        <input
                                type="number"
                                id="anoRegisto"
                                name="anoRegisto"
                                class="smart-input-dashboard"
                                min="1900"
                                max="2026"
                                placeholder="Ex: 2020"
                                required>
                        <small class="vehicle-field-help">
                            Indique o ano em que o veículo foi registado pela primeira vez.
                        </small>
                    </div>

                    <div class="vehicle-form-actions">
                        <button type="submit" class="smart-btn btn-smart-primary-custom">
                            Adicionar à Minha Conta
                        </button>
                        <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                            Cancelar
                        </a>
                    </div>
                </form>
            </div>

            <div class="smart-card vehicle-side-card">
                <h3 class="vehicle-side-title">Antes de continuar</h3>

                <div class="vehicle-tips-list">
                    <div class="vehicle-tip-item">
                        <span class="vehicle-tip-icon"><i class="bi bi-patch-check"></i></span>
                        <p class="vehicle-tip-text">
                            Escolha o modelo correto para garantir que os cálculos de emissões e taxas ficam fiáveis.
                        </p>
                    </div>

                    <div class="vehicle-tip-item">
                        <span class="vehicle-tip-icon"><i class="bi bi-fuel-pump"></i></span>
                        <p class="vehicle-tip-text">
                            O tipo de combustível do modelo selecionado será usado nas estatísticas e simulações futuras.
                        </p>
                    </div>

                    <div class="vehicle-tip-item">
                        <span class="vehicle-tip-icon"><i class="bi bi-bar-chart-line"></i></span>
                        <p class="vehicle-tip-text">
                            Depois do registo, poderá acompanhar quilómetros, emissões e posição no ranking ambiental.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

</body>
</html>