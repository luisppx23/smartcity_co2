<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Redefinir Taxas – Portal do Município</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">
</head>
<body>
<jsp:include page="navbarm.jsp"/>

<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">Redefinir Taxas de CO₂</h1>
    <p class="mun-hero-subtitle">Configure a tabela de taxas por quilómetro para cada nível de emissão.</p>
</section>
<main class="mun-page-content">


    <c:if test="${not empty mensagem}">
        <div class="mun-alert mun-alert-success">
            <i class="bi bi-check-circle"></i>
            <span>${mensagem}</span>
        </div>
    </c:if>

    <c:if test="${not empty erro}">
        <div class="mun-alert mun-alert-danger">
            <i class="bi bi-exclamation-circle"></i>
            <span>${erro}</span>
        </div>
    </c:if>

    <div class="mun-card mun-section">
        <h2 class="mun-card-title">Tabela atual</h2>
        <p class="mun-card-description">
            Pode rever abaixo os valores atualmente definidos para o município autenticado.
        </p>

        <table class="mun-table">
            <thead>
            <tr>
                <th>Nível</th>
                <th>Intervalo de emissão</th>
                <th>Taxa atual</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>1</td>
                <td>Superior a 250 g/km</td>
                <td>
                    <fmt:formatNumber value="${municipio.taxaNivel1}" minFractionDigits="2" maxFractionDigits="2"/> €/km
                </td>
            </tr>
            <tr>
                <td>2</td>
                <td>Entre 250 e 200 g/km</td>
                <td>
                    <fmt:formatNumber value="${municipio.taxaNivel2}" minFractionDigits="2" maxFractionDigits="2"/> €/km
                </td>
            </tr>
            <tr>
                <td>3</td>
                <td>Entre 200 e 150 g/km</td>
                <td>
                    <fmt:formatNumber value="${municipio.taxaNivel3}" minFractionDigits="2" maxFractionDigits="2"/> €/km
                </td>
            </tr>
            <tr>
                <td>4</td>
                <td>Entre 150 e 100 g/km</td>
                <td>
                    <fmt:formatNumber value="${municipio.taxaNivel4}" minFractionDigits="2" maxFractionDigits="2"/> €/km
                </td>
            </tr>
            <tr>
                <td>5</td>
                <td>Entre 100 e 50 g/km</td>
                <td>
                    <fmt:formatNumber value="${municipio.taxaNivel5}" minFractionDigits="2" maxFractionDigits="2"/> €/km
                </td>
            </tr>
            <tr>
                <td>6</td>
                <td>Menor que 50 g/km</td>
                <td>
                    <fmt:formatNumber value="${municipio.taxaNivel6}" minFractionDigits="2" maxFractionDigits="2"/> €/km
                </td>
            </tr>
            <tr>
                <td>7</td>
                <td>0 g/km</td>
                <td>
                    <fmt:formatNumber value="${municipio.taxaNivel7}" minFractionDigits="2" maxFractionDigits="2"/> €/km
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="mun-card mun-section">
        <h2 class="mun-card-title">Atualizar taxas</h2>
        <p class="mun-card-description">
            Introduza os novos valores em euros por quilómetro. As taxas não devem ser negativas e o nível 7 deve manter-se em 0.
        </p>

        <form method="post" action="<c:url value='/municipio/redefinirTaxaAction'/>">

            <div class="mun-grid-2">
                <div class="mun-section">
                    <label for="taxaNivel1" class="mun-card-description"><strong>Nível 1</strong> — superior a 250 g/km</label>
                    <input
                            type="number"
                            id="taxaNivel1"
                            name="taxaNivel1"
                            class="mun-input-clean"
                            step="0.01"
                            min="0"
                            value="${municipio.taxaNivel1}"
                            required />
                </div>

                <div class="mun-section">
                    <label for="taxaNivel2" class="mun-card-description"><strong>Nível 2</strong> — entre 250 e 200 g/km</label>
                    <input
                            type="number"
                            id="taxaNivel2"
                            name="taxaNivel2"
                            class="mun-input-clean"
                            step="0.01"
                            min="0"
                            value="${municipio.taxaNivel2}"
                            required />
                </div>

                <div class="mun-section">
                    <label for="taxaNivel3" class="mun-card-description"><strong>Nível 3</strong> — entre 200 e 150 g/km</label>
                    <input
                            type="number"
                            id="taxaNivel3"
                            name="taxaNivel3"
                            class="mun-input-clean"
                            step="0.01"
                            min="0"
                            value="${municipio.taxaNivel3}"
                            required />
                </div>

                <div class="mun-section">
                    <label for="taxaNivel4" class="mun-card-description"><strong>Nível 4</strong> — entre 150 e 100 g/km</label>
                    <input
                            type="number"
                            id="taxaNivel4"
                            name="taxaNivel4"
                            class="mun-input-clean"
                            step="0.01"
                            min="0"
                            value="${municipio.taxaNivel4}"
                            required />
                </div>

                <div class="mun-section">
                    <label for="taxaNivel5" class="mun-card-description"><strong>Nível 5</strong> — entre 100 e 50 g/km</label>
                    <input
                            type="number"
                            id="taxaNivel5"
                            name="taxaNivel5"
                            class="mun-input-clean"
                            step="0.01"
                            min="0"
                            value="${municipio.taxaNivel5}"
                            required />
                </div>

                <div class="mun-section">
                    <label for="taxaNivel6" class="mun-card-description"><strong>Nível 6</strong> — menor que 50 g/km</label>
                    <input
                            type="number"
                            id="taxaNivel6"
                            name="taxaNivel6"
                            class="mun-input-clean"
                            step="0.01"
                            min="0"
                            value="${municipio.taxaNivel6}"
                            required />
                </div>
            </div>

            <div class="mun-section">
                <label for="taxaNivel7" class="mun-card-description"><strong>Nível 7</strong> — 0 g/km</label>
                <input
                        type="number"
                        id="taxaNivel7"
                        name="taxaNivel7"
                        class="mun-input-clean"
                        step="0.01"
                        min="0"
                        value="${municipio.taxaNivel7}"
                        required />
            </div>

            <div class="mun-grid-2">
                <button type="submit" class="mun-btn-primary">
                    <i class="bi bi-save"></i>
                    Guardar alterações
                </button>

                <a href="<c:url value='/municipio/homeMunicipio'/>" class="mun-btn-secondary">
                    <i class="bi bi-arrow-left"></i>
                    Voltar
                </a>
            </div>
        </form>
    </div>

    <div class="mun-card mun-section">
        <h2 class="mun-card-title">Notas</h2>
        <p class="mun-card-description">
            O nível 1 corresponde aos veículos mais poluentes e o nível 7 aos veículos com emissão nula.
            Depois de guardar, os novos cálculos passam a usar esta tabela.
        </p>
    </div>

    <div class="dashboard-actions">
        <a href="${pageContext.request.contextPath}/municipio/homeMunicipio" class="smart-btn smart-btn-secondary">
            Voltar ao Home
        </a>
    </div>

</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>