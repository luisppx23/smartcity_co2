package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.models.RegistarKmsModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
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

    @GetMapping("/registoKms")
    public String mostrarFormulario(Authentication authentication, Model model) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        model.addAttribute("registoKmsModel", new RegistarKmsModel());
        model.addAttribute("cidadao", cidadao);
        return "cidadao/registoKms";
    }

    @PostMapping("/registoKmsAction")
    public String registarKms(@RequestParam double kms,
                              @RequestParam Long veiculoId,
                              Authentication authentication,
                              Model model) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);

        if (cidadao == null || kms <= 0) {
            model.addAttribute("erro", "Dados inválidos");
            return "redirect:/cidadao/registoKms";
        }

        Ownership ownership = encontrarOwnership(cidadao, veiculoId);
        if (ownership == null) {
            model.addAttribute("erro", "Veículo não encontrado");
            return "redirect:/cidadao/registoKms";
        }

        try {
            registoKmsService.salvarRegisto(cidadao, ownership, kms);
            model.addAttribute("mensagem", "Registo guardado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao guardar registo: " + e.getMessage());
        }

        return "redirect:/cidadao/registoKms?sucesso=true";
    }

    @GetMapping("/verRegistosKms")
    public String verHistorico(Authentication authentication, Model model) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        model.addAttribute("cidadao", cidadao);

        if (cidadao != null && cidadao.getListaDeVeiculos() != null) {
            List<RegistoKms> todosRegistos = new ArrayList<>();
            Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();

            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                matriculaPorVeiculo.put(ownership.getVeiculo().getId(), ownership.getMatricula());
                if (ownership.getRegistosKms() != null) {
                    todosRegistos.addAll(ownership.getRegistosKms());
                }
            }

            model.addAttribute("listaRegistos", todosRegistos);
            model.addAttribute("matriculaPorVeiculo", matriculaPorVeiculo);
        }

        return "cidadao/historicoKms";
    }

    private Cidadao obterCidadaoAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Cidadao) {
            return cidadaoRepository.findById(((Cidadao) authentication.getPrincipal()).getId()).orElse(null);
        }
        return null;
    }

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