<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Object currentUser = request.getAttribute("user");

    String displayName = "Utilizador";
    String firstName = "Utilizador";
    String email = "";
    String initials = "U";

    if (currentUser instanceof pt.upskill.smart_city_co2.entities.Cidadao) {
        pt.upskill.smart_city_co2.entities.Cidadao cidadao =
                (pt.upskill.smart_city_co2.entities.Cidadao) currentUser;

        String fn = cidadao.getFirstName() != null ? cidadao.getFirstName().trim() : "";
        String ln = cidadao.getLastName() != null ? cidadao.getLastName().trim() : "";

        displayName = (fn + " " + ln).trim();
        if (displayName.isEmpty()) {
            displayName = "Utilizador";
        }

        firstName = !fn.isEmpty() ? fn : displayName;
        email = cidadao.getEmail() != null ? cidadao.getEmail() : "";

        String i1 = !fn.isEmpty() ? fn.substring(0, 1).toUpperCase() : "";
        String i2 = !ln.isEmpty() ? ln.substring(0, 1).toUpperCase() : "";
        initials = !(i1 + i2).isEmpty() ? (i1 + i2) : "U";

    } else if (currentUser instanceof pt.upskill.smart_city_co2.entities.Municipio) {
        pt.upskill.smart_city_co2.entities.Municipio municipio =
                (pt.upskill.smart_city_co2.entities.Municipio) currentUser;

        displayName = municipio.getNome() != null ? municipio.getNome().trim() : "Município";
        firstName = displayName;
        email = "";

        if (displayName.length() >= 2) {
            initials = displayName.substring(0, 2).toUpperCase();
        } else if (displayName.length() == 1) {
            initials = displayName.substring(0, 1).toUpperCase();
        } else {
            initials = "M";
        }

    } else if (currentUser instanceof pt.upskill.smart_city_co2.entities.User) {
        pt.upskill.smart_city_co2.entities.User userObj =
                (pt.upskill.smart_city_co2.entities.User) currentUser;

        displayName = userObj.getUsername() != null ? userObj.getUsername().trim() : "Utilizador";
        if (displayName.isEmpty()) {
            displayName = "Utilizador";
        }

        firstName = displayName;
        email = userObj.getEmail() != null ? userObj.getEmail() : "";

        if (displayName.length() >= 2) {
            initials = displayName.substring(0, 2).toUpperCase();
        } else if (displayName.length() == 1) {
            initials = displayName.substring(0, 1).toUpperCase();
        } else {
            initials = "U";
        }
    }

    String uri = request.getRequestURI();
    String ctx = request.getContextPath();

    boolean isHome = uri.equals(ctx + "/cidadao/homeCidadao") || uri.equals(ctx + "/cidadao/dashboardCidadao");
    boolean isRegistoVeiculo = uri.equals(ctx + "/auth/cidadao/registoVeiculo");
    boolean isSimularTaxa = uri.equals(ctx + "/cidadao/simularTaxa");
    boolean isPerfil = uri.equals(ctx + "/cidadao/perfil");
%>

<nav class="smart-navbar">
    <div class="smart-navbar-inner">

        <div class="smart-navbar-left">
            <div class="smart-navbar-logo-wrap">
                <img src="${pageContext.request.contextPath}/images/logo_simples_preto.png"
                     alt="Logo"
                     class="smart-navbar-logo">
            </div>
            <span class="smart-navbar-role">Conta do cidadão</span>
        </div>

        <div class="smart-navbar-center">
            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao"
               class="smart-nav-link <%= isHome ? "active" : "" %>">
                <span class="smart-nav-icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24">
                        <path d="M3 10.5L12 3l9 7.5"></path>
                        <path d="M5 9.5V20h14V9.5"></path>
                    </svg>
                </span>
                <span>Home</span>
            </a>

