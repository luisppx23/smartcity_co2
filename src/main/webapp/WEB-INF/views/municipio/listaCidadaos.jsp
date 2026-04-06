<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home Municipio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/form-pagesm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>
<body class="form-page-body">
<jsp:include page="../navbarm.jsp"/>
<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>
    <div class="form-card history-card">
        <div class="form-card-header">
            <div class="form-card-logo">👥</div>
            <h1 class="form-card-title">Município</h1>
            <h2 class="form-card-subtitle">Cidadãos do Município ${user.getUsername()}</h2>
            <p class="form-card-description">
                Consulte abaixo a lista de cidadãos associados ao município.
            </p>
        </div>

        <div class="history-table-wrapper">
            <table class="history-table">
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Contacto</th>
                    <th>Morada</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="cidadao" items="${municipio.listaDeCidadaos}">
                    <tr>
                        <td>${cidadao.firstName} ${cidadao.lastName}</td>
                        <td>${cidadao.contacto}</td>
                        <td>${cidadao.morada}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="smart-form-actions history-actions">
            <a href="/municipio/homeMunicipio" class="smart-btn smart-btn-secondary">
                Voltar ao Dashboard
            </a>
        </div>
    </div>
</div>
</body>
</html>