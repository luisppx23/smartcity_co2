<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="pt">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil do Município – Smart City</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">
</head>
<body>

<jsp:include page="navbarm.jsp"/>

<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">Perfil do Município</h1>
    <p class="mun-hero-subtitle">Consulte e gere os dados da sua entidade</p>
</section>

<main class="mun-page-content">
    <div class="mun-card" style="padding: 0; overflow: hidden;">
        <div class="profile-layout-mun">
            <!-- Coluna da foto -->
            <div class="profile-avatar-card-mun">
                <c:choose>
                    <c:when test="${not empty municipio.fotoUrl}">
                        <img src="${municipio.fotoUrl}" class="profile-avatar-mun" alt="Foto do município">
                    </c:when>
                    <c:otherwise>
                        <div class="profile-avatar-placeholder-mun">🏛️</div>
                    </c:otherwise>
                </c:choose>
                <div class="profile-user-name-mun">${municipio.nome}</div>
                <p class="mun-card-description">${municipio.email}</p>
            </div>

            <!-- Coluna dos dados -->
            <div class="profile-info-card-mun">
                <h3 class="mun-card-title" style="margin-bottom: 1rem;">Informação da conta</h3>
                <div class="profile-info-list-mun">
                    <div class="profile-info-row-mun">
                        <span class="profile-label-mun">Username</span>
                        <span class="profile-value-mun">${municipio.username}</span>
                    </div>
                    <div class="profile-info-row-mun">
                        <span class="profile-label-mun">Email</span>
                        <span class="profile-value-mun">${municipio.email}</span>
                    </div>
                    <div class="profile-info-row-mun">
                        <span class="profile-label-mun">NIF</span>
                        <span class="profile-value-mun">${municipio.nif}</span>
                    </div>
                    <div class="profile-info-row-mun">
                        <span class="profile-label-mun">Data de Registo</span>
                        <span class="profile-value-mun">${municipio.data_registo != null ? municipio.data_registo.toString().replace('T', ' ') : ''}</span>
                    </div>
                    <div class="profile-info-row-mun">
                        <span class="profile-label-mun">Estado</span>
                        <span class="profile-value-mun">
                            <c:choose>
                                <c:when test="${municipio.ativo}">Activo</c:when>
                                <c:otherwise>Inactivo</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="profile-info-row-mun">
                        <span class="profile-label-mun">Objectivo CO₂ (kg/hab/mês)</span>
                        <span class="profile-value-mun">
                            <fmt:formatNumber value="${municipio.objetivo_co2_mes_hab}" minFractionDigits="2" maxFractionDigits="2"/>
                        </span>
                    </div>
                </div>

                <div class="profile-actions-mun">
                    <a href="${pageContext.request.contextPath}/municipio/perfil/editar" class="mun-btn-primary">
                        <i class="bi bi-pencil-square"></i> Editar Perfil
                    </a>
                    <a href="${pageContext.request.contextPath}/municipio/homeMunicipio" class="mun-btn-secondary">
                        <i class="bi bi-arrow-left"></i> Voltar
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<style>
    /* Estilos específicos para o perfil, mantendo a formatação da caixa mas alinhando fonte e espaçamentos */
    .profile-layout-mun {
        display: grid;
        grid-template-columns: 260px 1fr;
        gap: 24px;
        align-items: start;
    }
    .profile-avatar-card-mun {
        text-align: center;
        padding: 24px;
    }
    .profile-avatar-mun {
        width: 140px;
        height: 140px;
        object-fit: cover;
        border-radius: 50%;
        border: 3px solid #D4AF37;
        margin-bottom: 16px;
    }
    .profile-avatar-placeholder-mun {
        width: 140px;
        height: 140px;
        background: #e0e0e0;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 4rem;
        margin: 0 auto 16px;
        color: #6B7A8D;
    }
    .profile-user-name-mun {
        font-size: 1.2rem;
        font-weight: 700;
        color: #001F3F;
        font-family: 'Outfit', sans-serif;
    }
    .profile-info-card-mun {
        padding: 24px;
    }
    .profile-info-list-mun {
        display: flex;
        flex-direction: column;
        gap: 12px;
        margin-bottom: 24px;
    }
    .profile-info-row-mun {
        display: flex;
        justify-content: space-between;
        align-items: baseline;
        padding: 8px 0;
        border-bottom: 1px solid rgba(0, 31, 63, 0.08);
    }
    .profile-label-mun {
        font-weight: 700;
        color: #6B7A8D;
        font-size: 0.85rem;
        text-transform: uppercase;
        font-family: 'Outfit', sans-serif;
    }
    .profile-value-mun {
        font-weight: 600;
        color: #001F3F;
        text-align: right;
        font-family: 'Outfit', sans-serif;
    }
    .profile-actions-mun {
        display: flex;
        justify-content: flex-start;
        gap: 16px;
        margin-top: 24px;
    }
    @media (max-width: 768px) {
        .profile-layout-mun {
            grid-template-columns: 1fr;
        }
        .profile-info-row-mun {
            flex-direction: column;
            align-items: flex-start;
            gap: 4px;
        }
        .profile-value-mun {
            text-align: left;
        }
        .profile-actions-mun {
            justify-content: center;
        }
    }
</style>

</body>
</html>