package pt.upskill.smart_city_co2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {

    // Direciona para pagina de dashboard do municipio
    @GetMapping("/dashboardMunicipio")
    public String dashboardMunicipio() {
        return "municipio/dashboardMunicipio";
    }

    // Direciona para pagina de home do municipio
    @GetMapping("/homeMunicipio")
    public String homeMunicipio() {
        return "municipio/homeMunicipio";
    }

    // Direciona para pagina de redefinir taxa
    @GetMapping("/redefinirTaxa")
    public String redefinirTaxa() {
        return "municipio/redefinirTaxa";
    }

    // Direciona para pagina de exibicao de relatorios
    @GetMapping("/relatoriosMunicipio")
    public String relatoriosMunicipio() {
        return "municipio/relatoriosMunicipio";
    }
}