<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Smart City CO₂ - Home</title>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />

    <link rel="stylesheet" href="/styles/auth-entrada.css" />
    <link rel="stylesheet" href="/styles/home-page.css" />
</head>
<body class="auth-page home-page">

<div class="home-wrapper">

    <section class="home-hero-card">
        <div class="home-topbar">
            <img src="/images/logo.jpeg" alt="Smart City CO₂" class="auth-logo" />
        </div>

        <div class="home-hero-content">
            <span class="home-badge">Explora o teu impacto</span>

            <h1 class="home-title">
                Transforme <span>quilómetros</span> em consciência ambiental.
            </h1>

            <p class="home-description">
                Monitorização inteligente de emissões. Uma plataforma dedicada a cidadãos e municípios
                para transformar dados de mobilidade em decisões sustentáveis.
            </p>

            <div class="home-actions">
                <a href="/auth/login" class="auth-btn-primary home-main-btn">
                    Iniciar sessão
                </a>
                <a href="/auth/signup" class="home-secondary-btn">
                    Criar conta
                </a>
            </div>

            <p class="home-helper-text">
                Transparência e dados ao serviço do planeta.
            </p>
        </div>
    </section>

    <section class="home-section">
        <h2 class="home-section-title">O problema que resolvemos</h2>
        <div class="home-problem-grid">
            <article class="home-info-card">
                <div class="home-icon"><i class="bi bi-moisture"></i></div>
                <h3>Visibilidade da Pegada</h3>
                <p>Muitas vezes as emissões individuais são invisíveis. Tornamos o impacto do seu veículo claro e mensurável.</p>
            </article>
            <article class="home-info-card">
                <div class="home-icon"><i class="bi bi-graph-up-arrow"></i></div>
                <h3>Dados para Gestão</h3>
                <p>Municípios carecem de dados reais para planear ciclovias, transportes e zonas de baixa emissão.</p>
            </article>
        </div>
    </section>

    <section class="home-section home-dual-card">
        <div class="home-panel home-panel-cidadao">
            <h3><i class="bi bi-person-badge"></i> Cidadão</h3>
            <p class="auth-card-description">Gerencie as suas emissões individuais e compare o seu desempenho no ranking municipal.</p>
            <ul class="home-feature-list">
                <li>Registo de Quilometragem</li>
                <li>Simulador de Taxas CO₂</li>
                <li>Ranking de Sustentabilidade</li>
            </ul>
        </div>
        <div class="home-panel home-panel-municipio">
            <h3><i class="bi bi-bank"></i> Município</h3>
            <p class="auth-card-description">Aceda a dashboards agregados para fundamentar políticas públicas de mobilidade.</p>
            <ul class="home-feature-list">
                <li>Relatórios de Emissões</li>
                <li>Suporte ao Cumprimento de Metas por Municipio</li>
                <li>Apoio à Decisão Estratégica</li>
            </ul>
        </div>
    </section>

    <section class="home-section home-highlight">
        <div class="home-highlight-content">
            <h2>Pronto para mudar o futuro?</h2>
            <p>Junte-se a centenas de cidadãos que já monitorizam o seu impacto ambiental.</p>
        </div>
        <div class="home-actions">
            <a href="/auth/login" class="auth-btn-primary home-main-btn">Entrar agora</a>
            <a href="/auth/signup" class="home-secondary-btn">Registar</a>
        </div>
    </section>

</div>

<footer class="auth-page-footer">
    Smart City CO₂ · Mobilidade Sustentável · 2026
</footer>

</body>
</html>