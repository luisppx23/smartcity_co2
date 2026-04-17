package pt.upskill.smart_city_co2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.dto.DashboardMunicipioDataDTO;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardMunicipioAnalyticsService {

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Transactional(readOnly = true)
    public DashboardMunicipioDataDTO prepararDadosDashboard(Municipio municipio) {
        if (municipio == null) {
            return DashboardMunicipioDataDTO.empty(municipio);
        }

        List<Cidadao> cidadaos = cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());

        if (cidadaos == null || cidadaos.isEmpty()) {
            return DashboardMunicipioDataDTO.empty(municipio);
        }

        DashboardMunicipioDataDTO dto = new DashboardMunicipioDataDTO(municipio);
        dto.setCidadaos(cidadaos);
        dto.setNumeroHabitantes(cidadaos.size());

        Set<Veiculo> veiculosUnicos = new HashSet<>();
        List<RegistoKms> todosRegistos = new ArrayList<>();

        for (Cidadao cidadao : cidadaos) {
            if (cidadao.getListaDeVeiculos() != null) {
                for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                    if (ownership != null && ownership.getVeiculo() != null) {
                        veiculosUnicos.add(ownership.getVeiculo());
                        if (ownership.getRegistosKms() != null) {
                            todosRegistos.addAll(ownership.getRegistosKms());
                        }
                    }
                }
            }
        }

        dto.setVeiculos(new ArrayList<>(veiculosUnicos));
        dto.setQuantidadeVeiculosTotais(veiculosUnicos.size());
        dto.setRegistos(todosRegistos);

        return dto;
    }

    // ========== MÉTRICAS POR COMBUSTÍVEL ==========

    public Map<String, Double> calcularTotalKmsPorCombustivel(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .filter(r -> r.getOwnership() != null && r.getOwnership().getVeiculo() != null)
                .collect(Collectors.groupingBy(
                        r -> obterCombustivel(r.getOwnership().getVeiculo()),
                        Collectors.summingDouble(RegistoKms::getKms_mes)
                ));
    }

    public Map<String, Double> calcularTotalCo2PorCombustivel(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .filter(r -> r.getOwnership() != null && r.getOwnership().getVeiculo() != null)
                .collect(Collectors.groupingBy(
                        r -> obterCombustivel(r.getOwnership().getVeiculo()),
                        Collectors.summingDouble(RegistoKms::getEmissaoEfetivaKg)
                ));
    }

    public Map<String, Double> calcularTotalTaxaPorCombustivel(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .filter(r -> r.getTaxa() != null && r.getOwnership() != null && r.getOwnership().getVeiculo() != null)
                .collect(Collectors.groupingBy(
                        r -> obterCombustivel(r.getOwnership().getVeiculo()),
                        Collectors.summingDouble(r -> r.getTaxa().getValor())
                ));
    }

    public Map<String, Double> calcularPercentagemKmsPorCombustivel(DashboardMunicipioDataDTO dto) {
        Map<String, Double> kmsPorCombustivel = calcularTotalKmsPorCombustivel(dto);
        double totalKms = kmsPorCombustivel.values().stream().mapToDouble(Double::doubleValue).sum();

        return kmsPorCombustivel.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> totalKms > 0 ? (e.getValue() / totalKms) * 100 : 0.0
                ));
    }

    public Map<String, Double> calcularPercentagemCo2PorCombustivel(DashboardMunicipioDataDTO dto) {
        Map<String, Double> co2PorCombustivel = calcularTotalCo2PorCombustivel(dto);
        double totalCo2 = co2PorCombustivel.values().stream().mapToDouble(Double::doubleValue).sum();

        return co2PorCombustivel.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> totalCo2 > 0 ? (e.getValue() / totalCo2) * 100 : 0.0
                ));
    }

    public Map<String, Integer> calcularNumeroRegistosPorCombustivel(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .filter(r -> r.getOwnership() != null && r.getOwnership().getVeiculo() != null)
                .collect(Collectors.groupingBy(
                        r -> obterCombustivel(r.getOwnership().getVeiculo()),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    public Map<String, Double> calcularEmissaoMediaPorCombustivel(DashboardMunicipioDataDTO dto) {
        Map<String, Double> totalCo2 = calcularTotalCo2PorCombustivel(dto);
        Map<String, Integer> totalRegistos = calcularNumeroRegistosPorCombustivel(dto);

        return totalCo2.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> totalRegistos.getOrDefault(e.getKey(), 0) > 0
                                ? e.getValue() / totalRegistos.get(e.getKey())
                                : 0.0
                ));
    }

    public Map<String, Integer> contarVeiculosPorCombustivel(DashboardMunicipioDataDTO dto) {
        Map<String, Integer> resultado = new LinkedHashMap<>();

        for (TipoDeCombustivel tipo : TipoDeCombustivel.values()) {
            resultado.put(tipo.name(), 0);
        }

        Set<Long> veiculosContados = new HashSet<>();

        for (Veiculo veiculo : dto.getVeiculos()) {
            if (veiculo.getId() != null && !veiculosContados.contains(veiculo.getId())) {
                veiculosContados.add(veiculo.getId());
                if (veiculo.getTipoDeCombustivel() != null) {
                    String combustivel = veiculo.getTipoDeCombustivel().name();
                    resultado.put(combustivel, resultado.getOrDefault(combustivel, 0) + 1);
                }
            }
        }

        return resultado;
    }

    // ========== MÉTRICAS MENSAIS ==========

    public Map<String, Double> calcularTotalKmsPorMes(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .filter(r -> r.getMes_ano() != null)
                .collect(Collectors.groupingBy(
                        r -> formatarMesAno(r.getMes_ano()),
                        Collectors.summingDouble(RegistoKms::getKms_mes)
                ));
    }

    public Map<String, Double> calcularTotalCo2PorMes(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .filter(r -> r.getMes_ano() != null)
                .collect(Collectors.groupingBy(
                        r -> formatarMesAno(r.getMes_ano()),
                        Collectors.summingDouble(RegistoKms::getEmissaoEfetivaKg)
                ));
    }

    public Map<String, Double> calcularMediaCo2PorHabitantePorMes(DashboardMunicipioDataDTO dto) {
        Map<String, Double> co2PorMes = calcularTotalCo2PorMes(dto);
        int habitantes = dto.getNumeroHabitantes();

        return co2PorMes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> habitantes > 0 ? e.getValue() / habitantes : 0.0
                ));
    }

    public Map<String, Double> calcularMediaEmissoesPorMes(DashboardMunicipioDataDTO dto) {
        return calcularTotalCo2PorMes(dto);
    }

    public Map<String, Boolean> calcularObjetivoAtingidoPorMes(DashboardMunicipioDataDTO dto) {
        Map<String, Double> mediaPorHabitante = calcularMediaCo2PorHabitantePorMes(dto);
        Municipio municipio = dto.getMunicipio();

        if (municipio == null) {
            return new HashMap<>();
        }

        double objetivo = municipio.getObjetivo_co2_mes_hab();

        if (objetivo <= 0) {
            return new HashMap<>();
        }

        return mediaPorHabitante.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() <= objetivo
                ));
    }

    // ========== VARIAÇÕES ==========

    public Map<String, Double> calcularVariacaoMesAnterior(DashboardMunicipioDataDTO dto) {
        Map<String, Double> co2PorMes = calcularTotalCo2PorMes(dto);
        List<String> mesesOrdenados = getMesesOrdenados(dto);
        Map<String, Double> variacoes = new LinkedHashMap<>();

        for (int i = 1; i < mesesOrdenados.size(); i++) {
            String mesAtual = mesesOrdenados.get(i);
            String mesAnterior = mesesOrdenados.get(i - 1);

            double emissaoAtual = co2PorMes.getOrDefault(mesAtual, 0.0);
            double emissaoAnterior = co2PorMes.getOrDefault(mesAnterior, 0.0);

            variacoes.put(mesAtual, emissaoAtual - emissaoAnterior);
        }

        return variacoes;
    }

    public Map<String, String> calcularCorComparacaoMesAnterior(DashboardMunicipioDataDTO dto) {
        Map<String, Double> variacoes = calcularVariacaoMesAnterior(dto);
        return variacoes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> obterCorVariacao(e.getValue())
                ));
    }

    public Map<String, Double> calcularVariacaoAnoAnterior(DashboardMunicipioDataDTO dto) {
        Map<String, Double> co2PorMes = calcularTotalCo2PorMes(dto);
        Map<String, Double> variacoes = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : co2PorMes.entrySet()) {
            String mesAno = entry.getKey();
            String[] partes = mesAno.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            String mesAnoAnterior = String.format("%02d/%d", mes, ano - 1);
            double emissaoAtual = entry.getValue();
            double emissaoAnterior = co2PorMes.getOrDefault(mesAnoAnterior, -1.0);

            if (emissaoAnterior >= 0) {
                variacoes.put(mesAno, emissaoAtual - emissaoAnterior);
            }
        }

        return variacoes;
    }

    public Map<String, String> calcularCorComparacaoAnoAnteriorPorMes(DashboardMunicipioDataDTO dto) {
        Map<String, Double> variacoes = calcularVariacaoAnoAnterior(dto);
        return variacoes.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> obterCorVariacao(e.getValue())
                ));
    }

    // ========== EVOLUÇÃO ==========

    public Map<String, Double> calcularEvolucaoEmissoesMensais(DashboardMunicipioDataDTO dto) {
        Map<String, Double> co2PorMes = calcularTotalCo2PorMes(dto);
        List<String> mesesOrdenados = getMesesOrdenados(dto);

        return mesesOrdenados.stream()
                .collect(Collectors.toMap(
                        mes -> mes,
                        mes -> co2PorMes.getOrDefault(mes, 0.0),
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    // ========== INDICADORES GLOBAIS ==========

    public double getTotalKmsGeral(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .mapToDouble(RegistoKms::getKms_mes)
                .sum();
    }

    public double getTotalCo2Geral(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .mapToDouble(RegistoKms::getEmissaoEfetivaKg)
                .sum();
    }

    public double getTotalTaxaGeral(DashboardMunicipioDataDTO dto) {
        return dto.getRegistos().stream()
                .filter(r -> r.getTaxa() != null)
                .mapToDouble(r -> r.getTaxa().getValor())
                .sum();
    }

    public double getMediaPorVeiculo(DashboardMunicipioDataDTO dto) {
        double totalCo2 = getTotalCo2Geral(dto);
        int totalVeiculos = dto.getQuantidadeVeiculosTotais();
        return totalVeiculos > 0 ? totalCo2 / totalVeiculos : 0.0;
    }

    public double getMediaGlobalEmissoesMensais(DashboardMunicipioDataDTO dto) {
        Map<String, Double> co2PorMes = calcularTotalCo2PorMes(dto);
        return co2PorMes.isEmpty() ? 0.0 :
                co2PorMes.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double calcularSomaEmissoesMensais(DashboardMunicipioDataDTO dto) {
        return calcularTotalCo2PorMes(dto).values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public String getMesAtualLabel(DashboardMunicipioDataDTO dto) {
        List<String> mesesOrdenados = getMesesOrdenados(dto);
        return mesesOrdenados.isEmpty() ? "" : mesesOrdenados.get(mesesOrdenados.size() - 1);
    }

    public double getMesAtualCo2(DashboardMunicipioDataDTO dto) {
        String mesAtual = getMesAtualLabel(dto);
        if (mesAtual.isEmpty()) return 0.0;
        return calcularTotalCo2PorMes(dto).getOrDefault(mesAtual, 0.0);
    }

    // ========== INDICADORES ANUAIS ==========

    public double getMediaAnoAtual(DashboardMunicipioDataDTO dto) {
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        return calcularMediaAno(dto, anoAtual);
    }

    public double getMediaAnoAnterior(DashboardMunicipioDataDTO dto) {
        int anoAnterior = Calendar.getInstance().get(Calendar.YEAR) - 1;
        return calcularMediaAno(dto, anoAnterior);
    }

    private double calcularMediaAno(DashboardMunicipioDataDTO dto, int ano) {
        Map<String, Double> co2PorMes = calcularTotalCo2PorMes(dto);

        List<Double> valores = co2PorMes.entrySet().stream()
                .filter(entry -> {
                    String[] partes = entry.getKey().split("/");
                    return Integer.parseInt(partes[1]) == ano;
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        return valores.isEmpty() ? 0.0 :
                valores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    // ========== NÍVEL DO MUNICÍPIO ==========

    public Map<String, Object> calcularNivelMunicipio(DashboardMunicipioDataDTO dto) {
        Map<String, Boolean> objetivoAtingido = calcularObjetivoAtingidoPorMes(dto);
        long mesesAtingidos = objetivoAtingido.values().stream().filter(v -> v).count();

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("mesesAtingidos", mesesAtingidos);

        if (mesesAtingidos >= 12) {
            resultado.put("nivel", "Platinum");
            resultado.put("pct", 100);
            resultado.put("index", 4);
        } else if (mesesAtingidos >= 7) {
            resultado.put("nivel", "Gold");
            resultado.put("pct", 75);
            resultado.put("index", 3);
        } else if (mesesAtingidos >= 4) {
            resultado.put("nivel", "Silver");
            resultado.put("pct", 50);
            resultado.put("index", 2);
        } else if (mesesAtingidos >= 1) {
            resultado.put("nivel", "Bronze");
            resultado.put("pct", 25);
            resultado.put("index", 1);
        } else {
            resultado.put("nivel", "Initial");
            resultado.put("pct", 0);
            resultado.put("index", 0);
        }

        return resultado;
    }

    // ========== MÉTODOS AUXILIARES ==========

    public List<String> getMesesOrdenados(DashboardMunicipioDataDTO dto) {
        Set<String> meses = calcularTotalCo2PorMes(dto).keySet();
        List<String> mesesOrdenados = new ArrayList<>(meses);

        mesesOrdenados.sort((m1, m2) -> {
            String[] p1 = m1.split("/");
            String[] p2 = m2.split("/");
            int mes1 = Integer.parseInt(p1[0]);
            int ano1 = Integer.parseInt(p1[1]);
            int mes2 = Integer.parseInt(p2[0]);
            int ano2 = Integer.parseInt(p2[1]);
            if (ano1 != ano2) return Integer.compare(ano1, ano2);
            return Integer.compare(mes1, mes2);
        });

        return mesesOrdenados;
    }

    private String obterCombustivel(Veiculo veiculo) {
        return veiculo.getTipoDeCombustivel() != null
                ? veiculo.getTipoDeCombustivel().name()
                : "DESCONHECIDO";
    }

    private String formatarMesAno(Date data) {
        return new SimpleDateFormat("MM/yyyy").format(data);
    }

    private String obterCorVariacao(double variacao) {
        if (variacao < 0) return "green";
        if (variacao > 0) return "red";
        return "gray";
    }
}