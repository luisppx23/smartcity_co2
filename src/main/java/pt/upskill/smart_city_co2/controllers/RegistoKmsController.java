package pt.upskill.smart_city_co2.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.models.RegistarKmsModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping("/registoKms")
    public String mostrarFormulario(Model model, Authentication authentication) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);

        model.addAttribute("registoKmsModel", new RegistarKmsModel());

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

        if (cidadao == null) {
            model.addAttribute("erro", "Não foi possível identificar o cidadão.");
            return "cidadao/registoKms";
        }

        prepararDadosHistorico(model, cidadao);

        if (kms <= 0) {
            model.addAttribute("erro", "Os quilómetros devem ser superiores a 0.");
            return "cidadao/registoKms";
        }

        Veiculo veiculo = veiculoRepository.findById(veiculoId).orElse(null);

        if (veiculo == null) {
            model.addAttribute("erro", "Veículo não encontrado.");
            return "cidadao/registoKms";
        }

        boolean veiculoPertenceAoCidadao = cidadao.getListaDeVeiculos()
                .stream()
                .anyMatch(v -> v.getId().equals(veiculoId));

        if (!veiculoPertenceAoCidadao) {
            model.addAttribute("erro", "O veículo selecionado não pertence ao cidadão autenticado.");
            return "cidadao/registoKms";
        }

        try {
            RegistoKms registoGuardado = registoKmsService.salvarRegisto(cidadao, veiculo, kms);

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
        }

        return "cidadao/registoKms";
    }

    @GetMapping("/verRegistosKms")
    public String verHistorico(Authentication authentication, Model model) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);

        if (cidadao != null) {
            model.addAttribute("listaRegistos", cidadao.getListaDeRegistosKms());
            model.addAttribute("listaVeiculos", cidadao.getListaDeVeiculos());

            double totalKmsGeral = 0.0;
            double totalCo2Geral = 0.0;

            Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
            Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

            if (cidadao.getListaDeVeiculos() != null) {
                for (Veiculo veiculo : cidadao.getListaDeVeiculos()) {
                    totalKmsPorVeiculo.put(veiculo.getId(), 0.0);
                    totalCo2PorVeiculo.put(veiculo.getId(), 0.0);
                }
            }

            if (cidadao.getListaDeRegistosKms() != null) {
                for (RegistoKms registo : cidadao.getListaDeRegistosKms()) {
                    totalKmsGeral += registo.getKms_mes();
                    totalCo2Geral += registo.getEmissaoEfetivaKg();

                    if (registo.getVeiculo() != null && registo.getVeiculo().getId() != null) {
                        Long veiculoId = registo.getVeiculo().getId();

                        totalKmsPorVeiculo.put(
                                veiculoId,
                                totalKmsPorVeiculo.getOrDefault(veiculoId, 0.0) + registo.getKms_mes()
                        );

                        totalCo2PorVeiculo.put(
                                veiculoId,
                                totalCo2PorVeiculo.getOrDefault(veiculoId, 0.0) + registo.getEmissaoEfetivaKg()
                        );
                    }
                }
            }

            model.addAttribute("totalKmsGeral", totalKmsGeral);
            model.addAttribute("totalCo2Geral", totalCo2Geral);
            model.addAttribute("totalKmsPorVeiculo", totalKmsPorVeiculo);
            model.addAttribute("totalCo2PorVeiculo", totalCo2PorVeiculo);
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

    private void prepararDadosHistorico(Model model, Cidadao cidadao) {
        List<Veiculo> listaVeiculos = cidadao.getListaDeVeiculos();
        List<RegistoKms> listaRegistos = cidadao.getListaDeRegistosKms();

        Map<Long, List<RegistoKms>> historicoPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

        if (listaVeiculos == null) {
            listaVeiculos = new ArrayList<>();
        }

        // inicializar mapas com todos os veículos do cidadão
        for (Veiculo veiculo : listaVeiculos) {
            historicoPorVeiculo.put(veiculo.getId(), new ArrayList<>());
            totalKmsPorVeiculo.put(veiculo.getId(), 0.0);
            totalCo2PorVeiculo.put(veiculo.getId(), 0.0);
        }

        if (listaRegistos != null) {
            for (RegistoKms registo : listaRegistos) {
                Veiculo veiculo = registo.getVeiculo();

                if (veiculo != null && veiculo.getId() != null && historicoPorVeiculo.containsKey(veiculo.getId())) {
                    Long veiculoId = veiculo.getId();

                    historicoPorVeiculo.get(veiculoId).add(registo);

                    totalKmsPorVeiculo.put(
                            veiculoId,
                            totalKmsPorVeiculo.get(veiculoId) + registo.getKms_mes()
                    );

                    totalCo2PorVeiculo.put(
                            veiculoId,
                            totalCo2PorVeiculo.get(veiculoId) + registo.getEmissaoEfetivaKg()
                    );
                }
            }
        }

        model.addAttribute("listaVeiculos", listaVeiculos);
        model.addAttribute("historicoPorVeiculo", historicoPorVeiculo);
        model.addAttribute("totalKmsPorVeiculo", totalKmsPorVeiculo);
        model.addAttribute("totalCo2PorVeiculo", totalCo2PorVeiculo);
    }
}