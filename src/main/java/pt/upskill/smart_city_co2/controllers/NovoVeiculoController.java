package pt.upskill.smart_city_co2.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.models.AdicionarVeiculoModel;
import pt.upskill.smart_city_co2.repositories.UserRepository;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;
import pt.upskill.smart_city_co2.services.AuthService;
import pt.upskill.smart_city_co2.services.VeiculoService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/auth/cidadao")
public class NovoVeiculoController {

    @Autowired
    private VeiculoService veiculoService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping("/registoVeiculo")
    public String mostrarFormulario(Model model) {
        List<Veiculo> lista = veiculoRepository.findAll();
        System.out.println("DEBUG CONTROLLER: Lista tem " + lista.size() + " itens.");

        model.addAttribute("veiculosBase", lista);
        return "cidadao/registoVeiculo";
    }

    @PostMapping("/adicionarVeiculoAction")
    public String adicionarVeiculo(@ModelAttribute AdicionarVeiculoModel form,
                                   Model model,
                                   Principal principal) { // <--- Adicionamos o Principal aqui

        // 1. Verificar se o principal existe (segurança extra)
        if (principal == null) {
            return "redirect:/auth/login";
        }

        // 2. Buscar o utilizador pelo username que está no Principal
        String username = principal.getName();
        User utilizador = userRepository.findByUsername(username).orElse(null);

        if (utilizador == null) {
            model.addAttribute("error", "Utilizador não encontrado.");
            return "cidadao/registoVeiculo";
        }

        // --- O resto do teu código continua igual ---
        String[] partes = form.getModeloReferencia().split(":");
        if (partes.length < 2) return "redirect:/auth/cidadao/registoVeiculo";

        Veiculo veiculoBase = veiculoService.getVeiculoByMarcaEModelo(partes[0], partes[1]);

        if (veiculoBase == null) {
            model.addAttribute("error", "Modelo de veículo inválido.");
            return "cidadao/registoVeiculo";
        }

        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMatricula(form.getMatricula().toUpperCase());
        novoVeiculo.setAnoRegisto(form.getAnoRegisto());
        novoVeiculo.setMarca(veiculoBase.getMarca());
        novoVeiculo.setModelo(veiculoBase.getModelo());
        novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
        novoVeiculo.setConsumo(veiculoBase.getConsumo());

        veiculoRepository.save(novoVeiculo);

        // Agora o utilizador já não é nulo!
        utilizador.getMeusVeiculos().add(novoVeiculo);
        userRepository.save(utilizador);

        return "redirect:/auth/autenticado";
    }


    }

