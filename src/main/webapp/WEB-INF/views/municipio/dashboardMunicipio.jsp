<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard Municipio</title>
</head>
<body>
<div class="container">
    <div class="card mt-4">
        <div class="mt-3 text-center">
            <a href="/municipio/redefinirTaxa">Redefinir Taxa</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/municipio/homeMunicipio">Home Municipio</a>
        </div>
        <div class="mt-3 text-center">
            <a href="/municipio/relatoriosMunicipio">Relatorios Municipio</a>
        </div>
        <div class="mt-3 text-center">
            <form action="/logout" method="post">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>

    </div>
</div>
</body>
</html>