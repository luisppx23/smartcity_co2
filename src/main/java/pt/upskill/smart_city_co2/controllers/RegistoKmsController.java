package pt.upskill.smart_city_co2.controllers;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.dto.DTODashboardCidadaoService;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.models.RegistarKmsModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.services.CidadaoService;
import pt.upskill.smart_city_co2.services.RegistoKmsService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cidadao")
public class RegistoKmsController {

    @Autowired
    private RegistoKmsService registoKmsService;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private CidadaoService cidadaoService; // usado para carregar o cidadão completo

    @Autowired
    private DTODashboardCidadaoService dashboardService;

    @GetMapping("/registoKms")
    public String mostrarFormulario(Authentication authentication, Model model) {
        // Obtém o cidadão autenticado
        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        if (cidadao == null) return "redirect:/";

        // Recarrega o cidadão completo da base de dados
        // Isto é útil para garantir acesso a campos como fotoUrl e lista de veículos
        Cidadao cidadaoCompleto = cidadaoService.getUserC(cidadao.getId());

        // Dados necessários para o formulário
        model.addAttribute("user", cidadaoCompleto);
        model.addAttribute("registoKmsModel", new RegistarKmsModel());
        model.addAttribute("cidadao", cidadaoCompleto);

        return "cidadao/registoKms";
    }

    @PostMapping("/registoKmsAction")
    @Transactional
    public String registarKms(@RequestParam double kms,
                              @RequestParam Long veiculoId,
                              Authentication authentication,
                              Model model) {

        // Obtém o cidadão da sessão
        Cidadao cidadaoProxy = obterCidadaoAutenticado(authentication);

        // Validação básica: cidadão tem de existir e kms devem ser positivos
        if (cidadaoProxy == null || kms <= 0) {
            model.addAttribute("erro", "Dados inválidos");
            return "redirect:/cidadao/registoKms";
        }

        // Carrega o cidadão completo, incluindo os veículos associados
        Cidadao cidadao = cidadaoService.getUserC(cidadaoProxy.getId());

        if (cidadao == null) {
            model.addAttribute("erro", "Cidadão não encontrado");
            return "redirect:/cidadao/registoKms";
        }

        // Procura o veículo escolhido dentro da lista do cidadão
        Ownership ownership = encontrarOwnership(cidadao, veiculoId);

        if (ownership == null) {
            model.addAttribute("erro", "Veículo não encontrado");
            return "redirect:/cidadao/registoKms";
        }

        try {
            // Guarda o registo de kms através do service
            registoKmsService.salvarRegisto(cidadao, ownership, kms);
            model.addAttribute("mensagem", "Registo guardado com sucesso!");
        } catch (Exception e) {
            // Se algo falhar, informa o utilizador
            model.addAttribute("erro", "Erro ao guardar registo: " + e.getMessage());
        }

        return "redirect:/cidadao/registoKms?sucesso=true";
    }

