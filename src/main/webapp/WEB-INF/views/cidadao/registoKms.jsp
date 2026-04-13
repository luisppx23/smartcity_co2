<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registo Kms</title>

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
        <h1>Registo de Quilómetros</h1>
        <p>Insira o total de quilómetros percorridos este mês</p>
    </section>

    <section class="dashboard-sections">
        <c:choose>
            <c:when test="${empty cidadao.listaDeVeiculos}">
                <div class="kms-empty-state">
                    Ainda não tem veículos associados à sua conta. Registe primeiro um veículo para poder inserir quilómetros mensais.
                </div>

                <div class="dashboard-actions">
                    <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo" class="smart-btn btn-smart-primary-custom">
                        Registar Veículo
                    </a>
                    <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                        Voltar
                    </a>
                </div>
            </c:when>

            <c:otherwise>
                <div class="kms-layout">
                    <div class="smart-card kms-form-card">
                        <div class="kms-intro-badge">
                            <i class="bi bi-speedometer2"></i>
                            <span>Registo mensal</span>
                        </div>

                        <h3 class="dashboard-card-title">Novo registo</h3>


                        <form action="${pageContext.request.contextPath}/cidadao/registoKmsAction" method="POST" class="kms-form-grid">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                            <div class="kms-form-group">
                                <label for="veiculoId">Veículo</label>
                                <select id="veiculoId" name="veiculoId" class="smart-input-dashboard" required>
                                    <option value="">Selecione um veículo</option>
                                    <c:forEach var="ownership" items="${cidadao.listaDeVeiculos}">
                                        <option value="${ownership.veiculo.id}">
                                                ${ownership.matricula} - ${ownership.veiculo.marca} ${ownership.veiculo.modelo}
                                        </option>
                                    </c:forEach>
                                </select>
                                <small class="kms-field-help">
                                    Escolha o veículo ao qual pretende associar este registo mensal.
                                </small>
                            </div>

                            <div class="kms-form-group">
                                <label for="kms">Quilómetros (km)</label>
                                <input
                                        type="number"
                                        step="0.1"
                                        id="kms"
                                        name="kms"
                                        class="smart-input-dashboard"
                                        placeholder="Ex: 150.5"
                                        required>
                                <small class="kms-field-help">
                                    Introduza o total percorrido no mês, com uma casa decimal se necessário.
                                </small>
                            </div>

                            <div class="kms-form-actions">
                                <button type="submit" class="smart-btn btn-smart-primary-custom">
                                    Salvar Registo
                                </button>
                                <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                                    Voltar
                                </a>
                            </div>
                        </form>
                    </div>

                    <div class="smart-card kms-side-card">
                        <h3 class="kms-side-title">Antes de guardar</h3>

                        <div class="kms-tips-list">
                            <div class="kms-tip-item">
                                <span class="kms-tip-icon"><i class="bi bi-calendar-check"></i></span>
                                <p class="kms-tip-text">
                                    Registe os quilómetros uma vez por mês para manter o histórico consistente.
                                </p>
                            </div>

                            <div class="kms-tip-item">
                                <span class="kms-tip-icon"><i class="bi bi-car-front"></i></span>
                                <p class="kms-tip-text">
                                    Confirme que escolheu o veículo certo antes de submeter o formulário.
                                </p>
                            </div>

                            <div class="kms-tip-item">
                                <span class="kms-tip-icon"><i class="bi bi-graph-up-arrow"></i></span>
                                <p class="kms-tip-text">
                                    Estes dados podem influenciar cálculos de emissões e simulações de taxa.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</div>

</body>
</html>