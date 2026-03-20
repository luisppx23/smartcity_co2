<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Definir Nova Password</title>
    <link rel="stylesheet" href="/styles/reset.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/styles/estilos.css">
</head>
<body>
<div class="container">
    <div class="card mt-4 mx-auto" style="max-width: 500px;">
        <div class="card-header bg-success text-white">
            <h2>Nova Password</h2>
        </div>
        <div class="card-body">
            <form method="POST" action="/auth/atualizarPasswordAction">
                <input type="hidden" name="username" value="${username}">

                <div class="mb-3">
                    <label for="novaPassword" class="form-label">Nova Password</label>
                    <input type="password" class="form-control" name="novaPassword" required minlength="6">
                </div>
                <div class="mb-3">
                    <label for="confirmarPassword" class="form-label">Confirmar Password</label>
                    <input type="password" class="form-control" name="confirmarPassword" required minlength="6">
                </div>
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-success">Confirmar Alteração</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>