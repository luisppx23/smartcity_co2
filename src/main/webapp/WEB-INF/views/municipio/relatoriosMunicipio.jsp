<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Históricos Município</title>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/homem.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base-municipio.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/navbarm.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/municipio/dashboardm.css">
</head>

<jsp:include page="navbarm.jsp"/>
<%-- HERO --%>
<section class="mun-hero">
    <h1 class="mun-hero-title">Históricos do Município</h1>
    <p class="mun-hero-subtitle">Agregado de emissões, KMs e tendências do município de ${municipio.nome}</p>
</section>

<main class="mun-page-content">

    <%-- ── RESUMO MENSAL ── --%>
    <div class="mun-card mun-section">
        <h3 class="mun-card-title">Resumo Mensal</h3>
        <div class="table-responsive">
            <table class="mun-table" id="tabelaResumo">
                <thead>
                <tr>
                    <th>Mês</th>
                    <th>Total KMs</th>
                    <th>Total CO₂</th>
                    <th>Habitantes</th>
                    <th>Média CO₂/Hab.</th>
                    <th>Objectivo</th>
                    <th>Estado</th>
                    <th>vs Mês Anterior</th>
                    <th>vs Ano Anterior</th>
                </tr>
                </thead>
                <tbody id="resumoBody">
                <c:forEach var="mesAno" items="${totalKmsPorMes.keySet()}" varStatus="status">
                    <tr data-index="${status.index}">
                        <td><strong>${mesAno}</strong></td>
                        <td><fmt:formatNumber value="${totalKmsPorMes[mesAno]}" minFractionDigits="1"
                                              maxFractionDigits="1"/> km
                        </td>
                        <td><fmt:formatNumber value="${totalCo2PorMes[mesAno]}" minFractionDigits="2"
                                              maxFractionDigits="2"/> kg
                        </td>
                        <td>${numeroHabitantes}</td>
                        <td><fmt:formatNumber value="${mediaCo2PorHabitantePorMes[mesAno]}" minFractionDigits="2"
                                              maxFractionDigits="2"/> kg
                        </td>
                        <td><fmt:formatNumber value="${municipio.objetivo_co2_mes_hab}" minFractionDigits="2"
                                              maxFractionDigits="2"/> kg
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${objetivoAtingidoPorMes[mesAno]}">
                                            <span class="mun-badge mun-badge-success">
                                                <i class="bi bi-check-circle"></i> Atingido
                                            </span>
                                </c:when>
                                <c:otherwise>
                                            <span class="mun-badge mun-badge-danger">
                                                <i class="bi bi-x-circle"></i> Não atingido
                                            </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty variacaoMesAnterior[mesAno]}">
                                    <c:choose>
                                        <c:when test="${corComparacaoMesAnterior[mesAno] == 'green'}">
                                                    <span class="mun-variacao-negativa">
                                                        <i class="bi bi-arrow-down"></i>
                                                        <fmt:formatNumber value="${variacaoMesAnterior[mesAno]}"
                                                                          minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                        </c:when>
                                        <c:when test="${corComparacaoMesAnterior[mesAno] == 'red'}">
                                                    <span class="mun-variacao-positiva">
                                                        <i class="bi bi-arrow-up"></i>
                                                        <fmt:formatNumber value="${variacaoMesAnterior[mesAno]}"
                                                                          minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="mun-variacao-neutra">—</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <span class="mun-variacao-neutra">—</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty variacaoAnoAnteriorPorMes[mesAno]}">
                                    <c:choose>
                                        <c:when test="${corComparacaoAnoAnteriorPorMes[mesAno] == 'green'}">
                                                    <span class="mun-variacao-negativa">
                                                        <i class="bi bi-arrow-down"></i>
                                                        <fmt:formatNumber value="${variacaoAnoAnteriorPorMes[mesAno]}"
                                                                          minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                        </c:when>
                                        <c:when test="${corComparacaoAnoAnteriorPorMes[mesAno] == 'red'}">
                                                    <span class="mun-variacao-positiva">
                                                        <i class="bi bi-arrow-up"></i>
                                                        <fmt:formatNumber value="${variacaoAnoAnteriorPorMes[mesAno]}"
                                                                          minFractionDigits="2" maxFractionDigits="2"/> kg
                                                    </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="mun-variacao-neutra">—</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <span class="mun-variacao-neutra">—</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="pagination-container" id="paginationResumo"></div>
    </div>

    <%-- ── EVOLUÇÃO MENSAL ── --%>
    <div class="mun-card mun-section">
        <h3 class="mun-card-title">Evolução das Emissões Mensais</h3>
        <div class="table-responsive">
            <table class="mun-table" id="tabelaEvolucao">
                <thead>
                <tr>
                    <th>Mês</th>
                    <th>Emissões Totais</th>
                </tr>
                </thead>
                <tbody id="evolucaoBody">
                <c:forEach var="mesAno" items="${evolucaoEmissoesMensais.keySet()}" varStatus="status">
                    <tr data-index="${status.index}">
                        <td>${mesAno}</td>
                        <td><fmt:formatNumber value="${evolucaoEmissoesMensais[mesAno]}" minFractionDigits="2"
                                              maxFractionDigits="2"/> kg
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="pagination-container" id="paginationEvolucao"></div>
    </div>

    <%-- ── COMBUSTÍVEL ── --%>
    <div class="mun-card mun-section">
        <h3 class="mun-card-title">Percentagem por Tipo de Combustível</h3>
        <div class="table-responsive">
            <table class="mun-table" id="tabelaCombustivel">
                <thead>
                <tr>
                    <th>Combustível</th>
                    <th>Total KMs</th>
                    <th>% KMs</th>
                    <th>Total CO₂</th>
                    <th>% CO₂</th>
                </tr>
                </thead>
                <tbody id="combustivelBody">
                <c:forEach var="combustivel" items="${totalKmsPorCombustivel.keySet()}" varStatus="status">
                    <tr data-index="${status.index}">
                        <td>${combustivel}</td>
                        <td><fmt:formatNumber value="${totalKmsPorCombustivel[combustivel]}" minFractionDigits="1"
                                              maxFractionDigits="1"/> km
                        </td>
                        <td><fmt:formatNumber value="${percentagemKmsPorCombustivel[combustivel]}" minFractionDigits="2"
                                              maxFractionDigits="2"/>%
                        </td>
                        <td><fmt:formatNumber value="${totalCo2PorCombustivel[combustivel]}" minFractionDigits="2"
                                              maxFractionDigits="2"/> kg
                        </td>
                        <td><fmt:formatNumber value="${percentagemCo2PorCombustivel[combustivel]}" minFractionDigits="2"
                                              maxFractionDigits="2"/>%
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="pagination-container" id="paginationCombustivel"></div>
    </div>

    <%-- ── EMISSÕES MÉDIAS POR COMBUSTÍVEL ── --%>
    <div class="mun-card mun-section">
        <h3 class="mun-card-title">Emissões Médias por Tipo de Combustível</h3>
        <div class="table-responsive">
            <table class="mun-table" id="tabelaEmissoesMedias">
                <thead>
                <tr>
                    <th>Combustível</th>
                    <th>Emissão Média</th>
                </tr>
                </thead>
                <tbody id="emissoesMediasBody">
                <c:forEach var="combustivel" items="${emissaoMediaPorCombustivel.keySet()}" varStatus="status">
                    <tr data-index="${status.index}">
                        <td>${combustivel}</td>
                        <td><fmt:formatNumber value="${emissaoMediaPorCombustivel[combustivel]}" minFractionDigits="2"
                                              maxFractionDigits="2"/> kg
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="pagination-container" id="paginationEmissoesMedias"></div>
    </div>

    <div class="dashboard-actions">
        <a href="${pageContext.request.contextPath}/municipio/homeMunicipio" class="smart-btn smart-btn-secondary">
            Voltar ao Home
        </a>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function criarPaginacao(tabelaId, bodyId, linhasPorPagina, containerId) {
        const tbody = document.getElementById(bodyId);
        if (!tbody) return;

        const linhas = Array.from(tbody.querySelectorAll('tr'));
        const totalLinhas = linhas.length;
        const totalPaginas = Math.ceil(totalLinhas / linhasPorPagina);
        let paginaAtual = 1;

        function mostrarPagina(pagina) {
            const inicio = (pagina - 1) * linhasPorPagina;
            const fim = inicio + linhasPorPagina;

            linhas.forEach(linha => {
                linha.style.display = 'none';
            });

            for (let i = inicio; i < fim && i < totalLinhas; i++) {
                linhas[i].style.display = '';
            }
        }

        function criarBotoes() {
            const container = document.getElementById(containerId);
            if (!container) return;

            if (totalPaginas <= 1) {
                container.innerHTML = '';
                return;
            }

            let html = '<div class="pagination-wrapper">';

            if (paginaAtual === 1) {
                html += '<button class="pag-btn" disabled>« Anterior</button>';
            } else {
                html += '<button class="pag-btn" onclick="mudarPagina(\'' + containerId + '\', ' + (paginaAtual - 1) + ', \'' + bodyId + '\', ' + linhasPorPagina + ')">« Anterior</button>';
            }

            for (let i = 1; i <= totalPaginas; i++) {
                if (i === paginaAtual) {
                    html += '<button class="pag-btn active">' + i + '</button>';
                } else {
                    html += '<button class="pag-btn" onclick="mudarPagina(\'' + containerId + '\', ' + i + ', \'' + bodyId + '\', ' + linhasPorPagina + ')">' + i + '</button>';
                }
            }

            if (paginaAtual === totalPaginas) {
                html += '<button class="pag-btn" disabled>Próximo »</button>';
            } else {
                html += '<button class="pag-btn" onclick="mudarPagina(\'' + containerId + '\', ' + (paginaAtual + 1) + ', \'' + bodyId + '\', ' + linhasPorPagina + ')">Próximo »</button>';
            }

            html += '</div>';
            container.innerHTML = html;
        }

        window.mudarPagina = function(containerId, pagina, bodyId, linhasPorPagina) {
            const tbodyLocal = document.getElementById(bodyId);
            if (!tbodyLocal) return;

            const linhasLocal = Array.from(tbodyLocal.querySelectorAll('tr'));
            const totalPaginasLocal = Math.ceil(linhasLocal.length / linhasPorPagina);

            if (pagina < 1 || pagina > totalPaginasLocal) return;

            paginaAtual = pagina;
            const inicio = (pagina - 1) * linhasPorPagina;
            const fim = inicio + linhasPorPagina;

            linhasLocal.forEach((linha, idx) => {
                linha.style.display = (idx >= inicio && idx < fim) ? '' : 'none';
            });

            const container = document.getElementById(containerId);
            if (container) {
                let html = '<div class="pagination-wrapper">';

                if (pagina === 1) {
                    html += '<button class="pag-btn" disabled>« Anterior</button>';
                } else {
                    html += '<button class="pag-btn" onclick="mudarPagina(\'' + containerId + '\', ' + (pagina - 1) + ', \'' + bodyId + '\', ' + linhasPorPagina + ')">« Anterior</button>';
                }

                for (let i = 1; i <= totalPaginasLocal; i++) {
                    if (i === pagina) {
                        html += '<button class="pag-btn active">' + i + '</button>';
                    } else {
                        html += '<button class="pag-btn" onclick="mudarPagina(\'' + containerId + '\', ' + i + ', \'' + bodyId + '\', ' + linhasPorPagina + ')">' + i + '</button>';
                    }
                }

                if (pagina === totalPaginasLocal) {
                    html += '<button class="pag-btn" disabled>Próximo »</button>';
                } else {
                    html += '<button class="pag-btn" onclick="mudarPagina(\'' + containerId + '\', ' + (pagina + 1) + ', \'' + bodyId + '\', ' + linhasPorPagina + ')">Próximo »</button>';
                }

                html += '</div>';
                container.innerHTML = html;
            }
        };

        mostrarPagina(1);
        criarBotoes();
    }

    document.addEventListener('DOMContentLoaded', function() {
        if (document.getElementById('tabelaResumo')) criarPaginacao('tabelaResumo', 'resumoBody', 8, 'paginationResumo');
        if (document.getElementById('tabelaEvolucao')) criarPaginacao('tabelaEvolucao', 'evolucaoBody', 8, 'paginationEvolucao');
        if (document.getElementById('tabelaCombustivel')) criarPaginacao('tabelaCombustivel', 'combustivelBody', 8, 'paginationCombustivel');
        if (document.getElementById('tabelaEmissoesMedias')) criarPaginacao('tabelaEmissoesMedias', 'emissoesMediasBody', 8, 'paginationEmissoesMedias');
    });
</script>

</body>
</html>