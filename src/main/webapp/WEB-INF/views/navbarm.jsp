<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="currentURI" value="${pageContext.request.requestURI}" />

<nav class="smart-navbar-v2">
    <div class="smart-navbar-v2-inner">

        <div class="smart-navbar-v2-left">
            <div class="smart-navbar-logo-wrap">
                <img src="${pageContext.request.contextPath}/images/logo_simples_preto.png"
                     alt="Logo"
                     class="smart-navbar-logo-v2">
            </div>
            <span class="smart-navbar-brand">Portal do Município</span>
        </div>

        <div class="smart-navbar-v2-center">
            <a href="${pageContext.request.contextPath}/municipio/homeMunicipio"
               class="smart-nav-link-v2 ${currentURI.contains('homeMunicipio') ? 'active' : ''}">
                Home
            </a>

            <a href="${pageContext.request.contextPath}/municipio/relatoriosMunicipio"
               class="smart-nav-link-v2 ${currentURI.contains('relatoriosMunicipio') ? 'active' : ''}">
                Relatórios Município
            </a>

            <a href="${pageContext.request.contextPath}/municipio/redefinirMeta"
               class="smart-nav-link-v2 ${currentURI.contains('redefinirmeta') ? 'active' : ''}">
                Redefinir Meta
            </a>
        </div>

        <div class="smart-navbar-v2-right">
            <div class="smart-navbar-avatar" id="smartAvatarBtn">
                <div class="smart-avatar-circle">
                    <c:choose>
                        <c:when test="${not empty user.nome}">
                            ${user.nome.substring(0,2).toUpperCase()}
                        </c:when>
                        <c:when test="${not empty user.username}">
                            ${user.username.substring(0,2).toUpperCase()}
                        </c:when>
                        <c:otherwise>MU</c:otherwise>
                    </c:choose>
                </div>

                <span class="smart-avatar-name">
                    <c:choose>
                        <c:when test="${not empty user.nome}">
                            <c:out value="${user.nome}" />
                        </c:when>
                        <c:when test="${not empty user.username}">
                            <c:out value="${user.username}" />
                        </c:when>
                        <c:otherwise>Município</c:otherwise>
                    </c:choose>
                </span>

                <span class="smart-avatar-chevron">▼</span>
            </div>

            <div class="smart-dropdown" id="smartDropdown">
                <div class="smart-dd-header">
                    <div class="smart-dd-greeting">
                        Olá,
                        <c:choose>
                            <c:when test="${not empty user.nome}">
                                <c:out value="${user.nome}" />
                            </c:when>
                            <c:when test="${not empty user.username}">
                                <c:out value="${user.username}" />
                            </c:when>
                            <c:otherwise>Município</c:otherwise>
                        </c:choose>!
                    </div>

                    <div class="smart-dd-email">
                        <c:out value="${user.email}" default="" />
                    </div>
                </div>

                <a href="${pageContext.request.contextPath}/municipio/homeMunicipio" class="smart-dd-item">
                    Home
                </a>
                <a href="${pageContext.request.contextPath}/municipio/relatoriosMunicipio" class="smart-dd-item">
                    Relatórios Município
                </a>
                <a href="${pageContext.request.contextPath}/municipio/redefinirMeta" class="smart-dd-item">
                    Redefinir Meta
                </a>

                <div class="smart-dd-divider"></div>

                <div class="smart-dd-footer">
                    <form action="${pageContext.request.contextPath}/logout" method="post" style="margin:0;">
                        <button type="submit" class="smart-dd-logout">
                            Logout
                        </button>
                    </form>
                </div>
            </div>
        </div>

    </div>
</nav>

<script>
    (function() {
        const btn = document.getElementById('smartAvatarBtn');
        const dropdown = document.getElementById('smartDropdown');

        if (btn && dropdown) {
            btn.addEventListener('click', function(e) {
                e.stopPropagation();
                btn.classList.toggle('open');
                dropdown.classList.toggle('open');
            });

            document.addEventListener('click', function() {
                btn.classList.remove('open');
                dropdown.classList.remove('open');
            });

            dropdown.addEventListener('click', function(e) {
                e.stopPropagation();
            });
        }
    })();
</script>