package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.models.SimularTaxaModel;
import pt.upskill.smart_city_co2.services.CidadaoService;
import pt.upskill.smart_city_co2.services.EmissaoCO2Service;
import pt.upskill.smart_city_co2.services.TaxaService;

import java.io.IOException;

@Controller
@RequestMapping("/cidadao")
public class CidadaoController {

    @Autowired
    CidadaoService cidadaoService;

    @Autowired
    EmissaoCO2Service emissaoCO2Service;

    @Autowired
    TaxaService taxaService;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    // Direciona para pagina de dashboard do cidadão
    @GetMapping("/perfil")
    public String exibirPerfil(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";

        // Vamos buscar o Cidadao completo para garantir que temos morada, contacto, etc.
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        return "cidadao/perfil";
    }

    @GetMapping("/perfil/editar")
    public String exibirFormEditar(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";

        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        return "cidadao/editarPerfil";
    }

    @PostMapping("/perfil/salvar")
    public String salvarPerfil(@RequestParam String email,
                               @RequestParam String contacto,
                               @RequestParam String morada,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam(value = "fotoFicheiro", required = false) MultipartFile foto) {

        User userLogado = getAuthenticatedUser();
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());

        // Atualiza os dados
        cidadao.setEmail(email);
        cidadao.setContacto(contacto);
        cidadao.setMorada(morada);
        cidadao.setFirstName(firstName);
        cidadao.setLastName(lastName);

        // Lógica da Foto em Base64
        if (foto != null && !foto.isEmpty()) {
            try {
                String base64Image = java.util.Base64.getEncoder().encodeToString(foto.getBytes());
                cidadao.setFotoUrl("data:" + foto.getContentType() + ";base64," + base64Image);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        cidadaoService.salvarAlteracoes(cidadao);

        // Atualiza a sessão para a Navbar mostrar o novo nome/foto
        Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                cidadao, oldAuth.getCredentials(), oldAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/cidadao/perfil?sucesso=true";
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

    @GetMapping("/simularTaxa")
    public String simularTaxa(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        Cidadao cidadao = cidadaoService.getUserC(user.getId());
        model.addAttribute("cidadao", cidadao);

        // Adicionar objetos vazios para o formulário
        model.addAttribute("ownershipSelecionado", null);
        model.addAttribute("kmsSimulacao", 0);
        model.addAttribute("valorTaxa", null);

        return "cidadao/simularTaxa";
    }

    @PostMapping("/simularTaxa")
    public String calcularTaxa(@RequestParam Long veiculoId,
                               @RequestParam double kms,
                               Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        Cidadao cidadao = cidadaoService.getUserC(user.getId());
        model.addAttribute("cidadao", cidadao);

        // Encontrar o ownership pelo veículo
        Ownership ownership = null;
        for (Ownership own : cidadao.getListaDeVeiculos()) {
            if (own.getVeiculo().getId().equals(veiculoId)) {
                ownership = own;
                break;
            }
        }

        if (ownership == null) {
            model.addAttribute("erro", "Veículo não encontrado");
            return "cidadao/simularTaxa";
        }

        // Calcular emissão por km
        int anoReferencia = java.time.LocalDate.now().getYear();
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);

        // Simular taxa
        double valorTaxa = taxaService.simularTaxa(emissaoGPorKm, kms);

        // Adicionar dados ao modelo
        model.addAttribute("ownershipSelecionado", ownership);
        model.addAttribute("kmsSimulacao", kms);
        model.addAttribute("emissaoGPorKm", emissaoGPorKm);
        model.addAttribute("valorTaxa", valorTaxa);

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