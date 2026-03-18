jsp<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Município – Portal Smart City</title>
    <!-- Chart.js via CDN — substitui Recharts -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            background-color: #f3f4f6;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
            padding: 2rem 1rem;
        }

        .container { max-width: 1024px; margin: 0 auto; }

        /* --- Header banner --- */
        .header-banner {
            background: linear-gradient(90deg, #0f766e, #14b8a6);
            border-radius: 0.5rem;
            padding: 1.5rem;
            color: white;
            margin-bottom: 1.5rem;
        }
        .header-banner h2 { font-size: 1.5rem; font-weight: 700; margin-bottom: 0.25rem; }
        .header-banner p  { color: #ccfbf1; font-size: 0.95rem; }

        /* --- Grid stats 4 colunas --- */
        .grid-4 {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 1rem;
            margin-bottom: 1.5rem;
        }
        @media (max-width: 768px) { .grid-4 { grid-template-columns: repeat(2,1fr); } }
        @media (max-width: 480px) { .grid-4 { grid-template-columns: 1fr; } }

        .stat-card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            padding: 1.5rem;
        }
        .stat-card .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
        }
        .stat-card .header h3 { font-size: 0.875rem; font-weight: 500; color: #4b5563; }
        .stat-card .header .icon { font-size: 1.25rem; }
        .stat-card .value { font-size: 1.875rem; font-weight: 700; color: #111827; }
        .stat-card .trend { font-size: 0.875rem; margin-top: 0.25rem; }
        .trend-green { color: #16a34a; }
        .trend-blue  { color: #2563eb; }
        .trend-gray  { color: #6b7280; }

        /* --- Grid gráficos 2 colunas --- */
        .grid-2 {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1.5rem;
            margin-bottom: 1.5rem;
        }
        @media (max-width: 768px) { .grid-2 { grid-template-columns: 1fr; } }

        .card {
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0,0,0,0.07);
            padding: 1.5rem;
        }
        .card h3 {
            font-size: 1.125rem;
            font-weight: 600;
            color: #111827;
            margin-bottom: 1rem;
        }

        /* --- Distribuição veículos --- */
        .grid-3 {
            display: grid;
            grid-template-columns: repeat(3,1fr);
            gap: 1rem;
        }
        @media (max-width: 640px) { .grid-3 { grid-template-columns: 1fr; } }

        .veiculo-box {
            padding: 1rem;
            border-radius: 0.5rem;
            border: 1px solid;
        }
        .veiculo-box .vheader {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
        }
        .veiculo-box h4 { font-weight: 600; }
        .veiculo-box .vvalue { font-size: 1.875rem; font-weight: 700; }
        .veiculo-box .progress-bg {
            height: 0.5rem;
            border-radius: 9999px;
            margin: 0.5rem 0 0.25rem;
            overflow: hidden;
        }
        .veiculo-box .progress-fill { height: 100%; border-radius: 9999px; }
        .veiculo-box .vpct { font-size: 0.875rem; }

        .vbox-blue   { background:#eff6ff; border-color:#bfdbfe; }
        .vbox-blue h4, .vbox-blue .vpct { color: #1e40af; }
        .vbox-blue .vvalue { color: #2563eb; }
        .vbox-blue .progress-bg  { background: #bfdbfe; }
        .vbox-blue .progress-fill { background: #2563eb; }

        .vbox-orange { background:#fff7ed; border-color:#fed7aa; }
        .vbox-orange h4, .vbox-orange .vpct { color: #9a3412; }
        .vbox-orange .vvalue { color: #ea580c; }
        .vbox-orange .progress-bg  { background: #fed7aa; }
        .vbox-orange .progress-fill { background: #ea580c; }

        .vbox-green  { background:#f0fdf4; border-color:#bbf7d0; }
        .vbox-green h4, .vbox-green .vpct { color: #14532d; }
        .vbox-green .vvalue { color: #16a34a; }
        .vbox-green .progress-bg  { background: #bbf7d0; }
        .vbox-green .progress-fill { background: #16a34a; }
    </style>
</head>
<body>

<div class="container">

    <%-- Header --%>
    <div class="header-banner">
        <h2>Dashboard do Município</h2>
        <p>Visão geral das emissões e veículos</p>
    </div>

    <%-- Stats 4 cartões --%>
    <div class="grid-4">
        <div class="stat-card">
            <div class="header">
                <h3>Emissões Totais</h3>
                <span class="icon">📈</span>
            </div>
            <div class="value">1200 kg</div>
            <div class="trend trend-green">↓ -3% vs mês anterior</div>
        </div>
        <div class="stat-card">
            <div class="header">
                <h3>Veículos</h3>
                <span class="icon">🚗</span>
            </div>
            <div class="value">180</div>
            <div class="trend trend-blue">↑ +2 novos este mês</div>
        </div>
        <div class="stat-card">
            <div class="header">
                <h3>Média/Veículo</h3>
                <span class="icon">⚡</span>
            </div>
            <div class="value">6.7 kg</div>
            <div class="trend trend-gray">CO₂ por veículo</div>
        </div>
        <div class="stat-card">
            <div class="header">
                <h3>Taxa Atual</h3>
                <span class="icon">€</span>
            </div>
            <div class="value">0.25</div>
            <div class="trend trend-gray">€/km rodado</div>
        </div>
    </div>

    <%-- Gráficos --%>
    <div class="grid-2">
        <div class="card">
            <h3>Evolução das Emissões</h3>
            <canvas id="barChart" height="150"></canvas>
        </div>
        <div class="card">
            <h3>Emissões Esta Semana</h3>
            <canvas id="lineChart" height="150"></canvas>
        </div>
    </div>

    <%-- Distribuição veículos --%>
    <div class="card">
        <h3>Distribuição de Veículos</h3>
        <div class="grid-3">
            <div class="veiculo-box vbox-blue">
                <div class="vheader">
                    <h4>Carros</h4>
                    <span>🚗</span>
                </div>
                <div class="vvalue">145</div>
                <div class="progress-bg">
                    <div class="progress-fill" style="width:80%"></div>
                </div>
                <div class="vpct">80% do total</div>
            </div>
            <div class="veiculo-box vbox-orange">
                <div class="vheader">
                    <h4>Motos</h4>
                    <span>🏍️</span>
                </div>
                <div class="vvalue">25</div>
                <div class="progress-bg">
                    <div class="progress-fill" style="width:14%"></div>
                </div>
                <div class="vpct">14% do total</div>
            </div>
            <div class="veiculo-box vbox-green">
                <div class="vheader">
                    <h4>Elétricos</h4>
                    <span>⚡</span>
                </div>
                <div class="vvalue">10</div>
                <div class="progress-bg">
                    <div class="progress-fill" style="width:6%"></div>
                </div>
                <div class="vpct">6% do total</div>
            </div>
        </div>
    </div>

</div>

<%-- Chart.js — dados equivalentes ao Recharts do Figma --%>
<script>
    // BarChart — Evolução mensal
    new Chart(document.getElementById('barChart'), {
        type: 'bar',
        data: {
            labels: ['Out','Nov','Dez','Jan','Fev','Mar'],
            datasets: [{
                label: 'Emissões (kg)',
                data: [980, 1050, 1150, 1100, 1180, 1200],
                backgroundColor: '#14b8a6',
                borderRadius: 4
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { position: 'bottom' } },
            scales: { y: { beginAtZero: false } }
        }
    });

    // LineChart — Emissões semanais
    new Chart(document.getElementById('lineChart'), {
        type: 'line',
        data: {
            labels: ['Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
            datasets: [{
                label: 'Emissões (kg)',
                data: [38, 42, 35, 48, 52, 30, 25],
                borderColor: '#14b8a6',
                backgroundColor: 'rgba(20,184,166,0.1)',
                borderWidth: 3,
                pointBackgroundColor: '#14b8a6',
                pointRadius: 5,
                fill: true,
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { display: false } },
            scales: { y: { beginAtZero: false } }
        }
    });
</script>

</body>
</html>