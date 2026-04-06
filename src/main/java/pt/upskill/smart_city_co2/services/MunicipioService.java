package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (municipioRepository.count() > 0) {
            return;
        }

        municipioRepository.save(
                new Municipio(
                        "Lisboa",
                        2.5,
                        "lisboa",
                        LocalDateTime.now(),
                        "co2@cm-lisboa.pt",
                        passwordEncoder.encode("lisboa123"),
                        111222333,
                        "municipio",
                        true
                )
        );

        municipioRepository.save(
                new Municipio(
                        "Vila Verde",
                        3.0,
                        "vilaverde",
                        LocalDateTime.now(),
                        "co2@cm-vilaverde.pt",
                        passwordEncoder.encode("vilaverde123"),
                        444555666,
                        "municipio",
                        true
                )
        );
    }

    public List<Municipio> getNomes() {
        return municipioRepository.findAll();
    }

    public Municipio getUserM(String username) {
        return municipioRepository.findByUsername(username).orElse(null);
    }

    @Transactional()
    public List<Cidadao> buscarCidadaosDoMunicipio(Municipio municipio) {
        if (municipio == null) {
            return Collections.emptyList();
        }

        return cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());
    }

    @Transactional()
    public RelatorioMunicipioDados gerarRelatorioMunicipio(Municipio municipio) {
        List<Cidadao> listaCidadaos = cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());
        RelatorioMunicipioDados dados = inicializarDados(listaCidadaos);

        processarCidadaos(listaCidadaos, dados);
        calcularPercentagensPorCombustivel(dados);
        calcularMediasPorCombustivel(dados);
        calcularMetricasMensais(dados, municipio);
        calcularVariacaoMesAnterior(dados);
        calcularVariacaoAnoAnterior(dados);
        calcularEvolucaoMensal(dados);

        return dados;
    }

    private RelatorioMunicipioDados inicializarDados(List<Cidadao> listaCidadaos) {
        RelatorioMunicipioDados dados = new RelatorioMunicipioDados();
        dados.listaCidadaos = listaCidadaos;
        dados.numeroHabitantes = (listaCidadaos != null) ? listaCidadaos.size() : 0;
        return dados;
    }

    private void processarCidadaos(List<Cidadao> listaCidadaos, RelatorioMunicipioDados dados) {
        if (listaCidadaos == null) {
            return;
        }

        for (Cidadao cidadao : listaCidadaos) {
            processarCidadao(cidadao, dados);
        }

        dados.quantidadeVeiculosTotais = dados.idsVeiculosUnicos.size();
    }

    private void processarCidadao(Cidadao cidadao, RelatorioMunicipioDados dados) {
        if (cidadao == null || cidadao.getListaDeVeiculos() == null) {
            return;
        }

        for (Ownership ownership : cidadao.getListaDeVeiculos()) {
            processarOwnership(ownership, dados);
        }
    }

    private void processarOwnership(Ownership ownership, RelatorioMunicipioDados dados) {
        if (ownership == null) {
            return;
        }

        Veiculo veiculo = ownership.getVeiculo();
        if (veiculo == null) {
            return;
        }

        dados.listaVeiculos.add(veiculo);
        dados.idsVeiculosUnicos.add(veiculo.getId());
        dados.matriculaPorVeiculo.put(veiculo.getId(), ownership.getMatricula());

        String combustivel = obterCombustivel(veiculo);
        dados.combustivelPorVeiculo.put(veiculo.getId(), combustivel);

        inicializarEstruturasVeiculoECombustivel(dados, veiculo, combustivel);

        if (ownership.getRegistosKms() == null) {
            return;
        }

        for (RegistoKms registo : ownership.getRegistosKms()) {
            processarRegisto(registo, veiculo, combustivel, dados);
        }
    }

    private void inicializarEstruturasVeiculoECombustivel(RelatorioMunicipioDados dados, Veiculo veiculo, String combustivel) {
        dados.totalKmsPorVeiculo.putIfAbsent(veiculo.getId(), 0.0);
        dados.totalCo2PorVeiculo.putIfAbsent(veiculo.getId(), 0.0);

        dados.totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
        dados.totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);
        dados.numeroRegistosPorCombustivel.putIfAbsent(combustivel, 0);
    }

    private void processarRegisto(RegistoKms registo, Veiculo veiculo, String combustivel, RelatorioMunicipioDados dados) {
        if (registo == null) {
            return;
        }

        dados.listaRegistos.add(registo);

        double kms = registo.getKms_mes();
        double co2 = registo.getEmissaoEfetivaKg();
        String mesAno = formatarMesAno(registo.getMes_ano());

        dados.totalKmsGeral += kms;
        dados.totalCo2Geral += co2;

        dados.totalKmsPorVeiculo.put(
                veiculo.getId(),
                dados.totalKmsPorVeiculo.getOrDefault(veiculo.getId(), 0.0) + kms
        );

        dados.totalCo2PorVeiculo.put(
                veiculo.getId(),
                dados.totalCo2PorVeiculo.getOrDefault(veiculo.getId(), 0.0) + co2
        );

        dados.totalKmsPorCombustivel.put(
                combustivel,
                dados.totalKmsPorCombustivel.getOrDefault(combustivel, 0.0) + kms
        );

        dados.totalCo2PorCombustivel.put(
                combustivel,
                dados.totalCo2PorCombustivel.getOrDefault(combustivel, 0.0) + co2
        );

        dados.numeroRegistosPorCombustivel.put(
                combustivel,
                dados.numeroRegistosPorCombustivel.getOrDefault(combustivel, 0) + 1
        );

        dados.totalKmsPorMes.put(
                mesAno,
                dados.totalKmsPorMes.getOrDefault(mesAno, 0.0) + kms
        );

        dados.totalCo2PorMes.put(
                mesAno,
                dados.totalCo2PorMes.getOrDefault(mesAno, 0.0) + co2
        );
    }

    private void calcularPercentagensPorCombustivel(RelatorioMunicipioDados dados) {
        for (String combustivel : dados.totalKmsPorCombustivel.keySet()) {
            double kmsCombustivel = dados.totalKmsPorCombustivel.getOrDefault(combustivel, 0.0);
            double co2Combustivel = dados.totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);

            double percentagemKms = dados.totalKmsGeral > 0 ? (kmsCombustivel / dados.totalKmsGeral) * 100 : 0.0;
            double percentagemCo2 = dados.totalCo2Geral > 0 ? (co2Combustivel / dados.totalCo2Geral) * 100 : 0.0;

            dados.percentagemKmsPorCombustivel.put(combustivel, percentagemKms);
            dados.percentagemCo2PorCombustivel.put(combustivel, percentagemCo2);
        }
    }

    private void calcularMediasPorCombustivel(RelatorioMunicipioDados dados) {
        for (String combustivel : dados.totalCo2PorCombustivel.keySet()) {
            double totalCo2Combustivel = dados.totalCo2PorCombustivel.getOrDefault(combustivel, 0.0);
            int totalRegistos = dados.numeroRegistosPorCombustivel.getOrDefault(combustivel, 0);

            double media = totalRegistos > 0 ? totalCo2Combustivel / totalRegistos : 0.0;
            dados.emissaoMediaPorCombustivel.put(combustivel, media);
        }
    }

    private void calcularMetricasMensais(RelatorioMunicipioDados dados, Municipio municipio) {
        for (String mesAno : dados.totalCo2PorMes.keySet()) {
            double co2Mes = dados.totalCo2PorMes.get(mesAno);
            double mediaPorHabitante = dados.numeroHabitantes > 0 ? co2Mes / dados.numeroHabitantes : 0.0;

            dados.mediaCo2PorHabitantePorMes.put(mesAno, mediaPorHabitante);
            dados.objetivoAtingidoPorMes.put(mesAno, mediaPorHabitante <= municipio.getObjetivo_co2_mes_hab());

            dados.mediaEmissoesPorMes.put(mesAno, co2Mes);
            dados.somaEmissoesMensais += co2Mes;
        }

        dados.mediaGlobalEmissoesMensais = dados.totalCo2PorMes.size() > 0
                ? dados.somaEmissoesMensais / dados.totalCo2PorMes.size()
                : 0.0;

        dados.mesesOrdenados = ordenarMeses(dados.totalCo2PorMes.keySet());
    }

    private void calcularVariacaoMesAnterior(RelatorioMunicipioDados dados) {
        for (int i = 1; i < dados.mesesOrdenados.size(); i++) {
            String mesAtual = dados.mesesOrdenados.get(i);
            String mesAnterior = dados.mesesOrdenados.get(i - 1);

            double emissaoAtual = dados.totalCo2PorMes.getOrDefault(mesAtual, 0.0);
            double emissaoAnterior = dados.totalCo2PorMes.getOrDefault(mesAnterior, 0.0);

            double variacao = emissaoAtual - emissaoAnterior;
            dados.variacaoMesAnterior.put(mesAtual, variacao);
            dados.corComparacaoMesAnterior.put(mesAtual, obterCorVariacao(variacao));
        }
    }

    private void calcularVariacaoAnoAnterior(RelatorioMunicipioDados dados) {
        for (String mesAno : dados.totalCo2PorMes.keySet()) {
            String[] partes = mesAno.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            String mesAnoAnterior = String.format("%02d/%d", mes, ano - 1);

            double emissaoAtual = dados.totalCo2PorMes.getOrDefault(mesAno, 0.0);
            double emissaoAnoAnterior = dados.totalCo2PorMes.getOrDefault(mesAnoAnterior, -1.0);

            if (emissaoAnoAnterior >= 0) {
                double variacao = emissaoAtual - emissaoAnoAnterior;
                dados.variacaoAnoAnteriorPorMes.put(mesAno, variacao);
                dados.corComparacaoAnoAnteriorPorMes.put(mesAno, obterCorVariacao(variacao));
            }
        }
    }

    private void calcularEvolucaoMensal(RelatorioMunicipioDados dados) {
        for (String mes : dados.mesesOrdenados) {
            dados.evolucaoEmissoesMensais.put(mes, dados.totalCo2PorMes.get(mes));
        }
    }

    private List<String> ordenarMeses(Set<String> meses) {
        List<String> mesesOrdenados = new ArrayList<>(meses);

        mesesOrdenados.sort((m1, m2) -> {
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
        if (variacao < 0) {
            return "green";
        } else if (variacao > 0) {
            return "red";
        }
        return "gray";
    }

    public static class RelatorioMunicipioDados {
        List<Cidadao> listaCidadaos = new ArrayList<>();
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
        double mediaGlobalEmissoesMensais = 0.0;

        int numeroHabitantes = 0;
        int quantidadeVeiculosTotais = 0;

        List<String> mesesOrdenados = new ArrayList<>();

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

            return attrs;
        }
    }

    /**
     * Actualiza o objectivo de CO₂ por habitante por mês do município autenticado.
     * Só o próprio município pode alterar o seu objectivo.
     *
     * @param username   username do município autenticado
     * @param novoValor  novo objectivo em kg CO₂/habitante/mês (deve ser > 0)
     * @throws IllegalArgumentException se o valor for inválido ou o município não existir
     */
    @Transactional
    public void atualizarObjetivoCo2(String username, double novoValor) {
        if (novoValor <= 0) {
            throw new IllegalArgumentException("O objectivo de CO₂ deve ser um valor positivo.");
        }
        if (novoValor > 1000) {
            throw new IllegalArgumentException("O objectivo de CO₂ não pode exceder 1000 kg por habitante por mês.");
        }

        Municipio municipio = municipioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Município não encontrado."));

        municipio.setObjetivo_co2_mes_hab(novoValor);
        municipioRepository.save(municipio);
    }

    public void atualizarTaxas(
            String username,
            double taxaNivel1,
            double taxaNivel2,
            double taxaNivel3,
            double taxaNivel4,
            double taxaNivel5,
            double taxaNivel6,
            double taxaNivel7
    ) {
        Municipio municipio = municipioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Município não encontrado."));

        if (taxaNivel1 < 0 || taxaNivel2 < 0 || taxaNivel3 < 0 ||
                taxaNivel4 < 0 || taxaNivel5 < 0 || taxaNivel6 < 0 || taxaNivel7 < 0) {
            throw new IllegalArgumentException("Nenhuma taxa pode ser negativa.");
        }

        if (taxaNivel7 != 0) {
            throw new IllegalArgumentException("O nível 7 deve ter taxa 0, pois corresponde a 0 g/km.");
        }

        municipio.setTaxaNivel1(taxaNivel1);
        municipio.setTaxaNivel2(taxaNivel2);
        municipio.setTaxaNivel3(taxaNivel3);
        municipio.setTaxaNivel4(taxaNivel4);
        municipio.setTaxaNivel5(taxaNivel5);
        municipio.setTaxaNivel6(taxaNivel6);
        municipio.setTaxaNivel7(taxaNivel7);

        municipioRepository.save(municipio);
    }
}