package pt.upskill.smart_city_co2.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
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
        if (authentication != null && authentication.getPrincipal() instanceof User)
        {return (User) authentication.getPrincipal(); }
        return null;
    }

    @GetMapping("/dashboardMunicipio")
    @Transactional(readOnly = true)
    public String dashboardMunicipio(Model model) {
        User user = getAuthenticatedUser();
        model.addAttribute("user", user);

        if (user == null) {
            model.addAttribute("erro", "Não foi possível identificar o município autenticado.");
            return "municipio/dashboardMunicipio";
        }

        Municipio municipio = municipioService.getUserM(user.getUsername());
        model.addAttribute("municipio", municipio);

        if (municipio == null) {
            model.addAttribute("erro", "Município não encontrado.");
            return "municipio/dashboardMunicipio";
        }

        prepararDadosRelatorioMunicipio(model, municipio);

        return "municipio/dashboardMunicipio";
    }

    @GetMapping("/homeMunicipio")
    public String homeMunicipio(Model model) {
        model.addAttribute("user", getAuthenticatedUser());
        return "municipio/homeMunicipio"; }

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
            return "municipio/listaCidadaos"; }
        Municipio municipio = municipioService.getUserM(user.getUsername());
        model.addAttribute("municipio", municipio);
        if (municipio == null) {
            model.addAttribute("erro", "Município não encontrado.");
            return "municipio/listaCidadaos";
        }
        List<Cidadao> listaCidadaos = cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());
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
            return "municipio/listaVeiculos"; }
        List<Cidadao> listaCidadaos = cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());
        model.addAttribute("listaCidadaos", listaCidadaos);
        return "municipio/listaVeiculos";
    }
    private void prepararDadosRelatorioMunicipio(Model model, Municipio municipio) {
        List<Cidadao> listaCidadaos =
                cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());

        if (listaCidadaos == null) {
            listaCidadaos = new ArrayList<>();
        }

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

        int numeroHabitantes = listaCidadaos.size();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

        for (Cidadao cidadao : listaCidadaos) {
            if (cidadao.getListaDeVeiculos() == null) {
                continue;
            }

            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                if (ownership == null || ownership.getVeiculo() == null) {
                    continue;
                }

                Veiculo veiculo = ownership.getVeiculo();
                Long veiculoId = veiculo.getId();

                listaVeiculos.add(veiculo);
                idsVeiculosUnicos.add(veiculoId);

                matriculaPorVeiculo.put(veiculoId, ownership.getMatricula());

                String combustivel = getCombustivel(veiculo);
                combustivelPorVeiculo.put(veiculoId, combustivel);

                totalKmsPorVeiculo.putIfAbsent(veiculoId, 0.0);
                totalCo2PorVeiculo.putIfAbsent(veiculoId, 0.0);

                totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
                totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);
                numeroRegistosPorCombustivel.putIfAbsent(combustivel, 0);

                if (ownership.getRegistosKms() == null) {
                    continue;
                }

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
                            veiculoId,
                            totalKmsPorVeiculo.getOrDefault(veiculoId, 0.0) + kms
                    );

                    totalCo2PorVeiculo.put(
                            veiculoId,
                            totalCo2PorVeiculo.getOrDefault(veiculoId, 0.0) + co2
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

        int quantidadeVeiculosTotais = idsVeiculosUnicos.size();

        calcularPercentagensPorCombustivel(
                totalKmsPorCombustivel,
                totalCo2PorCombustivel,
                percentagemKmsPorCombustivel,
                percentagemCo2PorCombustivel,
                totalKmsGeral,
                totalCo2Geral
        );

        calcularMediaEmissaoPorCombustivel(
                totalCo2PorCombustivel,
                numeroRegistosPorCombustivel,
                emissaoMediaPorCombustivel
        );

        for (String mesAno : totalCo2PorMes.keySet()) {
            double co2Mes = totalCo2PorMes.get(mesAno);

            double mediaMesPorHabitante = numeroHabitantes > 0
                    ? co2Mes / numeroHabitantes
                    : 0.0;

            mediaCo2PorHabitantePorMes.put(mesAno, mediaMesPorHabitante);
            objetivoAtingidoPorMes.put(mesAno, mediaMesPorHabitante <= municipio.getObjetivo_co2_mes_hab());
            mediaEmissoesPorMes.put(mesAno, co2Mes);

            somaEmissoesMensais += co2Mes;
        }

        double mediaGlobalEmissoesMensais = totalCo2PorMes.isEmpty()
                ? 0.0
                : somaEmissoesMensais / totalCo2PorMes.size();

        List<String> mesesOrdenados = ordenarMeses(totalCo2PorMes.keySet());

        calcularVariacaoMesAnterior(
                mesesOrdenados,
                totalCo2PorMes,
                variacaoMesAnterior,
                corComparacaoMesAnterior
        );

        calcularVariacaoAnoAnterior(
                totalCo2PorMes,
                variacaoAnoAnteriorPorMes,
                corComparacaoAnoAnteriorPorMes
        );

        for (String mes : mesesOrdenados) {
            evolucaoEmissoesMensais.put(mes, totalCo2PorMes.get(mes));
        }

        double mediaPorVeiculo = quantidadeVeiculosTotais > 0
                ? totalCo2Geral / quantidadeVeiculosTotais
                : 0.0;

        String mesAtualLabel = mesesOrdenados.isEmpty()
                ? ""
                : mesesOrdenados.get(mesesOrdenados.size() - 1);

        double mesAtualCo2 = mesAtualLabel.isEmpty()
                ? 0.0
                : totalCo2PorMes.getOrDefault(mesAtualLabel, 0.0);

        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        int anoAnterior = anoAtual - 1;

        double somaAnoAtual = 0.0;
        int countAnoAtual = 0;
        double somaAnoAnterior = 0.0;
        int countAnoAnterior = 0;

        for (Map.Entry<String, Double> entry : totalCo2PorMes.entrySet()) {
            String mesAno = entry.getKey();
            double valor = entry.getValue();

            String[] partes = mesAno.split("/");
            int ano = Integer.parseInt(partes[1]);

            if (ano == anoAtual) {
                somaAnoAtual += valor;
                countAnoAtual++;
            } else if (ano == anoAnterior) {
                somaAnoAnterior += valor;
                countAnoAnterior++;
            }
        }

        Map<String, Integer> quantidadeVeiculosPorCombustivel = contarVeiculosPorCombustivel(listaCidadaos);

        double mediaAnoAtual = countAnoAtual > 0 ? somaAnoAtual / countAnoAtual : 0.0;
        double mediaAnoAnterior = countAnoAnterior > 0 ? somaAnoAnterior / countAnoAnterior : 0.0;

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

        model.addAttribute("mediaPorVeiculo", mediaPorVeiculo);
        model.addAttribute("mesAtualCo2", mesAtualCo2);
        model.addAttribute("mesAtualLabel", mesAtualLabel);
        model.addAttribute("mediaAnoAnterior", mediaAnoAnterior);
        model.addAttribute("anoAnterior", anoAnterior);
        model.addAttribute("mediaAnoAtual", mediaAnoAtual);
        model.addAttribute("anoAtual", anoAtual);

        model.addAttribute("quantidadeVeiculosPorCombustivel", quantidadeVeiculosPorCombustivel);
    }

    private String getCombustivel(Veiculo veiculo) {
        return veiculo.getTipoDeCombustivel() != null
                ? veiculo.getTipoDeCombustivel().name()
                : "DESCONHECIDO";
    }

    private void calcularPercentagensPorCombustivel(
            Map<String, Double> totalKmsPorCombustivel,
            Map<String, Double> totalCo2PorCombustivel,
            Map<String, Double> percentagemKmsPorCombustivel,
            Map<String, Double> percentagemCo2PorCombustivel,
            double totalKmsGeral,
            double totalCo2Geral
    ) {
        for (String combustivel : totalKmsPorCombustivel.keySet()) {
            double kmsCombustivel = totalKmsPorCombustivel.getOrDefault(combustivel, 0.0);
            double co2Combustivel = totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);

            double percentagemKms = totalKmsGeral > 0 ? (kmsCombustivel / totalKmsGeral) * 100 : 0.0;
            double percentagemCo2 = totalCo2Geral > 0 ? (co2Combustivel / totalCo2Geral) * 100 : 0.0;

            percentagemKmsPorCombustivel.put(combustivel, percentagemKms);
            percentagemCo2PorCombustivel.put(combustivel, percentagemCo2);
        }
    }

    private Map<String, Integer> contarVeiculosPorCombustivel(List<Cidadao> listaCidadaos) {
        Map<String, Integer> quantidadeVeiculosPorCombustivel = new LinkedHashMap<>();

        for (TipoDeCombustivel tipo : TipoDeCombustivel.values()) {
            quantidadeVeiculosPorCombustivel.put(tipo.name(), 0);
        }

        if (listaCidadaos == null) {
            return quantidadeVeiculosPorCombustivel;
        }

        Set<Long> veiculosJaContados = new HashSet<>();

        for (Cidadao cidadao : listaCidadaos) {
            if (cidadao == null || cidadao.getListaDeVeiculos() == null) {
                continue;
            }

            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                if (ownership == null || ownership.getVeiculo() == null) {
                    continue;
                }

                Veiculo veiculo = ownership.getVeiculo();
                Long veiculoId = veiculo.getId();

                if (veiculoId != null && veiculosJaContados.contains(veiculoId)) {
                    continue;
                }

                if (veiculoId != null) {
                    veiculosJaContados.add(veiculoId);
                }

                if (veiculo.getTipoDeCombustivel() != null) {
                    String combustivel = veiculo.getTipoDeCombustivel().name();
                    quantidadeVeiculosPorCombustivel.put(
                            combustivel,
                            quantidadeVeiculosPorCombustivel.getOrDefault(combustivel, 0) + 1
                    );
                }
            }
        }

        return quantidadeVeiculosPorCombustivel;
    }

    private void calcularMediaEmissaoPorCombustivel(
            Map<String, Double> totalCo2PorCombustivel,
            Map<String, Integer> numeroRegistosPorCombustivel,
            Map<String, Double> emissaoMediaPorCombustivel
    ) {
        for (String combustivel : totalCo2PorCombustivel.keySet()) {
            double totalCo2Combustivel = totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);
            int totalRegistosCombustivel = numeroRegistosPorCombustivel.getOrDefault(combustivel, 0);

            double mediaCombustivel = totalRegistosCombustivel > 0
                    ? totalCo2Combustivel / totalRegistosCombustivel
                    : 0.0;

            emissaoMediaPorCombustivel.put(combustivel, mediaCombustivel);
        }
    }

    private List<String> ordenarMeses(Set<String> meses) {
        List<String> mesesOrdenados = new ArrayList<>(meses);

        mesesOrdenados.sort(new Comparator<String>() {
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

        return mesesOrdenados;
    }

    private void calcularVariacaoMesAnterior(
            List<String> mesesOrdenados,
            Map<String, Double> totalCo2PorMes,
            Map<String, Double> variacaoMesAnterior,
            Map<String, String> corComparacaoMesAnterior
    ) {
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
    }

    private void calcularVariacaoAnoAnterior(
            Map<String, Double> totalCo2PorMes,
            Map<String, Double> variacaoAnoAnteriorPorMes,
            Map<String, String> corComparacaoAnoAnteriorPorMes
    ) {
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
    }
}