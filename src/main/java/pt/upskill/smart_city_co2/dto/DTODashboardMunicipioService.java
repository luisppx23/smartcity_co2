package pt.upskill.smart_city_co2.dto;

import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Veiculo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
  DTO usado para transportar todos os dados agregados necessários ao dashboard do município.

  Em vez de enviar apenas entidades isoladas para a view, esta classe concentra:
  - listas base (cidadãos, veículos, registos)
  - totais globais
  - totais por veículo
  - totais por combustível
  - indicadores mensais
  - comparações temporais
  - métricas anuais
  - classificação final do município

  Desta forma, a camada de controller mantém-se simples e a lógica de agregação
  fica centralizada no service. */

public class DTODashboardMunicipioService {

    // Dados base carregados do domínio
    private List<Cidadao> listaCidadaos = new ArrayList<>();
    private List<RegistoKms> listaRegistos = new ArrayList<>();
    private List<Veiculo> listaVeiculos = new ArrayList<>();

    // Set usado para garantir contagem de veículos únicos
    private Set<Long> idsVeiculosUnicos = new LinkedHashSet<>();

    // Informação de identificação por veículo
    private Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
    private Map<Long, String> combustivelPorVeiculo = new LinkedHashMap<>();

    // Totais acumulados por veículo
    private Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
    private Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

    // Totais e percentagens por tipo de combustível
    private Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> percentagemKmsPorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> percentagemCo2PorCombustivel = new LinkedHashMap<>();

    // Número de registos e média de emissões por combustível
    private Map<String, Integer> numeroRegistosPorCombustivel = new LinkedHashMap<>();
    private Map<String, Double> emissaoMediaPorCombustivel = new LinkedHashMap<>();

    // Métricas mensais
    private Map<String, Double> totalKmsPorMes = new LinkedHashMap<>();
    private Map<String, Double> totalCo2PorMes = new LinkedHashMap<>();
    private Map<String, Double> mediaCo2PorHabitantePorMes = new LinkedHashMap<>();
    private Map<String, Boolean> objetivoAtingidoPorMes = new LinkedHashMap<>();

    private Map<String, Double> mediaEmissoesPorMes = new LinkedHashMap<>();

    // Comparação com o mesmo mês do ano anterior
    private Map<String, Double> variacaoAnoAnteriorPorMes = new LinkedHashMap<>();
    private Map<String, String> corComparacaoAnoAnteriorPorMes = new LinkedHashMap<>();

    // Comparação com o mês anterior
    private Map<String, Double> variacaoMesAnterior = new LinkedHashMap<>();
    private Map<String, String> corComparacaoMesAnterior = new LinkedHashMap<>();

    // Estruturas usadas em gráficos e distribuição de veículos
    private Map<String, Double> evolucaoEmissoesMensais = new LinkedHashMap<>();
    private Map<String, Integer> quantidadeVeiculosPorCombustivel = new LinkedHashMap<>();

    // Indicadores globais resumidos
    private double totalKmsGeral = 0.0;
    private double totalCo2Geral = 0.0;
    private double somaEmissoesMensais = 0.0;
    private double mediaGlobalEmissoesMensais = 0.0;
    private double mediaPorVeiculo = 0.0;
    private double mesAtualCo2 = 0.0;
    private double mediaAnoAnterior = 0.0;
    private double mediaAnoAtual = 0.0;

    // Contadores e classificações
    private int numeroHabitantes = 0;
    private int quantidadeVeiculosTotais = 0;
    private int anoAnterior = 0;
    private int anoAtual = 0;
    private int nivelMunicipioPct = 0;
    private int nivelMunicipioIndex = 0;
    private int mesesAtingidos = 0;

    // Labels para apresentação na interface
    private String mesAtualLabel = "";
    private String nivelMunicipio = "Initial";

    // Lista de meses ordenados cronologicamente
    private List<String> mesesOrdenados = new ArrayList<>();

