package pt.upskill.smart_city_co2.dto;

import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Veiculo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DashboardMunicipioDataDTO {

    // Dados base
    private List<Cidadao> listaCidadaos = new ArrayList<>();
    private List<RegistoKms> listaRegistos = new ArrayList<>();
    private List<Veiculo> listaVeiculos = new ArrayList<>();

    // Controlo de veículos únicos
    private Set<Long> idsVeiculosUnicos = new LinkedHashSet<>();

    // Informação por veículo
    private Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
    private Map<Long, String> combustivelPorVeiculo = new LinkedHashMap<>();

    // Totais por veículo
    private Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
    private Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

    // Totais e percentagens por combustível
    private Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> percentagemKmsPorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> percentagemCo2PorCombustivel = new LinkedHashMap<>();

    // Médias e contagens por combustível
    private Map<String, Integer> numeroRegistosPorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> emissaoMediaPorCombustivel = new LinkedHashMap<>();
    private Map<String, Integer> quantidadeVeiculosPorCombustivel = new LinkedHashMap<>();

    // Métricas mensais
    private Map<String, Double> totalKmsPorMes = new LinkedHashMap<>();
    private Map<String, Double> totalCo2PorMes = new LinkedHashMap<>();
    private Map<String, Double> mediaCo2PorHabitantePorMes = new LinkedHashMap<>();
    private Map<String, Boolean> objetivoAtingidoPorMes = new LinkedHashMap<>();
    private Map<String, Double> mediaEmissoesPorMes = new LinkedHashMap<>();

    // Comparações
    private Map<String, Double> variacaoAnoAnteriorPorMes = new LinkedHashMap<>();
    private Map<String, String> corComparacaoAnoAnteriorPorMes = new LinkedHashMap<>();
    private Map<String, Double> variacaoMesAnterior = new LinkedHashMap<>();
    private Map<String, String> corComparacaoMesAnterior = new LinkedHashMap<>();

    // Dados para gráficos
    private Map<String, Double> evolucaoEmissoesMensais = new LinkedHashMap<>();

    // Indicadores globais
    private double totalKmsGeral = 0.0;
    private double totalCo2Geral = 0.0;
    private double somaEmissoesMensais = 0.0;
    private double mediaGlobalEmissoesMensais = 0.0;
    private double mediaPorVeiculo = 0.0;
    private double mesAtualCo2 = 0.0;
    private double mediaAnoAnterior = 0.0;
    private double mediaAnoAtual = 0.0;

    // Contadores
    private int numeroHabitantes = 0;
    private int quantidadeVeiculosTotais = 0;
    private int anoAnterior = 0;
    private int anoAtual = 0;
    private int nivelMunicipioPct = 0;
    private int nivelMunicipioIndex = 0;
    private int mesesAtingidos = 0;

    // Labels
    private String mesAtualLabel = "";
    private String nivelMunicipio = "Initial";

    // Ordem cronológica
    private List<String> mesesOrdenados = new ArrayList<>();

    public DashboardMunicipioDataDTO empty() {
        DashboardMunicipioDataDTO dto = new DashboardMunicipioDataDTO();
        dto.setListaCidadaos(new ArrayList<>());
        dto.setListaRegistos(new ArrayList<>());
        dto.setListaVeiculos(new ArrayList<>());
        dto.setIdsVeiculosUnicos(new LinkedHashSet<>());
        dto.setMatriculaPorVeiculo(new LinkedHashMap<>());
        dto.setCombustivelPorVeiculo(new LinkedHashMap<>());
        dto.setTotalKmsPorVeiculo(new LinkedHashMap<>());
        dto.setTotalCo2PorVeiculo(new LinkedHashMap<>());
        dto.setTotalKmsPorCombustivel(new LinkedHashMap<>());
        dto.setTotalCo2PorCombustivel(new LinkedHashMap<>());
        dto.setPercentagemKmsPorCombustivel(new LinkedHashMap<>());
        dto.setPercentagemCo2PorCombustivel(new LinkedHashMap<>());
        dto.setNumeroRegistosPorCombustivel(new LinkedHashMap<>());
        dto.setEmissaoMediaPorCombustivel(new LinkedHashMap<>());
        dto.setQuantidadeVeiculosPorCombustivel(new LinkedHashMap<>());
        dto.setTotalKmsPorMes(new LinkedHashMap<>());
        dto.setTotalCo2PorMes(new LinkedHashMap<>());
        dto.setMediaCo2PorHabitantePorMes(new LinkedHashMap<>());
        dto.setObjetivoAtingidoPorMes(new LinkedHashMap<>());
        dto.setMediaEmissoesPorMes(new LinkedHashMap<>());
        dto.setVariacaoAnoAnteriorPorMes(new LinkedHashMap<>());
        dto.setCorComparacaoAnoAnteriorPorMes(new LinkedHashMap<>());
        dto.setVariacaoMesAnterior(new LinkedHashMap<>());
        dto.setCorComparacaoMesAnterior(new LinkedHashMap<>());
        dto.setEvolucaoEmissoesMensais(new LinkedHashMap<>());
        dto.setMesesOrdenados(new ArrayList<>());
        return dto;
    }

    public List<Cidadao> getListaCidadaos() { return listaCidadaos; }
    public void setListaCidadaos(List<Cidadao> listaCidadaos) { this.listaCidadaos = listaCidadaos; }

    public List<RegistoKms> getListaRegistos() { return listaRegistos; }
    public void setListaRegistos(List<RegistoKms> listaRegistos) { this.listaRegistos = listaRegistos; }

    public List<Veiculo> getListaVeiculos() { return listaVeiculos; }
    public void setListaVeiculos(List<Veiculo> listaVeiculos) { this.listaVeiculos = listaVeiculos; }

    public Set<Long> getIdsVeiculosUnicos() { return idsVeiculosUnicos; }
    public void setIdsVeiculosUnicos(Set<Long> idsVeiculosUnicos) { this.idsVeiculosUnicos = idsVeiculosUnicos; }

    public Map<Long, String> getMatriculaPorVeiculo() { return matriculaPorVeiculo; }
    public void setMatriculaPorVeiculo(Map<Long, String> matriculaPorVeiculo) { this.matriculaPorVeiculo = matriculaPorVeiculo; }

    public Map<Long, String> getCombustivelPorVeiculo() { return combustivelPorVeiculo; }
    public void setCombustivelPorVeiculo(Map<Long, String> combustivelPorVeiculo) { this.combustivelPorVeiculo = combustivelPorVeiculo; }

    public Map<Long, Double> getTotalKmsPorVeiculo() { return totalKmsPorVeiculo; }
    public void setTotalKmsPorVeiculo(Map<Long, Double> totalKmsPorVeiculo) { this.totalKmsPorVeiculo = totalKmsPorVeiculo; }

    public Map<Long, Double> getTotalCo2PorVeiculo() { return totalCo2PorVeiculo; }
    public void setTotalCo2PorVeiculo(Map<Long, Double> totalCo2PorVeiculo) { this.totalCo2PorVeiculo = totalCo2PorVeiculo; }

    public Map<String, Double> getTotalKmsPorCombustivel() { return totalKmsPorCombustivel; }
    public void setTotalKmsPorCombustivel(Map<String, Double> totalKmsPorCombustivel) { this.totalKmsPorCombustivel = totalKmsPorCombustivel; }

    public Map<String, Double> getTotalCo2PorCombustivel() { return totalCo2PorCombustivel; }
    public void setTotalCo2PorCombustivel(Map<String, Double> totalCo2PorCombustivel) { this.totalCo2PorCombustivel = totalCo2PorCombustivel; }

    public Map<String, Double> getPercentagemKmsPorCombustivel() { return percentagemKmsPorCombustivel; }
    public void setPercentagemKmsPorCombustivel(Map<String, Double> percentagemKmsPorCombustivel) { this.percentagemKmsPorCombustivel = percentagemKmsPorCombustivel; }

    public Map<String, Double> getPercentagemCo2PorCombustivel() { return percentagemCo2PorCombustivel; }
    public void setPercentagemCo2PorCombustivel(Map<String, Double> percentagemCo2PorCombustivel) { this.percentagemCo2PorCombustivel = percentagemCo2PorCombustivel; }

    public Map<String, Integer> getNumeroRegistosPorCombustivel() { return numeroRegistosPorCombustivel; }
    public void setNumeroRegistosPorCombustivel(Map<String, Integer> numeroRegistosPorCombustivel) { this.numeroRegistosPorCombustivel = numeroRegistosPorCombustivel; }

    public Map<String, Double> getEmissaoMediaPorCombustivel() { return emissaoMediaPorCombustivel; }
    public void setEmissaoMediaPorCombustivel(Map<String, Double> emissaoMediaPorCombustivel) { this.emissaoMediaPorCombustivel = emissaoMediaPorCombustivel; }

    public Map<String, Integer> getQuantidadeVeiculosPorCombustivel() { return quantidadeVeiculosPorCombustivel; }
    public void setQuantidadeVeiculosPorCombustivel(Map<String, Integer> quantidadeVeiculosPorCombustivel) { this.quantidadeVeiculosPorCombustivel = quantidadeVeiculosPorCombustivel; }

    public Map<String, Double> getTotalKmsPorMes() { return totalKmsPorMes; }
    public void setTotalKmsPorMes(Map<String, Double> totalKmsPorMes) { this.totalKmsPorMes = totalKmsPorMes; }

    public Map<String, Double> getTotalCo2PorMes() { return totalCo2PorMes; }
    public void setTotalCo2PorMes(Map<String, Double> totalCo2PorMes) { this.totalCo2PorMes = totalCo2PorMes; }

    public Map<String, Double> getMediaCo2PorHabitantePorMes() { return mediaCo2PorHabitantePorMes; }
    public void setMediaCo2PorHabitantePorMes(Map<String, Double> mediaCo2PorHabitantePorMes) { this.mediaCo2PorHabitantePorMes = mediaCo2PorHabitantePorMes; }

    public Map<String, Boolean> getObjetivoAtingidoPorMes() { return objetivoAtingidoPorMes; }
    public void setObjetivoAtingidoPorMes(Map<String, Boolean> objetivoAtingidoPorMes) { this.objetivoAtingidoPorMes = objetivoAtingidoPorMes; }

    public Map<String, Double> getMediaEmissoesPorMes() { return mediaEmissoesPorMes; }
    public void setMediaEmissoesPorMes(Map<String, Double> mediaEmissoesPorMes) { this.mediaEmissoesPorMes = mediaEmissoesPorMes; }

    public Map<String, Double> getVariacaoAnoAnteriorPorMes() { return variacaoAnoAnteriorPorMes; }
    public void setVariacaoAnoAnteriorPorMes(Map<String, Double> variacaoAnoAnteriorPorMes) { this.variacaoAnoAnteriorPorMes = variacaoAnoAnteriorPorMes; }

    public Map<String, String> getCorComparacaoAnoAnteriorPorMes() { return corComparacaoAnoAnteriorPorMes; }
    public void setCorComparacaoAnoAnteriorPorMes(Map<String, String> corComparacaoAnoAnteriorPorMes) { this.corComparacaoAnoAnteriorPorMes = corComparacaoAnoAnteriorPorMes; }

    public Map<String, Double> getVariacaoMesAnterior() { return variacaoMesAnterior; }
    public void setVariacaoMesAnterior(Map<String, Double> variacaoMesAnterior) { this.variacaoMesAnterior = variacaoMesAnterior; }

    public Map<String, String> getCorComparacaoMesAnterior() { return corComparacaoMesAnterior; }
    public void setCorComparacaoMesAnterior(Map<String, String> corComparacaoMesAnterior) { this.corComparacaoMesAnterior = corComparacaoMesAnterior; }

    public Map<String, Double> getEvolucaoEmissoesMensais() { return evolucaoEmissoesMensais; }
    public void setEvolucaoEmissoesMensais(Map<String, Double> evolucaoEmissoesMensais) { this.evolucaoEmissoesMensais = evolucaoEmissoesMensais; }

    public double getTotalKmsGeral() { return totalKmsGeral; }
    public void setTotalKmsGeral(double totalKmsGeral) { this.totalKmsGeral = totalKmsGeral; }

    public double getTotalCo2Geral() { return totalCo2Geral; }
    public void setTotalCo2Geral(double totalCo2Geral) { this.totalCo2Geral = totalCo2Geral; }

    public double getSomaEmissoesMensais() { return somaEmissoesMensais; }
    public void setSomaEmissoesMensais(double somaEmissoesMensais) { this.somaEmissoesMensais = somaEmissoesMensais; }

    public double getMediaGlobalEmissoesMensais() { return mediaGlobalEmissoesMensais; }
    public void setMediaGlobalEmissoesMensais(double mediaGlobalEmissoesMensais) { this.mediaGlobalEmissoesMensais = mediaGlobalEmissoesMensais; }

    public double getMediaPorVeiculo() { return mediaPorVeiculo; }
    public void setMediaPorVeiculo(double mediaPorVeiculo) { this.mediaPorVeiculo = mediaPorVeiculo; }

    public double getMesAtualCo2() { return mesAtualCo2; }
    public void setMesAtualCo2(double mesAtualCo2) { this.mesAtualCo2 = mesAtualCo2; }

    public double getMediaAnoAnterior() { return mediaAnoAnterior; }
    public void setMediaAnoAnterior(double mediaAnoAnterior) { this.mediaAnoAnterior = mediaAnoAnterior; }

    public double getMediaAnoAtual() { return mediaAnoAtual; }
    public void setMediaAnoAtual(double mediaAnoAtual) { this.mediaAnoAtual = mediaAnoAtual; }

    public int getNumeroHabitantes() { return numeroHabitantes; }
    public void setNumeroHabitantes(int numeroHabitantes) { this.numeroHabitantes = numeroHabitantes; }

    public int getQuantidadeVeiculosTotais() { return quantidadeVeiculosTotais; }
    public void setQuantidadeVeiculosTotais(int quantidadeVeiculosTotais) { this.quantidadeVeiculosTotais = quantidadeVeiculosTotais; }

    public int getAnoAnterior() { return anoAnterior; }
    public void setAnoAnterior(int anoAnterior) { this.anoAnterior = anoAnterior; }

    public int getAnoAtual() { return anoAtual; }
    public void setAnoAtual(int anoAtual) { this.anoAtual = anoAtual; }

    public int getNivelMunicipioPct() { return nivelMunicipioPct; }
    public void setNivelMunicipioPct(int nivelMunicipioPct) { this.nivelMunicipioPct = nivelMunicipioPct; }

    public int getNivelMunicipioIndex() { return nivelMunicipioIndex; }
    public void setNivelMunicipioIndex(int nivelMunicipioIndex) { this.nivelMunicipioIndex = nivelMunicipioIndex; }

    public int getMesesAtingidos() { return mesesAtingidos; }
    public void setMesesAtingidos(int mesesAtingidos) { this.mesesAtingidos = mesesAtingidos; }

    public String getMesAtualLabel() { return mesAtualLabel; }
    public void setMesAtualLabel(String mesAtualLabel) { this.mesAtualLabel = mesAtualLabel; }

    public String getNivelMunicipio() { return nivelMunicipio; }
    public void setNivelMunicipio(String nivelMunicipio) { this.nivelMunicipio = nivelMunicipio; }

    public List<String> getMesesOrdenados() { return mesesOrdenados; }
    public void setMesesOrdenados(List<String> mesesOrdenados) { this.mesesOrdenados = mesesOrdenados; }
}