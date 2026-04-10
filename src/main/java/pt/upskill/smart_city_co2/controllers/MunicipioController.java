package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.upskill.smart_city_co2.dto.DTODashboardMunicipioService;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.services.MunicipioService;

import java.util.List;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    // Obtém o utilizador autenticado a partir do contexto de segurança do Spring Security.
    // Neste caso, espera-se que o utilizador autenticado seja um município.
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    // Carrega o dashboard principal do município com os indicadores agregados.
    @GetMapping("/dashboardMunicipio")
    @Transactional(readOnly = true)
    public String dashboardMunicipio(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "municipio/dashboardMunicipio";
        }

        adicionarRelatorioAoModel(model, municipio);
        return "municipio/dashboardMunicipio";
    }

    // Página inicial do município após autenticação.
    @GetMapping("/homeMunicipio")
    public String homeMunicipio(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/homeMunicipio";
    }

    // Mostra o formulário para redefinir a meta mensal de CO2.
    @GetMapping("/redefinirMeta")
    public String redefinirMeta(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "municipio/redefinirMeta";
        }

        return "municipio/redefinirMeta";
    }

    // Mostra o formulário para redefinir as taxas por nível de emissões.
    @GetMapping("/redefinirTaxa")
    public String redefinirTaxa(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "municipio/redefinirTaxa";
        }

        return "municipio/redefinirTaxa";
    }

    // Mostra a página de relatórios do município.
    @GetMapping("/relatoriosMunicipio")
    @Transactional(readOnly = true)
    public String relatoriosMunicipio(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "municipio/relatoriosMunicipio";
        }

        adicionarRelatorioAoModel(model, municipio);
        return "municipio/relatoriosMunicipio";
    }

    // Lista todos os cidadãos associados ao município autenticado.
    @GetMapping("/listaCidadaos")
    @Transactional(readOnly = true)
    public String listaDeCidadaos(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "municipio/listaCidadaos";
        }

        List<Cidadao> listaCidadaos = municipioService.buscarCidadaosDoMunicipio(municipio);
        model.addAttribute("listaCidadaos", listaCidadaos);
        return "municipio/listaCidadaos";
    }

    // Lista os veículos dos cidadãos do município.
    // A view recebe a lista de cidadãos e percorre os respetivos veículos.
    @GetMapping("/listaVeiculos")
    @Transactional(readOnly = true)
    public String listaDeVeiculos(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "municipio/listaVeiculos";
        }

        List<Cidadao> listaCidadaos = municipioService.buscarCidadaosDoMunicipio(municipio);
        model.addAttribute("listaCidadaos", listaCidadaos);
        return "municipio/listaVeiculos";
    }

    // Processa o formulário de atualização do objetivo de CO2.
    @PostMapping("/redefinirMetaAction")
    public String redefinirMetaAction(
            @RequestParam("novoObjetivo") double novoObjetivo,
            Model model) {

        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            model.addAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "municipio/redefinirMeta";
        }

        try {
            municipioService.atualizarObjetivoCo2(municipio.getUsername(), novoObjetivo);

            Municipio municipioAtualizado = municipioService.getUserM(municipio.getUsername());
            model.addAttribute("municipio", municipioAtualizado);
            model.addAttribute(
                    "mensagem",
                    "Objectivo CO₂ actualizado com sucesso para " + novoObjetivo + " kg/habitante/mês."
            );

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
        }

        return "municipio/redefinirMeta";
    }

    // Processa o formulário de atualização das taxas por nível.
    @PostMapping("/redefinirTaxaAction")
    public String redefinirTaxaAction(
            @RequestParam("taxaNivel1") double taxaNivel1,
            @RequestParam("taxaNivel2") double taxaNivel2,
            @RequestParam("taxaNivel3") double taxaNivel3,
            @RequestParam("taxaNivel4") double taxaNivel4,
            @RequestParam("taxaNivel5") double taxaNivel5,
            @RequestParam("taxaNivel6") double taxaNivel6,
            @RequestParam("taxaNivel7") double taxaNivel7,
            Model model) {

        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            model.addAttribute("erro", "Sessão expirada. Faça login novamente.");
            return "municipio/redefinirTaxa";
        }

        try {
            municipioService.atualizarTaxas(
                    municipio.getUsername(),
                    taxaNivel1,
                    taxaNivel2,
                    taxaNivel3,
                    taxaNivel4,
                    taxaNivel5,
                    taxaNivel6,
                    taxaNivel7
            );

            Municipio municipioAtualizado = municipioService.getUserM(municipio.getUsername());
            model.addAttribute("municipio", municipioAtualizado);
            model.addAttribute("mensagem", "Tabela de taxas atualizada com sucesso.");

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
        }

        return "municipio/redefinirTaxa";
    }

    // Página alternativa do dashboard, possivelmente usada para gráficos separados.
    @GetMapping("/dashboardMunicipio_b")
    public String dashboardGraficos(Model model) {
        Municipio municipio = adicionarContextoMunicipio(model);
        if (municipio == null) {
            return "municipio/dashboardMunicipio_b";
        }

        return "municipio/dashboardMunicipio_b";
    }

    // Método auxiliar para colocar no model o utilizador autenticado e o município correspondente.
    // Centraliza lógica repetida e garante consistência entre endpoints.
    private Municipio adicionarContextoMunicipio(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        if (user == null) {
            model.addAttribute("erro", "Não foi possível identificar o município autenticado.");
            return null;
        }

        Municipio municipio = municipioService.getUserM(user.getUsername());
        model.addAttribute("municipio", municipio);

        if (municipio == null) {
            model.addAttribute("erro", "Município não encontrado.");
            return null;
        }

        return municipio;
    }

    // Adiciona ao model todos os atributos calculados no relatório municipal,
    // para serem usados diretamente nas views Thymeleaf.
    private void adicionarRelatorioAoModel(Model model, Municipio municipio) {
        DTODashboardMunicipioService dados = municipioService.gerarRelatorioMunicipio(municipio);
        model.addAllAttributes(dados.toModelAttributes());
    }
}