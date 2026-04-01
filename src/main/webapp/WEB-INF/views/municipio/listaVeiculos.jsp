<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lista de Veículos - Município</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/formpagesm.css">
</head>
<body>
<jsp:include page="../navbarm.jsp"/>
<div class="form-page-wrapper">
    <div class="form-page-background-shape"></div>
    <div class="form-card history-card">
        <div class="form-card-header">
            <h2>Veículos dos Cidadãos do Município ${user.username}</h2>
        </div>
        <div class="form-card-description">
            <table class="table">
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
    </div>
</div>
<div class="container">
    <div class="card mt-4">
        <div class="mt-3 text-center">
            <a href="/municipio/dashboardMunicipio">Voltar ao Dashboard</a>
        </div>
    </div>
</div>
</body>
</html>