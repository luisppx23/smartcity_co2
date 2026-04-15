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
    private CidadaoRepository cidadaoRepository;

    // Obtém o cidadão autenticado a partir do contexto de segurança
    private Cidadao getAuthenticatedCidadao() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Cidadao) {
            return (Cidadao) authentication.getPrincipal();
        }

        return null;
    }

    @GetMapping("/registoVeiculo")
    public String mostrarFormulario(Model model) {
        // Envia para a página o utilizador autenticado e a lista base de veículos
        model.addAttribute("user", getAuthenticatedCidadao());
        model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());

        return "cidadao/registoVeiculo";
    }

    @PostMapping("/adicionarVeiculoAction")
    @Transactional
    public String adicionarVeiculo(@ModelAttribute AdicionarOwnershipModel form, Model model) {

        // 1. Obter o cidadão autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {
            return "redirect:/auth/login";
        }

        Cidadao cidadaoAutenticado = (Cidadao) authentication.getPrincipal();

        // 2. Buscar o cidadão real da base de dados
        // Isto garante que temos a entidade completa e gerida pelo Hibernate
        Cidadao cidadao = cidadaoRepository.findById(cidadaoAutenticado.getId()).orElse(null);

        if (cidadao == null) {
            model.addAttribute("error", "Cidadão não encontrado.");
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        // 3. Validar os dados do formulário antes de criar o veículo/ownership
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
            // 4. Criar a ownership através do service
            // Este passo já trata da criação do veículo associado
            Ownership novaOwnership = ownershipService.criarOwnership(form, cidadao);

            // 5. Adicionar a nova ownership à lista de veículos do cidadão
            List<Ownership> ownershipsAtuais = cidadao.getListaDeVeiculos();

            if (ownershipsAtuais == null) {
                ownershipsAtuais = new ArrayList<>();
            }

            ownershipsAtuais.add(novaOwnership);
            cidadao.setListaDeVeiculos(ownershipsAtuais);

            // 6. Guardar o cidadão já com a nova associação
            cidadaoRepository.save(cidadao);

        } catch (IllegalArgumentException e) {
            // Erros de validação/controlados
            model.addAttribute("error", e.getMessage());
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";

        } catch (Exception e) {
            // Erros inesperados
            model.addAttribute("error", "Erro ao adicionar veículo: " + e.getMessage());
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        // Se tudo correr bem, volta para a home do cidadão
        return "redirect:/cidadao/homeCidadao";
    }
}