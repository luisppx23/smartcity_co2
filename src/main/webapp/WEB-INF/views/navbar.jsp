<%-- ═══════════════════════════════════════════════
     NAVBAR FRAGMENT — Portal do Cidadão
     Caminho: src/main/webapp/WEB-INF/views/cidadao/navbar.jsp
     ════════════════════════════════════════════════ --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<nav class="smart-navbar">
    <div class="smart-navbar-inner">

        <div class="smart-navbar-left">
            <div class="smart-navbar-logo-wrap">
                <img src="/images/logo.jpeg" alt="Smart City CO₂" class="smart-navbar-logo" />
            </div>
            <span class="smart-navbar-role">Conta do cidadão</span>
        </div>

        <div class="smart-navbar-center">
            <a href="<c:url value='/cidadao/homeCidadao'/>" class="smart-nav-link">
                <i class="bi bi-house"></i> Home
            </a>
            <a href="<c:url value='/cidadao/listaVeiculos'/>" class="smart-nav-link">
                <i class="bi bi-car-front"></i> Lista de Veículos
            </a>
            <a href="<c:url value='/cidadao/registoKms'/>" class="smart-nav-link">
                <i class="bi bi-speedometer2"></i> Registo de Kms
            </a>
            <a href="<c:url value='/cidadao/dashboardCidadao'/>" class="smart-nav-link">
                <i class="bi bi-graph-up"></i> Dashboard
            </a>
        </div>

        <div class="smart-navbar-right">
            <!-- Caixa com avatar + nome (estilo município) -->
            <div class="smart-navbar-avatar" id="avatarBtn" title="Menu do utilizador">
                <div class="smart-avatar-circle">
                    <c:choose>
                        <c:when test="${not empty user.fotoUrl}">
                            <img src="${user.fotoUrl}" alt="Avatar" class="avatar-img">
                        </c:when>
                        <c:otherwise>
                            <span class="avatar-initials">${fn:substring(user.firstName, 0, 1)}${fn:substring(user.lastName, 0, 1)}</span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <span class="smart-avatar-name">
                    <c:out value="${user.firstName} ${user.lastName}" default="Cidadão"/>
                </span>
                <i class="bi bi-chevron-down smart-avatar-chevron"></i>
            </div>

            <div class="profile-dropdown" id="profileDropdown">
                <div class="pd-header">
                    <div class="pd-greeting">Olá, <c:out value="${user.firstName}" default="Cidadão"/>!</div>
                    <div class="pd-email"><c:out value="${user.email}" default=""/></div>
                </div>
                <div class="pd-divider"></div>
                <a href="<c:url value='/cidadao/perfil'/>" class="pd-menu-item">
                    <i class="bi bi-person-circle"></i> Visualizar Perfil
                </a>
                <div class="pd-divider"></div>
                <a href="<c:url value='/cidadao/perfil/editar'/>" class="pd-menu-item">
                    <i class="bi bi-pencil-square"></i> Editar Perfil
                </a>
                <div class="pd-footer">
                    <a href="<c:url value='/logout'/>" class="pd-logout">
                        <i class="bi bi-box-arrow-right"></i> Log out
                    </a>
                </div>
            </div>
        </div>

    </div>
</nav>
<div class="navbar-spacer"></div>

