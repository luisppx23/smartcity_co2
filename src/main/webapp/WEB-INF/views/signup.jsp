<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SignUp</title>
    <link rel="stylesheet" href="/styles/reset.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="/styles/estilos.css">
</head>
<body>
<header class="d-flex flex-column align-items-center">
    <img src="/images/logotipo_versao_3.jpeg" alt="Logótipo do Site" width="180" height="250">
    <h2>Portal Smart City</h2>
</header>
<div class="container">
    <div class="card mt-4">
        <div class="card-header">
            <h2>Registo de Utilizador</h2>
        </div>
        <div class="card-body">
            <form method="POST" action="/auth/signUpAction">
                <div class="mb-3">
                    <label for="firstName" class="form-label">Primeiro Nome</label>
                    <input type="text" class="form-control" id="firstName" name="firstName" placeholder="João">
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Último Nome</label>
                    <input type="text" class="form-control" id="lastName" name="lastName" placeholder="Silva">
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="O seu email">
                </div>
                <div class="mb-3">
                    <label for="tipo">Tipo de Conta:</label>
                    <select id="tipo" name="tipo">
                        <option value="cidadao">Cidadão</option>
                        <option value="municipio">Município</option>
                    </select>
                </div>
                <div>
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" placeholder="O seu username">
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="A sua password">
                </div>
                <div class="mb-3">
                    <button class="btn btn-primary">
                        Registar
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>