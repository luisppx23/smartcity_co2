<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Simular Taxa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cidadao/form-pages.css">
</head>
<body class="form-page-body">

<jsp:include page="../navbar.jsp"/>

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card">
        <div class="form-card-header">
            <div class="form-card-logo">
                <span>💰</span>
            </div>
            <h1 class="form-card-title">Portal Smart City</h1>
            <h2 class="form-card-subtitle">Simular Taxa</h2>
            <p class="form-card-description">
                Simule o valor da taxa com base nos quilómetros percorridos.
            </p>
        </div>

        <form action="${pageContext.request.contextPath}/cidadao/simularTaxa" method="POST" class="smart-form">
            <div class="smart-form-group">
                <label for="veiculoId">Veículo</label>
                <select id="veiculoId" name="veiculoId" class="smart-input" required>
                    <option value="">Selecione um veículo</option>
                    <c:forEach var="ownership" items="${cidadao.listaDeVeiculos}">
                        <option value="${ownership.veiculo.id}"
                            ${ownershipSelecionado != null && ownershipSelecionado.id == ownership.id ? 'selected' : ''}>
                                ${ownership.matricula} - ${ownership.veiculo.marca} ${ownership.veiculo.modelo}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="smart-form-group">
                <label for="kms">Quilómetros (km)</label>
                <input type="number" step="0.1" id="kms" name="kms" class="smart-input"
                       value="${kmsSimulacao > 0 ? kmsSimulacao : ''}"
                       placeholder="Ex: 150.5" required>
            </div>

            <div class="smart-form-actions">
                <button type="submit" class="smart-btn smart-btn-primary">
                    Calcular Taxa
                </button>
                <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-btn smart-btn-secondary">
                    Voltar
                </a>
            </div>
        </form>

        <!-- Resultado da simulação -->
        <c:if test="${valorTaxa != null}">
            <div class="result-box" style="margin-top: 30px; padding: 20px; background-color: #e8f5e9; border-radius: 8px; border-left: 4px solid #2e7d32;">
                <h3 style="color: #2e7d32; margin-bottom: 15px;">Resultado da Simulação</h3>
                <p><strong>Veículo:</strong> ${ownershipSelecionado.matricula} - ${ownershipSelecionado.veiculo.marca} ${ownershipSelecionado.veiculo.modelo}</p>
                <p><strong>Quilómetros:</strong> <fmt:formatNumber value="${kmsSimulacao}" minFractionDigits="1" maxFractionDigits="1"/> km</p>
                <p><strong>Emissão estimada:</strong> <fmt:formatNumber value="${emissaoGPorKm}" minFractionDigits="2" maxFractionDigits="2"/> g/km</p>
                <p><strong>Valor da Taxa Estimada:</strong> <fmt:formatNumber value="${valorTaxa}" minFractionDigits="2" maxFractionDigits="2"/> €</p>
            </div>
        </c:if>

        <c:if test="${not empty erro}">
            <div class="error-box" style="margin-top: 30px; padding: 20px; background-color: #ffebee; border-radius: 8px; border-left: 4px solid #c62828; color: #c62828;">
                <p>${erro}</p>
            </div>
        </c:if>
    </div>
</div>

</body>
</html>