<style>
    /* Estilos para os avatares com imagem - versão melhorada */
    .avatar-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 50%;
    }
    .avatar-initials {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 100%;
        font-weight: bold;
        background-color: #e0e0e0;
        border-radius: 50%;
        color: #333;
        font-size: 0.85rem;
    }
    .avatar-lg-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 50%;
    }
    .avatar-initials-lg {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 100%;
        background-color: #e0e0e0;
        border-radius: 50%;
        font-size: 28px;
        font-weight: bold;
        color: #333;
    }

    /* NOVOS ESTILOS: caixa do avatar + nome */
    .smart-navbar-avatar {
        display: flex;
        align-items: center;
        gap: 10px;
        cursor: pointer;
        padding: 6px 10px;
        border-radius: 999px;
        transition: background 0.18s ease;
        border: 1.5px solid rgba(255, 255, 255, 0.28);
        background: transparent;
    }
    .smart-navbar-avatar:hover {
        background: rgba(255, 255, 255, 0.10);
    }
    .smart-avatar-circle {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: rgba(255, 255, 255, 0.18);
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
        flex-shrink: 0;
    }
    .smart-avatar-name {
        color: rgba(255, 255, 255, 0.92);
        font-size: 0.88rem;
        font-weight: 600;
        white-space: nowrap;
    }
    .smart-avatar-chevron {
        color: rgba(255, 255, 255, 0.65);
        font-size: 0.75rem;
        transition: transform 0.2s ease;
    }
    .smart-navbar-avatar.open .smart-avatar-chevron {
        transform: rotate(180deg);
    }

    /* DROPDOWN melhorado (com cabeçalho) */
    .profile-dropdown {
        display: none;
        position: absolute;
        top: calc(100% + 12px);
        right: 0;
        width: 260px;
        background: #ffffff;
        border-radius: 16px;
        box-shadow: 0 8px 40px rgba(0, 0, 0, 0.18);
        overflow: hidden;
        z-index: 2000;
        animation: pd-drop-in 0.18s ease;
    }
    .profile-dropdown.open {
        display: block;
    }
    @keyframes pd-drop-in {
        from { opacity: 0; transform: translateY(-8px); }
        to   { opacity: 1; transform: translateY(0); }
    }
    .pd-header {
        padding: 16px 20px 12px;
        background: rgba(4, 82, 59, 0.04);
        border-bottom: 1px solid rgba(4, 82, 59, 0.08);
    }
    .pd-greeting {
        font-size: 1rem;
        font-weight: 700;
        color: #0f5a3c;
    }
    .pd-email {
        font-size: 0.78rem;
        color: #6B7A8D;
        margin-top: 2px;
        word-break: break-word;
    }
    .pd-menu-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px 20px;
        color: #1A2332;
        text-decoration: none;
        font-size: 0.88rem;
        font-weight: 500;
        transition: background 0.15s ease;
    }
    .pd-menu-item:hover {
        background: rgba(4, 82, 59, 0.05);
        color: #0f5a3c;
        text-decoration: none;
    }
    .pd-menu-item i {
        font-size: 1rem;
        color: #6B7A8D;
        width: 20px;
    }
    .pd-divider {
        height: 1px;
        background: rgba(0, 0, 0, 0.08);
        margin: 4px 0;
    }
    .pd-footer {
        padding: 12px 20px 16px;
        border-top: 1px solid rgba(0, 0, 0, 0.08);
    }
    .pd-logout {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        width: 100%;
        background: #0f5a3c;
        color: #ffffff;
        border: none;
        border-radius: 10px;
        padding: 10px 0;
        font-size: 0.88rem;
        font-weight: 600;
        text-decoration: none;
        transition: background 0.18s ease;
    }
    .pd-logout:hover {
        background: #0b4630;
        color: #ffffff;
        text-decoration: none;
    }

    /* RESPONSIVO: esconder nome em ecrãs pequenos */
    @media (max-width: 768px) {
        .smart-avatar-name {
            display: none;
        }
        .smart-navbar-avatar {
            padding: 6px;
        }
    }
</style>

<script>
    (function() {
        var btn = document.getElementById('avatarBtn');
        var dd  = document.getElementById('profileDropdown');
        if (btn && dd) {
            btn.addEventListener('click', function(e) {
                e.stopPropagation();
                dd.classList.toggle('open');
                btn.classList.toggle('open');
            });
            document.addEventListener('click', function() {
                dd.classList.remove('open');
                btn.classList.remove('open');
            });
            dd.addEventListener('click', function(e) { e.stopPropagation(); });
        }
    })();
</script>