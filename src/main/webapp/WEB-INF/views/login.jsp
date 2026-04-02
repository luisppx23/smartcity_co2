<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="/styles/reset.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="/styles/estilos.css">
</head>
<body>
<div class="page-overlay"></div>

<div class="page-content">
    <div class="hero-text">
        <h2>Transformando quilómetros<br>em consciência.</h2>
    </div>

    <section class="login-section">
        <div class="login-card">
            <div class="text-center mb-4">
                <img src="/images/logo.jpeg" alt="Logótipo" class="login-logo">
                <h1 class="portal-title">Portal Smart City</h1>
            </div>

            <form method="POST" action="/login">
                <div class="mb-4">
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

                <div class="d-grid mb-4">
                    <button type="submit" class="btn btn-primary custom-btn">
                        ENTRAR NO FUTURO SUSTENTÁVEL
                    </button>
                </div>

                <div class="text-center mb-2">
                    <a href="/auth/recuperarPassword">Esqueceu-se da password?</a>
                </div>

                <div class="text-center">
                    <span>Ainda não tem conta? <a href="/auth/signup">Registe-se aqui</a></span>
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