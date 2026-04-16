<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login – Smart City CO₂</title>
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
    </div>

    <div class="auth-divider"></div>

    <%-- Mensagem de erro (credenciais inválidas, etc.) --%>
    <c:if test="${not empty erro}">
        <div class="auth-alert auth-alert-error">
            <i class="bi bi-exclamation-circle"></i> ${erro}
        </div>
    </c:if>

    <%-- Mensagem de sucesso (conta criada, password alterada, etc.) --%>
    <c:if test="${not empty message}">
        <div class="auth-alert auth-alert-success">
            <i class="bi bi-check-circle"></i> ${message}
        </div>
    </c:if>

    <form method="POST" action="/login">

        <div class="auth-group">
            <label class="auth-label" for="username">Username</label>
            <input type="text" id="username" name="username"
                   class="auth-input"
                   placeholder="Introduza o seu username"
                   required />
        </div>

        <div class="auth-group">
            <label class="auth-label" for="password">Password</label>
            <input type="password" id="password" name="password"
                   class="auth-input"
                   placeholder="Introduza a sua password"
                   required />
        </div>

        <button type="submit" class="auth-btn-primary">
            ENTRAR NO FUTURO SUSTENTÁVEL
        </button>

    </form>

    <div class="auth-links">
        <a href="/auth/recuperarPassword" class="auth-link">Esqueceu-se da password?</a>
        <p class="auth-link-text">Ainda não tem conta? <a href="/auth/signup">Registe-se aqui</a></p>
    </div>

</div>

</body>
</html>