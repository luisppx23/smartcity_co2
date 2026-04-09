package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.dto.DTODashboardMunicipioService;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Veiculo;
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

    @Transactional
    public List<Cidadao> buscarCidadaosDoMunicipio(Municipio municipio) {
        if (municipio == null) {
            return Collections.emptyList();
        }

        return cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());
    }

    @Transactional
    public DTODashboardMunicipioService gerarRelatorioMunicipio(Municipio municipio) {
        List<Cidadao> listaCidadaos = buscarCidadaosDoMunicipio(municipio);

        DTODashboardMunicipioService dados = inicializarDados(listaCidadaos);

        processarCidadaos(listaCidadaos, dados);
        calcularPercentagensPorCombustivel(dados);
        calcularMediasPorCombustivel(dados);
        calcularMetricasMensais(dados, municipio);
        calcularVariacaoMesAnterior(dados);
        calcularVariacaoAnoAnterior(dados);
        calcularEvolucaoMensal(dados);
        dados.setQuantidadeVeiculosPorCombustivel(contarVeiculosPorCombustivel(listaCidadaos));
        calcularIndicadoresGlobais(dados);
        calcularIndicadoresAnuais(dados);
        calcularNivelMunicipio(dados);

        return dados;
    }

    private DTODashboardMunicipioService inicializarDados(List<Cidadao> listaCidadaos) {
        DTODashboardMunicipioService dados = new DTODashboardMunicipioService();
        dados.setListaCidadaos(listaCidadaos != null ? listaCidadaos : new ArrayList<>());
        dados.setNumeroHabitantes(dados.getListaCidadaos().size());
        return dados;
    }

    private void processarCidadaos(List<Cidadao> listaCidadaos, DTODashboardMunicipioService dados) {
        if (listaCidadaos == null) {
            return;
        }

        for (Cidadao cidadao : listaCidadaos) {
            processarCidadao(cidadao, dados);
        }

        dados.setQuantidadeVeiculosTotais(dados.getIdsVeiculosUnicos().size());
    }

    private void processarCidadao(Cidadao cidadao, DTODashboardMunicipioService dados) {
        if (cidadao == null || cidadao.getListaDeVeiculos() == null) {
            return;
        }

        for (Ownership ownership : cidadao.getListaDeVeiculos()) {
            processarOwnership(ownership, dados);
        }
    }

    private void processarOwnership(Ownership ownership, DTODashboardMunicipioService dados) {
        if (ownership == null || ownership.getVeiculo() == null) {
            return;
        }

        Veiculo veiculo = ownership.getVeiculo();
        Long veiculoId = veiculo.getId();

        dados.getListaVeiculos().add(veiculo);
        dados.getIdsVeiculosUnicos().add(veiculoId);
        dados.getMatriculaPorVeiculo().put(veiculoId, ownership.getMatricula());

        String combustivel = obterCombustivel(veiculo);
        dados.getCombustivelPorVeiculo().put(veiculoId, combustivel);

        inicializarEstruturasVeiculoECombustivel(dados, veiculoId, combustivel);

        if (ownership.getRegistosKms() == null) {
            return;
        }

        for (RegistoKms registo : ownership.getRegistosKms()) {
            processarRegisto(registo, veiculoId, combustivel, dados);
        }
    }

    private void inicializarEstruturasVeiculoECombustivel(
            DTODashboardMunicipioService dados,
            Long veiculoId,
            String combustivel
    ) {
        dados.getTotalKmsPorVeiculo().putIfAbsent(veiculoId, 0.0);
        dados.getTotalCo2PorVeiculo().putIfAbsent(veiculoId, 0.0);

        dados.getTotalKmsPorCombustivel().putIfAbsent(combustivel, 0.0);
        dados.getTotalCo2PorCombustivel().putIfAbsent(combustivel, 0.0);
        dados.getNumeroRegistosPorCombustivel().putIfAbsent(combustivel, 0);
    }

    private void processarRegisto(RegistoKms registo,Long veiculoId,String combustivel,DTODashboardMunicipioService dados) {
        if (registo == null) {
            return;
        }

        dados.getListaRegistos().add(registo);

        double kms = registo.getKms_mes();
        double co2 = registo.getEmissaoEfetivaKg();
        String mesAno = formatarMesAno(registo.getMes_ano());

        dados.setTotalKmsGeral(dados.getTotalKmsGeral() + kms);
        dados.setTotalCo2Geral(dados.getTotalCo2Geral() + co2);

        dados.getTotalKmsPorVeiculo().put(
                veiculoId,
                dados.getTotalKmsPorVeiculo().getOrDefault(veiculoId, 0.0) + kms
        );

        dados.getTotalCo2PorVeiculo().put(
                veiculoId,
                dados.getTotalCo2PorVeiculo().getOrDefault(veiculoId, 0.0) + co2
        );

        dados.getTotalKmsPorCombustivel().put(
                combustivel,
                dados.getTotalKmsPorCombustivel().getOrDefault(combustivel, 0.0) + kms
        );

        dados.getTotalCo2PorCombustivel().put(
                combustivel,
                dados.getTotalCo2PorCombustivel().getOrDefault(combustivel, 0.0) + co2
        );

        dados.getNumeroRegistosPorCombustivel().put(
                combustivel,
                dados.getNumeroRegistosPorCombustivel().getOrDefault(combustivel, 0) + 1
        );

        dados.getTotalKmsPorMes().put(
                mesAno,
                dados.getTotalKmsPorMes().getOrDefault(mesAno, 0.0) + kms
        );

        dados.getTotalCo2PorMes().put(
                mesAno,
                dados.getTotalCo2PorMes().getOrDefault(mesAno, 0.0) + co2
        );
    }

    private void calcularPercentagensPorCombustivel(DTODashboardMunicipioService dados) {
        for (String combustivel : dados.getTotalKmsPorCombustivel().keySet()) {
            double kmsCombustivel = dados.getTotalKmsPorCombustivel().getOrDefault(combustivel, 0.0);
            double co2Combustivel = dados.getTotalCo2PorCombustivel().getOrDefault(combustivel, 0.0);

            double percentagemKms = dados.getTotalKmsGeral() > 0
                    ? (kmsCombustivel / dados.getTotalKmsGeral()) * 100
                    : 0.0;

            double percentagemCo2 = dados.getTotalCo2Geral() > 0
                    ? (co2Combustivel / dados.getTotalCo2Geral()) * 100
                    : 0.0;

            dados.getPercentagemKmsPorCombustivel().put(combustivel, percentagemKms);
            dados.getPercentagemCo2PorCombustivel().put(combustivel, percentagemCo2);
        }
    }

    private void calcularMediasPorCombustivel(DTODashboardMunicipioService dados) {
        for (String combustivel : dados.getTotalCo2PorCombustivel().keySet()) {
            double totalCo2Combustivel = dados.getTotalCo2PorCombustivel().getOrDefault(combustivel, 0.0);
            int totalRegistos = dados.getNumeroRegistosPorCombustivel().getOrDefault(combustivel, 0);

            double media = totalRegistos > 0 ? totalCo2Combustivel / totalRegistos : 0.0;
            dados.getEmissaoMediaPorCombustivel().put(combustivel, media);
        }
    }

    private void calcularMetricasMensais(DTODashboardMunicipioService dados, Municipio municipio) {
        for (String mesAno : dados.getTotalCo2PorMes().keySet()) {
            double co2Mes = dados.getTotalCo2PorMes().get(mesAno);
            double mediaPorHabitante = dados.getNumeroHabitantes() > 0
                    ? co2Mes / dados.getNumeroHabitantes()
                    : 0.0;

            dados.getMediaCo2PorHabitantePorMes().put(mesAno, mediaPorHabitante);
            dados.getObjetivoAtingidoPorMes().put(
                    mesAno,
                    mediaPorHabitante <= municipio.getObjetivo_co2_mes_hab()
            );
            dados.getMediaEmissoesPorMes().put(mesAno, co2Mes);
            dados.setSomaEmissoesMensais(dados.getSomaEmissoesMensais() + co2Mes);
        }

        dados.setMediaGlobalEmissoesMensais(
                !dados.getTotalCo2PorMes().isEmpty()
                        ? dados.getSomaEmissoesMensais() / dados.getTotalCo2PorMes().size()
                        : 0.0
        );

        dados.setMesesOrdenados(ordenarMeses(dados.getTotalCo2PorMes().keySet()));
    }

    private void calcularVariacaoMesAnterior(DTODashboardMunicipioService dados) {
        for (int i = 1; i < dados.getMesesOrdenados().size(); i++) {
            String mesAtual = dados.getMesesOrdenados().get(i);
            String mesAnterior = dados.getMesesOrdenados().get(i - 1);

            double emissaoAtual = dados.getTotalCo2PorMes().getOrDefault(mesAtual, 0.0);
            double emissaoAnterior = dados.getTotalCo2PorMes().getOrDefault(mesAnterior, 0.0);

            double variacao = emissaoAtual - emissaoAnterior;
            dados.getVariacaoMesAnterior().put(mesAtual, variacao);
            dados.getCorComparacaoMesAnterior().put(mesAtual, obterCorVariacao(variacao));
        }
    }

    private void calcularVariacaoAnoAnterior(DTODashboardMunicipioService dados) {
        for (String mesAno : dados.getTotalCo2PorMes().keySet()) {
            String[] partes = mesAno.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            String mesAnoAnterior = String.format("%02d/%d", mes, ano - 1);

            double emissaoAtual = dados.getTotalCo2PorMes().getOrDefault(mesAno, 0.0);
            double emissaoAnoAnterior = dados.getTotalCo2PorMes().getOrDefault(mesAnoAnterior, -1.0);

            if (emissaoAnoAnterior >= 0) {
                double variacao = emissaoAtual - emissaoAnoAnterior;
                dados.getVariacaoAnoAnteriorPorMes().put(mesAno, variacao);
                dados.getCorComparacaoAnoAnteriorPorMes().put(mesAno, obterCorVariacao(variacao));
            }
        }
    }

    private void calcularEvolucaoMensal(DTODashboardMunicipioService dados) {
        for (String mes : dados.getMesesOrdenados()) {
            dados.getEvolucaoEmissoesMensais().put(
                    mes,
                    dados.getTotalCo2PorMes().get(mes)
            );
        }
    }

    private void calcularIndicadoresGlobais(DTODashboardMunicipioService dados) {
        dados.setMediaPorVeiculo(
                dados.getQuantidadeVeiculosTotais() > 0
                        ? dados.getTotalCo2Geral() / dados.getQuantidadeVeiculosTotais()
                        : 0.0
        );

        dados.setMesAtualLabel(
                dados.getMesesOrdenados().isEmpty()
                        ? ""
                        : dados.getMesesOrdenados().get(dados.getMesesOrdenados().size() - 1)
        );

        dados.setMesAtualCo2(
                dados.getMesAtualLabel().isEmpty()
                        ? 0.0
                        : dados.getTotalCo2PorMes().getOrDefault(dados.getMesAtualLabel(), 0.0)
        );
    }

    private void calcularIndicadoresAnuais(DTODashboardMunicipioService dados) {
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        int anoAnterior = anoAtual - 1;

        double somaAnoAtual = 0.0;
        int countAnoAtual = 0;
        double somaAnoAnterior = 0.0;
        int countAnoAnterior = 0;

        for (Map.Entry<String, Double> entry : dados.getTotalCo2PorMes().entrySet()) {
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

        dados.setAnoAtual(anoAtual);
        dados.setAnoAnterior(anoAnterior);
        dados.setMediaAnoAtual(countAnoAtual > 0 ? somaAnoAtual / countAnoAtual : 0.0);
        dados.setMediaAnoAnterior(countAnoAnterior > 0 ? somaAnoAnterior / countAnoAnterior : 0.0);
    }

    private void calcularNivelMunicipio(DTODashboardMunicipioService dados) {
        int mesesAtingidos = 0;

        if (dados.getObjetivoAtingidoPorMes() != null) {
            for (Boolean atingido : dados.getObjetivoAtingidoPorMes().values()) {
                if (Boolean.TRUE.equals(atingido)) {
                    mesesAtingidos++;
                }
            }
        }

        dados.setMesesAtingidos(mesesAtingidos);

        if (mesesAtingidos >= 12) {
            dados.setNivelMunicipio("Platinum");
            dados.setNivelMunicipioPct(100);
            dados.setNivelMunicipioIndex(4);
        } else if (mesesAtingidos >= 7) {
            dados.setNivelMunicipio("Gold");
            dados.setNivelMunicipioPct(75);
            dados.setNivelMunicipioIndex(3);
        } else if (mesesAtingidos >= 4) {
            dados.setNivelMunicipio("Silver");
            dados.setNivelMunicipioPct(50);
            dados.setNivelMunicipioIndex(2);
        } else if (mesesAtingidos >= 1) {
            dados.setNivelMunicipio("Bronze");
            dados.setNivelMunicipioPct(25);
            dados.setNivelMunicipioIndex(1);
        } else {
            dados.setNivelMunicipio("Initial");
            dados.setNivelMunicipioPct(0);
            dados.setNivelMunicipioIndex(0);
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

    @Transactional
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