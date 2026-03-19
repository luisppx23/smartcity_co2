package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.SignUpModel;
import pt.upskill.smart_city_co2.services.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/autenticadoTeste")
    public String autenticadoTestePage() {
        return "autenticadoTeste";
    }


    @GetMapping(value = "/signup")
    public String signUpPage() {
        return "signup";
    }

    @PostMapping(value = "/signUpAction")
    public String signUpAction(SignUpModel signUp) {
        User user = authService.register(signUp);
        return "login";
    }

}