<%--            <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo"--%>
<%--               class="smart-nav-link <%= isRegistoVeiculo ? "active" : "" %>">--%>
<%--                <span class="smart-nav-icon" aria-hidden="true">--%>
<%--                    <svg viewBox="0 0 24 24">--%>
<%--                        <path d="M5 16l1.5-5h11L19 16"></path>--%>
<%--                        <path d="M7 16v2"></path>--%>
<%--                        <path d="M17 16v2"></path>--%>
<%--                        <path d="M4 16h16"></path>--%>
<%--                        <circle cx="8" cy="16.5" r="1"></circle>--%>
<%--                        <circle cx="16" cy="16.5" r="1"></circle>--%>
<%--                        <path d="M8 11l1-3h6l1 3"></path>--%>
<%--                    </svg>--%>
<%--                </span>--%>
<%--                <span>Registar Veículo</span>--%>
<%--            </a>--%>

            <a href="${pageContext.request.contextPath}/cidadao/simularTaxa"
               class="smart-nav-link <%= isSimularTaxa ? "active" : "" %>">
                <span class="smart-nav-icon" aria-hidden="true">
                    <svg viewBox="0 0 24 24">
                        <rect x="6" y="3" width="12" height="18" rx="2"></rect>
                        <path d="M9 7h6"></path>
                        <path d="M9 11h2"></path>
                        <path d="M13 11h2"></path>
                        <path d="M9 15h2"></path>
                        <path d="M13 15h2"></path>
                    </svg>
                </span>
                <span>Simular Taxa</span>
            </a>

            <a href="${pageContext.request.contextPath}/cidadao/chartsCidadao"
               class="smart-nav-link smart-nav-link--primary <%= isHome ? "active" : "" %>">
    <span class="smart-nav-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24">
            <rect x="3" y="3" width="7" height="7"></rect>
            <rect x="14" y="3" width="7" height="5"></rect>
            <rect x="14" y="10" width="7" height="11"></rect>
            <rect x="3" y="12" width="7" height="9"></rect>
        </svg>
    </span>
                <span>Dashboard</span>
            </a>
        </div>



        <button class="smart-navbar-hamburger" id="navbarHamburger" type="button" aria-label="Abrir menu">
            ☰
        </button>

        <div class="smart-navbar-right">
            <div class="smart-navbar-avatar" id="avatarBtn" title="<%= displayName %>">
                <%= initials %>
            </div>

            <div class="profile-dropdown" id="profileDropdown">
                <div class="pd-email"><%= email.isEmpty() ? displayName : email %></div>

                <div class="pd-body">
                    <div class="pd-avatar-lg"><%= initials %></div>
                    <div class="pd-greeting">Olá, <%= firstName %>!</div>
                    <div class="pd-fullname"><%= displayName %></div>
                </div>

                <div class="pd-divider"></div>

                <a href="<c:url value='/cidadao/perfil'/>"
                   class="pd-menu-item <%= isPerfil ? "active" : "" %>">
                    <span class="smart-nav-icon" aria-hidden="true">
                        <svg viewBox="0 0 24 24">
                            <circle cx="12" cy="8" r="4"></circle>
                            <path d="M4 20c0-4 4-6 8-6s8 2 8 6"></path>
                        </svg>
                    </span>
                    <span>O meu perfil</span>
                </a>

                <div class="pd-footer">
                    <a href="${pageContext.request.contextPath}/logout" class="pd-logout">
                        <span class="smart-nav-icon" aria-hidden="true">
                            <svg viewBox="0 0 24 24">
                                <path d="M10 17l-5-5 5-5"></path>
                                <path d="M5 12h10"></path>
                                <path d="M14 4h4v16h-4"></path>
                            </svg>
                        </span>
                        <span>Logout</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</nav>

<div class="smart-navbar-mobile-menu" id="mobileMenu">
    <a href="${pageContext.request.contextPath}/cidadao/homeCidadao"
       class="<%= isHome ? "active" : "" %>">
        <span class="smart-nav-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24">
                <path d="M3 10.5L12 3l9 7.5"></path>
                <path d="M5 9.5V20h14V9.5"></path>
            </svg>
        </span>
        <span>Home</span>
    </a>

    <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo"
       class="<%= isRegistoVeiculo ? "active" : "" %>">
        <span class="smart-nav-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24">
                <path d="M5 16l1.5-5h11L19 16"></path>
                <path d="M7 16v2"></path>
                <path d="M17 16v2"></path>
                <path d="M4 16h16"></path>
                <circle cx="8" cy="16.5" r="1"></circle>
                <circle cx="16" cy="16.5" r="1"></circle>
                <path d="M8 11l1-3h6l1 3"></path>
            </svg>
        </span>
        <span>Registar Veículo</span>
    </a>

    <a href="${pageContext.request.contextPath}/cidadao/simularTaxa"
       class="<%= isSimularTaxa ? "active" : "" %>">
        <span class="smart-nav-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24">
                <rect x="6" y="3" width="12" height="18" rx="2"></rect>
                <path d="M9 7h6"></path>
                <path d="M9 11h2"></path>
                <path d="M13 11h2"></path>
                <path d="M9 15h2"></path>
                <path d="M13 15h2"></path>
            </svg>
        </span>
        <span>Simular Taxa</span>
    </a>

    <a href="<c:url value='/cidadao/perfil'/>"
       class="<%= isPerfil ? "active" : "" %>">
        <span class="smart-nav-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24">
                <circle cx="12" cy="8" r="4"></circle>
                <path d="M4 20c0-4 4-6 8-6s8 2 8 6"></path>
            </svg>
        </span>
        <span>O meu perfil</span>
    </a>

    <a href="${pageContext.request.contextPath}/logout" class="mobile-logout">
        <span class="smart-nav-icon" aria-hidden="true">
            <svg viewBox="0 0 24 24">
                <path d="M10 17l-5-5 5-5"></path>
                <path d="M5 12h10"></path>
                <path d="M14 4h4v16h-4"></path>
            </svg>
        </span>
        <span>Logout</span>
    </a>
</div>

<div class="navbar-spacer"></div>

<script>
    (function () {
        var avatarBtn = document.getElementById('avatarBtn');
        var profileDropdown = document.getElementById('profileDropdown');
        var hamburgerBtn = document.getElementById('navbarHamburger');
        var mobileMenu = document.getElementById('mobileMenu');

        if (avatarBtn && profileDropdown) {
            avatarBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                profileDropdown.classList.toggle('open');

                if (mobileMenu) {
                    mobileMenu.classList.remove('open');
                }
            });

            profileDropdown.addEventListener('click', function (e) {
                e.stopPropagation();
            });
        }

        if (hamburgerBtn && mobileMenu) {
            hamburgerBtn.addEventListener('click', function (e) {
                e.stopPropagation();
                mobileMenu.classList.toggle('open');

                if (profileDropdown) {
                    profileDropdown.classList.remove('open');
                }
            });

            mobileMenu.addEventListener('click', function (e) {
                e.stopPropagation();
            });
        }

        document.addEventListener('click', function () {
            if (profileDropdown) {
                profileDropdown.classList.remove('open');
            }
            if (mobileMenu) {
                mobileMenu.classList.remove('open');
            }
        });
    })();
</script>