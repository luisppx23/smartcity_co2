<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav style="box-shadow: 0 4px 12px -4px rgba(0,0,0,0.15), 0 2px 4px -2px rgba(0,0,0,0.05);">
    <div style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; ">

        <div>
            <img src="${pageContext.request.contextPath}/images/logo.jpeg" alt="Logo" style="height: 90px;">
        </div>

        <div>
            <span>Conta de ${user.tipo}</span>
        </div>

        <div>
            <%
                Object user = request.getAttribute("user");
                if (user instanceof pt.upskill.smart_city_co2.entities.Cidadao) {
                    pt.upskill.smart_city_co2.entities.Cidadao cidadao = (pt.upskill.smart_city_co2.entities.Cidadao) user;
                    out.print("<span>" + cidadao.getFirstName() + " " + cidadao.getLastName() + "</span>");
                } else if (user instanceof pt.upskill.smart_city_co2.entities.Municipio) {
                    pt.upskill.smart_city_co2.entities.Municipio municipio = (pt.upskill.smart_city_co2.entities.Municipio) user;
                    out.print("<span>" + municipio.getNome() + "</span>");
                } else if (user instanceof pt.upskill.smart_city_co2.entities.User) {
                    out.print("<span>" + ((pt.upskill.smart_city_co2.entities.User) user).getUsername() + "</span>");
                } else {
                    out.print("<span>Utilizador</span>");
                }
            %>
            <a href="/logout" class="btn btn-danger">Logout</a>
        </div>
        <div class="theme-strip">
            <div class="theme-strip-inner">
                <h2>Transformando quilómetros em consciência.</h2>
                <p>Mobilidade inteligente com foco num futuro mais sustentável.</p>
            </div>

    </div>
    </div>
</nav>