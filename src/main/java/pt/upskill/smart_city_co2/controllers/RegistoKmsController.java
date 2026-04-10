package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.models.RegistarKmsModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.services.RegistoKmsService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cidadao")
public class RegistoKmsController {

    @Autowired
    private RegistoKmsService registoKmsService;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    /* Mostra o formulário onde o cidadão pode registar os quilómetros percorridos.
    É também enviado para a view o cidadão autenticado, para permitir mostrar
    os veículos associados e restante contexto do utilizador.*/
    @GetMapping("/registoKms")
    public String mostrarFormulario(Authentication authentication, Model model) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        model.addAttribute("registoKmsModel", new RegistarKmsModel());
        model.addAttribute("cidadao", cidadao);
        return "cidadao/registoKms";
    }

    // Processa o pedido de registo de kms submetido pelo cidadão.
    // Valida se os dados recebidos são consistentes e confirma se o veículo
    // pertence efetivamente ao utilizador autenticado.
    @PostMapping("/registoKmsAction")
    public String registarKms(@RequestParam double kms,
                              @RequestParam Long veiculoId,
                              Authentication authentication,
                              Model model) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);

        // Validação básica dos dados recebidos
        if (cidadao == null || kms <= 0) {
            model.addAttribute("erro", "Dados inválidos");
            return "redirect:/cidadao/registoKms";
        }

        // Procura o veículo selecionado dentro da lista de veículos do cidadão
        Ownership ownership = encontrarOwnership(cidadao, veiculoId);
        if (ownership == null) {
            model.addAttribute("erro", "Veículo não encontrado");
            return "redirect:/cidadao/registoKms";
        }

        try {
            // Delegação da lógica de negócio para a camada de service
            registoKmsService.salvarRegisto(cidadao, ownership, kms);
            model.addAttribute("mensagem", "Registo guardado com sucesso!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao guardar registo: " + e.getMessage());
        }

        return "redirect:/cidadao/registoKms?sucesso=true";
    }

    // Mostra o histórico completo de registos de quilómetros do cidadão,
    // juntamente com os totais acumulados de kms e emissões de CO2.
    @GetMapping("/verRegistosKms")
    public String verHistorico(Authentication authentication, Model model) {
        Cidadao cidadao = obterCidadaoAutenticado(authentication);
        model.addAttribute("cidadao", cidadao);

        List<RegistoKms> todosRegistos = new ArrayList<>();
        Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
        double totalKms = 0.0;
        double totalCo2 = 0.0;

        // Percorre todos os veículos do cidadão e agrega os respetivos registos
        if (cidadao != null && cidadao.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();

                // Guarda associação entre id do veículo e matrícula para apresentação na view
                if (veiculo != null) {
                    matriculaPorVeiculo.put(veiculo.getId(), ownership.getMatricula());
                }

                // Acumula todos os registos e respetivos valores totais
                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms r : ownership.getRegistosKms()) {
                        todosRegistos.add(r);
                        totalKms += r.getKms_mes();
                        totalCo2 += r.getEmissaoEfetivaKg();
                    }
                }
            }
        }

        model.addAttribute("listaRegistos", todosRegistos);
        model.addAttribute("matriculaPorVeiculo", matriculaPorVeiculo);
        model.addAttribute("totalKmsGeral", totalKms);
        model.addAttribute("totalCo2Geral", totalCo2);

        return "cidadao/historicoKms";
    }

    // Obtém o cidadão autenticado com base no principal devolvido pelo Spring Security.
    // É feita uma nova leitura da base de dados para garantir que a entidade vem atualizada.
    private Cidadao obterCidadaoAutenticado(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof Cidadao) {
            return cidadaoRepository.findById(((Cidadao) authentication.getPrincipal()).getId()).orElse(null);
        }
        return null;
    }

    // Procura a relação de ownership correspondente ao veículo selecionado.
    // Isto permite garantir que o registo é feito sobre um veículo pertencente ao cidadão.
    private Ownership encontrarOwnership(Cidadao cidadao, Long veiculoId) {
        if (cidadao.getListaDeVeiculos() != null) {
            return cidadao.getListaDeVeiculos().stream()
                    .filter(o -> o.getVeiculo() != null && o.getVeiculo().getId().equals(veiculoId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}