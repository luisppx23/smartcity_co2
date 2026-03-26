<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home Municipio</title>
</head>
<body>

<div class="container">
    <div class="card mt-4">
        <div class="card-header">
            <h2>Cidadãos do Municipio ${user.getUsername()}</h2>
        </div>
        <div class="card-body">
            <table class="table">
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