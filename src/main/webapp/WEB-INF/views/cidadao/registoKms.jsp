<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registo Kms</title>
</head>
<body>
<div class="container">
    <div class="card mt-4">
        <form action="/auth/cidadao/registoKmsAction" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>


            <div class="card p-4 shadow">
                <h3>Registar Quilómetros Percorridos</h3>
                <p class="text-muted">Insira o total de kms percorridos este mês.</p>

                <div class="mb-3">
                    <label class="form-label">Quilómetros (km)</label>
                    <input type="number" step="0.1" name="kms" class="form-control" placeholder="Ex: 150.5" required>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-success">Salvar Registo</button>
                    <a href="/auth/autenticado" class="btn btn-secondary">Voltar</a>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>