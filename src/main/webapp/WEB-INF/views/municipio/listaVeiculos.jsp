<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Lista de Veículos – Portal do Município</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>
<body class="form-page-body">

<jsp:include page="navbarm.jsp"/>

<section class="mun-hero">
    <h1 class="mun-hero-title">Lista de Veículos</h1>
    <p class="mun-hero-subtitle">Veículos associados aos cidadãos do município
        <c:if test="${not empty municipio.nome}"><c:out value="${municipio.nome}"/></c:if>.
    </p>
</section>

<main class="mun-page-content">

    <c:if test="${not empty erro}">
        <div class="mun-alert mun-alert-danger mun-section">
            <i class="bi bi-exclamation-circle"></i> ${erro}
        </div>
    </c:if>

    <div class="mun-card mun-section">
        <div class="mun-table-header">
            <h3 class="mun-card-title">Veículos do Município</h3>
        </div>

        <c:choose>
            <c:when test="${empty listaCidadaos}">
                <div class="mun-alert mun-alert-info">
                    <i class="bi bi-info-circle"></i> Ainda não existem veículos registados neste município.
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="mun-table">
                        <thead>
                        <tr>
                            <th>Matrícula</th>
                            <th>Marca</th>
                            <th>Modelo</th>
                            <th>Combustível</th>
                            <th>Proprietário</th>
                            <th>Contacto</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cidadao" items="${listaCidadaos}">
                            <c:forEach var="ownership" items="${cidadao.listaDeVeiculos}">
                                <tr>
                                    <td><strong>${ownership.matricula}</strong></td>
                                    <td>${ownership.veiculo.marca}</td>
                                    <td>${ownership.veiculo.modelo}</td>
                                    <td>${ownership.veiculo.tipoDeCombustivel}</td>
                                    <td>${cidadao.firstName} ${cidadao.lastName}</td>
                                    <td>${cidadao.contacto}</td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="mun-mt-20">
            <a href="<c:url value='/municipio/dashboardMunicipio'/>" class="mun-btn-secondary">
                <i class="bi bi-arrow-left"></i> Voltar ao Dashboard
            </a>
        </div>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>