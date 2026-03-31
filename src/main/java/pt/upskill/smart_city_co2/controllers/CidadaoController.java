package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.SimularTaxaModel;
import pt.upskill.smart_city_co2.services.CidadaoService;
import pt.upskill.smart_city_co2.services.EmissaoCO2Service;
import pt.upskill.smart_city_co2.services.TaxaService;

@Controller
@RequestMapping("/cidadao")
public class CidadaoController {

    @Autowired
    CidadaoService cidadaoService;

    @Autowired
    EmissaoCO2Service emissaoCO2Service;

    @Autowired
    TaxaService taxaService;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    // Direciona para pagina de dashboard do cidadão
    @GetMapping("/dashboardCidadao")
    public String dashboardCidadao(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "cidadao/dashboardCidadao";
    }

    // Direciona para pagina de home do cidadão
    @GetMapping("/homeCidadao")
    public String homeCidadao(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "cidadao/homeCidadao";
    }

    // Direciona para pagina de registar veículo
    @GetMapping("/registoVeiculo")
    public String registarVeiculo(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "cidadao/registoVeiculo";
    }

    @GetMapping("/simularTaxa")
    public String simularTaxa(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        Cidadao cidadao = cidadaoService.getUserC(user.getId());
        model.addAttribute("cidadao", cidadao);

        // Adicionar objetos vazios para o formulário
        model.addAttribute("ownershipSelecionado", null);
        model.addAttribute("kmsSimulacao", 0);
        model.addAttribute("valorTaxa", null);

        return "cidadao/simularTaxa";
    }

    @PostMapping("/simularTaxa")
    public String calcularTaxa(@RequestParam Long veiculoId,
                               @RequestParam double kms,
                               Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        Cidadao cidadao = cidadaoService.getUserC(user.getId());
        model.addAttribute("cidadao", cidadao);

        // Encontrar o ownership pelo veículo
        Ownership ownership = null;
        for (Ownership own : cidadao.getListaDeVeiculos()) {
            if (own.getVeiculo().getId().equals(veiculoId)) {
                ownership = own;
                break;
            }
        }

        if (ownership == null) {
            model.addAttribute("erro", "Veículo não encontrado");
            return "cidadao/simularTaxa";
        }

        // Calcular emissão por km
        int anoReferencia = java.time.LocalDate.now().getYear();
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);

        // Simular taxa
        double valorTaxa = taxaService.simularTaxa(emissaoGPorKm, kms);

        // Adicionar dados ao modelo
        model.addAttribute("ownershipSelecionado", ownership);
        model.addAttribute("kmsSimulacao", kms);
        model.addAttribute("emissaoGPorKm", emissaoGPorKm);
        model.addAttribute("valorTaxa", valorTaxa);

        return "cidadao/simularTaxa";
    }

    @GetMapping("/listaVeiculos")
    public String listaVeiculos(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        // Carrega o cidadão completo com a lista de veículos
        Cidadao cidadao = cidadaoService.getUserC(user.getId());
        model.addAttribute("cidadao", cidadao);

        return "cidadao/listaVeiculos";
    }
}