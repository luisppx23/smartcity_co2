package pt.upskill.smart_city_co2.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.models.RegistarKmsModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.OwnershipRepository;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;
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

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/registoKms")
    public String mostrarFormulario(Model model, Authentication authentication) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);

        model.addAttribute("registoKmsModel", new RegistarKmsModel());
        model.addAttribute("cidadao", cidadao);

        if (cidadao != null) {
            prepararDadosHistorico(model, cidadao);
        }

        return "cidadao/registoKms";
    }

    @PostMapping("/registoKmsAction")
    @Transactional
    public String registarKms(@RequestParam("kms") double kms,
                              @RequestParam("veiculoId") Long veiculoId,
                              Authentication authentication,
                              Model model) {

        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        model.addAttribute("registoKmsModel", new RegistarKmsModel());
        model.addAttribute("cidadao", cidadao);

        if (cidadao == null) {
            model.addAttribute("erro", "Não foi possível identificar o cidadão.");
            prepararDadosHistorico(model, cidadao);
            return "cidadao/registoKms";
        }

        if (kms <= 0) {
            model.addAttribute("erro", "Os quilómetros devem ser superiores a 0.");
            prepararDadosHistorico(model, cidadao);
            return "cidadao/registoKms";
        }

        // Buscar o Ownership pelo veículo e cidadão
        Ownership ownership = encontrarOwnershipPorVeiculoECidadao(cidadao, veiculoId);

        if (ownership == null) {
            model.addAttribute("erro", "Veículo não encontrado ou não pertence ao cidadão.");
            prepararDadosHistorico(model, cidadao);
            return "cidadao/registoKms";
        }

        Veiculo veiculo = ownership.getVeiculo();

        try {
            RegistoKms registoGuardado = registoKmsService.salvarRegisto(cidadao, ownership, kms);

            // recarregar cidadão para ter a lista atualizada
            cidadao = cidadaoRepository.findById(cidadao.getId()).orElse(null);
            if (cidadao != null) {
                prepararDadosHistorico(model, cidadao);
            }

            model.addAttribute("mensagem", "Registo guardado com sucesso!");
            model.addAttribute("veiculoSelecionado", veiculo);
            model.addAttribute("kmsInseridos", kms);
            model.addAttribute("emissaoGPorKm", registoGuardado.getEmissaoGPorKm());
            model.addAttribute("emissaoEfetivaKg", registoGuardado.getEmissaoEfetivaKg());

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao guardar o registo: " + e.getMessage());
            prepararDadosHistorico(model, cidadao);
        }

        return "cidadao/registoKms";
    }

    @GetMapping("/verRegistosKms")
    public String verHistorico(Authentication authentication, Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        model.addAttribute("cidadao", cidadao);

        if (cidadao != null) {
            List<Ownership> ownerships = cidadao.getListaDeVeiculos();

            List<Veiculo> listaVeiculos = new ArrayList<>();
            Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
            Map<Long, String> combustivelPorVeiculo = new LinkedHashMap<>();

            List<RegistoKms> todosRegistos = new ArrayList<>();

            for (Ownership ownership : ownerships) {
                Veiculo veiculo = ownership.getVeiculo();
                listaVeiculos.add(veiculo);
                matriculaPorVeiculo.put(veiculo.getId(), ownership.getMatricula());
                combustivelPorVeiculo.put(veiculo.getId(), veiculo.getTipoDeCombustivel().name());

                if (ownership.getRegistos() != null) {
                    todosRegistos.addAll(ownership.getRegistos());
                }
            }

            model.addAttribute("listaVeiculos", listaVeiculos);
            model.addAttribute("matriculaPorVeiculo", matriculaPorVeiculo);
            model.addAttribute("combustivelPorVeiculo", combustivelPorVeiculo);
            model.addAttribute("listaRegistos", todosRegistos);

            double totalKmsGeral = 0.0;
            double totalCo2Geral = 0.0;

            Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
            Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

            Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
            Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();
            Map<String, Double> percentagemKmsPorCombustivel = new LinkedHashMap<>();
            Map<String, Double> percentagemCo2PorCombustivel = new LinkedHashMap<>();

            for (Ownership ownership : ownerships) {
                Veiculo veiculo = ownership.getVeiculo();
                Long veiculoId = veiculo.getId();
                String combustivel = veiculo.getTipoDeCombustivel().name();

                totalKmsPorVeiculo.put(veiculoId, 0.0);
                totalCo2PorVeiculo.put(veiculoId, 0.0);

                totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
                totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);
            }

            for (Ownership ownership : ownerships) {
                Veiculo veiculo = ownership.getVeiculo();
                Long veiculoId = veiculo.getId();
                String combustivel = veiculo.getTipoDeCombustivel().name();

                if (ownership.getRegistos() != null) {
                    for (RegistoKms registo : ownership.getRegistos()) {
                        double kms = registo.getKms_mes();
                        double co2 = registo.getEmissaoEfetivaKg();

                        totalKmsGeral += kms;
                        totalCo2Geral += co2;

                        totalKmsPorVeiculo.put(
                                veiculoId,
                                totalKmsPorVeiculo.getOrDefault(veiculoId, 0.0) + kms
                        );

                        totalCo2PorVeiculo.put(
                                veiculoId,
                                totalCo2PorVeiculo.getOrDefault(veiculoId, 0.0) + co2
                        );

                        totalKmsPorCombustivel.put(
                                combustivel,
                                totalKmsPorCombustivel.getOrDefault(combustivel, 0.0) + kms
                        );

                        totalCo2PorCombustivel.put(
                                combustivel,
                                totalCo2PorCombustivel.getOrDefault(combustivel, 0.0) + co2
                        );
                    }
                }
            }

            for (String combustivel : totalKmsPorCombustivel.keySet()) {
                double kmsCombustivel = totalKmsPorCombustivel.get(combustivel);
                double co2Combustivel = totalCo2PorCombustivel.get(combustivel);

                double percentagemKms = totalKmsGeral > 0 ? (kmsCombustivel / totalKmsGeral) * 100 : 0.0;
                double percentagemCo2 = totalCo2Geral > 0 ? (co2Combustivel / totalCo2Geral) * 100 : 0.0;

                percentagemKmsPorCombustivel.put(combustivel, percentagemKms);
                percentagemCo2PorCombustivel.put(combustivel, percentagemCo2);
            }

            // RANKING
            List<Cidadao> todosCidadaos = cidadaoRepository.findAll();
            Map<Long, Double> totalCo2PorCidadao = new LinkedHashMap<>();

            for (Cidadao outroCidadao : todosCidadaos) {
                double totalCo2Cidadao = 0.0;

                List<Ownership> ownershipsOutroCidadao = outroCidadao.getListaDeVeiculos();
                if (ownershipsOutroCidadao != null) {
                    for (Ownership ownership : ownershipsOutroCidadao) {
                        if (ownership.getRegistos() != null) {
                            for (RegistoKms registo : ownership.getRegistos()) {
                                totalCo2Cidadao += registo.getEmissaoEfetivaKg();
                            }
                        }
                    }
                }

                totalCo2PorCidadao.put(outroCidadao.getId(), totalCo2Cidadao);
            }

            List<Map.Entry<Long, Double>> rankingPoluidores = new ArrayList<>(totalCo2PorCidadao.entrySet());
            rankingPoluidores.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            int posicaoRankingPoluicao = 0;

            for (int i = 0; i < rankingPoluidores.size(); i++) {
                if (rankingPoluidores.get(i).getKey().equals(cidadao.getId())) {
                    posicaoRankingPoluicao = i + 1;
                    break;
                }
            }

            model.addAttribute("totalKmsGeral", totalKmsGeral);
            model.addAttribute("totalCo2Geral", totalCo2Geral);
            model.addAttribute("totalKmsPorVeiculo", totalKmsPorVeiculo);
            model.addAttribute("totalCo2PorVeiculo", totalCo2PorVeiculo);

            model.addAttribute("totalKmsPorCombustivel", totalKmsPorCombustivel);
            model.addAttribute("totalCo2PorCombustivel", totalCo2PorCombustivel);
            model.addAttribute("percentagemKmsPorCombustivel", percentagemKmsPorCombustivel);
            model.addAttribute("percentagemCo2PorCombustivel", percentagemCo2PorCombustivel);

            model.addAttribute("posicaoRankingPoluicao", posicaoRankingPoluicao);
            model.addAttribute("numeroTotalCidadaos", todosCidadaos.size());

            model.addAttribute("cidadao", cidadao);
        }

        return "cidadao/historicoKms";
    }

    private Cidadao obterCidadaoAutenticado(Authentication authentication) {
        Cidadao cidadao = null;

        if (authentication.getPrincipal() instanceof Cidadao) {
            cidadao = (Cidadao) authentication.getPrincipal();
            cidadao = cidadaoRepository.findById(cidadao.getId()).orElse(null);
        }
        return cidadao;
    }

    private Ownership encontrarOwnershipPorVeiculoECidadao(Cidadao cidadao, Long veiculoId) {
        // Assumindo que Cidadao tem uma lista de Ownership
        List<Ownership> ownerships = cidadao.getListaDeVeiculos();
        if (ownerships != null) {
            for (Ownership ownership : ownerships) {
                if (ownership.getVeiculo() != null && ownership.getVeiculo().getId().equals(veiculoId)) {
                    return ownership;
                }
            }
        }
        return null;
    }

    private void prepararDadosHistorico(Model model, Cidadao cidadao) {
        if (cidadao == null) {
            model.addAttribute("listaVeiculos", new ArrayList<>());
            model.addAttribute("historicoPorVeiculo", new LinkedHashMap<>());
            model.addAttribute("totalKmsPorVeiculo", new LinkedHashMap<>());
            model.addAttribute("totalCo2PorVeiculo", new LinkedHashMap<>());
            return;
        }

        List<Ownership> ownerships = cidadao.getListaDeVeiculos();
        List<Veiculo> listaVeiculos = new ArrayList<>();
        Map<Long, List<RegistoKms>> historicoPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

        if (ownerships != null) {
            for (Ownership ownership : ownerships) {
                Veiculo veiculo = ownership.getVeiculo();
                listaVeiculos.add(veiculo);

                List<RegistoKms> registos = ownership.getRegistos();
                if (registos == null) {
                    registos = new ArrayList<>();
                }

                historicoPorVeiculo.put(veiculo.getId(), registos);

                double totalKms = 0.0;
                double totalCo2 = 0.0;
                for (RegistoKms registo : registos) {
                    totalKms += registo.getKms_mes();
                    totalCo2 += registo.getEmissaoEfetivaKg();
                }

                totalKmsPorVeiculo.put(veiculo.getId(), totalKms);
                totalCo2PorVeiculo.put(veiculo.getId(), totalCo2);
            }
        }

        model.addAttribute("listaVeiculos", listaVeiculos);
        model.addAttribute("historicoPorVeiculo", historicoPorVeiculo);
        model.addAttribute("totalKmsPorVeiculo", totalKmsPorVeiculo);
        model.addAttribute("totalCo2PorVeiculo", totalCo2PorVeiculo);
    }

    @GetMapping("/estatisticas")
    public String verEstatisticas(Authentication authentication, Model model) {
        model.addAttribute("user", getAuthenticatedUser());

        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        model.addAttribute("cidadao", cidadao);

        if (cidadao == null) {
            model.addAttribute("erro", "Não foi possível identificar o cidadão.");
            return "cidadao/estatisticas";
        }

        List<Ownership> ownerships = cidadao.getListaDeVeiculos();

        double totalKmsGeral = 0.0;
        double totalCo2Geral = 0.0;

        Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();

        Map<String, Double> totalKmsPorMatricula = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorMatricula = new LinkedHashMap<>();

        if (ownerships != null) {
            for (Ownership ownership : ownerships) {
                String matricula = ownership.getMatricula();
                String combustivel = ownership.getVeiculo().getTipoDeCombustivel().name();

                totalKmsPorMatricula.put(matricula, 0.0);
                totalCo2PorMatricula.put(matricula, 0.0);

                totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
                totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);

                List<RegistoKms> registos = ownership.getRegistos();
                if (registos != null) {
                    for (RegistoKms registo : registos) {
                        double kms = registo.getKms_mes();
                        double co2 = registo.getEmissaoEfetivaKg();

                        totalKmsGeral += kms;
                        totalCo2Geral += co2;

                        totalKmsPorMatricula.put(
                                matricula,
                                totalKmsPorMatricula.getOrDefault(matricula, 0.0) + kms
                        );

                        totalCo2PorMatricula.put(
                                matricula,
                                totalCo2PorMatricula.getOrDefault(matricula, 0.0) + co2
                        );

                        totalKmsPorCombustivel.put(
                                combustivel,
                                totalKmsPorCombustivel.getOrDefault(combustivel, 0.0) + kms
                        );

                        totalCo2PorCombustivel.put(
                                combustivel,
                                totalCo2PorCombustivel.getOrDefault(combustivel, 0.0) + co2
                        );
                    }
                }
            }
        }

        Map<String, Double> percentagemKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> percentagemCo2PorCombustivel = new LinkedHashMap<>();

        for (String combustivel : totalKmsPorCombustivel.keySet()) {
            double kmsCombustivel = totalKmsPorCombustivel.getOrDefault(combustivel, 0.0);
            double co2Combustivel = totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);

            double percentagemKms = totalKmsGeral > 0 ? (kmsCombustivel / totalKmsGeral) * 100 : 0.0;
            double percentagemCo2 = totalCo2Geral > 0 ? (co2Combustivel / totalCo2Geral) * 100 : 0.0;

            percentagemKmsPorCombustivel.put(combustivel, percentagemKms);
            percentagemCo2PorCombustivel.put(combustivel, percentagemCo2);
        }

        // ranking de poluição entre cidadãos
        List<Cidadao> todosCidadaos = cidadaoRepository.findAll();
        Map<Long, Double> totalCo2PorCidadao = new LinkedHashMap<>();

        for (Cidadao outroCidadao : todosCidadaos) {
            double totalCo2Cidadao = 0.0;

            List<Ownership> ownershipsOutro = outroCidadao.getListaDeVeiculos();
            if (ownershipsOutro != null) {
                for (Ownership ownership : ownershipsOutro) {
                    List<RegistoKms> registos = ownership.getRegistos();
                    if (registos != null) {
                        for (RegistoKms registo : registos) {
                            totalCo2Cidadao += registo.getEmissaoEfetivaKg();
                        }
                    }
                }
            }

            totalCo2PorCidadao.put(outroCidadao.getId(), totalCo2Cidadao);
        }

        List<Map.Entry<Long, Double>> rankingMaisPoluidores = new ArrayList<>(totalCo2PorCidadao.entrySet());
        rankingMaisPoluidores.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<Map.Entry<Long, Double>> rankingMenosPoluidores = new ArrayList<>(totalCo2PorCidadao.entrySet());
        rankingMenosPoluidores.sort(Map.Entry.comparingByValue());

        int posicaoMaisPoluidor = 0;
        int posicaoMenosPoluidor = 0;

        for (int i = 0; i < rankingMaisPoluidores.size(); i++) {
            if (rankingMaisPoluidores.get(i).getKey().equals(cidadao.getId())) {
                posicaoMaisPoluidor = i + 1;
                break;
            }
        }

        for (int i = 0; i < rankingMenosPoluidores.size(); i++) {
            if (rankingMenosPoluidores.get(i).getKey().equals(cidadao.getId())) {
                posicaoMenosPoluidor = i + 1;
                break;
            }
        }

        model.addAttribute("totalKmsGeral", totalKmsGeral);
        model.addAttribute("totalCo2Geral", totalCo2Geral);

        model.addAttribute("totalKmsPorCombustivel", totalKmsPorCombustivel);
        model.addAttribute("totalCo2PorCombustivel", totalCo2PorCombustivel);
        model.addAttribute("percentagemKmsPorCombustivel", percentagemKmsPorCombustivel);
        model.addAttribute("percentagemCo2PorCombustivel", percentagemCo2PorCombustivel);

        model.addAttribute("totalKmsPorMatricula", totalKmsPorMatricula);
        model.addAttribute("totalCo2PorMatricula", totalCo2PorMatricula);

        model.addAttribute("posicaoMaisPoluidor", posicaoMaisPoluidor);
        model.addAttribute("posicaoMenosPoluidor", posicaoMenosPoluidor);
        model.addAttribute("numeroTotalCidadaos", todosCidadaos.size());

        return "cidadao/estatisticas";
    }

}