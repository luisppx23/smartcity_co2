package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.models.AdicionarOwnershipModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.services.CidadaoService;
import pt.upskill.smart_city_co2.services.OwnershipService;
import pt.upskill.smart_city_co2.services.VeiculoService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth/cidadao")
public class NewOwnershipController {

    @Autowired
    private OwnershipService ownershipService;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private CidadaoService cidadaoService;   // <-- substitui o CidadaoRepository

    private Cidadao getAuthenticatedCidadao() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Cidadao) {
            return (Cidadao) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/registoVeiculo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("user", getAuthenticatedCidadao());
        model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
        return "cidadao/registoVeiculo";
    }

    @PostMapping("/adicionarVeiculoAction")
    @Transactional
    public String adicionarVeiculo(@ModelAttribute AdicionarOwnershipModel form, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {
            return "redirect:/auth/login";
        }

        Cidadao cidadaoAutenticado = (Cidadao) authentication.getPrincipal();

        // Buscar o cidadão completo através do service (em vez do repository)
        Cidadao cidadao = cidadaoService.getUserC(cidadaoAutenticado.getId());

        if (cidadao == null) {
            model.addAttribute("error", "Cidadão não encontrado.");
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        // Validações (inalteradas)
        if (form.getMatricula() == null || form.getMatricula().trim().isEmpty()) {
            model.addAttribute("error", "Matrícula é obrigatória.");
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        if (form.getModeloReferencia() == null || form.getModeloReferencia().trim().isEmpty()) {
            model.addAttribute("error", "Modelo do veículo é obrigatório.");
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        if (form.getAnoRegisto() == null || form.getAnoRegisto() < 1900 || form.getAnoRegisto() > 2026) {
            model.addAttribute("error", "Ano de registo inválido.");
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        try {
            // Criar a ownership (já cria o veículo associado)
            Ownership novaOwnership = ownershipService.criarOwnership(form, cidadao);

            // Adicionar à lista de veículos do cidadão
            List<Ownership> ownershipsAtuais = cidadao.getListaDeVeiculos();
            if (ownershipsAtuais == null) {
                ownershipsAtuais = new ArrayList<>();
            }
            ownershipsAtuais.add(novaOwnership);
            cidadao.setListaDeVeiculos(ownershipsAtuais);

            // Salvar o cidadão através do service
            cidadaoService.salvarAlteracoes(cidadao);

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao adicionar veículo: " + e.getMessage());
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        return "redirect:/cidadao/homeCidadao";
    }
}