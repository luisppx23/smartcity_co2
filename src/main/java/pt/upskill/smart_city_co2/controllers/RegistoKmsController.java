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
            // Buscar todos os Ownerships do cidadão
            List<Ownership> ownerships = cidadao.getListaDeVeiculos();

            // Criar uma lista de veículos e um mapa de matrículas por veículo
            List<Veiculo> listaVeiculos = new ArrayList<>();
            Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
            Map<Long, String> combustivelPorVeiculo = new LinkedHashMap<>();

            // Buscar todos os registos
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

            // Calcular totais
            double totalKmsGeral = 0.0;
            double totalCo2Geral = 0.0;

            Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
            Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

            // Inicializar mapas
            for (Ownership ownership : ownerships) {
                Veiculo veiculo = ownership.getVeiculo();
                totalKmsPorVeiculo.put(veiculo.getId(), 0.0);
                totalCo2PorVeiculo.put(veiculo.getId(), 0.0);
            }

            // Calcular totais por veículo
            for (RegistoKms registo : todosRegistos) {
                totalKmsGeral += registo.getKms_mes();
                totalCo2Geral += registo.getEmissaoEfetivaKg();

                // Encontrar o veículo associado a este registo
                for (Ownership ownership : ownerships) {
                    if (ownership.getRegistos() != null && ownership.getRegistos().contains(registo)) {
                        Long veiculoId = ownership.getVeiculo().getId();
                        totalKmsPorVeiculo.put(
                                veiculoId,
                                totalKmsPorVeiculo.getOrDefault(veiculoId, 0.0) + registo.getKms_mes()
                        );
                        totalCo2PorVeiculo.put(
                                veiculoId,
                                totalCo2PorVeiculo.getOrDefault(veiculoId, 0.0) + registo.getEmissaoEfetivaKg()
                        );
                        break;
                    }
                }
            }

            model.addAttribute("totalKmsGeral", totalKmsGeral);
            model.addAttribute("totalCo2Geral", totalCo2Geral);
            model.addAttribute("totalKmsPorVeiculo", totalKmsPorVeiculo);
            model.addAttribute("totalCo2PorVeiculo", totalCo2PorVeiculo);
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
}