    /*
     - Converte o DTO num mapa de atributos para a view.
     - Este método permite fazer model.addAllAttributes(...)
     - no controller, para reduzir código repetido.*/
    public Map<String, Object> toModelAttributes() {
        Map<String, Object> attrs = new HashMap<>();

        attrs.put("listaCidadaos", listaCidadaos);
        attrs.put("listaRegistos", listaRegistos);
        attrs.put("listaVeiculos", listaVeiculos);
        attrs.put("matriculaPorVeiculo", matriculaPorVeiculo);
        attrs.put("combustivelPorVeiculo", combustivelPorVeiculo);

        attrs.put("totalKmsGeral", totalKmsGeral);
        attrs.put("totalCo2Geral", totalCo2Geral);

        attrs.put("totalKmsPorVeiculo", totalKmsPorVeiculo);
        attrs.put("totalCo2PorVeiculo", totalCo2PorVeiculo);

        attrs.put("totalKmsPorCombustivel", totalKmsPorCombustivel);
        attrs.put("totalCo2PorCombustivel", totalCo2PorCombustivel);
        attrs.put("percentagemKmsPorCombustivel", percentagemKmsPorCombustivel);
        attrs.put("percentagemCo2PorCombustivel", percentagemCo2PorCombustivel);

        attrs.put("totalKmsPorMes", totalKmsPorMes);
        attrs.put("totalCo2PorMes", totalCo2PorMes);
        attrs.put("mediaCo2PorHabitantePorMes", mediaCo2PorHabitantePorMes);
        attrs.put("objetivoAtingidoPorMes", objetivoAtingidoPorMes);

        attrs.put("numeroHabitantes", numeroHabitantes);
        attrs.put("quantidadeVeiculosTotais", quantidadeVeiculosTotais);

        attrs.put("mediaEmissoesPorMes", mediaEmissoesPorMes);
        attrs.put("mediaGlobalEmissoesMensais", mediaGlobalEmissoesMensais);

        attrs.put("variacaoAnoAnteriorPorMes", variacaoAnoAnteriorPorMes);
        attrs.put("corComparacaoAnoAnteriorPorMes", corComparacaoAnoAnteriorPorMes);

        attrs.put("variacaoMesAnterior", variacaoMesAnterior);
        attrs.put("corComparacaoMesAnterior", corComparacaoMesAnterior);

        attrs.put("evolucaoEmissoesMensais", evolucaoEmissoesMensais);
        attrs.put("emissaoMediaPorCombustivel", emissaoMediaPorCombustivel);

        attrs.put("quantidadeVeiculosPorCombustivel", quantidadeVeiculosPorCombustivel);

        attrs.put("mediaPorVeiculo", mediaPorVeiculo);
        attrs.put("mesAtualCo2", mesAtualCo2);
        attrs.put("mesAtualLabel", mesAtualLabel);

        attrs.put("mediaAnoAnterior", mediaAnoAnterior);
        attrs.put("anoAnterior", anoAnterior);
        attrs.put("mediaAnoAtual", mediaAnoAtual);
        attrs.put("anoAtual", anoAtual);

        attrs.put("nivelMunicipio", nivelMunicipio);
        attrs.put("nivelMunicipioPct", nivelMunicipioPct);
        attrs.put("nivelMunicipioIndex", nivelMunicipioIndex);
        attrs.put("mesesAtingidos", mesesAtingidos);

        return attrs;
    }

    // GETTERS E SETTERS
    public List<Cidadao> getListaCidadaos() {
        return listaCidadaos;
    }

    public void setListaCidadaos(List<Cidadao> listaCidadaos) {
        this.listaCidadaos = listaCidadaos;
    }

    public List<RegistoKms> getListaRegistos() {
        return listaRegistos;
    }

    public void setListaRegistos(List<RegistoKms> listaRegistos) {
        this.listaRegistos = listaRegistos;
    }

    public List<Veiculo> getListaVeiculos() {
        return listaVeiculos;
    }

    public void setListaVeiculos(List<Veiculo> listaVeiculos) {
        this.listaVeiculos = listaVeiculos;
    }

    public Set<Long> getIdsVeiculosUnicos() {
        return idsVeiculosUnicos;
    }

    public void setIdsVeiculosUnicos(Set<Long> idsVeiculosUnicos) {
        this.idsVeiculosUnicos = idsVeiculosUnicos;
    }

    public Map<Long, String> getMatriculaPorVeiculo() {
        return matriculaPorVeiculo;
    }

    public void setMatriculaPorVeiculo(Map<Long, String> matriculaPorVeiculo) {
        this.matriculaPorVeiculo = matriculaPorVeiculo;
    }

    public Map<Long, String> getCombustivelPorVeiculo() {
        return combustivelPorVeiculo;
    }

    public void setCombustivelPorVeiculo(Map<Long, String> combustivelPorVeiculo) {
        this.combustivelPorVeiculo = combustivelPorVeiculo;
    }

    public Map<Long, Double> getTotalKmsPorVeiculo() {
        return totalKmsPorVeiculo;
    }

    public void setTotalKmsPorVeiculo(Map<Long, Double> totalKmsPorVeiculo) {
        this.totalKmsPorVeiculo = totalKmsPorVeiculo;
    }

    public Map<Long, Double> getTotalCo2PorVeiculo() {
        return totalCo2PorVeiculo;
    }

    public void setTotalCo2PorVeiculo(Map<Long, Double> totalCo2PorVeiculo) {
        this.totalCo2PorVeiculo = totalCo2PorVeiculo;
    }

