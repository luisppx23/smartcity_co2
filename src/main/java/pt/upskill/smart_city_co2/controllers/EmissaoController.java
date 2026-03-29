package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.services.EmissaoCO2Service;

@RestController
@RequestMapping("/emissoes")
public class EmissaoController {

    @Autowired
    EmissaoCO2Service emissaoCO2Service;

    public EmissaoController(EmissaoCO2Service emissaoCO2Service) {
        this.emissaoCO2Service = emissaoCO2Service;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/gkm")
    public double calcularGPorKm(@RequestParam int anoRegisto) {
        Ownership ownership = new Ownership();
        return emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoRegisto);
    }
}
