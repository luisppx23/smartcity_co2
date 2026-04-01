<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard – Conta do Cidadão</title>

    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />

    <style>
        :root {
            --green-deep:  #04523B;
            --green-mid:   #1f5a3d;
            --gold:        #D4AF37;
            --off-white:   #F7F4EE;
            --text-dark:   #111111;
            --text-muted:  #5f5f5f;
            --card-bg:     rgba(255, 255, 255, 0.55);
            --card-radius: 24px;
            --card-shadow: 0 8px 32px rgba(0, 0, 0, 0.06);
            --blur:        blur(16px);
        }

        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        html, body {
            min-height: 100vh;
            font-family: 'Outfit', sans-serif;
            color: var(--text-dark);
        }

        body {
            background-color: #ddd8c8;
            background-image: url('/assets/Background_cidadao.png');
            background-size: cover;
            background-position: center center;
            background-attachment: fixed;
            background-repeat: no-repeat;
        }

        /* ══ NAVBAR sticky ══ */
        .nb {
            position: sticky;
            top: 0;
            z-index: 1000;
            background: linear-gradient(90deg, var(--green-deep) 0%, var(--green-mid) 100%);
            box-shadow: 0 4px 24px rgba(0,0,0,0.14);
        }

        .nb-inner {
            max-width: 1080px;
            margin: 0 auto;
            padding: 0 28px;
            height: 74px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 20px;
        }

        .nb-left {
            display: flex;
            align-items: center;
            gap: 14px;
            flex-shrink: 0;
        }

        .nb-logo-wrap {
            width: 52px;
            height: 52px;
            border-radius: 50%;
            background: var(--off-white);
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            overflow: hidden;
            border: 2px solid rgba(255,255,255,0.35);
        }

        .nb-logo-wrap img {
            width: 44px;
            height: 44px;
            object-fit: contain;
        }

        .nb-brand {
            color: rgba(255,255,255,0.92);
            font-size: 1rem;
            font-weight: 600;
            white-space: nowrap;
        }

        .nb-center {
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .nb-link {
            color: rgba(255,255,255,0.88);
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 600;
            padding: 8px 15px;
            border-radius: 999px;
            display: flex;
            align-items: center;
            gap: 6px;
            transition: background 0.18s;
        }

        .nb-link:hover {
            background: rgba(255,255,255,0.13);
            color: #fff;
        }

        .nb-right {
            position: relative;
            flex-shrink: 0;
        }

        .nb-avatar {
            width: 44px;
            height: 44px;
            border-radius: 50%;
            background: var(--off-white);
            color: var(--gold);
            font-size: 0.95rem;
            font-weight: 700;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            border: 2px solid rgba(212,175,55,0.6);
            transition: box-shadow 0.2s;
            user-select: none;
        }

        .nb-avatar:hover {
            box-shadow: 0 0 0 5px rgba(212,175,55,0.20);
        }

        /* ══ DROPDOWN ══ */
        .profile-dropdown {
            display: none;
            position: absolute;
            top: calc(100% + 14px);
            right: 0;
            width: 284px;
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 8px 40px rgba(0,0,0,0.18);
            overflow: hidden;
            z-index: 2000;
            animation: dropIn 0.18s ease;
        }

        .profile-dropdown.open { display: block; }

        @keyframes dropIn {
            from { opacity:0; transform:translateY(-8px); }
            to   { opacity:1; transform:translateY(0); }
        }

        .pd-email {
            padding: 13px 20px;
            font-size: 0.82rem;
            color: var(--text-muted);
            background: #fafafa;
            border-bottom: 1px solid #f0f0f0;
        }

        .pd-body { padding: 22px 20px 10px; text-align: center; }

        .pd-avatar-lg {
            width: 72px;
            height: 72px;
            border-radius: 50%;
            background: var(--off-white);
            color: var(--gold);
            font-size: 1.6rem;
            font-weight: 700;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 12px;
            border: 2px solid rgba(212,175,55,0.35);
        }

        .pd-greeting {
            font-size: 1rem;
            font-weight: 600;
            color: var(--text-dark);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
        }

        .pd-greeting i { color: var(--gold); font-size: 0.85rem; }
        .pd-divider { height: 1px; background: #f0f0f0; margin: 14px 0 6px; }

        .pd-menu-item {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 11px 20px;
            color: var(--text-dark);
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 500;
            transition: background 0.15s;
        }

        .pd-menu-item:hover { background: #f5f5f5; color: var(--text-dark); }
        .pd-menu-item i { font-size: 1.05rem; color: var(--text-muted); }

        .pd-footer { padding: 14px 20px 16px; border-top: 1px solid #f0f0f0; }

        .pd-logout {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            width: 100%;
            background: var(--green-deep);
            color: #fff;
            border: none;
            border-radius: 10px;
            padding: 11px 0;
            font-size: 0.9rem;
            font-weight: 600;
            text-decoration: none;
            transition: background 0.18s;
            margin-bottom: 10px;
            cursor: pointer;
        }

        .pd-logout:hover { background: #033d2c; color: #fff; }

        .pd-forgot {
            display: block;
            text-align: center;
            font-size: 0.78rem;
            color: var(--text-muted);
            text-decoration: none;
        }

        .pd-forgot:hover { text-decoration: underline; color: var(--green-mid); }

        /* ══ HERO ══ */
        .hero {
            position: relative;
            z-index: 1;
            text-align: center;
            padding: 52px 20px 22px;
        }

        .hero h1 {
            font-size: clamp(1.7rem, 3.8vw, 2.6rem);
            font-weight: 300;
            color: var(--gold);
            text-shadow: 0 2px 16px rgba(0,0,0,0.20);
            line-height: 1.2;
        }

        .hero p {
            margin-top: 10px;
            font-size: 1rem;
            color: rgba(40, 35, 20, 0.85);
            font-weight: 400;
            text-shadow: 0 1px 6px rgba(255,255,255,0.55);
        }

        /* ══ WRAPPER ══ */
        .dash-wrap {
            position: relative;
            z-index: 1;
            max-width: 1000px;
            margin: 28px auto 60px;
            padding: 0 20px;
        }

        /* ══ CARDS ══ */
        .dash-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            margin-bottom: 20px;
        }

        .dash-card {
            background: var(--card-bg);
            border-radius: var(--card-radius);
            padding: 28px 26px;
            box-shadow: var(--card-shadow);
            backdrop-filter: var(--blur);
            -webkit-backdrop-filter: var(--blur);
            border: 1px solid rgba(255,255,255,0.50);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
        }

        .dash-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 18px 40px rgba(0,0,0,0.09);
        }

        .dash-card h3 {
            font-size: 1.2rem;
            font-weight: 700;
            color: var(--green-deep);
            margin-bottom: 8px;
        }

        .dash-card p {
            font-size: 0.88rem;
            color: var(--text-muted);
            line-height: 1.5;
            margin-bottom: 18px;
        }

        .dash-btn {
            display: inline-flex;
            align-items: center;
            gap: 7px;
            background: var(--green-deep);
            color: #fff;
            text-decoration: none;
            border: none;
            border-radius: 12px;
            padding: 11px 20px;
            font-size: 0.88rem;
            font-weight: 600;
            font-family: 'Outfit', sans-serif;
            cursor: pointer;
            transition: background 0.18s, transform 0.15s;
        }

        .dash-btn:hover { background: #033d2c; color: #fff; transform: scale(1.02); }

        .km-input-row { display: flex; align-items: center; gap: 12px; flex-wrap: wrap; }

        .km-input {
            flex: 1;
            min-width: 100px;
            border: none;
            border-bottom: 1.5px solid rgba(31,90,61,0.28);
            border-radius: 0;
            background: transparent;
            padding: 10px 4px;
            font-size: 0.95rem;
            font-family: 'Outfit', sans-serif;
            color: var(--text-dark);
            outline: none;
            transition: border-color 0.18s;
        }

        .km-input:focus { border-bottom-color: var(--green-mid); }
        .km-input::placeholder { color: #bbb; }

        .card-icon-right {
            font-size: 2rem;
            color: rgba(31,90,61,0.18);
            margin-left: auto;
        }

        /* ══ ACESSOS RÁPIDOS ══ */
        .quick-links { display: flex; gap: 10px; flex-wrap: wrap; }

        .quick-link {
            display: inline-flex;
            align-items: center;
            gap: 7px;
            color: var(--green-mid);
            text-decoration: none;
            font-weight: 600;
            font-size: 0.86rem;
            padding: 8px 14px;
            border-radius: 10px;
            background: rgba(31,90,61,0.08);
            transition: background 0.15s;
        }

        .quick-link:hover { background: rgba(31,90,61,0.15); color: var(--green-deep); }

        /* ══ GAMIFICAÇÃO ══ */
        .gamif-card {
            background: var(--card-bg);
            border-radius: var(--card-radius);
            padding: 22px 28px;
            box-shadow: var(--card-shadow);
            backdrop-filter: var(--blur);
            -webkit-backdrop-filter: var(--blur);
            border: 1px solid rgba(255,255,255,0.50);
            display: flex;
            align-items: center;
            gap: 24px;
        }

        .gamif-bar-wrap { flex: 1; }

        .gamif-track {
            height: 6px;
            background: rgba(0,0,0,0.10);
            border-radius: 999px;
            overflow: hidden;
        }

        .gamif-fill {
            height: 100%;
            width: 82%;
            background: linear-gradient(90deg, var(--green-mid), var(--gold));
            border-radius: 999px;
        }

        .gamif-milestones { display: flex; justify-content: space-between; margin-top: 10px; }

        .gamif-step {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 5px;
            font-size: 0.72rem;
            color: var(--text-muted);
        }

        .gamif-step-dot {
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background: rgba(0,0,0,0.12);
        }

        .gamif-step.done .gamif-step-dot  { background: var(--green-mid); }
        .gamif-step.active .gamif-step-dot {
            background: var(--gold);
            box-shadow: 0 0 8px rgba(212,175,55,0.55);
        }

        .gamif-right {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 4px;
            flex-shrink: 0;
        }

        .gamif-tree-svg   { width: 38px; height: 38px; }
        .gamif-tree-label { font-size: 0.74rem; font-weight: 700; color: var(--gold); }
        .gamif-count      { font-size: 0.72rem; color: var(--text-muted); white-space: nowrap; }

        /* ══ RESPONSIVO ══ */
        @media (max-width: 768px) {
            .nb-center   { display: none; }
            .nb-brand    { font-size: 0.85rem; }
            .dash-grid   { grid-template-columns: 1fr; }
            .hero h1     { font-size: 1.5rem; }
            .gamif-card  { flex-direction: column; align-items: flex-start; gap: 16px; }
            .gamif-right { flex-direction: row; gap: 10px; align-items: center; }
        }

        @media (max-width: 480px) {
            .nb-inner        { padding: 0 14px; }
            .dash-wrap       { padding: 0 12px; }
            .nb-logo-wrap    { width: 40px; height: 40px; }
            .nb-logo-wrap img{ width: 34px; height: 34px; }
        }
    </style>
</head>

<body>

<!-- ══ NAVBAR ══ -->
<nav class="nb">
    <div class="nb-inner">

        <div class="nb-left">
            <div class="nb-logo-wrap">
                <%--
                    Tenta /assets/logo-smart-city.png primeiro.
                    Se falhar, usa /images/logo-smart-city.png (fallback automático).
                --%>
                <img src="/assets/logo-smart-city.png"
                     alt="Smart City CO₂"
                     onerror="this.onerror=null; this.src='/images/logo-smart-city.png';" />
            </div>
            <span class="nb-brand">Conta do cidadão</span>
        </div>

        <div class="nb-center">
            <a href="<c:url value='/cidadao/homeCidadao'/>" class="nb-link">
                <i class="bi bi-house"></i> Home
            </a>
            <a href="<c:url value='/cidadao/registoVeiculo'/>" class="nb-link">
                <i class="bi bi-car-front"></i> Registar Veículo
            </a>
            <a href="<c:url value='/cidadao/simularTaxa'/>" class="nb-link">
                <i class="bi bi-speedometer2"></i> Simular Taxa
            </a>
        </div>

        <div class="nb-right">
            <div class="nb-avatar" id="avatarBtn" title="Menu do utilizador">JC</div>

            <div class="profile-dropdown" id="profileDropdown">
                <div class="pd-email">joao.castor@exemplo.com</div>
                <div class="pd-body">
                    <div class="pd-avatar-lg">JC</div>
                    <div class="pd-greeting">
                        Hi, João Castor! <i class="bi bi-chevron-down"></i>
                    </div>
                </div>
                <div class="pd-divider"></div>
                <a href="<c:url value='/cidadao/perfil'/>" class="pd-menu-item">
                    <i class="bi bi-person-circle"></i> Visualizar Perfil
                </a>
                <div class="pd-footer">
                    <a href="<c:url value='/logout'/>" class="pd-logout">
                        <i class="bi bi-box-arrow-right"></i> Log out
                    </a>
                    <a href="<c:url value='/auth/recuperarPassword'/>" class="pd-forgot">
                        Forgot Password?
                    </a>
                </div>
            </div>
        </div>

    </div>
</nav>

<!-- ══ HERO ══ -->
<section class="hero">
    <h1>Transformando quilómetros em consciência.</h1>
    <p>Mobilidade mais inteligente para uma cidade mais sustentável.</p>
</section>

<!-- ══ DASHBOARD ══ -->
<main class="dash-wrap">

    <div class="dash-grid">

        <div class="dash-card">
            <h3>Registar Veículo</h3>
            <p>Adicione um novo veículo à sua conta.</p>
            <a href="<c:url value='/cidadao/registoVeiculo'/>" class="dash-btn">
                <i class="bi bi-car-front-fill"></i> Registar Veículo
            </a>
        </div>

        <div class="dash-card">
            <h3>Simular Taxa</h3>
            <p>Consulte uma simulação de taxa associada.</p>
            <a href="<c:url value='/cidadao/simularTaxa'/>" class="dash-btn">
                <i class="bi bi-calculator"></i> Simular Taxa
            </a>
        </div>

        <div class="dash-card">
            <h3>Registar KMs</h3>
            <p>Introduza novos registos de quilometragem do seu veículo.</p>
            <div class="km-input-row">
                <a href="<c:url value='/cidadao/registarKms'/>" class="dash-btn">
                    <i class="bi bi-pencil-fill"></i> Registar KMs
                </a>
                <input type="number" class="km-input" placeholder="Km actuais…" min="0" />
            </div>
        </div>

        <div class="dash-card">
            <h3>Ver Registos de KMs</h3>
            <p>Consulte o histórico dos seus registos de quilometragem.</p>
            <div style="display:flex;align-items:center;gap:12px;">
                <a href="<c:url value='/cidadao/historicoKms'/>" class="dash-btn">
                    <i class="bi bi-list-ul"></i> Ver Registos de KMs
                </a>
                <i class="bi bi-clock-history card-icon-right"></i>
            </div>
        </div>

    </div>

    <!-- Acessos Rápidos -->
    <div class="dash-card" style="margin-bottom:20px;">
        <h3 style="margin-bottom:14px;">Acessos Rápidos</h3>
        <div class="quick-links">
            <a href="<c:url value='/cidadao/emissoes'/>" class="quick-link">
                <i class="bi bi-bar-chart-line"></i> As minhas emissões
            </a>
            <a href="<c:url value='/cidadao/listaVeiculos'/>" class="quick-link">
                <i class="bi bi-car-front"></i> Os meus veículos
            </a>
            <a href="<c:url value='/cidadao/simularTaxa'/>" class="quick-link">
                <i class="bi bi-calculator"></i> Simular taxa
            </a>
            <a href="<c:url value='/cidadao/perfil'/>" class="quick-link">
                <i class="bi bi-person"></i> O meu perfil
            </a>
        </div>
    </div>

    <!-- Gamificação -->
    <div class="gamif-card">
        <div class="gamif-bar-wrap">
            <div class="gamif-track">
                <div class="gamif-fill"></div>
            </div>
            <div class="gamif-milestones">
                <div class="gamif-step done">
                    <div class="gamif-step-dot"></div><span>Initial</span>
                </div>
                <div class="gamif-step done">
                    <div class="gamif-step-dot"></div><span>Bronze</span>
                </div>
                <div class="gamif-step done">
                    <div class="gamif-step-dot"></div><span>Silver</span>
                </div>
                <div class="gamif-step active">
                    <div class="gamif-step-dot"></div><span>Gold</span>
                </div>
            </div>
        </div>

        <div class="gamif-right">
            <svg class="gamif-tree-svg" viewBox="0 0 40 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <ellipse cx="20" cy="18" rx="14" ry="14" fill="#D4AF37" opacity="0.92"/>
                <ellipse cx="20" cy="22" rx="10" ry="10" fill="#c49b28" opacity="0.70"/>
                <rect x="17" y="32" width="6" height="12" rx="2" fill="#8B6914"/>
                <ellipse cx="20" cy="18" rx="9" ry="9" fill="#e8cc6a" opacity="0.45"/>
            </svg>
            <div class="gamif-tree-label">Guardião</div>
            <div class="gamif-count">Árvores acumuladas: 100+</div>
        </div>
    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script>
    var avatarBtn       = document.getElementById('avatarBtn');
    var profileDropdown = document.getElementById('profileDropdown');

    avatarBtn.addEventListener('click', function (e) {
        e.stopPropagation();
        profileDropdown.classList.toggle('open');
    });

    document.addEventListener('click', function () {
        profileDropdown.classList.remove('open');
    });

    profileDropdown.addEventListener('click', function (e) {
        e.stopPropagation();
    });
</script>

</body>
</html>
