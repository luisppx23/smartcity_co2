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
import pt.upskill.smart_city_co2.repositories.OwnershipRepository;
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
    public String adicionarVeiculo(@ModelAttribute AdicionarOwnershipModel form,
                                   Model model) {

        // 1. Obter o cidadão autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {

            return "redirect:/auth/login";
        }

        Cidadao cidadaoAutenticado = (Cidadao) authentication.getPrincipal();

        // 2. Buscar o cidadão do banco
        Cidadao cidadao = cidadaoRepository.findById(cidadaoAutenticado.getId()).orElse(null);

        if (cidadao == null) {

            model.addAttribute("error", "Cidadão não encontrado.");
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        // 3. Validar dados
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
            // 4. Criar a ownership usando o service (que já cria o Veiculo associado)
            Ownership novaOwnership = ownershipService.criarOwnership(form, cidadao);

            // 5. Adicionar à lista de veículos do cidadão
            List<Ownership> ownershipsAtuais = cidadao.getListaDeVeiculos();

            if (ownershipsAtuais == null) {
                ownershipsAtuais = new ArrayList<>();
            }

            ownershipsAtuais.add(novaOwnership);
            cidadao.setListaDeVeiculos(ownershipsAtuais);

            // 6. Salvar o cidadão atualizado
            cidadaoRepository.save(cidadao);

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        } catch (Exception e) {

            model.addAttribute("error", "Erro ao adicionar veículo: " + e.getMessage());
            model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
            return "cidadao/registoVeiculo";
        }

        return "redirect:/cidadao/dashboardCidadao";
    }
}