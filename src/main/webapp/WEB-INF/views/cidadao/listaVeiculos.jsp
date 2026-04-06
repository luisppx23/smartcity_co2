<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Lista de Veículos - Cidadão</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/form-pages.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-cidadao.css">
</head>
<body class="form-page-body">

<jsp:include page="../navbar.jsp"/>

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card history-card vehicle-list-card">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>🚘</span>
            </div>
            <h1 class="form-card-title">Portal Smart City</h1>
            <h2 class="form-card-subtitle">Lista de Veículos</h2>
            <p class="form-card-description">
                Veículos associados ao cidadão <strong>${user.username}</strong>.
            </p>
        </div>

        <c:if test="${empty cidadao.listaDeVeiculos}">
            <div class="empty-state-box">
                Ainda não existem veículos associados à sua conta.
            </div>
        </c:if>

        <c:if test="${not empty cidadao.listaDeVeiculos}">
            <div class="history-table-wrapper">
                <table class="history-table">
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
                            <td>${ownership.veiculo.marca}</td>    <!-- Acessa através do veiculo associado -->
                            <td>${ownership.veiculo.modelo}</td>   <!-- Acessa através do veiculo associado -->
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <div class="smart-form-actions history-actions">
            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                Voltar à Home
            </a>
        </div>
    </div>
</div>

</body>
</html>