    public Map<String, Double> getTotalKmsPorCombustivel() {
        return totalKmsPorCombustivel;
    }

    public void setTotalKmsPorCombustivel(Map<String, Double> totalKmsPorCombustivel) {
        this.totalKmsPorCombustivel = totalKmsPorCombustivel;
    }

    public Map<String, Double> getTotalCo2PorCombustivel() {
        return totalCo2PorCombustivel;
    }

    public void setTotalCo2PorCombustivel(Map<String, Double> totalCo2PorCombustivel) {
        this.totalCo2PorCombustivel = totalCo2PorCombustivel;
    }

    public Map<String, Double> getPercentagemKmsPorCombustivel() {
        return percentagemKmsPorCombustivel;
    }

    public void setPercentagemKmsPorCombustivel(Map<String, Double> percentagemKmsPorCombustivel) {
        this.percentagemKmsPorCombustivel = percentagemKmsPorCombustivel;
    }

    public Map<String, Double> getPercentagemCo2PorCombustivel() {
        return percentagemCo2PorCombustivel;
    }

    public void setPercentagemCo2PorCombustivel(Map<String, Double> percentagemCo2PorCombustivel) {
        this.percentagemCo2PorCombustivel = percentagemCo2PorCombustivel;
    }

    public Map<String, Integer> getNumeroRegistosPorCombustivel() {
        return numeroRegistosPorCombustivel;
    }

    public void setNumeroRegistosPorCombustivel(Map<String, Integer> numeroRegistosPorCombustivel) {
        this.numeroRegistosPorCombustivel = numeroRegistosPorCombustivel;
    }

    public Map<String, Double> getEmissaoMediaPorCombustivel() {
        return emissaoMediaPorCombustivel;
    }

    public void setEmissaoMediaPorCombustivel(Map<String, Double> emissaoMediaPorCombustivel) {
        this.emissaoMediaPorCombustivel = emissaoMediaPorCombustivel;
    }

    public Map<String, Double> getTotalKmsPorMes() {
        return totalKmsPorMes;
    }

    public void setTotalKmsPorMes(Map<String, Double> totalKmsPorMes) {
        this.totalKmsPorMes = totalKmsPorMes;
    }

    public Map<String, Double> getTotalCo2PorMes() {
        return totalCo2PorMes;
    }

    public void setTotalCo2PorMes(Map<String, Double> totalCo2PorMes) {
        this.totalCo2PorMes = totalCo2PorMes;
    }

    public Map<String, Double> getMediaCo2PorHabitantePorMes() {
        return mediaCo2PorHabitantePorMes;
    }

    public void setMediaCo2PorHabitantePorMes(Map<String, Double> mediaCo2PorHabitantePorMes) {
        this.mediaCo2PorHabitantePorMes = mediaCo2PorHabitantePorMes;
    }

    public Map<String, Boolean> getObjetivoAtingidoPorMes() {
        return objetivoAtingidoPorMes;
    }

    public void setObjetivoAtingidoPorMes(Map<String, Boolean> objetivoAtingidoPorMes) {
        this.objetivoAtingidoPorMes = objetivoAtingidoPorMes;
    }

    public Map<String, Double> getMediaEmissoesPorMes() {
        return mediaEmissoesPorMes;
    }

    public void setMediaEmissoesPorMes(Map<String, Double> mediaEmissoesPorMes) {
        this.mediaEmissoesPorMes = mediaEmissoesPorMes;
    }

    public Map<String, Double> getVariacaoAnoAnteriorPorMes() {
        return variacaoAnoAnteriorPorMes;
    }

    public void setVariacaoAnoAnteriorPorMes(Map<String, Double> variacaoAnoAnteriorPorMes) {
        this.variacaoAnoAnteriorPorMes = variacaoAnoAnteriorPorMes;
    }

    public Map<String, String> getCorComparacaoAnoAnteriorPorMes() {
        return corComparacaoAnoAnteriorPorMes;
    }

    public void setCorComparacaoAnoAnteriorPorMes(Map<String, String> corComparacaoAnoAnteriorPorMes) {
        this.corComparacaoAnoAnteriorPorMes = corComparacaoAnoAnteriorPorMes;
    }

    public Map<String, Double> getVariacaoMesAnterior() {
        return variacaoMesAnterior;
    }

    public void setVariacaoMesAnterior(Map<String, Double> variacaoMesAnterior) {
        this.variacaoMesAnterior = variacaoMesAnterior;
    }

    public Map<String, String> getCorComparacaoMesAnterior() {
        return corComparacaoMesAnterior;
    }

    public void setCorComparacaoMesAnterior(Map<String, String> corComparacaoMesAnterior) {
        this.corComparacaoMesAnterior = corComparacaoMesAnterior;
    }

