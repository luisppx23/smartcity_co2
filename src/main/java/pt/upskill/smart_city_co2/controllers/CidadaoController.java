package pt.upskill.smart_city_co2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.services.CidadaoService;
import pt.upskill.smart_city_co2.dto.DTODashboardCidadaoService;
import pt.upskill.smart_city_co2.dto.DTODashboardCidadaoService.DashboardDataDTO;
import pt.upskill.smart_city_co2.services.EmissaoCO2Service;
import pt.upskill.smart_city_co2.services.TaxaService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cidadao")
public class CidadaoController {

    @Autowired
    private CidadaoService cidadaoService;

    @Autowired
    private DTODashboardCidadaoService dashboardService;

    @Autowired
    private EmissaoCO2Service emissaoCO2Service;

    @Autowired
    private TaxaService taxaService;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }


    @GetMapping("/perfil")
    public String exibirPerfil(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";

        // Buscar o Cidadao completo para garantir que temos morada, contacto, etc.
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
        if (userLogado == null) return "redirect:/auth/login";

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

    @PostMapping("/perfil/apagar")
    public String apagarPerfil(HttpServletRequest request, HttpServletResponse response) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";

        Long id = userLogado.getId();
        cidadaoService.deleteCidadao(id);

        // Fazer logout forçado
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        return "redirect:/auth/login?contaApagada=true";
    }

    // Direciona para pagina de home do cidadão
    @GetMapping("/homeCidadao")
    public String homeCidadao(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        return "cidadao/homeCidadao";
    }

    // Direciona para pagina de registar veículo
    @GetMapping("/registoVeiculo")
    public String registarVeiculo(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        return "cidadao/registoVeiculo";
    }

    @GetMapping("/simularTaxa")
    public String simularTaxa(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        model.addAttribute("cidadao", cidadao);
        model.addAttribute("ownershipSelecionado", null);
        model.addAttribute("kmsSimulacao", 0);
        model.addAttribute("valorTaxa", null);
        return "cidadao/simularTaxa";
    }

    @PostMapping("/simularTaxa")
    public String calcularTaxa(@RequestParam Long veiculoId,
                               @RequestParam double kms,
                               Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        model.addAttribute("cidadao", cidadao);

        // Encontrar o ownership pelo veículo
        Ownership ownership = null;
        if (cidadao.getListaDeVeiculos() != null) {
            for (Ownership own : cidadao.getListaDeVeiculos()) {
                if (own.getVeiculo().getId().equals(veiculoId)) {
                    ownership = own;
                    break;
                }
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
        Municipio municipio = cidadao.getMunicipio();
        double valorTaxa = taxaService.simularTaxa(emissaoGPorKm, kms, municipio);

        // Adicionar dados ao modelo
        model.addAttribute("ownershipSelecionado", ownership);
        model.addAttribute("kmsSimulacao", kms);
        model.addAttribute("emissaoGPorKm", emissaoGPorKm);
        model.addAttribute("valorTaxa", valorTaxa);

        return "cidadao/simularTaxa";
    }

    @GetMapping("/listaVeiculos")
    public String listaVeiculos(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        model.addAttribute("cidadao", cidadao);
        return "cidadao/listaVeiculos";
    }

    @GetMapping("/dashboardCidadao")
    public String dashboardCompleto(Authentication authentication, Model model) {
        // Verificar autenticação
        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {
            return "redirect:/auth/login";
        }

        Cidadao cidadao = (Cidadao) authentication.getPrincipal();

        // Buscar o cidadão completo do banco de dados para garantir dados atualizados
        Cidadao cidadaoCompleto = cidadaoService.getUserC(cidadao.getId());

        // Preparar os dados do dashboard usando o service com @Transactional
        DashboardDataDTO dados = dashboardService.prepararDadosDashboard(cidadaoCompleto);

        // Adicionar todos os dados ao modelo
        model.addAttribute("listaVeiculos", dados.getListaVeiculos());
        model.addAttribute("matriculaPorVeiculo", dados.getMatriculaPorVeiculo());
        model.addAttribute("totalKmsPorVeiculo", dados.getTotalKmsPorVeiculo());
        model.addAttribute("totalCo2PorVeiculo", dados.getTotalCo2PorVeiculo());
        model.addAttribute("mediaKmsMensalCidadao", dados.getMediaKmsMensal());
        model.addAttribute("mediaCo2MensalCidadao", dados.getMediaCo2Mensal());
        model.addAttribute("kmsMesAtual", dados.getKmsMesAtual());
        model.addAttribute("co2MesAtual", dados.getCo2MesAtual());
        model.addAttribute("co2MesmoMesAnoPassado", dados.getCo2MesmoMesAnoPassado());
        model.addAttribute("listaRegistos", dados.getListaRegistos());
        model.addAttribute("totalKmsGeral", dados.getTotalKmsGeral());
        model.addAttribute("totalCo2Geral", dados.getTotalCo2Geral());
        model.addAttribute("combustiveisData", dados.getCombustiveisData());
        model.addAttribute("posicaoRankingPoluicao", dados.getPosicaoRankingPoluicao());
        model.addAttribute("numeroTotalCidadaos", dados.getNumeroTotalCidadaos());

        // Adicionar o user para a navbar
        model.addAttribute("user", cidadaoCompleto);

        return "cidadao/dashboardCidadao";
    }

    @GetMapping("/chartsCidadao")
    public String chartsCidadao(Authentication authentication, Model model) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {
            return "redirect:/auth/login";
        }

        Cidadao cidadao = (Cidadao) authentication.getPrincipal();
        Cidadao cidadaoCompleto = cidadaoService.getUserC(cidadao.getId());

        // Dados principais do dashboard
        DashboardDataDTO dados = dashboardService.prepararDadosCharts(cidadaoCompleto);

        // CALCULAR DADOS DO MÊS MAIS RECENTE POR VEÍCULO
        Map<Long, Double> kmsUltimoMes = new HashMap<>();
        Map<Long, Double> co2UltimoMes = new HashMap<>();
        Map<Long, Double> taxaUltimoMes = new HashMap<>();

        // Encontrar o mês mais recente com registos
        java.time.LocalDate dataMaisRecente = null;

        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms registo : ownership.getRegistosKms()) {
                        if (registo.getMes_ano() != null) {
                            java.time.LocalDate dataRegisto = new java.sql.Date(registo.getMes_ano().getTime()).toLocalDate();
                            if (dataMaisRecente == null || dataRegisto.isAfter(dataMaisRecente)) {
                                dataMaisRecente = dataRegisto;
                            }
                        }
                    }
                }
            }
        }

        // Se encontrou alguma data, usar essa data
        if (dataMaisRecente != null) {
            int mesRecente = dataMaisRecente.getMonthValue();
            int anoRecente = dataMaisRecente.getYear();

            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();
                if (veiculo == null) continue;

                double kmsMes = 0.0;
                double co2Mes = 0.0;

                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms registo : ownership.getRegistosKms()) {
                        if (registo.getMes_ano() != null) {
                            java.time.LocalDate dataRegisto = new java.sql.Date(registo.getMes_ano().getTime()).toLocalDate();
                            if (dataRegisto.getMonthValue() == mesRecente && dataRegisto.getYear() == anoRecente) {
                                kmsMes += registo.getKms_mes();
                                co2Mes += registo.getEmissaoEfetivaKg();
                            }
                        }
                    }
                }

                kmsUltimoMes.put(veiculo.getId(), kmsMes);
                co2UltimoMes.put(veiculo.getId(), co2Mes);
                taxaUltimoMes.put(veiculo.getId(), co2Mes * 0.25);
            }
        } else {
            // Se não houver registos, inicializar com zeros
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();
                if (veiculo != null) {
                    kmsUltimoMes.put(veiculo.getId(), 0.0);
                    co2UltimoMes.put(veiculo.getId(), 0.0);
                    taxaUltimoMes.put(veiculo.getId(), 0.0);
                }
            }
        }

        model.addAttribute("listaVeiculos", dados.getListaVeiculos());
        model.addAttribute("matriculaPorVeiculo", dados.getMatriculaPorVeiculo());
        model.addAttribute("totalKmsPorVeiculo", dados.getTotalKmsPorVeiculo());
        model.addAttribute("totalCo2PorVeiculo", dados.getTotalCo2PorVeiculo());
        model.addAttribute("totalKmsGeral", dados.getTotalKmsGeral());
        model.addAttribute("totalCo2Geral", dados.getTotalCo2Geral());
        model.addAttribute("listaRegistos", dados.getListaRegistos());
        model.addAttribute("combustiveisData", dados.getCombustiveisData());
        model.addAttribute("posicaoRankingPoluicao", dados.getPosicaoRankingPoluicao());
        model.addAttribute("numeroTotalCidadaos", dados.getNumeroTotalCidadaos());

        // Adicionar dados do último mês
        model.addAttribute("kmsUltimoMes", kmsUltimoMes);
        model.addAttribute("co2UltimoMes", co2UltimoMes);
        model.addAttribute("taxaUltimoMes", taxaUltimoMes);

        // Dados para gráficos
        Map<String, Double> emissoesPorMes = new LinkedHashMap<>();
        if (dados.getListaRegistos() != null) {
            for (Map<String, Object> registo : dados.getListaRegistos()) {
                String mes = (String) registo.get("mes");
                Double emissoes = (Double) registo.get("emissoes");
                if (mes != null && emissoes != null) {
                    emissoesPorMes.merge(mes, emissoes, Double::sum);
                }
            }
        }

        Map<String, Double> kmsPorMes = new LinkedHashMap<>();
        if (dados.getListaRegistos() != null) {
            for (Map<String, Object> registo : dados.getListaRegistos()) {
                String mes = (String) registo.get("mes");
                Double kms = (Double) registo.get("kms");
                if (mes != null && kms != null) {
                    kmsPorMes.merge(mes, kms, Double::sum);
                }
            }
        }

        model.addAttribute("meses", new ArrayList<>(emissoesPorMes.keySet()));
        model.addAttribute("emissoesPorMes", new ArrayList<>(emissoesPorMes.values()));
        model.addAttribute("kmsPorMes", new ArrayList<>(kmsPorMes.values()));

        List<Map<String, Object>> veiculosData = new ArrayList<>();
        for (Veiculo veiculo : dados.getListaVeiculos()) {
            Map<String, Object> veiculoMap = new HashMap<>();
            veiculoMap.put("id", veiculo.getId());
            veiculoMap.put("marca", veiculo.getMarca());
            veiculoMap.put("modelo", veiculo.getModelo());
            veiculoMap.put("emissoes", dados.getTotalCo2PorVeiculo().getOrDefault(veiculo.getId(), 0.0));
            veiculoMap.put("kms", dados.getTotalKmsPorVeiculo().getOrDefault(veiculo.getId(), 0.0));
            veiculosData.add(veiculoMap);
        }
        model.addAttribute("veiculosData", veiculosData);

        // Cores para os veículos
        List<String> coresVeiculos = Arrays.asList("#04523B", "#D4AF37", "#1f5a3d", "#8B6914", "#2b6a49", "#c49b28");
        model.addAttribute("coresVeiculos", coresVeiculos);

        model.addAttribute("user", cidadaoCompleto);

        return "cidadao/chartsCidadao";
    }


}