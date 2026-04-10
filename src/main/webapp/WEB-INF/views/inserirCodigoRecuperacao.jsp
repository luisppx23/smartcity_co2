<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Verificar Código – Smart City CO₂</title>
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
        <h2 class="auth-card-subtitle">Verificar Código</h2>
        <p class="auth-card-description">Introduza o código de 6 dígitos enviado para o seu email.</p>
    </div>

    <div class="auth-divider"></div>

    <% if (request.getAttribute("error") != null) { %>
    <div class="auth-alert auth-alert-error">
        <i class="bi bi-exclamation-circle"></i> <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <% if (request.getAttribute("message") != null) { %>
    <div class="auth-alert auth-alert-success">
        <i class="bi bi-check-circle"></i> <%= request.getAttribute("message") %>
    </div>
    <% } %>

    <form method="POST" action="/auth/validarCodigoAction">
        <input type="hidden" name="username" value="${username}" />

        <div class="auth-group">
            <label class="auth-label" for="codigo">Código de Verificação</label>
            <input type="text" id="codigo" name="codigo"
                   class="auth-input"
                   placeholder="000000"
                   maxlength="6"
                   pattern="[0-9]{6}"
                   required />
            <small class="auth-helper">Digite o código de 6 dígitos recebido por email</small>
        </div>

        <div class="auth-btn-icon-wrap">✉️</div>

        <button type="submit" class="auth-btn-primary">
            VERIFICAR CÓDIGO
        </button>

    </form>

    <div class="auth-links">
        <a href="/auth/recuperarPassword" class="auth-link">← Solicitar novo código</a>
        <span class="auth-separator">|</span>
        <a href="/auth/login" class="auth-link">Voltar ao login</a>
    </div>

</div>

</body>
</html>