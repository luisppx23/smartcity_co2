package pt.upskill.smart_city_co2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cidadao")
public class CidadaoController {

    // Direciona para pagina de dashboard do cidadão
    @GetMapping("/dashboardCidadao")
    public String dashboardCidadao() {
        return "cidadao/dashboardCidadao";
    }

    // Direciona para pagina de home do cidadão
    @GetMapping("/homeCidadao")
    public String homeCidadao() {
        return "cidadao/homeCidadao";
    }

    // Direciona para pagina de registar veículo
    @GetMapping("/registoVeiculo")
    public String registarVeiculo() {
        return "cidadao/registoVeiculo";
    }

    // Direciona para pagina de simular taxa
    @GetMapping("/simularTaxa")
    public String simularTaxa() {
        return "cidadao/simularTaxa";
    }
}