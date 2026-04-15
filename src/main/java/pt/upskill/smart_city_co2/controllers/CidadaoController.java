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

    // Método auxiliar para obter o utilizador autenticado na sessão atual
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se existe autenticação e se o principal é um User
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }

        // Se não existir utilizador autenticado devolve null
        return null;
    }

    @GetMapping("/perfil")
    public String exibirPerfil(Model model) {
        // Obtém o utilizador autenticado
        User userLogado = getAuthenticatedUser();

        // Se não houver sessão ativa, redireciona para a página inicial
        if (userLogado == null) return "redirect:/";

        // Vai buscar o cidadão completo através do id do utilizador
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());

        // Envia o cidadão para a view
        model.addAttribute("user", cidadao);

        return "cidadao/perfil";
    }

    @GetMapping("/perfil/editar")
    public String exibirFormEditar(Model model) {
        // Obtém o utilizador autenticado
        User userLogado = getAuthenticatedUser();

        // Se não estiver autenticado, volta para a home
        if (userLogado == null) return "redirect:/";

        // Carrega os dados atuais do cidadão para preencher o formulário
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

        // Obtém o utilizador autenticado
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";

        // Vai buscar o cidadão correspondente ao utilizador logado
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());

        // Atualiza os campos editáveis do perfil
        cidadao.setEmail(email);
        cidadao.setContacto(contacto);
        cidadao.setMorada(morada);
        cidadao.setFirstName(firstName);
        cidadao.setLastName(lastName);

        // Se foi enviada uma nova fotografia, converte-a para Base64 e guarda no campo fotoUrl
        if (foto != null && !foto.isEmpty()) {
            try {
                String base64Image = java.util.Base64.getEncoder().encodeToString(foto.getBytes());
                cidadao.setFotoUrl("data:" + foto.getContentType() + ";base64," + base64Image);
            } catch (java.io.IOException e) {
                // Em caso de erro na leitura da imagem, imprime o erro na consola
                e.printStackTrace();
            }
        }

        // Guarda as alterações na base de dados
        cidadaoService.salvarAlteracoes(cidadao);

        // Atualiza também o objeto de autenticação para refletir os novos dados em sessão
        Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                cidadao,
                oldAuth.getCredentials(),
                oldAuth.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        // Redireciona para o perfil com mensagem de sucesso
        return "redirect:/cidadao/perfil?sucesso=true";
    }

    @PostMapping("/perfil/apagar")
    public String apagarPerfil(HttpServletRequest request, HttpServletResponse response) {
        // Obtém o utilizador autenticado
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";

        Long id = userLogado.getId();

        // Remove o cidadão da base de dados
        cidadaoService.deleteCidadao(id);

        // Limpa a autenticação e invalida a sessão atual
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        // Redireciona para o login com indicação de conta apagada
        return "redirect:/auth/login?contaApagada=true";
    }

    @GetMapping("/homeCidadao")
    public String homeCidadao(Model model) {
        // Obtém o utilizador autenticado
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";

        // Carrega o cidadão para mostrar informação personalizada na home
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);

        return "cidadao/homeCidadao";
    }

    @GetMapping("/registoVeiculo")
    public String registarVeiculo(Model model) {
        // Obtém o utilizador autenticado
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/";

        // Envia os dados do cidadão para a página de registo de veículo
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);

        return "cidadao/registoVeiculo";
    }

    @GetMapping("/simularTaxa")
    public String simularTaxa(Model model) {
        // Verifica se o utilizador está autenticado
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";

        // Carrega o cidadão completo
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());

        // Atributos base necessários para a página de simulação
        model.addAttribute("user", cidadao);
        model.addAttribute("cidadao", cidadao);
        model.addAttribute("ownershipSelecionado", null);
        model.addAttribute("kmsSimulacao", 0);
        model.addAttribute("valorTaxa", null);

        return "cidadao/simularTaxa";
    }

    @PostMapping("/simularTaxa")
    public String calcularTaxa(@RequestParam Long veiculoId, @RequestParam double kms, Model model) {
        // Garante que existe utilizador autenticado
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";

        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());

        model.addAttribute("user", cidadao);
        model.addAttribute("cidadao", cidadao);

        Ownership ownership = null;

        // Procura o veículo selecionado na lista de veículos do cidadão
        if (cidadao.getListaDeVeiculos() != null) {
            for (Ownership own : cidadao.getListaDeVeiculos()) {
                if (own.getVeiculo().getId().equals(veiculoId)) {
                    ownership = own;
                    break;
                }
            }
        }

        // Se não encontrar o veículo, devolve erro na mesma página
        if (ownership == null) {
            model.addAttribute("erro", "Veículo não encontrado");
            return "cidadao/simularTaxa";
        }

        // Usa o ano atual como referência para calcular emissão por km
        int anoReferencia = java.time.LocalDate.now().getYear();

        // Calcula a emissão em gramas por km para este veículo
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);

        // Obtém o município do cidadão, pois a taxa depende do município
        Municipio municipio = cidadao.getMunicipio();

        // Simula o valor da taxa com base na emissão e kms indicados
        double valorTaxa = taxaService.simularTaxa(emissaoGPorKm, kms, municipio);

        // Envia os resultados para a view
        model.addAttribute("ownershipSelecionado", ownership);
        model.addAttribute("kmsSimulacao", kms);
        model.addAttribute("emissaoGPorKm", emissaoGPorKm);
        model.addAttribute("valorTaxa", valorTaxa);

        return "cidadao/simularTaxa";
    }

    @GetMapping("/listaVeiculos")
    public String listaVeiculos(Model model) {
        // Verifica autenticação
        User userLogado = getAuthenticatedUser();
        if (userLogado == null) return "redirect:/auth/login";

        // Carrega o cidadão e envia os dados para a listagem
        Cidadao cidadao = cidadaoService.getUserC(userLogado.getId());
        model.addAttribute("user", cidadao);
        model.addAttribute("cidadao", cidadao);

        return "cidadao/listaVeiculos";
    }

    @GetMapping("/dashboardCidadao")
    public String dashboardCompleto(Authentication authentication, Model model) {
        // Garante que o utilizador autenticado é um cidadão
        if (authentication == null || !(authentication.getPrincipal() instanceof Cidadao)) {
            return "redirect:/auth/login";
        }

        // Obtém o cidadão da autenticação e depois recarrega-o completo da base de dados
        Cidadao cidadao = (Cidadao) authentication.getPrincipal();
        Cidadao cidadaoCompleto = cidadaoService.getUserC(cidadao.getId());

        // Usa o serviço DTO para preparar dados já agregados do dashboard
        DashboardDataDTO dados = dashboardService.prepararDadosDashboard(cidadaoCompleto);

        // Dados gerais já preparados no DTO
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

        // Mapas para guardar valores do mês mais recente por veículo
        Map<Long, Double> kmsUltimoMes = new HashMap<>();
        Map<Long, Double> co2UltimoMes = new HashMap<>();
        Map<Long, Double> taxaUltimoMes = new HashMap<>();

        // Guarda a taxa total acumulada por veículo
        Map<Long, Double> totalTaxaPorVeiculo = new LinkedHashMap<>();

        // Vai descobrir a data mais recente presente nos registos
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

                            // Guarda a maior data encontrada
                            if (dataMaisRecente == null || dataRegisto.isAfter(dataMaisRecente)) {
                                dataMaisRecente = dataRegisto;
                            }
                        }
                    }
                }
            }
        }

        // Estruturas para organizar os dados por mês
        SortedSet<YearMonth> mesesOrdenadosSet = new TreeSet<>();
        Map<YearMonth, Double> totalCo2MensalMap = new LinkedHashMap<>();
        Map<YearMonth, Double> totalKmsMensalMap = new LinkedHashMap<>();
        Map<YearMonth, Double> totalTaxaMensalMap = new LinkedHashMap<>();

        // Mapas mensais por veículo
        Map<Long, Map<YearMonth, Double>> co2MensalPorVeiculoMap = new LinkedHashMap<>();
        Map<Long, Map<YearMonth, Double>> kmsMensalPorVeiculoMap = new LinkedHashMap<>();
        Map<Long, Map<YearMonth, Double>> taxaMensalPorVeiculoMap = new LinkedHashMap<>();

        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();

                // Se por algum motivo não houver veículo associado, ignora
                if (veiculo == null) continue;

                Long veiculoId = veiculo.getId();

                // Inicializa os mapas desse veículo se ainda não existirem
                co2MensalPorVeiculoMap.putIfAbsent(veiculoId, new LinkedHashMap<>());
                kmsMensalPorVeiculoMap.putIfAbsent(veiculoId, new LinkedHashMap<>());
                taxaMensalPorVeiculoMap.putIfAbsent(veiculoId, new LinkedHashMap<>());

                // Acumuladores do mês mais recente
                double kmsMesRecente = 0.0;
                double co2MesRecente = 0.0;
                double taxaMesRecente = 0.0;

                // Acumulador da taxa total desse veículo
                double totalTaxa = 0.0;

                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms registo : ownership.getRegistosKms()) {
                        if (registo.getMes_ano() == null) continue;

                        // Converte a data do registo para LocalDate e depois YearMonth
                        LocalDate dataRegisto = registo.getMes_ano()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        YearMonth ym = YearMonth.from(dataRegisto);
                        mesesOrdenadosSet.add(ym);

                        // Valores do registo
                        double kms = registo.getKms_mes();
                        double co2 = registo.getEmissaoEfetivaKg();

                        // Calcula a taxa desse registo com base no município
                        double taxa = calcularTaxaRegisto(ownership, registo, cidadaoCompleto.getMunicipio());

                        // Soma para os totais globais por mês
                        totalCo2MensalMap.merge(ym, co2, Double::sum);
                        totalKmsMensalMap.merge(ym, kms, Double::sum);
                        totalTaxaMensalMap.merge(ym, taxa, Double::sum);

                        // Soma para os totais desse veículo por mês
                        co2MensalPorVeiculoMap.get(veiculoId).merge(ym, co2, Double::sum);
                        kmsMensalPorVeiculoMap.get(veiculoId).merge(ym, kms, Double::sum);
                        taxaMensalPorVeiculoMap.get(veiculoId).merge(ym, taxa, Double::sum);

                        totalTaxa += taxa;

                        // Se o registo pertencer ao mês mais recente, acumula para os cartões resumo
                        if (dataMaisRecente != null &&
                                dataRegisto.getMonthValue() == dataMaisRecente.getMonthValue() &&
                                dataRegisto.getYear() == dataMaisRecente.getYear()) {

                            kmsMesRecente += kms;
                            co2MesRecente += co2;
                            taxaMesRecente += taxa;
                        }
                    }
                }

                // Guarda os resumos do último mês por veículo
                kmsUltimoMes.put(veiculoId, kmsMesRecente);
                co2UltimoMes.put(veiculoId, co2MesRecente);
                taxaUltimoMes.put(veiculoId, taxaMesRecente);

                // Guarda a taxa total desse veículo
                totalTaxaPorVeiculo.put(veiculoId, totalTaxa);
            }
        }

        // Listas finais para os gráficos globais
        List<String> meses = new ArrayList<>();
        List<Double> emissoesPorMes = new ArrayList<>();
        List<Double> kmsPorMes = new ArrayList<>();
        List<Double> taxaPorMes = new ArrayList<>();

        // Listas finais para gráficos por veículo
        Map<Long, List<Double>> co2MensalPorVeiculo = new LinkedHashMap<>();
        Map<Long, List<Double>> kmsMensalPorVeiculo = new LinkedHashMap<>();
        Map<Long, List<Double>> taxaMensalPorVeiculo = new LinkedHashMap<>();

        // Formata os meses em português
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yyyy", new Locale("pt", "PT"));

        // Converte os mapas mensais globais em listas ordenadas para o front-end
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

                // Para cada mês ordenado, coloca o valor correspondente ou 0 se não houver registo
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

        // Soma de todas as taxas de todos os veículos
        double totalTaxaGeral = totalTaxaPorVeiculo.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // Envia todos os dados calculados manualmente para a view
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

        // Lista de cores usada nos gráficos dos veículos
        List<String> coresVeiculos = Arrays.asList(
                "#04523B", "#D4AF37", "#1f5a3d", "#8B6914", "#2b6a49", "#c49b28"
        );
        model.addAttribute("coresVeiculos", coresVeiculos);

        // Utilizador atual para a navbar/perfil
        model.addAttribute("user", cidadaoCompleto);

        return "cidadao/dashboardCidadao";
    }

    // Método auxiliar para calcular a taxa associada a um registo de kms
    private double calcularTaxaRegisto(Ownership ownership, RegistoKms registo, Municipio municipio) {
        // Se faltar informação essencial, devolve 0
        if (ownership == null || registo == null || municipio == null) {
            return 0.0;
        }

        // Por defeito usa o ano atual
        int anoReferencia = LocalDate.now().getYear();

        // Se o registo tiver data, usa o ano desse registo
        if (registo.getMes_ano() != null) {
            anoReferencia = registo.getMes_ano()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .getYear();
        }

        // Obtém os kms do registo
        double kms = registo.getKms_mes();

        // Calcula a emissão em g/km para o veículo nesse ano
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);

        // Devolve a simulação da taxa para esse registo
        return taxaService.simularTaxa(emissaoGPorKm, kms, municipio);
    }
}