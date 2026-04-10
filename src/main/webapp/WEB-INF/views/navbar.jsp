<%-- ═══════════════════════════════════════════════
     NAVBAR FRAGMENT — Portal do Cidadão
     Caminho: src/main/webapp/WEB-INF/views/cidadao/navbar-cidadao.jsp
     Incluir em cada página com:
     <jsp:include page="navbar-cidadao.jsp"/>
     ════════════════════════════════════════════════ --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
            <!-- Avatar na navbar (miniatura redonda) -->
            <div class="smart-navbar-avatar" id="avatarBtn" title="Menu do utilizador">
                <c:choose>
                    <c:when test="${not empty user.fotoUrl}">
                        <img src="${user.fotoUrl}" alt="Avatar" class="avatar-img">
                    </c:when>
                    <c:otherwise>
                        <span class="avatar-initials">JC</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="profile-dropdown" id="profileDropdown">
                <div class="pd-email">${user.email}</div>
                <div class="pd-body">
                    <!-- Avatar grande no dropdown -->
                    <div class="pd-avatar-lg">
                        <c:choose>
                            <c:when test="${not empty user.fotoUrl}">
                                <img src="${user.fotoUrl}" alt="Avatar" class="avatar-lg-img">
                            </c:when>
                            <c:otherwise>
                                <span class="avatar-initials-lg">JC</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="pd-greeting">
                        Hi, ${user.firstName}! <i class="bi bi-chevron-down"></i>
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
<div class="navbar-spacer"></div>

<style>
    /* Estilos para os avatares com imagem */
    .avatar-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 50%;
    }
    .avatar-initials {
        display: inline-block;
        width: 100%;
        height: 100%;
        line-height: 40px; /* Ajuste conforme o tamanho do avatar */
        text-align: center;
        font-weight: bold;
        background-color: #e0e0e0;
        border-radius: 50%;
        color: #333;
    }
    .avatar-lg-img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 50%;
    }
    .avatar-initials-lg {
        display: inline-block;
        width: 100%;
        height: 100%;
        line-height: 70px; /* Ajuste para o tamanho do avatar grande */
        text-align: center;
        font-weight: bold;
        background-color: #e0e0e0;
        border-radius: 50%;
        font-size: 28px;
        color: #333;
    }
    /* Garantir que o contêiner do avatar tenha tamanho fixo e overflow hidden */
    .smart-navbar-avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        overflow: hidden;
        cursor: pointer;
        background-color: #f0f0f0;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .pd-avatar-lg {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        overflow: hidden;
        background-color: #f0f0f0;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
    }
</style>

<script>
    (function() {
        var btn = document.getElementById('avatarBtn');
        var dd  = document.getElementById('profileDropdown');
        if (btn && dd) {
            btn.addEventListener('click', function(e) { e.stopPropagation(); dd.classList.toggle('open'); });
            document.addEventListener('click', function() { dd.classList.remove('open'); });
            dd.addEventListener('click', function(e) { e.stopPropagation(); });
        }
    })();
</script>