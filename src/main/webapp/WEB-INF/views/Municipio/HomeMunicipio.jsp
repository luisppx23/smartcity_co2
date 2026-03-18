<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home Município – Portal Smart City</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            background-color: #f3f4f6;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            padding: 2rem 1rem;
        }

        .container { max-width: 896px; margin: 0 auto; }

        /* --- Cartão genérico --- */
        .card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            padding: 1.5rem;
            margin-bottom: 1.5rem;
        }

        .card h2 {
            font-size: 1.5rem;
            font-weight: 700;
            color: #111827;
            margin-bottom: 1.5rem;
        }

        /* --- Grid 2 colunas --- */
        .grid-2 {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1.5rem;
        }
        @media (max-width: 640px) { .grid-2 { grid-template-columns: 1fr; } }

        /* --- Botões de navegação --- */
        .nav-btn {
            display: block;
            width: 100%;
            padding: 1.5rem;
            border-radius: 0.5rem;
            border: 2px solid;
            text-align: left;
            cursor: pointer;
            text-decoration: none;
            transition: border-color 0.2s, transform 0.1s;
        }
        .nav-btn:hover { transform: translateY(-1px); }

        .nav-btn-blue  { background: linear-gradient(135deg,#eff6ff,#dbeafe); border-color: #bfdbfe; }
        .nav-btn-blue:hover  { border-color: #60a5fa; }

        .nav-btn-orange { background: linear-gradient(135deg,#fff7ed,#ffedd5); border-color: #fed7aa; }
        .nav-btn-orange:hover { border-color: #fb923c; }

        .btn-header { display: flex; align-items: center; gap: 0.75rem; margin-bottom: 1rem; }

        .icon-box {
            padding: 0.75rem;
            border-radius: 0.5rem;
            font-size: 1.25rem;
            line-height: 1;
            color: white;
        }
        .icon-blue   { background-color: #3b82f6; }
        .icon-orange { background-color: #f97316; }

        .btn-header h3 { font-size: 1rem; font-weight: 600; color: #111827; }
        .btn-desc { font-size: 0.875rem; color: #4b5563; }

        /* --- Dashboard preview --- */
        .card h3 {
            font-size: 1.25rem;
            font-weight: 600;
            color: #111827;
            margin-bottom: 1rem;
        }

        .grid-3 {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 1rem;
            margin-bottom: 1.5rem;
        }
        @media (max-width: 640px) { .grid-3 { grid-template-columns: 1fr; } }

        .stat-box {
            padding: 1rem;
            border-radius: 0.5rem;
            border: 1px solid;
        }
        .stat-box p.label  { font-size: 0.875rem; color: #4b5563; margin-bottom: 0.25rem; }
        .stat-box p.value  { font-size: 1.875rem; font-weight: 700; line-height: 1.1; }
        .stat-box p.sublabel { font-size: 0.75rem; color: #6b7280; margin-top: 0.25rem; }
        .stat-box span.unit { font-size: 1rem; }

        .stat-blue   { background:#eff6ff; border-color:#bfdbfe; }
        .stat-blue   .value { color: #2563eb; }

        .stat-orange { background:#fff7ed; border-color:#fed7aa; }
        .stat-orange .value { color: #ea580c; }

        .stat-green  { background:#f0fdf4; border-color:#bbf7d0; }
        .stat-green  .value { color: #16a34a; }

        /* --- Chart placeholder --- */
        .chart-placeholder {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 1.5rem;
            padding: 2rem;
            background: #f9fafb;
            border-radius: 0.5rem;
        }

        .chart-icon { font-size: 3rem; line-height: 1; }

        .chart-bars { display: flex; flex-direction: column; gap: 0.5rem; }
        .bar { height: 0.5rem; border-radius: 9999px; }
        .bar-blue  { width: 8rem;  background: #93c5fd; }
        .bar-teal  { width: 6rem;  background: #5eead4; }
        .bar-green { width: 7rem;  background: #86efac; }

        .link-dashboard {
            display: block;
            text-align: center;
            margin-top: 1rem;
            color: #0d9488;
            font-weight: 500;
            text-decoration: none;
        }
        .link-dashboard:hover { color: #0f766e; }
    </style>
</head>
<body>

<div class="container">

    <%-- CARD 1: Navegação principal --%>
    <div class="card">
        <h2>HOME MUNICÍPIO</h2>

        <div class="grid-2">

            <%-- Emissões Totais --%>
            <a href="${pageContext.request.contextPath}/municipio/dashboard"
               class="nav-btn nav-btn-blue">
                <div class="btn-header">
                    <div class="icon-box icon-blue">📊</div>
                    <h3>Emissões Totais</h3>
                </div>
                <p class="btn-desc">Ver estatísticas completas</p>
            </a>

            <%-- Veículos --%>
            <a href="${pageContext.request.contextPath}/municipio/dashboard"
               class="nav-btn nav-btn-orange">
                <div class="btn-header">
                    <div class="icon-box icon-orange">🚗</div>
                    <h3>Veículos</h3>
                </div>
                <p class="btn-desc">Gerenciar veículos registados</p>
            </a>

        </div>
    </div>

    <%-- CARD 2: Dashboard preview --%>
    <div class="card">
        <h3>DASHBOARD MUNICÍPIO</h3>

        <div class="grid-3">
            <div class="stat-box stat-blue">
                <p class="label">Emissões Totais</p>
                <p class="value">1200 <span class="unit">kg</span></p>
                <p class="sublabel">Este mês</p>
            </div>
            <div class="stat-box stat-orange">
                <p class="label">Veículos</p>
                <p class="value">180</p>
                <p class="sublabel">Registados</p>
            </div>
            <div class="stat-box stat-green">
                <p class="label">Média</p>
                <p class="value">6.7 <span class="unit">kg</span></p>
                <p class="sublabel">Por veículo</p>
            </div>
        </div>

        <%-- Placeholder do gráfico --%>
        <div class="chart-placeholder">
            <span class="chart-icon">📊</span>
            <span class="chart-icon">✉️</span>
            <div class="chart-bars">
                <div class="bar bar-blue"></div>
                <div class="bar bar-teal"></div>
                <div class="bar bar-green"></div>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/municipio/dashboard"
           class="link-dashboard">
            Ver dashboard completo →
        </a>
    </div>

</div>

</body>
</html>