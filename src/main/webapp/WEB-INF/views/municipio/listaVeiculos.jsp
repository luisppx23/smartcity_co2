<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lista de Veículos - Município</title>
</head>
<body>

<div class="container">
    <div class="card mt-4">
        <div class="card-header">
            <h2>Veículos dos Cidadãos do Município ${user.username}</h2>
        </div>
        <div class="card-body">
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
                    <c:forEach var="veiculo" items="${cidadao.listaDeVeiculos}">
                        <tr>
                            <td>${veiculo.matricula}</td>
                            <td>${veiculo.marca}</td>
                            <td>${veiculo.modelo}</td>
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