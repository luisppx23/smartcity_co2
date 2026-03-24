<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>

    <link rel="stylesheet" href="/styles/reset.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="/styles/estilos.css">
</head>
<body>

<div class="page-overlay"></div>

<div class="page-content">
    <div class="hero-text">
        <h2>Junte-se ao futuro<br>sustentável.</h2>
    </div>

    <section class="login-section">
        <div class="login-card">
            <div class="text-center mb-4">
                <img src="/images/logo-smart-city.png" alt="Logótipo" class="login-logo">
                <h1 class="portal-title">Portal Smart City</h1>
            </div>

            <div class="text-center mb-4">
                <h2 class="section-title">Registo de Utilizador</h2>
            </div>

            <form method="POST" action="/auth/signUpAction">
                <div class="mb-3">
                    <label for="firstName" class="form-label">Primeiro Nome</label>
                    <input
                            type="text"
                            class="form-control custom-input"
                            id="firstName"
                            name="firstName"
                            placeholder="João"
                            required
                    >
                </div>

                <div class="mb-3">
                    <label for="lastName" class="form-label">Último Nome</label>
                    <input
                            type="text"
                            class="form-control custom-input"
                            id="lastName"
                            name="lastName"
                            placeholder="Silva"
                            required
                    >
                </div>

                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input
                            type="email"
                            class="form-control custom-input"
                            id="email"
                            name="email"
                            placeholder="Introduza o seu email"
                            required
                    >
                </div>

                <div class="mb-3">
                    <label for="tipo" class="form-label">Tipo de Conta</label>
                    <select id="tipo" name="tipo" class="form-select custom-select">
                        <option value="cidadao">Cidadão</option>
                        <option value="municipio">Município</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="nif" class="form-label">NIF</label>
                    <input type="number" class="form-control" id="nif" name="nif" placeholder="O seu NIF">
                </div>

                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input
                            type="text"
                            class="form-control custom-input"
                            id="username"
                            name="username"
                            placeholder="Introduza o seu username"
                            required
                    >
                </div>

                <div class="mb-4">
                    <label for="password" class="form-label">Password</label>
                    <input
                            type="password"
                            class="form-control custom-input"
                            id="password"
                            name="password"
                            placeholder="Introduza a sua password"
                            required
                    >
                </div>

                <div class="d-grid mb-3">
                    <button type="submit" class="btn btn-primary custom-btn">
                        REGISTAR
                    </button>
                </div>

                <div class="text-center mb-2">
                    <span>Já possui conta? <a href="/auth/login">Faça login</a>
                </div>

            </form>
        </div>
    </section>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>