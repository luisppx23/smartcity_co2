package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pt.upskill.smart_city_co2.dto.DashboardMunicipioDataDTO;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.services.DashboardMunicipioAnalyticsService;
import pt.upskill.smart_city_co2.services.MunicipioService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private DashboardMunicipioAnalyticsService analyticsService;

    // Metodo auxiliar para obter o utilizador autenticado
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }

        return null;
    }

    @GetMapping("/dashboardMunicipio")
    @Transactional(readOnly = true)
    public String dashboardMunicipio(Model model) {
        // Adiciona ao model o município autenticado e o utilizador
        Municipio municipio = adicionarContextoMunicipio(model);

        // Se não encontrar município, devolve a página na mesma com erro no model
        if (municipio == null) {
            return "municipio/dashboardMunicipio";
        }

        // Preenche o model com os dados do relatório/dashboard
        adicionarRelatorioAoModel(model, municipio);

        return "municipio/dashboardMunicipio";
    }

    @GetMapping("/homeMunicipio")
    public String homeMunicipio(Model model) {
        // Envia o utilizador autenticado para a home do município
        Municipio municipio = adicionarContextoMunicipio(model);
        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/homeMunicipio";
    }

    @GetMapping("/perfil")
    public String perfilMunicipio(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "redirect:/municipio/homeMunicipio";
        }
        return "municipio/perfilM";
    }

    @GetMapping("/perfil/editar")
    public String editarPerfilMunicipio(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "redirect:/municipio/homeMunicipio";
        }
        return "municipio/editarPerfilM";
    }

    @PostMapping("/perfil/salvar")
    public String salvarPerfilMunicipio(
            @RequestParam(required = false) MultipartFile fotoFicheiro,
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam int nif,
            @RequestParam double objetivo,
            Model model) {

        User userLogado = getAuthenticatedUser();
        if (userLogado == null) {
            return "redirect:/auth/login";
        }

        String fotoUrl = null;
        if (fotoFicheiro != null && !fotoFicheiro.isEmpty()) {
            try {
                String base64Image = java.util.Base64.getEncoder().encodeToString(fotoFicheiro.getBytes());
                fotoUrl = "data:" + fotoFicheiro.getContentType() + ";base64," + base64Image;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        try {
            municipioService.atualizarPerfil(userLogado.getUsername(), nome, email, nif, objetivo, fotoUrl);
            model.addAttribute("mensagem", "Perfil actualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
        }

        // Recarregar o município actualizado para a vista
        adicionarContextoMunicipio(model);
        return "municipio/perfilM";
    }

    @GetMapping("/redefinirMeta")
    public String redefinirMeta(Model model) {
        // Prepara o contexto do município para a página
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            return "municipio/redefinirMeta";
        }

        return "municipio/redefinirMeta";
    }

    @GetMapping("/redefinirTaxa")
    public String redefinirTaxa(Model model) {
        // Prepara o contexto do município para a página
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            return "municipio/redefinirTaxa";
        }

        return "municipio/redefinirTaxa";
    }

    @GetMapping("/relatoriosMunicipio")
    @Transactional(readOnly = true)
    public String relatoriosMunicipio(Model model) {
        // Adiciona dados do município e o relatório completo
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            return "municipio/relatoriosMunicipio";
        }

        adicionarRelatorioAoModel(model, municipio);

        return "municipio/relatoriosMunicipio";
    }

    @GetMapping("/listaCidadaos")
    @Transactional(readOnly = true)
    public String listaDeCidadaos(Model model) {
        // Obtém o município autenticado
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            return "municipio/listaCidadaos";
        }

        // Vai buscar todos os cidadãos desse município
        List<Cidadao> listaCidadaos = municipioService.buscarCidadaosDoMunicipio(municipio);
        model.addAttribute("listaCidadaos", listaCidadaos);

        return "municipio/listaCidadaos";
    }

    @GetMapping("/listaVeiculos")
    @Transactional(readOnly = true)
    public String listaDeVeiculos(Model model) {
        // Obtém o município autenticado
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            return "municipio/listaVeiculos";
        }

        // Reaproveita a lista de cidadãos para depois mostrar os veículos associados
        List<Cidadao> listaCidadaos = municipioService.buscarCidadaosDoMunicipio(municipio);
        model.addAttribute("listaCidadaos", listaCidadaos);

        return "municipio/listaVeiculos";
    }

    @PostMapping("/redefinirMetaAction")
    public String redefinirMetaAction(@RequestParam("novoObjetivo") double novoObjetivo, Model model) {
        // Obtém o município autenticado
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            model.addAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "municipio/redefinirMeta";
        }

        try {
            // Atualiza o objetivo de CO2 do município
            municipioService.atualizarObjetivoCo2(municipio.getUsername(), novoObjetivo);

            // Recarrega os dados atualizados
            Municipio municipioAtualizado = municipioService.getUserM(municipio.getUsername());
            model.addAttribute("municipio", municipioAtualizado);

            // Mensagem de sucesso
            model.addAttribute(
                    "mensagem",
                    "Objectivo CO₂ actualizado com sucesso para " + novoObjetivo + " kg/habitante/mês."
            );
        } catch (IllegalArgumentException e) {
            // Caso a validação falhe, mostra o erro devolvido pelo service
            model.addAttribute("erro", e.getMessage());
        }

        return "municipio/redefinirMeta";
    }

    @PostMapping("/redefinirTaxaAction")
    public String redefinirTaxaAction(@RequestParam("taxaNivel1") double taxaNivel1,
                                      @RequestParam("taxaNivel2") double taxaNivel2,
                                      @RequestParam("taxaNivel3") double taxaNivel3,
                                      @RequestParam("taxaNivel4") double taxaNivel4,
                                      @RequestParam("taxaNivel5") double taxaNivel5,
                                      @RequestParam("taxaNivel6") double taxaNivel6,
                                      @RequestParam("taxaNivel7") double taxaNivel7,
                                      Model model) {

        // Obtém o município autenticado
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            model.addAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "municipio/redefinirTaxa";
        }

        try {
            // Atualiza a tabela de taxas dos vários níveis
            municipioService.atualizarTaxas(
                    municipio.getUsername(),
                    taxaNivel1, taxaNivel2, taxaNivel3, taxaNivel4,
                    taxaNivel5, taxaNivel6, taxaNivel7
            );

            // Recarrega o município já com os novos valores
            Municipio municipioAtualizado = municipioService.getUserM(municipio.getUsername());
            model.addAttribute("municipio", municipioAtualizado);

            model.addAttribute("mensagem", "Tabela de taxas atualizada com sucesso.");
        } catch (IllegalArgumentException e) {
            // Mostra o erro de validação ao utilizador
            model.addAttribute("erro", e.getMessage());
        }

        return "municipio/redefinirTaxa";
    }

    @GetMapping("/dashboardMunicipio_b")
    @Transactional(readOnly = true)
    public String dashboardGraficos(Model model) {
        // Variante alternativa do dashboard do município
        Municipio municipio = adicionarContextoMunicipio(model);

        if (municipio == null) {
            return "municipio/dashboardMunicipio_b";
        }

        adicionarRelatorioAoModel(model, municipio);

        return "municipio/dashboardMunicipio_b";
    }

    // Metodo auxiliar que coloca no model o utilizador e o município autenticado
    private Municipio adicionarContextoMunicipio(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        // Se não existir utilizador autenticado, não é possível identificar o município
        if (user == null) {
            model.addAttribute("erro", "Não foi possível identificar o município autenticado.");
            return null;
        }

        // Procura o município através do username
        Municipio municipio = municipioService.getUserM(user.getUsername());
        model.addAttribute("municipio", municipio);

        // Se não encontrar município, adiciona mensagem de erro
        if (municipio == null) {
            model.addAttribute("erro", "Município não encontrado.");
            return null;
        }

        return municipio;
    }

    // Metodo auxiliar que adiciona ao model todos os dados necessários para o dashboard/relatórios
    private void adicionarRelatorioAoModel(Model model, Municipio municipio) {
        DashboardMunicipioDataDTO dados = municipioService.gerarRelatorioMunicipio(municipio);

        // Adiciona os dados brutos ao model
        model.addAttribute("dados", dados);
        model.addAttribute("listaCidadaos", dados.getCidadaos());
        model.addAttribute("listaRegistos", dados.getRegistos());
        model.addAttribute("listaVeiculos", dados.getVeiculos());
        model.addAttribute("numeroHabitantes", dados.getNumeroHabitantes());
        model.addAttribute("quantidadeVeiculosTotais", dados.getQuantidadeVeiculosTotais());

        // Calcula e adiciona todas as métricas derivadas
        model.addAttribute("totalKmsGeral", analyticsService.getTotalKmsGeral(dados));
        model.addAttribute("totalCo2Geral", analyticsService.getTotalCo2Geral(dados));
        model.addAttribute("totalTaxaGeral", analyticsService.getTotalTaxaGeral(dados));

        model.addAttribute("totalKmsPorCombustivel", analyticsService.calcularTotalKmsPorCombustivel(dados));
        model.addAttribute("totalCo2PorCombustivel", analyticsService.calcularTotalCo2PorCombustivel(dados));
        model.addAttribute("totalTaxaPorCombustivel", analyticsService.calcularTotalTaxaPorCombustivel(dados));

        model.addAttribute("percentagemKmsPorCombustivel", analyticsService.calcularPercentagemKmsPorCombustivel(dados));
        model.addAttribute("percentagemCo2PorCombustivel", analyticsService.calcularPercentagemCo2PorCombustivel(dados));

        model.addAttribute("numeroRegistosPorCombustivel", analyticsService.calcularNumeroRegistosPorCombustivel(dados));
        model.addAttribute("emissaoMediaPorCombustivel", analyticsService.calcularEmissaoMediaPorCombustivel(dados));
        model.addAttribute("quantidadeVeiculosPorCombustivel", analyticsService.contarVeiculosPorCombustivel(dados));

        model.addAttribute("totalKmsPorMes", analyticsService.calcularTotalKmsPorMes(dados));
        model.addAttribute("totalCo2PorMes", analyticsService.calcularTotalCo2PorMes(dados));
        model.addAttribute("mediaCo2PorHabitantePorMes", analyticsService.calcularMediaCo2PorHabitantePorMes(dados));
        model.addAttribute("objetivoAtingidoPorMes", analyticsService.calcularObjetivoAtingidoPorMes(dados));
        model.addAttribute("mediaEmissoesPorMes", analyticsService.calcularMediaEmissoesPorMes(dados));

        model.addAttribute("variacaoAnoAnteriorPorMes", analyticsService.calcularVariacaoAnoAnterior(dados));
        model.addAttribute("corComparacaoAnoAnteriorPorMes", analyticsService.calcularCorComparacaoAnoAnteriorPorMes(dados));
        model.addAttribute("variacaoMesAnterior", analyticsService.calcularVariacaoMesAnterior(dados));
        model.addAttribute("corComparacaoMesAnterior", analyticsService.calcularCorComparacaoMesAnterior(dados));

        model.addAttribute("evolucaoEmissoesMensais", analyticsService.calcularEvolucaoEmissoesMensais(dados));
        model.addAttribute("mediaGlobalEmissoesMensais", analyticsService.getMediaGlobalEmissoesMensais(dados));
        model.addAttribute("mediaPorVeiculo", analyticsService.getMediaPorVeiculo(dados));

        model.addAttribute("mesAtualLabel", analyticsService.getMesAtualLabel(dados));
        model.addAttribute("mesAtualCo2", analyticsService.getMesAtualCo2(dados));

        model.addAttribute("mediaAnoAnterior", analyticsService.getMediaAnoAnterior(dados));
        model.addAttribute("mediaAnoAtual", analyticsService.getMediaAnoAtual(dados));

        Map<String, Object> nivelMunicipio = analyticsService.calcularNivelMunicipio(dados);
        model.addAttribute("nivelMunicipio", nivelMunicipio.get("nivel"));
        model.addAttribute("nivelMunicipioPct", nivelMunicipio.get("pct"));
        model.addAttribute("nivelMunicipioIndex", nivelMunicipio.get("index"));
        model.addAttribute("mesesAtingidos", nivelMunicipio.get("mesesAtingidos"));

        model.addAttribute("mesesOrdenados", analyticsService.getMesesOrdenados(dados));
        model.addAttribute("somaEmissoesMensais", analyticsService.calcularSomaEmissoesMensais(dados));

        // Adiciona ano atual e anterior
        int anoAtual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        model.addAttribute("anoAtual", anoAtual);
        model.addAttribute("anoAnterior", anoAtual - 1);
    }
}