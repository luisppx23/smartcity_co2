package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.SignUpModel;
import pt.upskill.smart_city_co2.services.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    // Exibe a página de registo de novo user
    @GetMapping(value = "/signup")
    public String signUpPage() {
        return "signup";
    }

    // Exibe a página de login
    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    // Redireciona o user autenticado para a página correspondente ao seu tipo (município ou cidadão)
    @GetMapping("/autenticado")
    public String direcionarUtilizador(Model model) {
        User user = authService.getAuthenticatedUser();

        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);

        if ("municipio".equalsIgnoreCase(user.getTipo())) {
            return "autenticadoTesteMunicipio";
        } else {
            return "autenticadoTesteCidadao";
        }
    }

    //Processa o formulário registo de novo user
    @PostMapping(value = "/signUpAction")
    public String signUpAction(SignUpModel signUp) {
        User user = authService.register(signUp);
        return "login";
    }


    @GetMapping(value = "/recuperarPassword")
    public String recuperarPasswordPage() {
        return "recuperarPassword";
    }


    @PostMapping("/verificarUtilizadorAction")
    public String verificarUtilizadorAction(@RequestParam String username,
                                            @RequestParam String email,
                                            Model model) {

        boolean existe = authService.verificarDadosRecuperacao(username, email);

        if (existe) {
            // Se existir, levamos para a página de mudar a senha
            model.addAttribute("username", username);
            return "definirNovaPassword";
        } else {
            // Se não existir, volta para a página inicial de recuperação com erro
            model.addAttribute("error", "Os dados introduzidos não coincidem com os nossos registos.");
            return "recuperarPassword";
        }
    }

    @PostMapping("/atualizarPasswordAction")
    public String atualizarPasswordAction(@RequestParam String username,
                                          @RequestParam String novaPassword,
                                          @RequestParam String confirmarPassword,
                                          Model model) {

        if (!novaPassword.equals(confirmarPassword)) {
            model.addAttribute("error", "As passwords não coincidem.");
            model.addAttribute("username", username);
            return "definirNovaPassword";
        }

        try {
            authService.atualizarPassword(username, novaPassword);
            model.addAttribute("message", "Password alterada com sucesso! Faça login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao atualizar a password. Tente novamente.");
            return "recuperarPassword";
        }
    }


}