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
            <a href="<c:url value='/cidadao/dashboardCidadao'/>" class="smart-nav-link">
                <i class="bi bi-house"></i> Home
            </a>
            <a href="<c:url value='/auth/cidadao/registoVeiculo'/>" class="smart-nav-link">
                <i class="bi bi-car-front"></i> Registar Veículo
            </a>
            <a href="<c:url value='/cidadao/simularTaxa'/>" class="smart-nav-link">
                <i class="bi bi-speedometer2"></i> Simular Taxa
            </a>
        </div>

        <div class="smart-navbar-right">
            <div class="smart-navbar-avatar" id="avatarBtn" title="Menu do utilizador">
                JC
            </div>

            <div class="profile-dropdown" id="profileDropdown">
                <div class="pd-email">${user.email}</div>
                <div class="pd-body">
                    <div class="pd-avatar-lg">JC</div>
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
