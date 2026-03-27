<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home Cidadao</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/auth.css">
</head>
<body>
<div class="auth-page">
    <div class="auth-overlay">
        <div class="auth-card">
            <div class="auth-header">
                <h1>Área do Cidadão</h1>
                <p>Bem-vindo, <strong>${user.firstName} ${user.lastName}</strong></p>
            </div>

            <div class="table-wrapper">
                <table class="user-table">
                    <tbody>
                    <tr>
                        <th>Username</th>
                        <td>${user.username}</td>
                    </tr>
                    <tr>
                        <th>Email</th>
                        <td>${user.email}</td>
                    </tr>
                    <tr>
                        <th>Contacto</th>
                        <td>${user.contacto}</td>
                    </tr>
                    <tr>
                        <th>Morada</th>
                        <td>${user.morada}</td>
                    </tr>
                    <tr>
                        <th>Data de Registo</th>
                        <td>${user.data_registo}</td>
                    </tr>
                    <tr>
                        <th>Ativo</th>
                        <td>${user.ativo}</td>
                    </tr>
                    <tr>
                        <th>Tipo</th>
                        <td>${user.tipo}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="auth-actions">
                <a href="${pageContext.request.contextPath}/cidadao/dashboardCidadao" class="dashboard-btn">
                    Ir para o Dashboard
                </a>
            </div>
        </div>
    </div>
</div>
</html>