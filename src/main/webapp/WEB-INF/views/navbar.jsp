<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="currentURI" value="${pageContext.request.requestURI}" />

<nav class="smart-navbar">
    <div class="smart-navbar-inner">

        <!-- ESQUERDA -->
        <div class="smart-navbar-left">
            <div class="smart-navbar-logo-wrap">
                <img src="/images/logo.jpeg" alt="Smart City CO₂" class="smart-navbar-logo" />
            </div>
            <span class="smart-navbar-role">Conta do cidadão</span>
        </div>

        <!-- CENTRO -->
        <div class="smart-navbar-center">
            <a href="<c:url value='/cidadao/homeCidadao'/>"
               class="smart-nav-link ${currentURI.contains('homeCidadao') ? 'active' : ''}">
                <i class="bi bi-house"></i> Home
            </a>
            <a href="<c:url value='/cidadao/listaVeiculos'/>"
               class="smart-nav-link ${currentURI.contains('listaVeiculos') ? 'active' : ''}">
                <i class="bi bi-car-front"></i> Lista de Veículos
            </a>
            <a href="<c:url value='/cidadao/registoKms'/>"
               class="smart-nav-link ${currentURI.contains('registoKms') ? 'active' : ''}">
                <i class="bi bi-speedometer2"></i> Registo de Kms
            </a>
            <a href="<c:url value='/cidadao/dashboardCidadao'/>"
               class="smart-nav-link ${currentURI.contains('dashboardCidadao') ? 'active' : ''}">
                <i class="bi bi-graph-up"></i> Dashboard
            </a>
        </div>

        <!-- DIREITA -->
        <div class="smart-navbar-actions">
            <!-- HAMBURGUER -->
            <button class="smart-navbar-hamburger" id="smartHamburgerBtn" aria-label="Abrir menu">
                <i class="bi bi-list" id="smartHamburgerIcon"></i>
            </button>

            <!-- AVATAR -->
            <div class="smart-navbar-right">
                <div class="smart-navbar-avatar" id="avatarBtn" title="Menu do utilizador">
                    <div class="smart-avatar-circle">
                        <c:choose>
                            <c:when test="${not empty user.fotoUrl}">
                                <img src="${user.fotoUrl}" alt="Avatar" class="avatar-img">
                            </c:when>
                            <c:otherwise>
                                <span class="avatar-initials">
                                        ${fn:substring(user.firstName, 0, 1)}${fn:substring(user.lastName, 0, 1)}
                                </span>
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

                    <a href="<c:url value='/cidadao/perfil'/>" class="pd-menu-item">
                        <i class="bi bi-person-circle"></i> Visualizar Perfil
                    </a>

                    <a href="<c:url value='/cidadao/perfil/editar'/>" class="pd-menu-item">
                        <i class="bi bi-pencil-square"></i> Editar Perfil
                    </a>

                    <div class="pd-divider"></div>

                    <div class="pd-footer">
                        <a href="<c:url value='/logout'/>" class="pd-logout">
                            <i class="bi bi-box-arrow-right"></i> Log out
                        </a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</nav>

<!-- MENU MOBILE -->
<div class="smart-navbar-mobile-menu" id="smartMobileMenu">
    <a href="<c:url value='/cidadao/homeCidadao'/>"
       class="${currentURI.contains('homeCidadao') ? 'active' : ''}">
        <i class="bi bi-house"></i> Home
    </a>

    <a href="<c:url value='/cidadao/listaVeiculos'/>"
       class="${currentURI.contains('listaVeiculos') ? 'active' : ''}">
        <i class="bi bi-car-front"></i> Lista de Veículos
    </a>

    <a href="<c:url value='/cidadao/registoKms'/>"
       class="${currentURI.contains('registoKms') ? 'active' : ''}">
        <i class="bi bi-speedometer2"></i> Registo de Kms
    </a>

    <a href="<c:url value='/cidadao/dashboardCidadao'/>"
       class="${currentURI.contains('dashboardCidadao') ? 'active' : ''}">
        <i class="bi bi-graph-up"></i> Dashboard
    </a>

    <a href="<c:url value='/logout'/>" class="mobile-logout">
        <i class="bi bi-box-arrow-right"></i> Log out
    </a>
</div>

<div class="navbar-spacer"></div>

<script>
    (function() {
        var avatarBtn = document.getElementById('avatarBtn');
        var profileDropdown = document.getElementById('profileDropdown');

        var hamburgerBtn = document.getElementById('smartHamburgerBtn');
        var mobileMenu = document.getElementById('smartMobileMenu');
        var hamburgerIcon = document.getElementById('smartHamburgerIcon');

        if (avatarBtn && profileDropdown) {
            avatarBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                profileDropdown.classList.toggle('open');
                avatarBtn.classList.toggle('open');

                if (mobileMenu) {
                    mobileMenu.classList.remove('open');
                    if (hamburgerIcon) hamburgerIcon.className = 'bi bi-list';
                }
            });

            profileDropdown.addEventListener('click', function(e) {
                e.stopPropagation();
            });
        }

        if (hamburgerBtn && mobileMenu) {
            hamburgerBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                var isOpen = mobileMenu.classList.toggle('open');
                if (hamburgerIcon) {
                    hamburgerIcon.className = isOpen ? 'bi bi-x-lg' : 'bi bi-list';
                }

                if (profileDropdown) {
                    profileDropdown.classList.remove('open');
                }
                if (avatarBtn) {
                    avatarBtn.classList.remove('open');
                }
            });

            mobileMenu.addEventListener('click', function(e) {
                e.stopPropagation();
            });
        }

        document.addEventListener('click', function() {
            if (profileDropdown) profileDropdown.classList.remove('open');
            if (avatarBtn) avatarBtn.classList.remove('open');

            if (mobileMenu) mobileMenu.classList.remove('open');
            if (hamburgerIcon) hamburgerIcon.className = 'bi bi-list';
        });
    })();
</script>