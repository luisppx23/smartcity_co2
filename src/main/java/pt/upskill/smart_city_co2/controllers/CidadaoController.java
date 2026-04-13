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
import pt.upskill.smart_city_co2.dto.DTODashboardCidadaoService;
import pt.upskill.smart_city_co2.dto.DTODashboardCidadaoService.DashboardDataDTO;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.services.CidadaoService;
import pt.upskill.smart_city_co2.services.EmissaoCO2Service;
import pt.upskill.smart_city_co2.services.TaxaService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        if (userLogado == null) return "redirect:/";

        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        return "cidadao/perfil";
    }

    @GetMapping("/perfil/editar")
    public String exibirFormEditar(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";

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
        if (userLogado == null) return "redirect:/";

        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());

        cidadao.setEmail(email);
        cidadao.setContacto(contacto);
        cidadao.setMorada(morada);
        cidadao.setFirstName(firstName);
        cidadao.setLastName(lastName);

        if (foto != null && !foto.isEmpty()) {
            try {
                String base64Image = java.util.Base64.getEncoder().encodeToString(foto.getBytes());
                cidadao.setFotoUrl("data:" + foto.getContentType() + ";base64," + base64Image);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }

        cidadaoService.salvarAlteracoes(cidadao);

        Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                cidadao, oldAuth.getCredentials(), oldAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/cidadao/perfil?sucesso=true";
    }

    @PostMapping("/perfil/apagar")
    public String apagarPerfil(HttpServletRequest request, HttpServletResponse response) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";

        Long id = userLogado.getId();
        cidadaoService.deleteCidadao(id);

        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        return "redirect:/auth/login?contaApagada=true";
    }

    @GetMapping("/homeCidadao")
    public String homeCidadao(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        return "cidadao/homeCidadao";
    }

    @GetMapping("/registoVeiculo")
    public String registarVeiculo(Model model) {
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";
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

        int anoReferencia = java.time.LocalDate.now().getYear();
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);

        Municipio municipio = cidadao.getMunicipio();
        double valorTaxa = taxaService.simularTaxa(emissaoGPorKm, kms, municipio);

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
        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {
            return "redirect:/auth/login";
        }

        Cidadao cidadao = (Cidadao) authentication.getPrincipal();
        Cidadao cidadaoCompleto = cidadaoService.getUserC(cidadao.getId());

        DashboardDataDTO dados = dashboardService.prepararDadosDashboard(cidadaoCompleto);

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

        Map<Long, Double> kmsUltimoMes = new HashMap<>();
        Map<Long, Double> co2UltimoMes = new HashMap<>();
        Map<Long, Double> taxaUltimoMes = new HashMap<>();
        Map<Long, Double> totalTaxaPorVeiculo = new LinkedHashMap<>();

        LocalDate dataMaisRecente = null;

        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms registo : ownership.getRegistosKms()) {
                        if (registo.getMes_ano() != null) {
                            LocalDate dataRegisto = registo.getMes_ano()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();

                            if (dataMaisRecente == null || dataRegisto.isAfter(dataMaisRecente)) {
                                dataMaisRecente = dataRegisto;
                            }
                        }
                    }
                }
            }
        }

        SortedSet<YearMonth> mesesOrdenadosSet = new TreeSet<>();
        Map<YearMonth, Double> totalCo2MensalMap = new LinkedHashMap<>();
        Map<YearMonth, Double> totalKmsMensalMap = new LinkedHashMap<>();
        Map<YearMonth, Double> totalTaxaMensalMap = new LinkedHashMap<>();

        Map<Long, Map<YearMonth, Double>> co2MensalPorVeiculoMap = new LinkedHashMap<>();
        Map<Long, Map<YearMonth, Double>> kmsMensalPorVeiculoMap = new LinkedHashMap<>();
        Map<Long, Map<YearMonth, Double>> taxaMensalPorVeiculoMap = new LinkedHashMap<>();

        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();
                if (veiculo == null) continue;

                Long veiculoId = veiculo.getId();

                co2MensalPorVeiculoMap.putIfAbsent(veiculoId, new LinkedHashMap<>());
                kmsMensalPorVeiculoMap.putIfAbsent(veiculoId, new LinkedHashMap<>());
                taxaMensalPorVeiculoMap.putIfAbsent(veiculoId, new LinkedHashMap<>());

                double kmsMesRecente = 0.0;
                double co2MesRecente = 0.0;
                double taxaMesRecente = 0.0;
                double totalTaxa = 0.0;

                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms registo : ownership.getRegistosKms()) {
                        if (registo.getMes_ano() == null) continue;

                        LocalDate dataRegisto = registo.getMes_ano()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        YearMonth ym = YearMonth.from(dataRegisto);
                        mesesOrdenadosSet.add(ym);

                        double kms = registo.getKms_mes();
                        double co2 = registo.getEmissaoEfetivaKg();
                        double taxa = calcularTaxaRegisto(ownership, registo, cidadaoCompleto.getMunicipio());

                        totalCo2MensalMap.merge(ym, co2, Double::sum);
                        totalKmsMensalMap.merge(ym, kms, Double::sum);
                        totalTaxaMensalMap.merge(ym, taxa, Double::sum);

                        co2MensalPorVeiculoMap.get(veiculoId).merge(ym, co2, Double::sum);
                        kmsMensalPorVeiculoMap.get(veiculoId).merge(ym, kms, Double::sum);
                        taxaMensalPorVeiculoMap.get(veiculoId).merge(ym, taxa, Double::sum);

                        totalTaxa += taxa;

                        if (dataMaisRecente != null &&
                                dataRegisto.getMonthValue() == dataMaisRecente.getMonthValue() &&
                                dataRegisto.getYear() == dataMaisRecente.getYear()) {
                            kmsMesRecente += kms;
                            co2MesRecente += co2;
                            taxaMesRecente += taxa;
                        }
                    }
                }

                kmsUltimoMes.put(veiculoId, kmsMesRecente);
                co2UltimoMes.put(veiculoId, co2MesRecente);
                taxaUltimoMes.put(veiculoId, taxaMesRecente);
                totalTaxaPorVeiculo.put(veiculoId, totalTaxa);
            }
        }

        List<String> meses = new ArrayList<>();
        List<Double> emissoesPorMes = new ArrayList<>();
        List<Double> kmsPorMes = new ArrayList<>();
        List<Double> taxaPorMes = new ArrayList<>();

        Map<Long, List<Double>> co2MensalPorVeiculo = new LinkedHashMap<>();
        Map<Long, List<Double>> kmsMensalPorVeiculo = new LinkedHashMap<>();
        Map<Long, List<Double>> taxaMensalPorVeiculo = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", new Locale("pt", "PT"));

        for (YearMonth ym : mesesOrdenadosSet) {
            meses.add(ym.format(formatter));
            emissoesPorMes.add(totalCo2MensalMap.getOrDefault(ym, 0.0));
            kmsPorMes.add(totalKmsMensalMap.getOrDefault(ym, 0.0));
            taxaPorMes.add(totalTaxaMensalMap.getOrDefault(ym, 0.0));
        }

        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();
                if (veiculo == null) continue;

                Long veiculoId = veiculo.getId();

                List<Double> listaCo2 = new ArrayList<>();
                List<Double> listaKms = new ArrayList<>();
                List<Double> listaTaxa = new ArrayList<>();

                Map<YearMonth, Double> mapaCo2 = co2MensalPorVeiculoMap.getOrDefault(veiculoId, new LinkedHashMap<>());
                Map<YearMonth, Double> mapaKms = kmsMensalPorVeiculoMap.getOrDefault(veiculoId, new LinkedHashMap<>());
                Map<YearMonth, Double> mapaTaxa = taxaMensalPorVeiculoMap.getOrDefault(veiculoId, new LinkedHashMap<>());

                for (YearMonth ym : mesesOrdenadosSet) {
                    listaCo2.add(mapaCo2.getOrDefault(ym, 0.0));
                    listaKms.add(mapaKms.getOrDefault(ym, 0.0));
                    listaTaxa.add(mapaTaxa.getOrDefault(ym, 0.0));
                }

                co2MensalPorVeiculo.put(veiculoId, listaCo2);
                kmsMensalPorVeiculo.put(veiculoId, listaKms);
                taxaMensalPorVeiculo.put(veiculoId, listaTaxa);
            }
        }

        double totalTaxaGeral = totalTaxaPorVeiculo.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        model.addAttribute("kmsUltimoMes", kmsUltimoMes);
        model.addAttribute("co2UltimoMes", co2UltimoMes);
        model.addAttribute("taxaUltimoMes", taxaUltimoMes);
        model.addAttribute("totalTaxaPorVeiculo", totalTaxaPorVeiculo);
        model.addAttribute("totalTaxaGeral", totalTaxaGeral);

        model.addAttribute("meses", meses);
        model.addAttribute("emissoesPorMes", emissoesPorMes);
        model.addAttribute("kmsPorMes", kmsPorMes);
        model.addAttribute("taxaPorMes", taxaPorMes);

        model.addAttribute("co2MensalPorVeiculo", co2MensalPorVeiculo);
        model.addAttribute("kmsMensalPorVeiculo", kmsMensalPorVeiculo);
        model.addAttribute("taxaMensalPorVeiculo", taxaMensalPorVeiculo);

        List<String> coresVeiculos = Arrays.asList(
                "#04523B", "#D4AF37", "#1f5a3d", "#8B6914", "#2b6a49", "#c49b28"
        );
        model.addAttribute("coresVeiculos", coresVeiculos);

        model.addAttribute("user", cidadaoCompleto);

        return "cidadao/dashboardCidadao";
    }

    private double calcularTaxaRegisto(Ownership ownership, RegistoKms registo, Municipio municipio) {
        if (ownership == null || registo == null || municipio == null) {
            return 0.0;
        }

        int anoReferencia = LocalDate.now().getYear();
        if (registo.getMes_ano() != null) {
            anoReferencia = registo.getMes_ano()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .getYear();
        }

        double kms = registo.getKms_mes();
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);

        return taxaService.simularTaxa(emissaoGPorKm, kms, municipio);
    }
}