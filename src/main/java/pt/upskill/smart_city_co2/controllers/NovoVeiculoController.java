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
        // Envia todos os modelos base para o dropdown do JSP
        model.addAttribute("veiculosBase", veiculoService.getAllVeiculos());
        return "cidadao/registoVeiculo";
    }

    @PostMapping("/adicionarVeiculoAction")
    public String adicionarVeiculo(@ModelAttribute AdicionarVeiculoModel form) {
        User utilizador = authService.getAuthenticatedUser();
        if (utilizador == null) return "redirect:/auth/login";

        // Separar a Marca e o Modelo que vieram do Select (ex: "Tesla:Model 3")
        String[] partes = form.getModeloReferencia().split(":");
        if (partes.length < 2) return "redirect:/auth/cidadao/registoVeiculo";

        String marcaBase = partes[0];
        String modeloBase = partes[1];

        // Buscar os dados técnicos na "base de dados" do Service
        Veiculo veiculoBase = veiculoService.getVeiculoByMarcaEModelo(marcaBase, modeloBase);

        if (veiculoBase != null) {
            Veiculo novoVeiculo = new Veiculo();
            novoVeiculo.setMatricula(form.getMatricula().toUpperCase());
            novoVeiculo.setAnoRegisto(form.getAnoRegisto());

            // Copiar dados técnicos do veículo base
            novoVeiculo.setMarca(veiculoBase.getMarca());
            novoVeiculo.setModelo(veiculoBase.getModelo());
            novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
            novoVeiculo.setConsumo(veiculoBase.getConsumo());

            // Salvar primeiro o veículo (caso a matrícula ainda não exista na BD global)
            veiculoRepository.save(novoVeiculo);

            // Associar ao utilizador
            utilizador.getMeusVeiculos().add(novoVeiculo);
            userRepository.save(utilizador);
        }

        return "redirect:/auth/autenticado";
    }


    }

