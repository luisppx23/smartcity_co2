package pt.upskill.smart_city_co2.controllers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.RegistarKmsModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.UserRepository;
import pt.upskill.smart_city_co2.services.RegistoKmsService;

import java.security.Principal;

@Controller
@RequestMapping("/auth/cidadao")
public class RegistoKmsController {

    @Autowired
    private RegistoKmsService registoKmsService;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registoKms")
    public String mostrarFormulario(Model model) {
        model.addAttribute("registoKmsModel", new RegistarKmsModel());
        return "cidadao/registoKms";
    }

    @PostMapping("/registoKmsAction")
    @Transactional
    public String registarKms(@RequestParam("kms") double kms, Authentication authentication) {
        System.out.println("====== 1. RECEBI O PEDIDO DE REGISTO ======");

        Cidadao cidadao = null;

        // Se o teu Principal já é o objeto Cidadao (como o log indica)
        if (authentication.getPrincipal() instanceof Cidadao) {
            cidadao = (Cidadao) authentication.getPrincipal();

            // IMPORTANTE: O objeto que vem da sessão pode estar "detached" (desconectado do Hibernate)
            // Vamos recarregá-lo da base de dados pelo ID para garantir que as listas funcionam
            cidadao = cidadaoRepository.findById(cidadao.getId()).orElse(null);
        }

        if (cidadao != null && kms > 0) {
            System.out.println("====== 2. Cidadão identificado: " + cidadao.getFirstName() + " ======");
            registoKmsService.salvarRegisto(cidadao, kms);
            System.out.println("====== 3. Guardado com sucesso! ======");
        } else {
            System.out.println("====== ERRO: Não foi possível identificar o cidadão ou KMS inválidos ======");
        }

        return "redirect:/auth/autenticado";
    }

    @GetMapping("/verRegistosKms")
    public String verHistorico(Authentication authentication, Model model) {
        // 1. Obter o cidadão do Principal
        Cidadao principalCidadao = (Cidadao) authentication.getPrincipal();

        // 2. FORÇAR o carregamento da DB para trazer a lista de registos atualizada
        Cidadao cidadaoCompleto = cidadaoRepository.findById(principalCidadao.getId()).orElse(null);

        if (cidadaoCompleto != null) {
            // 3. Passar a lista para o JSP
            model.addAttribute("listaRegistos", cidadaoCompleto.getListaDeRegistosKms());
            System.out.println("DEBUG: Enviando " + cidadaoCompleto.getListaDeRegistosKms().size() + " registos para o JSP.");
        }

        return "cidadao/historicoKms"; // nome do seu ficheiro JSP
    }
}
