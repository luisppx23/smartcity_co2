<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en"> <head>
    <meta charset="UTF-8">
    <title>SignUp</title>
    <link rel="stylesheet" href="/styles/reset.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <link rel="stylesheet" href="/styles/estilos.css">
</head>
<body>
<div class="page-overlay"></div>

<div class="page-content">
    <div class="hero-text">
        <h2>Defina uma nova<br>password.</h2>
    </div>

    <section class="login-section">
        <div class="login-card">
            <div class="text-center mb-4">
                <img src="/images/logo-smart-city.png" alt="Logótipo" class="login-logo">
                <h1 class="portal-title">Portal Smart City</h1>
            </div>

            <div class="text-center mb-4">
                <h2 class="section-title">Nova Password</h2>
            </div>

            <form method="POST" action="/auth/atualizarPasswordAction">
                <input type="hidden" name="username" value="${username}">

                <div class="mb-4">
                    <label for="novaPassword" class="form-label">Nova Password</label>
                    <input
                            type="password"
                            class="form-control custom-input"
                            id="novaPassword"
                            name="novaPassword"
                            placeholder="Introduza a nova password"
                            required
                            minlength="6"
                    >
                </div>

                <div class="mb-4">
                    <label for="confirmarPassword" class="form-label">Confirmar Password</label>
                    <input
                            type="password"
                            class="form-control custom-input"
                            id="confirmarPassword"
                            name="confirmarPassword"
                            placeholder="Confirme a nova password"
                            required
                            minlength="6"
                    >
                </div>

                <div class="d-grid mb-3">
                    <button type="submit" class="btn btn-primary custom-btn">
                        CONFIRMAR ALTERAÇÃO
                    </button>
                </div>

                <div class="text-center">
                    <a href="/login">Voltar ao login</a>
                </div>
            </form>
        </div>
    </section>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous">
</script>
</body>
</html>