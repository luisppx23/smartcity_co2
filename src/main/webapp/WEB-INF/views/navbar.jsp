<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav style="box-shadow: 0 4px 12px -4px rgba(0,0,0,0.15), 0 2px 4px -2px rgba(0,0,0,0.05);">
    <div style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; ">

        <div>
            <img src="${pageContext.request.contextPath}/images/logo.jpeg" alt="Logo" style="height: 40px;">
        </div>

        <div>
            <span>Conta de ${user.tipo}</span>
        </div>

        <div>
            <span>${user.firstName} ${user.lastName}</span>
        </div>
    </div>
</nav>