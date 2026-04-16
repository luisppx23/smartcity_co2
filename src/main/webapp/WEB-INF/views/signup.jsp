<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Registo – Smart City CO₂</title>
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
        <h2 class="auth-card-subtitle">Registo de Cidadão</h2>
    </div>

    <div class="auth-divider"></div>

    <c:if test="${not empty erro}">
        <div class="auth-alert auth-alert-error">
            <i class="bi bi-exclamation-circle"></i> ${erro}
        </div>
    </c:if>

    <form method="POST" action="/auth/signUpAction">
        <input type="hidden" name="tipo" value="cidadao" />

        <div class="auth-grid-2">
            <div class="auth-group">
                <label class="auth-label" for="firstName">Primeiro Nome</label>
                <input type="text" id="firstName" name="firstName"
                       class="auth-input" placeholder="João" required />
            </div>
            <div class="auth-group">
                <label class="auth-label" for="lastName">Último Nome</label>
                <input type="text" id="lastName" name="lastName"
                       class="auth-input" placeholder="Silva" required />
            </div>
        </div>

        <div class="auth-group">
            <label class="auth-label" for="email">Email</label>
            <input type="email" id="email" name="email"
                   class="auth-input" placeholder="Introduza o seu email" required />
        </div>

        <div class="auth-group">
            <label class="auth-label" for="morada">Morada</label>
            <input type="text" id="morada" name="morada"
                   class="auth-input" placeholder="Rua, número, andar, localidade" required />
        </div>

        <div class="auth-grid-2">
            <div class="auth-group">
                <label class="auth-label" for="contacto">Contacto</label>
                <input type="tel" id="contacto" name="contacto"
                       class="auth-input" placeholder="912345678" required />
            </div>
            <div class="auth-group">
                <label class="auth-label" for="nif">NIF</label>
                <input type="tel" id="nif" name="nif"
                       class="auth-input" placeholder="123456789" required />
            </div>
        </div>

        <div class="auth-group">
            <label class="auth-label" for="municipioId">Município</label>
            <select id="municipioId" name="municipioId" class="auth-select" required>
                <option value="">Selecione um município</option>
                <c:forEach var="municipio" items="${municipios}">
                    <option value="${municipio.id}">${municipio.nome}</option>
                </c:forEach>
            </select>
            <small class="auth-help">Selecione o município onde reside.</small>
        </div>

        <div class="auth-group">
            <label class="auth-label" for="username">Username</label>
            <input type="text" id="username" name="username"
                   class="auth-input" placeholder="Introduza o seu username" required />
        </div>

        <div class="auth-grid-2">
            <div class="auth-group">
                <label class="auth-label" for="password">Password</label>
                <input type="password" id="password" name="password"
                       class="auth-input" placeholder="Password" required />
            </div>
            <div class="auth-group">
                <label class="auth-label" for="confirmPassword">Confirmar</label>
                <input type="password" id="confirmPassword" name="confirmPassword"
                       class="auth-input" placeholder="Confirmar" required />
            </div>
        </div>

        <button type="submit" class="auth-btn-primary">
            REGISTAR
        </button>

    </form>

    <div class="auth-links">
        <p class="auth-link-text">Já possui conta? <a href="/auth/login">Faça login</a></p>
    </div>

</div>

<p class="auth-page-footer">X Toneladas de CO₂ evitadas pelo seu município.</p>

</body>
</html>