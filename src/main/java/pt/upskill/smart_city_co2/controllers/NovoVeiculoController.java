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
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.models.AdicionarVeiculoModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;
import pt.upskill.smart_city_co2.services.VeiculoService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth/cidadao")
public class NovoVeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

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

        List<Veiculo> lista = veiculoService.getAllVeiculos();
        System.out.println("DEBUG CONTROLLER: Lista tem " + lista.size() + " itens.");

        model.addAttribute("veiculosBase", lista);
        return "cidadao/registoVeiculo";
    }

    @PostMapping("/adicionarVeiculoAction")
    @Transactional
    public String adicionarVeiculo(@ModelAttribute AdicionarVeiculoModel form,
                                   Model model) {

        System.out.println("=== ADICIONANDO VEÍCULO ===");

        // 1. Obter o cidadão autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {
            System.out.println("ERRO: Usuário não autenticado como cidadão");
            return "redirect:/auth/login";
        }

        Cidadao cidadaoAutenticado = (Cidadao) authentication.getPrincipal();
        System.out.println("Cidadão autenticado ID: " + cidadaoAutenticado.getId());

        // 2. Buscar o cidadão do banco (como no RelacoesService)
        Cidadao cidadao = cidadaoRepository.findById(cidadaoAutenticado.getId()).orElse(null);

        if (cidadao == null) {
            System.out.println("ERRO: Cidadão não encontrado");
            model.addAttribute("error", "Cidadão não encontrado.");
            return "cidadao/registoVeiculo";
        }

        // 3. Validar dados
        if (form.getMatricula() == null || form.getMatricula().trim().isEmpty()) {
            model.addAttribute("error", "Matrícula é obrigatória.");
            return "cidadao/registoVeiculo";
        }

        if (form.getModeloReferencia() == null || form.getModeloReferencia().trim().isEmpty()) {
            model.addAttribute("error", "Modelo do veículo é obrigatório.");
            return "cidadao/registoVeiculo";
        }

        if (form.getAnoRegisto() == null || form.getAnoRegisto() < 1900 || form.getAnoRegisto() > 2026) {
            model.addAttribute("error", "Ano de registo inválido.");
            return "cidadao/registoVeiculo";
        }

        // 4. Processar modelo de referência
        String[] partes = form.getModeloReferencia().split(":");
        if (partes.length < 2) {
            model.addAttribute("error", "Formato de veículo inválido.");
            return "cidadao/registoVeiculo";
        }

        String marca = partes[0];
        String modelo = partes[1];

        // 5. Buscar veículo base
        Veiculo veiculoBase = veiculoService.getVeiculoByMarcaEModelo(marca, modelo);

        if (veiculoBase == null) {
            System.out.println("ERRO: Veículo base não encontrado");
            model.addAttribute("error", "Modelo de veículo inválido.");
            return "cidadao/registoVeiculo";
        }

        // 6. Criar novo veículo
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMatricula(form.getMatricula().toUpperCase().trim());
        novoVeiculo.setAnoRegisto(form.getAnoRegisto());
        novoVeiculo.setMarca(veiculoBase.getMarca());
        novoVeiculo.setModelo(veiculoBase.getModelo());
        novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
        novoVeiculo.setConsumo(veiculoBase.getConsumo());

        // 7. Salvar o veículo
        Veiculo veiculoSalvo = veiculoRepository.save(novoVeiculo);
        System.out.println("Veículo salvo com ID: " + veiculoSalvo.getId());

        // 8. Adicionar à lista de veículos do cidadão (como no RelacoesService)
        List<Veiculo> veiculosAtuais = cidadao.getListaDeVeiculos();

        if (veiculosAtuais == null) {
            veiculosAtuais = new ArrayList<>();
        }

        veiculosAtuais.add(veiculoSalvo);
        cidadao.setListaDeVeiculos(veiculosAtuais);

        // 9. Salvar o cidadão atualizado (igual ao RelacoesService)
        cidadaoRepository.save(cidadao);

        System.out.println("Veículo associado ao cidadão. Total de veículos: " + veiculosAtuais.size());

        return "redirect:/auth/autenticado";
    }
}