package pt.upskill.smart_city_co2.dto;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DTODashboardCidadaoService {

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Transactional(readOnly = true)
    public DashboardDataDTO prepararDadosDashboard(Cidadao cidadao) {
        // Buscar cidadão com veículos (mas sem registos para evitar MultipleBagFetch)
        Cidadao cidadaoCompleto = cidadaoRepository.findByIdWithVeiculos(cidadao.getId())
                .orElseThrow(() -> new RuntimeException("Cidadão não encontrado"));

        // Agora inicializar os registos de cada veículo separadamente
        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                // Forçar inicialização dos registos dentro da transação
                Hibernate.initialize(ownership.getRegistosKms());
            }
        }

        DashboardDataDTO dados = new DashboardDataDTO();

        if (cidadaoCompleto == null || cidadaoCompleto.getListaDeVeiculos() == null) {
            return dados.empty();
        }

        List<Ownership> ownerships = cidadaoCompleto.getListaDeVeiculos();

        // Inicializar estruturas
        List<Veiculo> listaVeiculos = new ArrayList<>();
        Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();
        List<Map<String, Object>> listaRegistos = new ArrayList<>();
        Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();

        double somaTodosKms = 0.0;
        double somaTodosCo2 = 0.0;
        int totalRegistosCount = 0;

        // Dados do mês atual vs ano passado
        java.time.LocalDate hoje = java.time.LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();
        int anoPassado = anoAtual - 1;
        double kmsMesAtual = 0.0;
        double co2MesAtual = 0.0;
        double co2MesmoMesAnoPassado = 0.0;

        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");

        for (Ownership ownership : ownerships) {
            Veiculo veiculo = ownership.getVeiculo();
            if (veiculo == null) continue;

            Long veiculoId = veiculo.getId();
            String combustivel = veiculo.getTipoDeCombustivel().name();

            listaVeiculos.add(veiculo);
            matriculaPorVeiculo.put(veiculoId, ownership.getMatricula());
            totalKmsPorVeiculo.put(veiculoId, 0.0);
            totalCo2PorVeiculo.put(veiculoId, 0.0);

            totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
            totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);

            if (ownership.getRegistosKms() != null) {
                for (RegistoKms registo : ownership.getRegistosKms()) {
                    double kms = registo.getKms_mes();
                    double co2 = registo.getEmissaoEfetivaKg();

                    somaTodosKms += kms;
                    somaTodosCo2 += co2;
                    totalRegistosCount++;

                    totalKmsPorVeiculo.put(veiculoId, totalKmsPorVeiculo.get(veiculoId) + kms);
                    totalCo2PorVeiculo.put(veiculoId, totalCo2PorVeiculo.get(veiculoId) + co2);

                    totalKmsPorCombustivel.put(combustivel, totalKmsPorCombustivel.get(combustivel) + kms);
                    totalCo2PorCombustivel.put(combustivel, totalCo2PorCombustivel.get(combustivel) + co2);

                    // Adicionar registo formatado
                    Map<String, Object> registoMap = new LinkedHashMap<>();
                    if (registo.getMes_ano() != null) {
                        registoMap.put("mes", sdf.format(registo.getMes_ano()));
                    } else {
                        registoMap.put("mes", "Data desconhecida");
                    }
                    registoMap.put("veiculo", veiculo.getMarca() + " " + veiculo.getModelo());
                    registoMap.put("matricula", ownership.getMatricula());
                    registoMap.put("kms", kms);
                    registoMap.put("emissoes", co2);
                    listaRegistos.add(registoMap);

                    // Calcular mês atual vs ano passado
                    if (registo.getMes_ano() != null) {
                        java.time.LocalDate dataRegisto = new java.sql.Date(registo.getMes_ano().getTime()).toLocalDate();
                        if (dataRegisto.getMonthValue() == mesAtual && dataRegisto.getYear() == anoAtual) {
                            kmsMesAtual += kms;
                            co2MesAtual += co2;
                        }
                        if (dataRegisto.getMonthValue() == mesAtual && dataRegisto.getYear() == anoPassado) {
                            co2MesmoMesAnoPassado += co2;
                        }
                    }
                }
            }
        }

        // Ordenar registos por data decrescente
        listaRegistos.sort((a, b) -> {
            String mesA = (String) a.get("mes");
            String mesB = (String) b.get("mes");
            if (mesA.equals("Data desconhecida")) return 1;
            if (mesB.equals("Data desconhecida")) return -1;
            return mesB.compareTo(mesA);
        });

        // Calcular médias
        double mediaKmsMensal = totalRegistosCount > 0 ? somaTodosKms / totalRegistosCount : 0.0;
        double mediaCo2Mensal = totalRegistosCount > 0 ? somaTodosCo2 / totalRegistosCount : 0.0;

        // Calcular dados por combustível com percentagens e cores
        Map<String, String> coresCombustiveis = getCoresCombustiveis();
        List<Map<String, Object>> combustiveisData = new ArrayList<>();
        for (String combustivel : totalKmsPorCombustivel.keySet()) {
            Map<String, Object> combustivelMap = new LinkedHashMap<>();
            double kmsCombustivel = totalKmsPorCombustivel.getOrDefault(combustivel, 0.0);
            double co2Combustivel = totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);

            double percentagemKms = somaTodosKms > 0 ? (kmsCombustivel / somaTodosKms) * 100 : 0.0;
            double percentagemCo2 = somaTodosCo2 > 0 ? (co2Combustivel / somaTodosCo2) * 100 : 0.0;

            combustivelMap.put("tipo", combustivel);
            combustivelMap.put("kms", kmsCombustivel);
            combustivelMap.put("percentagemKms", percentagemKms);
            combustivelMap.put("emissoes", co2Combustivel);
            combustivelMap.put("percentagemCo2", percentagemCo2);
            combustivelMap.put("cor", coresCombustiveis.getOrDefault(combustivel, "#999999"));
            combustiveisData.add(combustivelMap);
        }

        // Calcular ranking - passando o objeto diretamente
        int posicaoRanking = calcularPosicaoRanking(cidadaoCompleto);
        int totalCidadaos = (int) cidadaoRepository.count();

        dados.setListaVeiculos(listaVeiculos);
        dados.setMatriculaPorVeiculo(matriculaPorVeiculo);
        dados.setTotalKmsPorVeiculo(totalKmsPorVeiculo);
        dados.setTotalCo2PorVeiculo(totalCo2PorVeiculo);
        dados.setMediaKmsMensal(mediaKmsMensal);
        dados.setMediaCo2Mensal(mediaCo2Mensal);
        dados.setKmsMesAtual(kmsMesAtual);
        dados.setCo2MesAtual(co2MesAtual);
        dados.setCo2MesmoMesAnoPassado(co2MesmoMesAnoPassado);
        dados.setListaRegistos(listaRegistos);
        dados.setTotalKmsGeral(somaTodosKms);
        dados.setTotalCo2Geral(somaTodosCo2);
        dados.setCombustiveisData(combustiveisData);
        dados.setPosicaoRankingPoluicao(posicaoRanking);
        dados.setNumeroTotalCidadaos(totalCidadaos);

        return dados;
    }

    @Transactional(readOnly = true)
    public DashboardDataDTO prepararDadosCharts(Cidadao cidadao) {
        // Buscar cidadão com veículos
        Cidadao cidadaoCompleto = cidadaoRepository.findByIdWithVeiculos(cidadao.getId())
                .orElseThrow(() -> new RuntimeException("Cidadão não encontrado"));

        // Inicializar registos
        if (cidadaoCompleto.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadaoCompleto.getListaDeVeiculos()) {
                Hibernate.initialize(ownership.getRegistosKms());
            }
        }

        // Reutilizar a lógica existente do prepararDadosDashboard
        return prepararDadosDashboard(cidadaoCompleto);
    }

    private int calcularPosicaoRanking(Cidadao cidadao) {
        // Buscar todos os cidadãos com veículos (sem registos para evitar MultipleBagFetch)
        List<Cidadao> todosCidadaos = cidadaoRepository.findAllWithVeiculos();

        Map<Long, Double> totalCo2PorCidadao = new LinkedHashMap<>();

        for (Cidadao outro : todosCidadaos) {
            double total = 0.0;
            if (outro.getListaDeVeiculos() != null) {
                for (Ownership ownership : outro.getListaDeVeiculos()) {
                    // Inicializar registos de cada ownership
                    Hibernate.initialize(ownership.getRegistosKms());

                    if (ownership.getRegistosKms() != null) {
                        total += ownership.getRegistosKms().stream()
                                .mapToDouble(RegistoKms::getEmissaoEfetivaKg)
                                .sum();
                    }
                }
            }
            totalCo2PorCidadao.put(outro.getId(), total);
        }

        List<Map.Entry<Long, Double>> ranking = new ArrayList<>(totalCo2PorCidadao.entrySet());
        ranking.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).getKey().equals(cidadao.getId())) {
                return i + 1;
            }
        }
        return 0;
    }

    private Map<String, String> getCoresCombustiveis() {
        Map<String, String> cores = new HashMap<>();
        cores.put("GASOLINA", "#FF6384");
        cores.put("DIESEL", "#36A2EB");
        cores.put("HIBRIDO", "#FFCE56");
        cores.put("GPL", "#4BC0C0");
        cores.put("ELETRICO", "#9966FF");
        return cores;
    }

    // DTO interno (mantém o mesmo)
    public static class DashboardDataDTO {
        private List<Veiculo> listaVeiculos = new ArrayList<>();
        private Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
        private Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
        private Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();
        private double mediaKmsMensal = 0.0;
        private double mediaCo2Mensal = 0.0;
        private double kmsMesAtual = 0.0;
        private double co2MesAtual = 0.0;
        private double co2MesmoMesAnoPassado = 0.0;
        private List<Map<String, Object>> listaRegistos = new ArrayList<>();
        private double totalKmsGeral = 0.0;
        private double totalCo2Geral = 0.0;
        private List<Map<String, Object>> combustiveisData = new ArrayList<>();
        private int posicaoRankingPoluicao = 0;
        private int numeroTotalCidadaos = 0;

        public DashboardDataDTO empty() {
            DashboardDataDTO empty = new DashboardDataDTO();
            empty.setListaVeiculos(new ArrayList<>());
            empty.setMatriculaPorVeiculo(new LinkedHashMap<>());
            empty.setTotalKmsPorVeiculo(new LinkedHashMap<>());
            empty.setTotalCo2PorVeiculo(new LinkedHashMap<>());
            empty.setListaRegistos(new ArrayList<>());
            empty.setCombustiveisData(new ArrayList<>());
            return empty;
        }

        // Getters e Setters
        public List<Veiculo> getListaVeiculos() { return listaVeiculos; }
        public void setListaVeiculos(List<Veiculo> listaVeiculos) { this.listaVeiculos = listaVeiculos; }
        public Map<Long, String> getMatriculaPorVeiculo() { return matriculaPorVeiculo; }
        public void setMatriculaPorVeiculo(Map<Long, String> matriculaPorVeiculo) { this.matriculaPorVeiculo = matriculaPorVeiculo; }
        public Map<Long, Double> getTotalKmsPorVeiculo() { return totalKmsPorVeiculo; }
        public void setTotalKmsPorVeiculo(Map<Long, Double> totalKmsPorVeiculo) { this.totalKmsPorVeiculo = totalKmsPorVeiculo; }
        public Map<Long, Double> getTotalCo2PorVeiculo() { return totalCo2PorVeiculo; }
        public void setTotalCo2PorVeiculo(Map<Long, Double> totalCo2PorVeiculo) { this.totalCo2PorVeiculo = totalCo2PorVeiculo; }
        public double getMediaKmsMensal() { return mediaKmsMensal; }
        public void setMediaKmsMensal(double mediaKmsMensal) { this.mediaKmsMensal = mediaKmsMensal; }
        public double getMediaCo2Mensal() { return mediaCo2Mensal; }
        public void setMediaCo2Mensal(double mediaCo2Mensal) { this.mediaCo2Mensal = mediaCo2Mensal; }
        public double getKmsMesAtual() { return kmsMesAtual; }
        public void setKmsMesAtual(double kmsMesAtual) { this.kmsMesAtual = kmsMesAtual; }
        public double getCo2MesAtual() { return co2MesAtual; }
        public void setCo2MesAtual(double co2MesAtual) { this.co2MesAtual = co2MesAtual; }
        public double getCo2MesmoMesAnoPassado() { return co2MesmoMesAnoPassado; }
        public void setCo2MesmoMesAnoPassado(double co2MesmoMesAnoPassado) { this.co2MesmoMesAnoPassado = co2MesmoMesAnoPassado; }
        public List<Map<String, Object>> getListaRegistos() { return listaRegistos; }
        public void setListaRegistos(List<Map<String, Object>> listaRegistos) { this.listaRegistos = listaRegistos; }
        public double getTotalKmsGeral() { return totalKmsGeral; }
        public void setTotalKmsGeral(double totalKmsGeral) { this.totalKmsGeral = totalKmsGeral; }
        public double getTotalCo2Geral() { return totalCo2Geral; }
        public void setTotalCo2Geral(double totalCo2Geral) { this.totalCo2Geral = totalCo2Geral; }
        public List<Map<String, Object>> getCombustiveisData() { return combustiveisData; }
        public void setCombustiveisData(List<Map<String, Object>> combustiveisData) { this.combustiveisData = combustiveisData; }
        public int getPosicaoRankingPoluicao() { return posicaoRankingPoluicao; }
        public void setPosicaoRankingPoluicao(int posicaoRankingPoluicao) { this.posicaoRankingPoluicao = posicaoRankingPoluicao; }
        public int getNumeroTotalCidadaos() { return numeroTotalCidadaos; }
        public void setNumeroTotalCidadaos(int numeroTotalCidadaos) { this.numeroTotalCidadaos = numeroTotalCidadaos; }
    }
}