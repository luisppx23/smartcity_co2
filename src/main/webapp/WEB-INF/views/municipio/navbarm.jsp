<%-- ═══════════════════════════════════════════════
     NAVBAR FRAGMENT — Portal do Município
     Caminho: src/main/webapp/WEB-INF/views/municipio/navbar-municipio.jsp
     UX: active link dinâmico · hamburguer mobile
     ════════════════════════════════════════════════ --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="currentURI" value="${pageContext.request.requestURI}"/>

<nav class="mun-navbar">
    <div class="mun-navbar-inner">

        <div class="mun-navbar-left">
            <div class="mun-navbar-logo-wrap">
                <img src="/images/logo.jpeg" alt="Smart City CO₂" class="mun-navbar-logo" />
            </div>
            <span class="mun-navbar-brand">Portal do Município</span>
        </div>

        <%-- Links com active dinâmico --%>
        <div class="mun-navbar-center">
            <a href="<c:url value='/municipio/dashboardMunicipio'/>"
               class="mun-nav-link ${currentURI.contains('dashboardMunicipio') ? 'active' : ''}">
                <i class="bi bi-speedometer2"></i> Dashboard
            </a>
            <a href="<c:url value='/municipio/relatoriosMunicipio'/>"
               class="mun-nav-link ${currentURI.contains('relatoriosMunicipio') ? 'active' : ''}">
                <i class="bi bi-file-earmark-bar-graph"></i> Relatórios
            </a>
            <a href="<c:url value='/municipio/redefinirTaxa'/>"
               class="mun-nav-link ${currentURI.contains('redefinirTaxa') ? 'active' : ''}">
                <i class="bi bi-sliders"></i> Redefinir Taxa
            </a>
            <a href="<c:url value='/municipio/listaCidadaos'/>"
               class="mun-nav-link ${currentURI.contains('listaCidadaos') ? 'active' : ''}">
                <i class="bi bi-people"></i> Cidadãos
            </a>
        </div>

        <%-- Hamburguer mobile --%>
        <button class="mun-navbar-hamburger" id="munHamburgerBtn" aria-label="Menu">
            <i class="bi bi-list" id="munHamburgerIcon"></i>
        </button>

        <%-- Avatar município --%>
        <div class="mun-navbar-right">
            <div class="mun-navbar-avatar" id="munAvatarBtn">
                <div class="mun-avatar-circle">
                    <c:choose>
                        <c:when test="${not empty municipio.nome}">
                            ${municipio.nome.substring(0,2).toUpperCase()}
                        </c:when>
                        <c:otherwise>MN</c:otherwise>
                    </c:choose>
                </div>
                <span class="mun-avatar-name">
                    <c:out value="${municipio.nome}" default="Município"/>
                </span>
                <i class="bi bi-chevron-down mun-avatar-chevron"></i>
            </div>

            <div class="mun-dropdown" id="munDropdown">
                <div class="mun-dd-header">
                    <div class="mun-dd-greeting">Olá, <c:out value="${municipio.nome}" default="Município"/>!</div>
                    <div class="mun-dd-email"><c:out value="${user.email}" default=""/></div>
                </div>
                <a href="<c:url value='/municipio/dashboardMunicipio'/>" class="mun-dd-item">
                    <i class="bi bi-speedometer2"></i> Dashboard
                </a>
                <a href="<c:url value='/municipio/relatoriosMunicipio'/>" class="mun-dd-item">
                    <i class="bi bi-file-earmark-bar-graph"></i> Relatórios
                </a>
                <a href="<c:url value='/municipio/listaCidadaos'/>" class="mun-dd-item">
                    <i class="bi bi-people"></i> Lista de Cidadãos
                </a>
                <a href="<c:url value='/municipio/listaVeiculos'/>" class="mun-dd-item">
                    <i class="bi bi-car-front"></i> Lista de Veículos
                </a>
                <div class="mun-dd-divider"></div>
                <div class="mun-dd-footer">
                    <a href="<c:url value='/logout'/>" class="mun-dd-logout">
                        <i class="bi bi-box-arrow-right"></i> Log out
                    </a>
                </div>
            </div>
        </div>

    </div>
</nav>

<%-- Menu mobile drawer --%>
<div class="mun-mobile-menu" id="munMobileMenu">
    <a href="<c:url value='/municipio/dashboardMunicipio'/>"
       class="${currentURI.contains('dashboardMunicipio') ? 'active' : ''}">
        <i class="bi bi-speedometer2"></i> Dashboard
    </a>
    <a href="<c:url value='/municipio/relatoriosMunicipio'/>"
       class="${currentURI.contains('relatoriosMunicipio') ? 'active' : ''}">
        <i class="bi bi-file-earmark-bar-graph"></i> Relatórios
    </a>
    <a href="<c:url value='/municipio/redefinirTaxa'/>"
       class="${currentURI.contains('redefinirTaxa') ? 'active' : ''}">
        <i class="bi bi-sliders"></i> Redefinir Taxa
    </a>
    <a href="<c:url value='/municipio/listaCidadaos'/>"
       class="${currentURI.contains('listaCidadaos') ? 'active' : ''}">
        <i class="bi bi-people"></i> Cidadãos
    </a>
    <a href="<c:url value='/municipio/listaVeiculos'/>">
        <i class="bi bi-car-front"></i> Veículos
    </a>
    <a href="<c:url value='/logout'/>" class="mun-mobile-logout">
        <i class="bi bi-box-arrow-right"></i> Log out
    </a>
</div>

<div class="mun-navbar-spacer"></div>

<script>
    (function() {
        var btn  = document.getElementById('munAvatarBtn');
        var dd   = document.getElementById('munDropdown');
        var burg = document.getElementById('munHamburgerBtn');
        var menu = document.getElementById('munMobileMenu');
        var icon = document.getElementById('munHamburgerIcon');

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

        if (burg && menu) {
            burg.addEventListener('click', function(e) {
                e.stopPropagation();
                var isOpen = menu.classList.toggle('open');
                icon.className = isOpen ? 'bi bi-x-lg' : 'bi bi-list';
            });
            document.addEventListener('click', function() {
                menu.classList.remove('open');
                if (icon) icon.className = 'bi bi-list';
            });
            menu.addEventListener('click', function(e) { e.stopPropagation(); });
        }
    })();
</script>