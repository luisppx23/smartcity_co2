<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Lista de Veículos - Município</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/form-pagesm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
</head>
<body class="form-page-body">

<jsp:include page="../navbarm.jsp"/>

<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>

    <div class="form-card history-card">
        <div class="form-card-header">
            <h2 class="form-card-title">
                Veículos dos Cidadãos do Município ${user.username}
            </h2>
        </div>

        <div class="history-table-wrapper">
            <table class="history-table">
                <thead>
                <tr>
                    <th>Matrícula</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Proprietário</th>
                    <th>Contacto</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="cidadao" items="${municipio.listaDeCidadaos}">
                    <c:forEach var="ownership" items="${cidadao.listaDeVeiculos}">
                        <tr>
                            <td>${ownership.matricula}</td>
                            <td>${ownership.veiculo.marca}</td>
                            <td>${ownership.veiculo.modelo}</td>
                            <td>${cidadao.firstName} ${cidadao.lastName}</td>
                            <td>${cidadao.contacto}</td>
                        </tr>
                    </c:forEach>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="history-actions">
            <a href="${pageContext.request.contextPath}/municipio/homeMunicipio"
               class="smart-btn smart-btn-secondary">
                Voltar ao Dashboard
            </a>
        </div>
    </div>
</div>

</body>
</html>