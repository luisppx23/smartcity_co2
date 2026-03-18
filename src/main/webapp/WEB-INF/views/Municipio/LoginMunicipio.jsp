<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Município – Portal Smart City</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            min-height: 100vh;
            background: linear-gradient(135deg, #f0fdfa 0%, #ccfbf1 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 1rem;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
        }

        .container { width: 100%; max-width: 448px; }

        .btn-voltar {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            background: none;
            border: none;
            color: #0f766e;
            font-size: 0.95rem;
            cursor: pointer;
            margin-bottom: 1.5rem;
            text-decoration: none;
        }
        .btn-voltar:hover { color: #115e59; }

        .card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 10px 15px rgba(0,0,0,0.1);
            padding: 2rem;
        }

        .banner {
            display: flex;
            align-items: center;
            gap: 0.75rem;
            background-color: #14b8a6;
            color: white;
            padding: 0.75rem 1rem;
            border-radius: 0.5rem;
            margin-bottom: 1.5rem;
        }
        .banner h2 { font-size: 1.25rem; font-weight: 600; }

        .card h3 {
            font-size: 1.1rem;
            font-weight: 600;
            color: #111827;
            margin-bottom: 1rem;
        }

        .form-group { margin-bottom: 1rem; }

        .form-group label {
            display: block;
            font-size: 0.875rem;
            font-weight: 500;
            color: #374151;
            margin-bottom: 0.25rem;
        }

        .form-group input {
            width: 100%;
            padding: 0.5rem 1rem;
            border: 1px solid #d1d5db;
            border-radius: 0.5rem;
            font-size: 1rem;
            outline: none;
            transition: border-color 0.2s;
        }
        .form-group input:focus {
            border-color: #14b8a6;
            box-shadow: 0 0 0 2px rgba(20,184,166,0.3);
        }

        .btn-primary {
            width: 100%;
            padding: 0.625rem;
            background-color: #14b8a6;
            color: white;
            border: none;
            border-radius: 0.5rem;
            font-size: 1rem;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.2s;
            margin-top: 0.5rem;
        }
        .btn-primary:hover { background-color: #0f766e; }

        .error-msg {
            background: #fee2e2;
            color: #dc2626;
            padding: 0.5rem 1rem;
            border-radius: 0.5rem;
            font-size: 0.875rem;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>

<div class="container">

    <a href="${pageContext.request.contextPath}/" class="btn-voltar">
        ← Voltar
    </a>

    <div class="card">

        <div class="banner">
            <span>🏛️</span>
            <h2>MUNICÍPIO</h2>
        </div>

        <h3>Sign In Município</h3>

        <% if (request.getAttribute("erro") != null) { %>
        <div class="error-msg">${erro}</div>
        <% } %>

        <form action="${pageContext.request.contextPath}/municipio/login"
              method="post">
            <div class="form-group">
                <label for="codigo">Código</label>
                <input type="text" id="codigo" name="codigo"
                       placeholder="Código do Município" required />
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email"
                       placeholder="Email" required />
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password"
                       placeholder="Password" required />
            </div>
            <button type="submit" class="btn-primary">Entrar</button>
        </form>

    </div>
</div>

</body>
</html>