<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../navbar.jsp"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registo de Veiculo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/navbar.css">
</head>
<body>
<p>Teste JSTL (deve aparecer 4): ${2 + 2}</p>
<p>Quantidade de veículos vindos do Java: ${veiculosBase.size()}</p>
<div class="container">
    <div class="card mt-4">
        <form action="/auth/cidadao/adicionarVeiculoAction" method="POST">
            <div class="card p-4">
                <h3>Registar Meu Veículo</h3>

                <div class="mb-3">
                    <label>Matrícula</label>
                    <input type="text" name="matricula" class="form-control"
                           placeholder="AA-00-AA" required
                           style="text-transform: uppercase;">
                </div>

                <div class="mb-3">
                    <label>Selecione o Modelo</label>
                    <select name="modeloReferencia" class="form-control" required>
                        <option value="">-- Escolha o seu veículo --</option>
                        <c:forEach var="v" items="${veiculosBase}">
                            <option value="${v.marca}:${v.modelo}">
                                    ${v.marca} ${v.modelo} - ${v.tipoDeCombustivel}
                                    ${v.marca} - ${v.modelo}
                            </option>
                        </c:forEach>
                    </select>
                    <small class="text-muted">Os dados de consumo serão aplicados automaticamente.</small>
                </div>

                <div class="mb-3">
                    <label>Ano de Registo</label>
                    <input type="number" name="anoRegisto" class="form-control"
                           min="1900" max="2026" required>
                </div>

                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">Adicionar à Minha Conta</button>
                    <a href="/auth/autenticado" class="btn btn-secondary">Cancelar</a>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>