    @GetMapping("/historicoKms")
    @Transactional(readOnly = true)
    public String verHistorico(Authentication authentication, Model model) {
        // Obtém o cidadão autenticado
        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        if (cidadao == null) return "redirect:/";

        // Recarrega o cidadão completo da base de dados
        Cidadao cidadaoCompleto = cidadaoService.getUserC(cidadao.getId());
        model.addAttribute("user", cidadaoCompleto);

        // 1. Recolher todos os registos de kms para a tabela de histórico
        List<RegistoKms> todosRegistos = new ArrayList<>();
        Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
        double totalKmsGeral = 0.0;
        double totalCo2Geral = 0.0;

        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();

                // Associa id do veículo à matrícula para mostrar na view
                if (veiculo != null) {
                    matriculaPorVeiculo.put(veiculo.getId(), ownership.getMatricula());
                }

                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms r : ownership.getRegistosKms()) {
                        // Garante que a taxa está carregada antes de sair da transação
                        // Evita problemas de LazyInitialization no JSP
                        Hibernate.initialize(r.getTaxa());

                        todosRegistos.add(r);
                        totalKmsGeral += r.getKms_mes();
                        totalCo2Geral += r.getEmissaoEfetivaKg();
                    }
                }
            }

            // Ordena os registos do mais recente para o mais antigo
            todosRegistos.sort((a, b) -> b.getMes_ano().compareTo(a.getMes_ano()));
        }

        // 2. Obter dados agregados do dashboard através do DTO service
        DTODashboardCidadaoService.DashboardDataDTO dados = dashboardService.prepararDadosDashboard(cidadaoCompleto);

        // 3. Converter combustiveisData em mapas separados porque a JSP espera essa estrutura
        Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> percentagemKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();
        Map<String, Double> percentagemCo2PorCombustivel = new LinkedHashMap<>();

        if (dados.getCombustiveisData() != null) {
            for (Map<String, Object> item : dados.getCombustiveisData()) {
                String tipo = (String) item.get("tipo");
                Double kms = (Double) item.get("kms");
                Double percKms = (Double) item.get("percentagemKms");
                Double co2 = (Double) item.get("emissoes");
                Double percCo2 = (Double) item.get("percentagemCo2");

                if (tipo != null) {
                    totalKmsPorCombustivel.put(tipo, kms != null ? kms : 0.0);
                    percentagemKmsPorCombustivel.put(tipo, percKms != null ? percKms : 0.0);
                    totalCo2PorCombustivel.put(tipo, co2 != null ? co2 : 0.0);
                    percentagemCo2PorCombustivel.put(tipo, percCo2 != null ? percCo2 : 0.0);
                }
            }
        }

        // 4. Calcular taxa total por veículo com base nos registos
        Map<Long, Double> totalTaxaPorVeiculo = new LinkedHashMap<>();

        for (RegistoKms registo : todosRegistos) {
            if (registo.getTaxa() != null) {
                Long veiculoId = registo.getOwnership().getVeiculo().getId();
                double taxa = registo.getTaxa().getValor();

                totalTaxaPorVeiculo.put(
                        veiculoId,
                        totalTaxaPorVeiculo.getOrDefault(veiculoId, 0.0) + taxa
                );
            }
        }

        model.addAttribute("totalTaxaPorVeiculo", totalTaxaPorVeiculo);

        // 5. Enviar todos os dados para o model
        model.addAttribute("listaRegistos", todosRegistos);
        model.addAttribute("matriculaPorVeiculo", matriculaPorVeiculo);
        model.addAttribute("listaVeiculos", dados.getListaVeiculos());
        model.addAttribute("totalKmsPorVeiculo", dados.getTotalKmsPorVeiculo());
        model.addAttribute("totalCo2PorVeiculo", dados.getTotalCo2PorVeiculo());
        model.addAttribute("totalKmsGeral", totalKmsGeral);
        model.addAttribute("totalCo2Geral", totalCo2Geral);

        // Dados agrupados por combustível
        model.addAttribute("totalKmsPorCombustivel", totalKmsPorCombustivel);
        model.addAttribute("percentagemKmsPorCombustivel", percentagemKmsPorCombustivel);
        model.addAttribute("totalCo2PorCombustivel", totalCo2PorCombustivel);
        model.addAttribute("percentagemCo2PorCombustivel", percentagemCo2PorCombustivel);

        // Ranking do cidadão relativamente à poluição
        model.addAttribute("posicaoRankingPoluicao", dados.getPosicaoRankingPoluicao());
        model.addAttribute("numeroTotalCidadaos", dados.getNumeroTotalCidadaos());

        // Utilizador atual
        model.addAttribute("user", cidadaoCompleto);

        return "cidadao/historicoKms";
    }

    // Metodo auxiliar para obter o cidadão autenticado
    private Cidadao obterCidadaoAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Cidadao) {
            return (Cidadao) authentication.getPrincipal();
        }

        return null;
    }

    // Metodo auxiliar para encontrar a ownership correspondente ao veículo selecionado
    private Ownership encontrarOwnership(Cidadao cidadao, Long veiculoId) {
        if (cidadao.getListaDeVeiculos() != null) {
            return cidadao.getListaDeVeiculos().stream()
                    .filter(o -> o.getVeiculo() != null && o.getVeiculo().getId().equals(veiculoId))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }
}