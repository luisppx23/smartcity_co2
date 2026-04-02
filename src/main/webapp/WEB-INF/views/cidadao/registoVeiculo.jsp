<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Registo de Veículo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/form-pages.css">
</head>
<body class="form-page-body">

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>🚗</span>
            </div>
            <h1 class="form-card-title">Portal Smart City</h1>
            <h2 class="form-card-subtitle">Registo de Veículo</h2>
            <p class="form-card-description">
                Associe o seu veículo à sua conta de cidadão.
            </p>
        </div>

        <form action="${pageContext.request.contextPath}/auth/cidadao/adicionarVeiculoAction" method="POST" class="smart-form">
            <div class="smart-form-group">
                <label for="matricula">Matrícula</label>
                <input
                        type="text"
                        id="matricula"
                        name="matricula"
                        class="smart-input"
                        placeholder="AA-00-AA"
                        required
                        style="text-transform: uppercase;">
            </div>

            <div class="smart-form-group">
                <label for="modeloReferencia">Selecione o Modelo</label>
                <select id="modeloReferencia" name="modeloReferencia" class="smart-input smart-select" required>
                    <option value="">Selecione o seu veículo</option>
                    <c:forEach var="v" items="${veiculosBase}">
                        <option value="${v.marca}:${v.modelo}">
                                ${v.marca} ${v.modelo} - ${v.tipoDeCombustivel}
                        </option>
                    </c:forEach>
                </select>
                <small class="smart-help-text">
                    Os dados de consumo serão aplicados automaticamente.
                </small>
            </div>

            <div class="smart-form-group">
                <label for="anoRegisto">Ano de Registo</label>
                <input
                        type="number"
                        id="anoRegisto"
                        name="anoRegisto"
                        class="smart-input"
                        min="1900"
                        max="2026"
                        placeholder="Ex: 2020"
                        required>
            </div>

            <div class="smart-form-actions">
                <button type="submit" class="smart-btn smart-btn-primary">
                    Adicionar à Minha Conta
                </button>
                <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                    Cancelar
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>