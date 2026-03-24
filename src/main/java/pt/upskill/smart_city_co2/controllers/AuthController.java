package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String autenticado(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("user", user);

            // Redirect based on user type
            if ("cidadao".equals(user.getTipo())) {
                return "autenticadoTesteCidadao";
            } else if ("municipio".equals(user.getTipo())) {
                return "autenticadoTesteMunicipio";
            }
        }

        return "redirect:/auth/login";
    }

    //Processa o formulário registo de novo user
    @PostMapping(value = "/signUpAction")
    public String signUpAction(SignUpModel signUp) {
        User user = authService.register(signUp);
        return "login";
    }

    // Redireciona o user autenticado para a página de recuperar password
    @GetMapping(value = "/recuperarPassword")
    public String recuperarPasswordPage() {
        return "recuperarPassword";
    }

    // Verifica se o utilizador existe e direciona para a alteracao de password
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

    // Atualiza a password em caso de sucesso (as duas passwords correspondem) ou redireciona para o recuperar password em caso de erro.
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

    // Direciona para pagina de dashboard do cidadão
    @GetMapping("/DashboardCidadao")
    public String dashboardCidadao() {
        return "Cidadao/DashboardCidadao";
    }

    // Direciona para pagina de home do cidadão
    @GetMapping("/HomeCidadao")
    public String homeCidadao() {
        return "Cidadao/HomeCidadao";
    }

    // Direciona para pagina de registar veículo
    @GetMapping("/RegistoVeiculo")
    public String registarVeiculo() {
        return "Cidadao/RegistoVeiculo";
    }

    // Direciona para pagina de dashboard do municipio
    @GetMapping("/DashboardMunicipio")
    public String dashboardMunicipio() {
        return "Municipio/DashboardMunicipio";
    }

    // Direciona para pagina de home do municipio
    @GetMapping("/HomeMunicipio")
    public String homeMunicipio() {
        return "Municipio/HomeMunicipio";
    }

    // Direciona para pagina de redefinir taxa
    @GetMapping("/RedefinirTaxa")
    public String redefinirTaxa() {
        return "Municipio/RedefinirTaxa";
    }

    // Direciona para pagina de exibicao de relatorios
    @GetMapping("/RelatoriosMunicipio")
    public String relatoriosMunicipio() {
        return "Municipio/RelatoriosMunicipio";
    }

}


