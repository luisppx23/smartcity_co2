package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.services.CidadaoService;

@Controller
@RequestMapping("/cidadao")
public class CidadaoController {

    @Autowired
    CidadaoService cidadaoService;

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

    // Direciona para pagina de simular taxa
    @GetMapping("/simularTaxa")
    public String simularTaxa(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
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