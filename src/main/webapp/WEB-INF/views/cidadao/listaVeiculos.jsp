<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lista de Veículos - Cidadão </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
</head>
<body>

<div class="container">
    <div class="card mt-4">
        <div class="card-header">
            <h2>Veículos do Cidadão ${user.username}</h2>
        </div>
        <div class="card-body">
            <table class="table">
                <thead>
                <tr>
                    <th>Matrícula</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="veiculo" items="${cidadao.listaDeVeiculos}">
                    <tr>
                        <td>${veiculo.matricula}</td>
                        <td>${veiculo.marca}</td>
                        <td>${veiculo.modelo}</td>
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
            <a href="/cidadao/dashboardCidadao">Voltar ao Dashboard</a>
        </div>
    </div>
</div>
</body>
</html>