package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.RegistoKmsRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/charts")
public class ChartsDataController {

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private RegistoKmsRepository registoKmsRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/cidadao/emissoes")
    public ResponseEntity<Map<String, Object>> getEmissoesMensais() {
        User user = getAuthenticatedUser();
        if (user == null || !(user instanceof Cidadao)) {
            return ResponseEntity.status(401).build();
        }

        Cidadao cidadao = cidadaoRepository.findById(user.getId()).orElse(null);
        if (cidadao == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        Map<String, Double> emissoesPorMes = new LinkedHashMap<>();

        if (cidadao.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms registo : ownership.getRegistosKms()) {
                        if (registo.getMes_ano() != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
                            String mesAno = sdf.format(registo.getMes_ano());

                            double emissaoAtual = emissoesPorMes.getOrDefault(mesAno, 0.0);
                            emissoesPorMes.put(mesAno, emissaoAtual + registo.getEmissaoEfetivaKg());
                        }
                    }
                }
            }
        }

        List<String> meses = new ArrayList<>(emissoesPorMes.keySet());
        List<Double> emissoes = new ArrayList<>(emissoesPorMes.values());
        double totalEmissoes = emissoes.stream().mapToDouble(Double::doubleValue).sum();

        response.put("meses", meses);
        response.put("emissoes", emissoes);
        response.put("totalEmissoes", totalEmissoes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cidadao/dados-dashboard")
    public ResponseEntity<Map<String, Object>> getDadosDashboard() {
        User user = getAuthenticatedUser();
        if (user == null || !(user instanceof Cidadao)) {
            return ResponseEntity.status(401).build();
        }

        Cidadao cidadao = cidadaoRepository.findById(user.getId()).orElse(null);
        if (cidadao == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();

        // Lista de registos para o histórico
        List<Map<String, Object>> listaRegistos = new ArrayList<>();

        // Totais gerais
        double totalKmsGeral = 0.0;
        double totalCo2Geral = 0.0;

        // Dados por veículo
        List<Map<String, Object>> veiculosData = new ArrayList<>();
        Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

        // Dados por combustível
        Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();

        // Dados por mês para o gráfico de evolução
        Map<String, Double> emissoesPorMes = new LinkedHashMap<>();

        if (cidadao.getListaDeVeiculos() != null) {
            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();
                Long veiculoId = veiculo.getId();
                String combustivel = veiculo.getTipoDeCombustivel().name();
                String matricula = ownership.getMatricula();

                totalKmsPorVeiculo.put(veiculoId, 0.0);
                totalCo2PorVeiculo.put(veiculoId, 0.0);
                totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
                totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);
            }

            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                Veiculo veiculo = ownership.getVeiculo();
                Long veiculoId = veiculo.getId();
                String combustivel = veiculo.getTipoDeCombustivel().name();
                String matricula = ownership.getMatricula();
                double totalEmissoesVeiculo = 0.0;
                double totalKmsVeiculo = 0.0;

                if (ownership.getRegistosKms() != null) {
                    for (RegistoKms registo : ownership.getRegistosKms()) {
                        double kms = registo.getKms_mes();
                        double co2 = registo.getEmissaoEfetivaKg();

                        totalKmsGeral += kms;
                        totalCo2Geral += co2;

                        totalKmsVeiculo += kms;
                        totalEmissoesVeiculo += co2;

                        totalKmsPorVeiculo.put(veiculoId, totalKmsPorVeiculo.getOrDefault(veiculoId, 0.0) + kms);
                        totalCo2PorVeiculo.put(veiculoId, totalCo2PorVeiculo.getOrDefault(veiculoId, 0.0) + co2);

                        totalKmsPorCombustivel.put(combustivel, totalKmsPorCombustivel.getOrDefault(combustivel, 0.0) + kms);
                        totalCo2PorCombustivel.put(combustivel, totalCo2PorCombustivel.getOrDefault(combustivel, 0.0) + co2);

                        // Adicionar registo à lista
                        Map<String, Object> registoMap = new HashMap<>();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
                        registoMap.put("mes", sdf.format(registo.getMes_ano()));
                        registoMap.put("veiculo", veiculo.getMarca() + " " + veiculo.getModelo());
                        registoMap.put("matricula", matricula);
                        registoMap.put("kms", kms);
                        registoMap.put("emissoes", co2);
                        listaRegistos.add(registoMap);

                        // Agrupar por mês para o gráfico de evolução
                        String mesAno = sdf.format(registo.getMes_ano());
                        emissoesPorMes.put(mesAno, emissoesPorMes.getOrDefault(mesAno, 0.0) + co2);
                    }
                }

                // Adicionar veículo à lista de dados
                Map<String, Object> veiculoData = new HashMap<>();
                veiculoData.put("id", veiculo.getId());
                veiculoData.put("matricula", matricula);
                veiculoData.put("marca", veiculo.getMarca());
                veiculoData.put("modelo", veiculo.getModelo());
                veiculoData.put("combustivel", combustivel);
                veiculoData.put("emissoes", totalEmissoesVeiculo);
                veiculoData.put("kms", totalKmsVeiculo);
                veiculosData.add(veiculoData);
            }
        }

        // Calcular percentagens por combustível
        List<Map<String, Object>> combustiveisData = new ArrayList<>();
        for (String combustivel : totalKmsPorCombustivel.keySet()) {
            Map<String, Object> combustivelMap = new HashMap<>();
            double kmsCombustivel = totalKmsPorCombustivel.getOrDefault(combustivel, 0.0);
            double co2Combustivel = totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);

            double percentagemKms = totalKmsGeral > 0 ? (kmsCombustivel / totalKmsGeral) * 100 : 0.0;
            double percentagemCo2 = totalCo2Geral > 0 ? (co2Combustivel / totalCo2Geral) * 100 : 0.0;

            combustivelMap.put("tipo", combustivel);
            combustivelMap.put("kms", kmsCombustivel);
            combustivelMap.put("percentagemKms", percentagemKms);
            combustivelMap.put("emissoes", co2Combustivel);
            combustivelMap.put("percentagemCo2", percentagemCo2);
            combustiveisData.add(combustivelMap);
        }

        // Ranking de poluição
        List<Cidadao> todosCidadaos = cidadaoRepository.findAll();
        Map<Long, Double> totalCo2PorCidadao = new LinkedHashMap<>();

        for (Cidadao outroCidadao : todosCidadaos) {
            double totalCo2Cidadao = 0.0;
            List<Ownership> ownershipsOutro = outroCidadao.getListaDeVeiculos();
            if (ownershipsOutro != null) {
                for (Ownership ownership : ownershipsOutro) {
                    if (ownership.getRegistosKms() != null) {
                        for (RegistoKms registo : ownership.getRegistosKms()) {
                            totalCo2Cidadao += registo.getEmissaoEfetivaKg();
                        }
                    }
                }
            }
            totalCo2PorCidadao.put(outroCidadao.getId(), totalCo2Cidadao);
        }

        List<Map.Entry<Long, Double>> rankingPoluidores = new ArrayList<>(totalCo2PorCidadao.entrySet());
        rankingPoluidores.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        int posicaoRankingPoluicao = 0;
        for (int i = 0; i < rankingPoluidores.size(); i++) {
            if (rankingPoluidores.get(i).getKey().equals(cidadao.getId())) {
                posicaoRankingPoluicao = i + 1;
                break;
            }
        }

        // Ordenar registos por data decrescente
        listaRegistos.sort((a, b) -> ((String)b.get("mes")).compareTo((String)a.get("mes")));

        // Ordenar veículos por emissões decrescente
        veiculosData.sort((a, b) -> Double.compare((double) b.get("emissoes"), (double) a.get("emissoes")));

        // Preparar dados para o gráfico de evolução
        List<String> meses = new ArrayList<>(emissoesPorMes.keySet());
        List<Double> emissoesMensais = new ArrayList<>(emissoesPorMes.values());

        // Ordenar meses cronologicamente
        Collections.sort(meses);
        emissoesMensais.clear();
        for (String mes : meses) {
            emissoesMensais.add(emissoesPorMes.get(mes));
        }

        response.put("meses", meses);
        response.put("emissoesPorMes", emissoesMensais);
        response.put("veiculosData", veiculosData);
        response.put("combustiveisData", combustiveisData);
        response.put("listaRegistos", listaRegistos);
        response.put("totalKmsGeral", totalKmsGeral);
        response.put("totalCo2Geral", totalCo2Geral);
        response.put("totalKmsPorCombustivel", totalKmsPorCombustivel);
        response.put("totalCo2PorCombustivel", totalCo2PorCombustivel);
        response.put("posicaoRankingPoluicao", posicaoRankingPoluicao);
        response.put("numeroTotalCidadaos", todosCidadaos.size());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/municipio/dados-dashboard")
    public ResponseEntity<Map<String, Object>> getDadosDashboardMunicipio() {
        User user = getAuthenticatedUser();
        if (user == null || !(user instanceof Municipio)) {
            return ResponseEntity.status(401).build();
        }

        Municipio municipio = (Municipio) user;

        Map<String, Object> response = new HashMap<>();

        // Buscar todos os cidadãos do município
        List<Cidadao> cidadaos = cidadaoRepository.findByMunicipioId(municipio.getId());

        // Inicializar estruturas
        Map<String, Double> emissoesPorMes = new LinkedHashMap<>();
        Map<Integer, Double> emissoesPorAno = new LinkedHashMap<>(); // NOVO: para evolução anual
        Map<String, Integer> quantidadePorTipo = new LinkedHashMap<>();
        Map<String, Double> co2PorTipo = new LinkedHashMap<>();
        Map<String, Double> kmsPorTipo = new LinkedHashMap<>();

        // Dados por tipo de combustível para cards
        Map<String, Object> gasolinaData = new HashMap<>();
        Map<String, Object> dieselData = new HashMap<>();
        Map<String, Object> hibridoData = new HashMap<>();
        Map<String, Object> gplData = new HashMap<>();
        Map<String, Object> eletricoData = new HashMap<>();

        // Inicializar contadores
        for (TipoDeCombustivel tipo : TipoDeCombustivel.values()) {
            String nome = tipo.name();
            quantidadePorTipo.put(nome, 0);
            co2PorTipo.put(nome, 0.0);
            kmsPorTipo.put(nome, 0.0);

            // Inicializar dados dos cards
            switch (nome) {
                case "GASOLINA":
                    gasolinaData.put("quantidade", 0);
                    gasolinaData.put("kms", 0.0);
                    gasolinaData.put("co2", 0.0);
                    break;
                case "DIESEL":
                    dieselData.put("quantidade", 0);
                    dieselData.put("kms", 0.0);
                    dieselData.put("co2", 0.0);
                    break;
                case "HIBRIDO":
                    hibridoData.put("quantidade", 0);
                    hibridoData.put("kms", 0.0);
                    hibridoData.put("co2", 0.0);
                    break;
                case "GPL":
                    gplData.put("quantidade", 0);
                    gplData.put("kms", 0.0);
                    gplData.put("co2", 0.0);
                    break;
                case "ELETRICO":
                    eletricoData.put("quantidade", 0);
                    eletricoData.put("kms", 0.0);
                    break;
            }
        }

        // Processar dados
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);

        for (Cidadao cidadao : cidadaos) {
            if (cidadao.getListaDeVeiculos() == null) continue;

            for (Ownership ownership : cidadao.getListaDeVeiculos()) {
                if (ownership == null || ownership.getVeiculo() == null) continue;

                Veiculo veiculo = ownership.getVeiculo();
                String tipo = veiculo.getTipoDeCombustivel().name();

                // Contar veículos únicos por tipo
                quantidadePorTipo.put(tipo, quantidadePorTipo.getOrDefault(tipo, 0) + 1);

                // Atualizar cards
                switch (tipo) {
                    case "GASOLINA":
                        gasolinaData.put("quantidade", (int) gasolinaData.get("quantidade") + 1);
                        break;
                    case "DIESEL":
                        dieselData.put("quantidade", (int) dieselData.get("quantidade") + 1);
                        break;
                    case "HIBRIDO":
                        hibridoData.put("quantidade", (int) hibridoData.get("quantidade") + 1);
                        break;
                    case "GPL":
                        gplData.put("quantidade", (int) gplData.get("quantidade") + 1);
                        break;
                    case "ELETRICO":
                        eletricoData.put("quantidade", (int) eletricoData.get("quantidade") + 1);
                        break;
                }

                if (ownership.getRegistosKms() == null) continue;

                for (RegistoKms registo : ownership.getRegistosKms()) {
                    if (registo == null || registo.getMes_ano() == null) continue;

                    double kms = registo.getKms_mes();
                    double co2 = registo.getEmissaoEfetivaKg();

                    // Dados do ano atual para evolução mensal
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(registo.getMes_ano());
                    int ano = cal.get(Calendar.YEAR);

                    if (ano == anoAtual) {
                        String mesAno = sdf.format(registo.getMes_ano());
                        emissoesPorMes.put(mesAno, emissoesPorMes.getOrDefault(mesAno, 0.0) + co2);
                    }

                    // Dados para evolução anual (soma por ano)
                    emissoesPorAno.put(ano, emissoesPorAno.getOrDefault(ano, 0.0) + co2);

                    // Totais por tipo
                    kmsPorTipo.put(tipo, kmsPorTipo.getOrDefault(tipo, 0.0) + kms);
                    co2PorTipo.put(tipo, co2PorTipo.getOrDefault(tipo, 0.0) + co2);

                    // Atualizar cards
                    switch (tipo) {
                        case "GASOLINA":
                            gasolinaData.put("kms", (double) gasolinaData.get("kms") + kms);
                            gasolinaData.put("co2", (double) gasolinaData.get("co2") + co2);
                            break;
                        case "DIESEL":
                            dieselData.put("kms", (double) dieselData.get("kms") + kms);
                            dieselData.put("co2", (double) dieselData.get("co2") + co2);
                            break;
                        case "HIBRIDO":
                            hibridoData.put("kms", (double) hibridoData.get("kms") + kms);
                            hibridoData.put("co2", (double) hibridoData.get("co2") + co2);
                            break;
                        case "GPL":
                            gplData.put("kms", (double) gplData.get("kms") + kms);
                            gplData.put("co2", (double) gplData.get("co2") + co2);
                            break;
                        case "ELETRICO":
                            eletricoData.put("kms", (double) eletricoData.get("kms") + kms);
                            break;
                    }
                }
            }
        }

        // Preparar dados para o gráfico de evolução mensal
        List<String> meses = new ArrayList<>(emissoesPorMes.keySet());
        List<Double> emissoesMensais = new ArrayList<>(emissoesPorMes.values());

        // Ordenar meses cronologicamente
        meses.sort((a, b) -> {
            try {
                SimpleDateFormat sdfOrder = new SimpleDateFormat("MMM/yyyy");
                Date d1 = sdfOrder.parse(a);
                Date d2 = sdfOrder.parse(b);
                return d1.compareTo(d2);
            } catch (Exception e) {
                return a.compareTo(b);
            }
        });

        emissoesMensais.clear();
        for (String mes : meses) {
            emissoesMensais.add(emissoesPorMes.get(mes));
        }

        // Preparar dados para o gráfico de evolução anual
        List<Integer> anos = new ArrayList<>(emissoesPorAno.keySet());
        List<Double> emissoesAnuais = new ArrayList<>(emissoesPorAno.values());

        // Ordenar anos cronologicamente
        Collections.sort(anos);
        emissoesAnuais.clear();
        for (Integer ano : anos) {
            emissoesAnuais.add(emissoesPorAno.get(ano));
        }

        // Preparar dados para gráficos de pizza
        List<Map<String, Object>> frotaPorTipo = new ArrayList<>();
        List<Map<String, Object>> co2PorTipoGrafico = new ArrayList<>();
        List<Map<String, Object>> kmsPorTipoGrafico = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : quantidadePorTipo.entrySet()) {
            String tipo = entry.getKey();
            int quantidade = entry.getValue();
            if (quantidade > 0) {
                Map<String, Object> item = new HashMap<>();
                item.put("tipo", tipo);
                item.put("quantidade", quantidade);
                frotaPorTipo.add(item);

                Map<String, Object> itemCo2 = new HashMap<>();
                itemCo2.put("tipo", tipo);
                itemCo2.put("co2", co2PorTipo.getOrDefault(tipo, 0.0));
                co2PorTipoGrafico.add(itemCo2);

                Map<String, Object> itemKms = new HashMap<>();
                itemKms.put("tipo", tipo);
                itemKms.put("kms", kmsPorTipo.getOrDefault(tipo, 0.0));
                kmsPorTipoGrafico.add(itemKms);
            }
        }

        response.put("meses", meses);
        response.put("emissoesMensais", emissoesMensais);
        response.put("anos", anos);
        response.put("emissoesAnuais", emissoesAnuais); // NOVO: dados para evolução anual
        response.put("frotaPorTipo", frotaPorTipo);
        response.put("co2PorTipo", co2PorTipoGrafico);
        response.put("kmsPorTipo", kmsPorTipoGrafico);

        response.put("gasolinaData", gasolinaData);
        response.put("dieselData", dieselData);
        response.put("hibridoData", hibridoData);
        response.put("gplData", gplData);
        response.put("eletricoData", eletricoData);

        return ResponseEntity.ok(response);
    }
}