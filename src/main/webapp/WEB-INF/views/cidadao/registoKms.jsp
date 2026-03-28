<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Registo Kms</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/form-pages.css">
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
            <h2 class="form-card-subtitle">Registo de Quilómetros</h2>
            <p class="form-card-description">
                Insira o total de quilómetros percorridos este mês.
            </p>
        </div>

        <form action="${pageContext.request.contextPath}/cidadao/registoKmsAction" method="POST" class="smart-form">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="smart-form-group">
                <label for="veiculoId">Veículo</label>
                <select id="veiculoId" name="veiculoId" class="smart-input" required>
                    <option value="">Selecione um veículo</option>
                    <c:forEach var="veiculo" items="${listaVeiculos}">
                        <option value="${veiculo.id}">
                                ${veiculo.matricula}
                        </option>
                    </c:forEach>
                </select>

                <label for="kms">Quilómetros (km)</label>
                <input
                        type="number"
                        step="0.1"
                        id="kms"
                        name="kms"
                        class="smart-input"
                        placeholder="Ex: 150.5"
                        required>
            </div>


            <div class="smart-form-actions">
                <button type="submit" class="smart-btn smart-btn-primary">
                    Salvar Registo
                </button>
                <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao" class="smart-btn smart-btn-secondary">
                    Voltar
                </a>
            </div>
        </form>
    </div>
</div>

</body>
</html>