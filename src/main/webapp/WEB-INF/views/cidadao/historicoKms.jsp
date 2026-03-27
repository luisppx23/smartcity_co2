<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../navbar.jsp"/>

<div class="container mt-5">
    <h2>Histórico de Quilómetros</h2>
    <hr>

    <c:if test="${empty listaRegistos}">
        <div class="alert alert-warning">Ainda não existem quilómetros registados na sua conta.</div>
    </c:if>

    <c:if test="${not empty listaRegistos}">
        <table class="table">
            <thead>
            <tr>
                <th>Data</th>
                <th>Kilómetros</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="registo" items="${listaRegistos}">
                <tr>
                    <td>${registo.mes_ano}</td>
                    <td>${registo.kms_mes} km</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <a href="/auth/autenticado" class="btn btn-secondary">Voltar ao Dashboard</a>
</div>