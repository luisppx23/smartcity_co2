<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Histórico de Quilómetros</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/form-pages.css">
</head>
<body class="form-page-body">

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card history-card">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>🚗</span>
            </div>
            <h1 class="form-card-title">Portal Smart City</h1>
            <h2 class="form-card-subtitle">Histórico de Quilómetros</h2>
            <p class="form-card-description">
                Consulte os quilómetros registados na sua conta ao longo do tempo.
            </p>
        </div>

        <c:if test="${empty listaRegistos}">
            <div class="empty-state-box">
                Ainda não existem quilómetros registados na sua conta.
            </div>
        </c:if>

        <c:if test="${not empty listaRegistos}">
            <div class="history-table-wrapper">
                <table class="history-table">
                    <thead>
                    <tr>
                        <th>Data</th>
                        <th>Quilómetros</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="registo" items="${listaRegistos}">
                        <tr>
                            <td>${registo.mes_ano}</td>
                            <td>${registo.kms_mes} km</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <div class="smart-form-actions history-actions">
            <a href="${pageContext.request.contextPath}/auth/autenticado" class="smart-btn smart-btn-secondary">
                Voltar ao Dashboard
            </a>
        </div>
    </div>
</div>

</body>
</html>