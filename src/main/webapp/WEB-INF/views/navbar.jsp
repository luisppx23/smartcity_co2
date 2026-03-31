<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="smart-navbar">
    <div class="smart-navbar-inner">

        <div class="smart-navbar-left">
            <img src="${pageContext.request.contextPath}/images/logo_gradient_white.png" alt="Logo" class="smart-navbar-logo">

        </div>

        <div class="smart-navbar-center">
            <span class="smart-navbar-role">Conta do ${user.tipo} ${user.username} </span>

            <a href="${pageContext.request.contextPath}/cidadao/homeCidadao" class="smart-nav-link">Home</a>
            <a href="${pageContext.request.contextPath}/auth/cidadao/registoVeiculo" class="smart-nav-link">
                Registar Veículo
            </a>
            <a href="${pageContext.request.contextPath}/cidadao/simularTaxa" class="smart-nav-link">Simular Taxa</a>
        </div>

        <div class="smart-navbar-right">
            <span class="smart-navbar-user">
                <%
                    Object user = request.getAttribute("user");
                    if (user instanceof pt.upskill.smart_city_co2.entities.Cidadao) {
                        pt.upskill.smart_city_co2.entities.Cidadao cidadao = (pt.upskill.smart_city_co2.entities.Cidadao) user;
                        out.print(cidadao.getFirstName() + " " + cidadao.getLastName());
                    } else if (user instanceof pt.upskill.smart_city_co2.entities.Municipio) {
                        pt.upskill.smart_city_co2.entities.Municipio municipio = (pt.upskill.smart_city_co2.entities.Municipio) user;
                        out.print(municipio.getNome());
                    } else if (user instanceof pt.upskill.smart_city_co2.entities.User) {
                        out.print(((pt.upskill.smart_city_co2.entities.User) user).getUsername());
                    } else {
                        out.print("Utilizador");
                    }
                %>
            </span>

            <a href="${pageContext.request.contextPath}/logout" class="smart-logout">Logout</a>
        </div>

    </div>
</nav>