<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Veículos - Cidadão</title>

    <!-- Fonte igual ao Município -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap" rel="stylesheet" />

    <!-- Ícones -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

    <!-- Navbar -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">

    <!-- CSS da página -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/listaveiculos.css">
</head>
<body class="dashboard-body">

<jsp:include page="../navbar.jsp"/>

<div class="dashboard-wrapper dashboard-wrapper-cidadao-style">
    <section class="dashboard-page-header dashboard-page-header-left">
        <h1>Lista de Veículos</h1>
        <p>Veículos associados ao cidadão <strong>${user.username}</strong></p>
    </section>

    <section class="dashboard-sections">

        <c:if test="${empty cidadao.listaDeVeiculos}">
            <div class="empty-state-box">
                Ainda não existem veículos associados à sua conta.
            </div>
        </c:if>

        <c:if test="${not empty cidadao.listaDeVeiculos}">
            <div class="dashboard-row row-3 dashboard-secondary-summary">
                <div class="info-card">
                    <div class="card-label">Veículos associados</div>
                    <div class="card-value">${cidadao.listaDeVeiculos.size()}</div>
                    <div class="card-subtext">Total atualmente registado</div>
                </div>

                <div class="info-card">
                    <div class="card-label">Estado</div>
                    <div class="card-value">Ativo</div>
                    <div class="card-subtext">Conta com veículos vinculados</div>
                </div>

                <div class="info-card">
                    <div class="card-label">Gestão</div>
                    <div class="card-value">Disponível</div>
                    <div class="card-subtext">Pode registar novos veículos</div>
                </div>
            </div>

            <div class="smart-card">
                <h3 class="dashboard-card-title">Veículos registados</h3>

                <div class="table-responsive history-table-wrapper">
                    <table class="smart-table vehicle-table">
                        <thead>
                        <tr>
                            <th>Matrícula</th>
                            <th>Marca</th>
                            <th>Modelo</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="ownership" items="${cidadao.listaDeVeiculos}">
                            <tr>
                                <td>${ownership.matricula}</td>
                                <td>${ownership.veiculo.marca}</td>
                                <td>${ownership.veiculo.modelo}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

        <div class="dashboard-actions actions-inline">
            <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo" class="smart-btn btn-smart-primary-custom">
                <i class="bi bi-plus-lg"></i>
                Registar Veículo
            </a>

            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                <i class="bi bi-arrow-left"></i>
                Voltar à Home
            </a>
        </div>
    </section>
</div>

</body>
</html>