    public Map<String, Double> getEvolucaoEmissoesMensais() {
        return evolucaoEmissoesMensais;
    }

    public void setEvolucaoEmissoesMensais(Map<String, Double> evolucaoEmissoesMensais) {
        this.evolucaoEmissoesMensais = evolucaoEmissoesMensais;
    }

    public Map<String, Integer> getQuantidadeVeiculosPorCombustivel() {
        return quantidadeVeiculosPorCombustivel;
    }

    public void setQuantidadeVeiculosPorCombustivel(Map<String, Integer> quantidadeVeiculosPorCombustivel) {
        this.quantidadeVeiculosPorCombustivel = quantidadeVeiculosPorCombustivel;
    }

    public double getTotalKmsGeral() {
        return totalKmsGeral;
    }

    public void setTotalKmsGeral(double totalKmsGeral) {
        this.totalKmsGeral = totalKmsGeral;
    }

    public double getTotalCo2Geral() {
        return totalCo2Geral;
    }

    public void setTotalCo2Geral(double totalCo2Geral) {
        this.totalCo2Geral = totalCo2Geral;
    }

    public double getSomaEmissoesMensais() {
        return somaEmissoesMensais;
    }

    public void setSomaEmissoesMensais(double somaEmissoesMensais) {
        this.somaEmissoesMensais = somaEmissoesMensais;
    }

    public double getMediaGlobalEmissoesMensais() {
        return mediaGlobalEmissoesMensais;
    }

    public void setMediaGlobalEmissoesMensais(double mediaGlobalEmissoesMensais) {
        this.mediaGlobalEmissoesMensais = mediaGlobalEmissoesMensais;
    }

    public double getMediaPorVeiculo() {
        return mediaPorVeiculo;
    }

    public void setMediaPorVeiculo(double mediaPorVeiculo) {
        this.mediaPorVeiculo = mediaPorVeiculo;
    }

    public double getMesAtualCo2() {
        return mesAtualCo2;
    }

    public void setMesAtualCo2(double mesAtualCo2) {
        this.mesAtualCo2 = mesAtualCo2;
    }

    public double getMediaAnoAnterior() {
        return mediaAnoAnterior;
    }

    public void setMediaAnoAnterior(double mediaAnoAnterior) {
        this.mediaAnoAnterior = mediaAnoAnterior;
    }

    public double getMediaAnoAtual() {
        return mediaAnoAtual;
    }

    public void setMediaAnoAtual(double mediaAnoAtual) {
        this.mediaAnoAtual = mediaAnoAtual;
    }

    public int getNumeroHabitantes() {
        return numeroHabitantes;
    }

    public void setNumeroHabitantes(int numeroHabitantes) {
        this.numeroHabitantes = numeroHabitantes;
    }

    public int getQuantidadeVeiculosTotais() {
        return quantidadeVeiculosTotais;
    }

    public void setQuantidadeVeiculosTotais(int quantidadeVeiculosTotais) {
        this.quantidadeVeiculosTotais = quantidadeVeiculosTotais;
    }

    public int getAnoAnterior() {
        return anoAnterior;
    }

    public void setAnoAnterior(int anoAnterior) {
        this.anoAnterior = anoAnterior;
    }

    public int getAnoAtual() {
        return anoAtual;
    }

    public void setAnoAtual(int anoAtual) {
        this.anoAtual = anoAtual;
    }

    public int getNivelMunicipioPct() {
        return nivelMunicipioPct;
    }

    public void setNivelMunicipioPct(int nivelMunicipioPct) {
        this.nivelMunicipioPct = nivelMunicipioPct;
    }

    public int getNivelMunicipioIndex() {
        return nivelMunicipioIndex;
    }

    public void setNivelMunicipioIndex(int nivelMunicipioIndex) {
        this.nivelMunicipioIndex = nivelMunicipioIndex;
    }

    public int getMesesAtingidos() {
        return mesesAtingidos;
    }

    public void setMesesAtingidos(int mesesAtingidos) {
        this.mesesAtingidos = mesesAtingidos;
    }

    public String getMesAtualLabel() {
        return mesAtualLabel;
    }

    public void setMesAtualLabel(String mesAtualLabel) {
        this.mesAtualLabel = mesAtualLabel;
    }

    public String getNivelMunicipio() {
        return nivelMunicipio;
    }

    public void setNivelMunicipio(String nivelMunicipio) {
        this.nivelMunicipio = nivelMunicipio;
    }

    public List<String> getMesesOrdenados() {
        return mesesOrdenados;
    }

    public void setMesesOrdenados(List<String> mesesOrdenados) {
        this.mesesOrdenados = mesesOrdenados;
    }
}