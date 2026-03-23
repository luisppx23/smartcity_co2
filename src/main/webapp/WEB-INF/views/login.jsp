<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Portal Smart City</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
        html, body { height: 100%; font-family: 'Inter', sans-serif; }

        .page {
            display: grid;
            grid-template-columns: 55% 45%;
            min-height: 100vh;
        }

        .left {
            position: relative;
            background-color: #f0f0ee;
            overflow: hidden;
        }
        .left-bg {
            position: absolute;
            inset: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
            object-position: center center;
        }
        .left-text {
            position: absolute;
            bottom: 12%;
            left: 8%;
            z-index: 2;
        }
        .left-text p {
            font-size: clamp(1.5rem, 2.8vw, 2.3rem);
            font-weight: 300;
            color: #1a1a1a;
            line-height: 1.25;
            letter-spacing: -0.01em;
        }

        .right {
            background-color: #e8e8e6;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem 1.5rem;
        }

        .card {
            background-color: #ebebdf;
            border-radius: 12px;
            padding: 2.5rem 2rem 2rem;
            width: 100%;
            max-width: 400px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .logo-img {
            width: 80px;
            height: 80px;
            object-fit: contain;
            margin-bottom: 1.1rem;
        }

        .card-title {
            font-size: 1.75rem;
            font-weight: 700;
            color: #1a1a1a;
            text-align: center;
            margin-bottom: 2rem;
            letter-spacing: -0.02em;
        }

        .field { width: 100%; margin-bottom: 1.25rem; }

        .field input {
            width: 100%;
            background: transparent;
            border: none;
            border-bottom: 1.5px solid #888;
            padding: 0.4rem 0;
            font-family: 'Inter', sans-serif;
            font-size: 0.95rem;
            color: #1a1a1a;
            outline: none;
            transition: border-color 0.2s;
        }
        .field input::placeholder { color: #888; }
        .field input:focus { border-bottom-color: #2d5a27; }

        .field-hint { font-size: 0.72rem; color: #aaa; margin-top: 0.3rem; }
        .field-last { margin-bottom: 2rem; }

        .btn-main {
            width: 100%;
            padding: 1rem;
            background-color: #2d5a27;
            color: #fff;
            border: none;
            border-radius: 4px;
            font-family: 'Inter', sans-serif;
            font-size: 0.75rem;
            font-weight: 600;
            letter-spacing: 0.13em;
            text-transform: uppercase;
            cursor: pointer;
            transition: background-color 0.2s, transform 0.1s;
            margin-bottom: 2rem;
        }
        .btn-main:hover { background-color: #1e3d1a; }
        .btn-main:active { transform: scale(0.99); }

        .forgot {
            font-size: 0.88rem;
            color: #555;
            text-decoration: none;
            text-align: center;
            display: block;
            transition: color 0.2s;
        }
        .forgot:hover { color: #2d5a27; }

        .error-msg {
            width: 100%;
            background: #fee2e2;
            color: #dc2626;
            padding: 0.6rem 0.9rem;
            border-radius: 4px;
            font-size: 0.82rem;
            margin-bottom: 1rem;
            border-left: 3px solid #dc2626;
        }

        @media (max-width: 700px) {
            .page { grid-template-columns: 1fr; }
            .left { min-height: 260px; }
            .left-text p { font-size: 1.3rem; }
        }
    </style>
</head>
<body>
<div class="page">

    <div class="left">
        <img class="left-bg"
             src="${pageContext.request.contextPath}/images/leaf-bg.png"
             alt="Folha com circuitos"/>
        <div class="left-text">
            <p>Transformando quilómetros<br>em consciência.</p>
        </div>
    </div>

    <div class="right">
        <div class="card">

            <img class="logo-img"
                 src="${pageContext.request.contextPath}/images/logo-smart-city.png"
                 alt="Logo Portal Smart City"/>

            <h1 class="card-title">Portal Smart City</h1>

            <% if (request.getAttribute("erro") != null) { %>
            <div class="error-msg">${erro}</div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login"
                  method="post" style="width:100%;">

                <div class="field">
                    <input type="email" name="email"
                           placeholder="Email" required
                           autocomplete="email"/>
                    <div class="field-hint">Enter your email address</div>
                </div>

                <div class="field field-last">
                    <input type="password" name="password"
                           placeholder="Password" required
                           autocomplete="current-password"/>
                </div>

                <button type="submit" class="btn-main">
                    Entrar no Futuro Sustentável
                </button>

            </form>

            <a href="${pageContext.request.contextPath}/recuperarPassword"
               class="forgot">Forgot Password?</a>

        </div>
    </div>

</div>
</body>
</html>
