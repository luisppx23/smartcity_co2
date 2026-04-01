package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.services.MunicipioService;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/municipio")
public class MunicipioController {

    @Autowired
    private MunicipioService municipioService;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/dashboardMunicipio")
    public String dashboardMunicipio(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/dashboardMunicipio";
    }

    @GetMapping("/homeMunicipio")
    public String homeMunicipio(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/homeMunicipio";
    }

    @GetMapping("/redefinirTaxa")
    public String redefinirTaxa(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/redefinirTaxa";
    }

    @GetMapping("/relatoriosMunicipio")
    @Transactional(readOnly = true)
    public String relatoriosMunicipio(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        if (user == null) {
            model.addAttribute("erro", "Não foi possível identificar o município autenticado.");
            return "municipio/relatoriosMunicipio";
        }

        Municipio municipio = municipioService.getUserM(user.getUsername());
        model.addAttribute("municipio", municipio);

        if (municipio == null) {
            model.addAttribute("erro", "Município não encontrado.");
            return "municipio/relatoriosMunicipio";
        }

        prepararDadosRelatorioMunicipio(model, municipio);

        return "municipio/relatoriosMunicipio";
    }

    @GetMapping("/listaCidadaos")
    @Transactional(readOnly = true)
    public String listaDeCidadaos(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        if (user == null) {
            model.addAttribute("erro", "Não foi possível identificar o município autenticado.");
            return "municipio/listaCidadaos";
        }

        Municipio municipio = municipioService.getUserM(user.getUsername());
        model.addAttribute("municipio", municipio);

        if (municipio == null) {
            model.addAttribute("erro", "Município não encontrado.");
            return "municipio/listaCidadaos";
        }

        List<Cidadao> listaCidadaos =
                cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());

        model.addAttribute("listaCidadaos", listaCidadaos);

