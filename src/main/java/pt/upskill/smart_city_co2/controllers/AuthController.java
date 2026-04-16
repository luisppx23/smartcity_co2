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
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.SignUpModel;
import pt.upskill.smart_city_co2.services.AuthService;
import pt.upskill.smart_city_co2.services.MunicipioService;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    MunicipioService municipioService;

    // Exibe a página de registo de novo user
    @GetMapping(value = "/signup")
    public String signUpPage(Model model) {  // ← ADICIONAR Model COMO PARÂMETRO
        // Buscar todos os municípios
        List<Municipio> municipios = municipioService.getNomes(); // ou municipioService.getNome()
        model.addAttribute("municipios", municipios);  // ← ADICIONAR AO MODEL
        return "signup";
    }

    // Exibe a página de login
    @GetMapping(value = "/login")
    public String loginPage(@RequestParam(value = "contaApagada", required = false) boolean contaApagada,
                            @RequestParam(value = "error", required = false) String error,
                            Model model) {
        if (contaApagada) {
            model.addAttribute("message", "A sua conta foi apagada com sucesso.");
        }
        if (error != null) {
            model.addAttribute("erro", "Username ou password incorretos. Tente novamente.");
        }
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
                return "redirect:/cidadao/homeCidadao";
            } else if ("municipio".equals(user.getTipo())) {
                return "redirect:/municipio/homeMunicipio";
            }
        }

        return "redirect:/auth/login";
    }

    // Processa o formulário registo de novo user
    @PostMapping(value = "/signUpAction")
    public String signUpAction(SignUpModel signUp, Model model) {

        if (!signUp.getPassword().equals(signUp.getConfirmPassword())) {
            model.addAttribute("erro", "As passwords não coincidem.");
            model.addAttribute("signUpModel", signUp);
            return "redirect:/auth/signup";
        }


        try {
            authService.registar(signUp);
            model.addAttribute("message", "Conta criada com sucesso! Faça login.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("signUpModel", signUp);
            return "redirect:/auth/signup";
        }
    }

    // Redireciona o user autenticado para a página de recuperar password
    @GetMapping(value = "/recuperarPassword")
    public String recuperarPasswordPage() {
        return "recuperarPassword";
    }

    // Processa a verificação do usuário e envia código por email
    @PostMapping("/verificarUtilizadorAction")
    public String verificarUtilizadorAction(@RequestParam String username,
                                            @RequestParam String email,
                                            Model model) {

        boolean existe = authService.verificarDadosRecuperacao(username, email);

        if (existe) {
            try {
                // Gerar código e enviar email
                authService.gerarCodigoRecuperacao(username, email);
                model.addAttribute("username", username);
                model.addAttribute("message", "Código de verificação enviado para o seu email.");
                return "inserirCodigoRecuperacao";
            } catch (Exception e) {
                model.addAttribute("error", "Erro ao enviar código. Tente novamente.");
                return "recuperarPassword";
            }
        } else {
            model.addAttribute("error", "Os dados introduzidos não coincidem com os nossos registos.");
            return "recuperarPassword";
        }
    }

    // Exibe página para inserir o código
    @GetMapping("/inserirCodigoRecuperacao")
    public String inserirCodigoRecuperacaoPage(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "inserirCodigoRecuperacao";
    }

    // Processa a validação do código
    @PostMapping("/validarCodigoAction")
    public String validarCodigoAction(@RequestParam String username,
                                      @RequestParam String codigo,
                                      Model model) {
        try {
            authService.validarCodigoRecuperacao(username, codigo);
            model.addAttribute("username", username);
            model.addAttribute("codigo", codigo); // Passar código para o próximo passo
            return "definirNovaPassword";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "inserirCodigoRecuperacao";
        }
    }

    // Atualiza a password após validação do código
    @PostMapping("/atualizarPasswordAction")
    public String atualizarPasswordAction(@RequestParam String username,
                                          @RequestParam String codigo,
                                          @RequestParam String novaPassword,
                                          @RequestParam String confirmarPassword,
                                          Model model) {

        if (!novaPassword.equals(confirmarPassword)) {
            model.addAttribute("error", "As passwords não coincidem.");
            model.addAttribute("username", username);
            return "definirNovaPassword";
        }

        try {
            authService.atualizarPasswordComCodigo(username, codigo, novaPassword);
            model.addAttribute("message", "Password alterada com sucesso! Faça login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return "inserirCodigoRecuperacao";
        }
    }

}