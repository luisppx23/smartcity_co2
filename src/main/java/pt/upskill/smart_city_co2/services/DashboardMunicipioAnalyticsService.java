package pt.upskill.smart_city_co2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.dto.DashboardMunicipioDataDTO;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DashboardMunicipioAnalyticsService {

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Transactional(readOnly = true)
    public DashboardMunicipioDataDTO prepararDadosDashboard(Municipio municipio) {
        DashboardMunicipioDataDTO dados = new DashboardMunicipioDataDTO();

        if (municipio == null) {
            return dados.empty();
        }

        List<Cidadao> listaCidadaos = cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());

        if (listaCidadaos == null || listaCidadaos.isEmpty()) {
            return dados.empty();
        }

        dados.setListaCidadaos(listaCidadaos);
        dados.setNumeroHabitantes(listaCidadaos.size());

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

    @Transactional(readOnly = true)
    public DashboardMunicipioDataDTO prepararDadosCharts(Municipio municipio) {
        return prepararDadosDashboard(municipio);
    }

    private void processarCidadaos(List<Cidadao> listaCidadaos, DashboardMunicipioDataDTO dados) {
        for (Cidadao cidadao : listaCidadaos) {
            processarCidadao(cidadao, dados);
        }

        dados.setQuantidadeVeiculosTotais(dados.getIdsVeiculosUnicos().size());
    }

    private void processarCidadao(Cidadao cidadao, DashboardMunicipioDataDTO dados) {
        if (cidadao == null || cidadao.getListaDeVeiculos() == null) {
            return;
        }

        for (Ownership ownership : cidadao.getListaDeVeiculos()) {
            processarOwnership(ownership, dados);
        }
    }

    private void processarOwnership(Ownership ownership, DashboardMunicipioDataDTO dados) {
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
            DashboardMunicipioDataDTO dados,
            Long veiculoId,
            String combustivel
    ) {
        dados.getTotalKmsPorVeiculo().putIfAbsent(veiculoId, 0.0);
        dados.getTotalCo2PorVeiculo().putIfAbsent(veiculoId, 0.0);

        dados.getTotalKmsPorCombustivel().putIfAbsent(combustivel, 0.0);
        dados.getTotalCo2PorCombustivel().putIfAbsent(combustivel, 0.0);
        dados.getNumeroRegistosPorCombustivel().putIfAbsent(combustivel, 0);
        dados.getTotalTaxaPorCombustivel().putIfAbsent(combustivel, 0.0);
    }

    private void processarRegisto(
            RegistoKms registo,
            Long veiculoId,
            String combustivel,
            DashboardMunicipioDataDTO dados
    ) {
        if (registo == null || registo.getMes_ano() == null) {
            return;
        }

        dados.getListaRegistos().add(registo);

        double kms = registo.getKms_mes();
        double co2 = registo.getEmissaoEfetivaKg();

        String mesAno = formatarMesAno(registo.getMes_ano());

        dados.setTotalKmsGeral(dados.getTotalKmsGeral() + kms);
        dados.setTotalCo2Geral(dados.getTotalCo2Geral() + co2);



        double taxa = 0.0;
        if (registo.getTaxa() != null) {
            taxa = registo.getTaxa().getValor();
            dados.setTotalTaxaGeral(dados.getTotalTaxaGeral() + taxa);
        }
        dados.getTotalTaxaPorCombustivel().put(
                combustivel,
                dados.getTotalTaxaPorCombustivel().getOrDefault(combustivel, 0.0) + taxa
        );

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

    private void calcularPercentagensPorCombustivel(DashboardMunicipioDataDTO dados) {
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

    private void calcularMediasPorCombustivel(DashboardMunicipioDataDTO dados) {
        for (String combustivel : dados.getTotalCo2PorCombustivel().keySet()) {
            double totalCo2Combustivel = dados.getTotalCo2PorCombustivel().getOrDefault(combustivel, 0.0);
            int totalRegistos = dados.getNumeroRegistosPorCombustivel().getOrDefault(combustivel, 0);

            double media = totalRegistos > 0 ? totalCo2Combustivel / totalRegistos : 0.0;
            dados.getEmissaoMediaPorCombustivel().put(combustivel, media);
        }
    }

    private void calcularMetricasMensais(DashboardMunicipioDataDTO dados, Municipio municipio) {
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

    private void calcularVariacaoMesAnterior(DashboardMunicipioDataDTO dados) {
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

    private void calcularVariacaoAnoAnterior(DashboardMunicipioDataDTO dados) {
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

    private void calcularEvolucaoMensal(DashboardMunicipioDataDTO dados) {
        for (String mes : dados.getMesesOrdenados()) {
            dados.getEvolucaoEmissoesMensais().put(
                    mes,
                    dados.getTotalCo2PorMes().getOrDefault(mes, 0.0)
            );
        }
    }

    private void calcularIndicadoresGlobais(DashboardMunicipioDataDTO dados) {
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

    private void calcularIndicadoresAnuais(DashboardMunicipioDataDTO dados) {
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

    private void calcularNivelMunicipio(DashboardMunicipioDataDTO dados) {
        int mesesAtingidos = 0;

        for (Boolean atingido : dados.getObjetivoAtingidoPorMes().values()) {
            if (Boolean.TRUE.equals(atingido)) {
                mesesAtingidos++;
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
}