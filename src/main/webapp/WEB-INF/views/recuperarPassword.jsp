<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Recuperar Password – Smart City CO₂</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
    <link rel="stylesheet" href="/styles/auth-entrada.css" />
</head>
<body class="auth-page">

<div class="auth-card">

    <div class="auth-card-header">
        <img src="/images/logo.jpeg" alt="Smart City CO₂" class="auth-logo" />
        <h1 class="auth-card-title">Portal Smart City</h1>
        <h2 class="auth-card-subtitle">Recuperar Password</h2>
        <p class="auth-card-description">Introduza o seu username e email para verificar a sua identidade.</p>
    </div>

    <div class="auth-divider"></div>

    <% if (request.getAttribute("error") != null) { %>
    <div class="auth-alert auth-alert-error">
        <i class="bi bi-exclamation-circle"></i> <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <form method="POST" action="/auth/verificarUtilizadorAction">

        <div class="auth-group">
            <label class="auth-label" for="username">Username</label>
            <input type="text" id="username" name="username"
                   class="auth-input"
                   placeholder="Introduza o seu username"
                   required />
        </div>

        <div class="auth-group">
            <label class="auth-label" for="email">Email</label>
            <input type="email" id="email" name="email"
                   class="auth-input"
                   placeholder="Introduza o seu email"
                   required />
        </div>

        <div class="auth-btn-icon-wrap">🌿</div>

        <button type="submit" class="auth-btn-primary">
            AVANÇAR
        </button>

    </form>

    <div class="auth-links">
        <a href="/auth/login" class="auth-link">Voltar ao login</a>
    </div>

</div>

<%--<p class="auth-page-footer">X Toneladas de CO₂ evitadas pelo seu município.</p>--%>

</body>
</html>