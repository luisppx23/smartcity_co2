package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.services.MunicipioService;

import java.util.List;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    // Direciona para pagina de dashboard do municipio
    @GetMapping("/dashboardMunicipio")
    public String dashboardMunicipio(Model model) {

        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/dashboardMunicipio";
    }

    // Direciona para pagina de home do municipio
    @GetMapping("/homeMunicipio")
    public String homeMunicipio(Model model) {

        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/homeMunicipio";
    }

    // Direciona para pagina de redefinir taxa
    @GetMapping("/redefinirTaxa")
    public String redefinirTaxa(Model model) {

        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/redefinirTaxa";
    }

    // Direciona para pagina de exibicao de relatorios
    @GetMapping("/relatoriosMunicipio")
    public String relatoriosMunicipio(Model model) {

        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/relatoriosMunicipio";
    }

    // Direciona para pagina de lista de cidadãos do município
    @GetMapping("/listaCidadaos")
    public String listaDeCidadaos(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        // Carrega o municipio completo com a lista de cidadãos
        Municipio municipio = municipioService.getUserM(user.getId());
        model.addAttribute("municipio", municipio);

        return "municipio/listaCidadaos";
    }

    // Direciona para pagina de lista de veiculos dos cidadãos do município
    @GetMapping("/listaVeiculos")
    public String listaDeVeiculos(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        // Carrega o municipio completo com a lista de cidadãos
        // Os veículos serão acessados através de cidadao.listaDeVeiculos
        Municipio municipio = municipioService.getUserM(user.getId());
        model.addAttribute("municipio", municipio);

        return "municipio/listaVeiculos";  // Retorna a página correta de veículos
    }
}