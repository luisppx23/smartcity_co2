<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Lista de Cidadãos – Portal do Município</title>
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
<body class="form-page-body">
<jsp:include page="navbarm.jsp"/>
<section class="mun-hero">
    <h1 class="mun-hero-title">Lista de Cidadãos</h1>
    <p class="mun-hero-subtitle">Cidadãos registados no município
        <c:if test="${not empty municipio.nome}"><c:out value="${municipio.nome}"/></c:if>
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
            <h3 class="mun-card-title">Cidadãos do Município</h3>
        </div>

        <c:choose>
            <c:when test="${empty listaCidadaos}">
                <div class="mun-alert mun-alert-info">
                    <i class="bi bi-info-circle"></i> Ainda não existem cidadãos registados neste município.
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive">
                    <table class="mun-table">
                        <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Contacto</th>
                            <th>Morada</th>
                            <th>Veículos</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cidadao" items="${listaCidadaos}">
                            <tr>
                                <td><strong>${cidadao.firstName} ${cidadao.lastName}</strong></td>
                                <td>${cidadao.contacto}</td>
                                <td>${cidadao.morada}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty cidadao.listaDeVeiculos}">
                                                <span class="mun-badge mun-badge-success">
                                                    ${cidadao.listaDeVeiculos.size()} veículo(s)
                                                </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="mun-badge mun-badge-warning">Sem veículos</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
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