<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <title>Recuperar Password</title>
    <link rel="stylesheet" href="/styles/reset.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="/styles/estilos.css">
</head>
<body>
<div class="page-overlay"></div>

<div class="page-content">
    <div class="auth-wrapper">
        <div class="hero-text">
            <h2>Recupere o acesso<br>à sua conta.</h2>
        </div>

        <section class="login-section">
            <div class="login-card">
                <div class="text-center mb-4">
                    <img src="/images/logo-smart-city.png" alt="Logótipo" class="login-logo">
                    <h1 class="portal-title">Portal Smart City</h1>
                </div>

                <div class="text-center mb-4">
                    <h2 class="section-title">Recuperar Password</h2>
                    <p class="text-muted custom-muted">
                        Introduza o seu email para receber as instruções de recuperação.
                    </p>
                </div>

                <form method="POST" action="/auth/verificarUtilizadorAction">
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

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-primary custom-btn">
                            AVANÇAR
                        </button>
                    </div>

                    <div class="text-center">
                        <a href="login">Voltar ao login</a>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>
</body>
</html>