        return "municipio/listaCidadaos";
    }

    @GetMapping("/listaVeiculos")
    @Transactional(readOnly = true)
    public String listaDeVeiculos(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        if (user == null) {
            model.addAttribute("erro", "Não foi possível identificar o município autenticado.");
            return "municipio/listaVeiculos";
        }

        Municipio municipio = municipioService.getUserM(user.getUsername());
        model.addAttribute("municipio", municipio);

        if (municipio == null) {
            model.addAttribute("erro", "Município não encontrado.");
            return "municipio/listaVeiculos";
        }

        List<Cidadao> listaCidadaos =
                cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());

        model.addAttribute("listaCidadaos", listaCidadaos);

        return "municipio/listaVeiculos";
    }

    private void prepararDadosRelatorioMunicipio(Model model, Municipio municipio) {
        List<Cidadao> listaCidadaos = cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());

        List<RegistoKms> listaRegistos = new ArrayList<>();
        List<Veiculo> listaVeiculos = new ArrayList<>();

        Set<Long> idsVeiculosUnicos = new LinkedHashSet<>();

        Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
        Map<Long, String> combustivelPorVeiculo = new LinkedHashMap<>();

        Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

        Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();
        Map<String, Double> percentagemKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> percentagemCo2PorCombustivel = new LinkedHashMap<>();

        Map<String, Integer> numeroRegistosPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> emissaoMediaPorCombustivel = new LinkedHashMap<>();

        Map<String, Double> totalKmsPorMes = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorMes = new LinkedHashMap<>();
        Map<String, Double> mediaCo2PorHabitantePorMes = new LinkedHashMap<>();
        Map<String, Boolean> objetivoAtingidoPorMes = new LinkedHashMap<>();

        Map<String, Double> mediaEmissoesPorMes = new LinkedHashMap<>();

        Map<String, Double> variacaoAnoAnteriorPorMes = new LinkedHashMap<>();
        Map<String, String> corComparacaoAnoAnteriorPorMes = new LinkedHashMap<>();

        Map<String, Double> variacaoMesAnterior = new LinkedHashMap<>();
        Map<String, String> corComparacaoMesAnterior = new LinkedHashMap<>();

        Map<String, Double> evolucaoEmissoesMensais = new LinkedHashMap<>();

        double totalKmsGeral = 0.0;
        double totalCo2Geral = 0.0;
        double somaEmissoesMensais = 0.0;

        int numeroHabitantes = (listaCidadaos != null) ? listaCidadaos.size() : 0;
        int quantidadeVeiculosTotais = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        System.out.println("Município: " + municipio.getNome());
        System.out.println("Número de cidadãos: " + numeroHabitantes);

        if (listaCidadaos != null) {
            for (Cidadao cidadao : listaCidadaos) {
                System.out.println("Cidadão ID: " + cidadao.getId());

                if (cidadao.getListaDeVeiculos() != null) {
                    System.out.println("Número de ownerships: " + cidadao.getListaDeVeiculos().size());

                    for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                        Veiculo veiculo = ownership.getVeiculo();

                        if (veiculo == null) {
                            continue;
                        }

                        listaVeiculos.add(veiculo);
                        idsVeiculosUnicos.add(veiculo.getId());

                        matriculaPorVeiculo.put(veiculo.getId(), ownership.getMatricula());

                        String combustivel = veiculo.getTipoDeCombustivel() != null
                                ? veiculo.getTipoDeCombustivel().name()
                                : "DESCONHECIDO";

                        combustivelPorVeiculo.put(veiculo.getId(), combustivel);

                        totalKmsPorVeiculo.putIfAbsent(veiculo.getId(), 0.0);
                        totalCo2PorVeiculo.putIfAbsent(veiculo.getId(), 0.0);

                        totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
                        totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);
                        numeroRegistosPorCombustivel.putIfAbsent(combustivel, 0);

                        System.out.println("Ownership ID: " + ownership.getId());
                        System.out.println("Registos: " +
                                (ownership.getRegistosKms() != null ? ownership.getRegistosKms().size() : 0));

                        if (ownership.getRegistosKms() != null) {
                            for (RegistoKms registo : ownership.getRegistosKms()) {
                                if (registo == null) {
                                    continue;
                                }

                                listaRegistos.add(registo);

                                double kms = registo.getKms_mes();
                                double co2 = registo.getEmissaoEfetivaKg();

                                totalKmsGeral += kms;
                                totalCo2Geral += co2;

                                totalKmsPorVeiculo.put(
                                        veiculo.getId(),
                                        totalKmsPorVeiculo.getOrDefault(veiculo.getId(), 0.0) + kms
                                );

                                totalCo2PorVeiculo.put(
                                        veiculo.getId(),
                                        totalCo2PorVeiculo.getOrDefault(veiculo.getId(), 0.0) + co2
                                );

                                totalKmsPorCombustivel.put(
                                        combustivel,
                                        totalKmsPorCombustivel.getOrDefault(combustivel, 0.0) + kms
                                );

                                totalCo2PorCombustivel.put(
                                        combustivel,
                                        totalCo2PorCombustivel.getOrDefault(combustivel, 0.0) + co2
                                );

                                numeroRegistosPorCombustivel.put(
                                        combustivel,
                                        numeroRegistosPorCombustivel.getOrDefault(combustivel, 0) + 1
                                );

                                String mesAno = sdf.format(registo.getMes_ano());

                                totalKmsPorMes.put(
                                        mesAno,
                                        totalKmsPorMes.getOrDefault(mesAno, 0.0) + kms
                                );

                                totalCo2PorMes.put(
                                        mesAno,
                                        totalCo2PorMes.getOrDefault(mesAno, 0.0) + co2
                                );
                            }
                        }
                    }
                }
            }
        }

        quantidadeVeiculosTotais = idsVeiculosUnicos.size();

        for (String combustivel : totalKmsPorCombustivel.keySet()) {
            double kmsCombustivel = totalKmsPorCombustivel.getOrDefault(combustivel, 0.0);
            double co2Combustivel = totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);

            double percentagemKms = totalKmsGeral > 0 ? (kmsCombustivel / totalKmsGeral) * 100 : 0.0;
            double percentagemCo2 = totalCo2Geral > 0 ? (co2Combustivel / totalCo2Geral) * 100 : 0.0;

            percentagemKmsPorCombustivel.put(combustivel, percentagemKms);
            percentagemCo2PorCombustivel.put(combustivel, percentagemCo2);
        }

        for (String combustivel : totalCo2PorCombustivel.keySet()) {
            double totalCo2Combustivel = totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);
            int totalRegistosCombustivel = numeroRegistosPorCombustivel.getOrDefault(combustivel, 0);

            double mediaCombustivel = totalRegistosCombustivel > 0
                    ? totalCo2Combustivel / totalRegistosCombustivel
                    : 0.0;

            emissaoMediaPorCombustivel.put(combustivel, mediaCombustivel);
        }

        for (String mesAno : totalCo2PorMes.keySet()) {
            double co2Mes = totalCo2PorMes.get(mesAno);
            double mediaMesPorHabitante = numeroHabitantes > 0 ? co2Mes / numeroHabitantes : 0.0;

            mediaCo2PorHabitantePorMes.put(mesAno, mediaMesPorHabitante);
            objetivoAtingidoPorMes.put(mesAno, mediaMesPorHabitante <= municipio.getObjetivo_co2_mes_hab());

            mediaEmissoesPorMes.put(mesAno, co2Mes);
            somaEmissoesMensais += co2Mes;
        }

        double mediaGlobalEmissoesMensais = totalCo2PorMes.size() > 0
                ? somaEmissoesMensais / totalCo2PorMes.size()
                : 0.0;

        List<String> mesesOrdenados = new ArrayList<>(totalCo2PorMes.keySet());

        Collections.sort(mesesOrdenados, new Comparator<String>() {
            @Override
            public int compare(String m1, String m2) {
                String[] p1 = m1.split("/");
                String[] p2 = m2.split("/");

                int mes1 = Integer.parseInt(p1[0]);
                int ano1 = Integer.parseInt(p1[1]);

                int mes2 = Integer.parseInt(p2[0]);
                int ano2 = Integer.parseInt(p2[1]);

                if (ano1 != ano2) {
                    return Integer.compare(ano1, ano2);
                }
                return Integer.compare(mes1, mes2);
            }
        });

        for (int i = 1; i < mesesOrdenados.size(); i++) {
            String mesAtual = mesesOrdenados.get(i);
            String mesAnterior = mesesOrdenados.get(i - 1);

            double emissaoAtual = totalCo2PorMes.getOrDefault(mesAtual, 0.0);
            double emissaoAnterior = totalCo2PorMes.getOrDefault(mesAnterior, 0.0);

            double variacao = emissaoAtual - emissaoAnterior;
            variacaoMesAnterior.put(mesAtual, variacao);

            if (variacao < 0) {
                corComparacaoMesAnterior.put(mesAtual, "green");
            } else if (variacao > 0) {
                corComparacaoMesAnterior.put(mesAtual, "red");
            } else {
                corComparacaoMesAnterior.put(mesAtual, "gray");
            }
        }

        for (String mesAno : totalCo2PorMes.keySet()) {
            String[] partes = mesAno.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            String mesAnoAnterior = String.format("%02d/%d", mes, ano - 1);

            double emissaoAtual = totalCo2PorMes.getOrDefault(mesAno, 0.0);
            double emissaoAnoAnterior = totalCo2PorMes.getOrDefault(mesAnoAnterior, -1.0);

            if (emissaoAnoAnterior >= 0) {
                double variacao = emissaoAtual - emissaoAnoAnterior;
                variacaoAnoAnteriorPorMes.put(mesAno, variacao);

                if (variacao < 0) {
                    corComparacaoAnoAnteriorPorMes.put(mesAno, "green");
                } else if (variacao > 0) {
                    corComparacaoAnoAnteriorPorMes.put(mesAno, "red");
                } else {
                    corComparacaoAnoAnteriorPorMes.put(mesAno, "gray");
                }
            }
        }

        for (String mes : mesesOrdenados) {
            evolucaoEmissoesMensais.put(mes, totalCo2PorMes.get(mes));
        }

        model.addAttribute("listaCidadaos", listaCidadaos);
        model.addAttribute("listaRegistos", listaRegistos);
        model.addAttribute("listaVeiculos", listaVeiculos);
        model.addAttribute("matriculaPorVeiculo", matriculaPorVeiculo);
        model.addAttribute("combustivelPorVeiculo", combustivelPorVeiculo);

        model.addAttribute("totalKmsGeral", totalKmsGeral);
        model.addAttribute("totalCo2Geral", totalCo2Geral);

        model.addAttribute("totalKmsPorVeiculo", totalKmsPorVeiculo);
        model.addAttribute("totalCo2PorVeiculo", totalCo2PorVeiculo);

        model.addAttribute("totalKmsPorCombustivel", totalKmsPorCombustivel);
        model.addAttribute("totalCo2PorCombustivel", totalCo2PorCombustivel);
        model.addAttribute("percentagemKmsPorCombustivel", percentagemKmsPorCombustivel);
        model.addAttribute("percentagemCo2PorCombustivel", percentagemCo2PorCombustivel);

        model.addAttribute("totalKmsPorMes", totalKmsPorMes);
        model.addAttribute("totalCo2PorMes", totalCo2PorMes);
        model.addAttribute("mediaCo2PorHabitantePorMes", mediaCo2PorHabitantePorMes);
        model.addAttribute("objetivoAtingidoPorMes", objetivoAtingidoPorMes);

        model.addAttribute("numeroHabitantes", numeroHabitantes);

        model.addAttribute("quantidadeVeiculosTotais", quantidadeVeiculosTotais);

        model.addAttribute("mediaEmissoesPorMes", mediaEmissoesPorMes);
        model.addAttribute("mediaGlobalEmissoesMensais", mediaGlobalEmissoesMensais);

        model.addAttribute("variacaoAnoAnteriorPorMes", variacaoAnoAnteriorPorMes);
        model.addAttribute("corComparacaoAnoAnteriorPorMes", corComparacaoAnoAnteriorPorMes);

        model.addAttribute("variacaoMesAnterior", variacaoMesAnterior);
        model.addAttribute("corComparacaoMesAnterior", corComparacaoMesAnterior);

        model.addAttribute("evolucaoEmissoesMensais", evolucaoEmissoesMensais);

        model.addAttribute("emissaoMediaPorCombustivel", emissaoMediaPorCombustivel);
    }
}