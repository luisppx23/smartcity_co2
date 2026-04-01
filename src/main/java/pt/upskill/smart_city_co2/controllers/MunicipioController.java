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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<Cidadao> listaCidadaos =
                cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());

        List<RegistoKms> listaRegistos = new ArrayList<>();
        List<Veiculo> listaVeiculos = new ArrayList<>();

        Map<Long, String> matriculaPorVeiculo = new LinkedHashMap<>();
        Map<Long, String> combustivelPorVeiculo = new LinkedHashMap<>();

        Map<Long, Double> totalKmsPorVeiculo = new LinkedHashMap<>();
        Map<Long, Double> totalCo2PorVeiculo = new LinkedHashMap<>();

        Map<String, Double> totalKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorCombustivel = new LinkedHashMap<>();
        Map<String, Double> percentagemKmsPorCombustivel = new LinkedHashMap<>();
        Map<String, Double> percentagemCo2PorCombustivel = new LinkedHashMap<>();

        Map<String, Double> totalKmsPorMes = new LinkedHashMap<>();
        Map<String, Double> totalCo2PorMes = new LinkedHashMap<>();
        Map<String, Double> mediaCo2PorHabitantePorMes = new LinkedHashMap<>();
        Map<String, Boolean> objetivoAtingidoPorMes = new LinkedHashMap<>();

        double totalKmsGeral = 0.0;
        double totalCo2Geral = 0.0;

        int numeroHabitantes = (listaCidadaos != null) ? listaCidadaos.size() : 0;

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/yyyy");

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
                        matriculaPorVeiculo.put(veiculo.getId(), ownership.getMatricula());

                        String combustivel = veiculo.getTipoDeCombustivel() != null
                                ? veiculo.getTipoDeCombustivel().name()
                                : "DESCONHECIDO";

                        combustivelPorVeiculo.put(veiculo.getId(), combustivel);

                        totalKmsPorVeiculo.putIfAbsent(veiculo.getId(), 0.0);
                        totalCo2PorVeiculo.putIfAbsent(veiculo.getId(), 0.0);

                        totalKmsPorCombustivel.putIfAbsent(combustivel, 0.0);
                        totalCo2PorCombustivel.putIfAbsent(combustivel, 0.0);

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

        for (String combustivel : totalKmsPorCombustivel.keySet()) {
            double kmsCombustivel = totalKmsPorCombustivel.get(combustivel);
            double co2Combustivel = totalCo2PorCombustivel.get(combustivel);

            double percentagemKms = totalKmsGeral > 0 ? (kmsCombustivel / totalKmsGeral) * 100 : 0.0;
            double percentagemCo2 = totalCo2Geral > 0 ? (co2Combustivel / totalCo2Geral) * 100 : 0.0;

            percentagemKmsPorCombustivel.put(combustivel, percentagemKms);
            percentagemCo2PorCombustivel.put(combustivel, percentagemCo2);
        }

        for (String mesAno : totalCo2PorMes.keySet()) {
            double co2Mes = totalCo2PorMes.get(mesAno);
            double mediaMes = numeroHabitantes > 0 ? co2Mes / numeroHabitantes : 0.0;

            mediaCo2PorHabitantePorMes.put(mesAno, mediaMes);
            objetivoAtingidoPorMes.put(mesAno, mediaMes <= municipio.getObjetivo_co2_mes_hab());
        }

        System.out.println("Total de registos encontrados: " + listaRegistos.size());
        System.out.println("Total kms geral: " + totalKmsGeral);
        System.out.println("Total CO2 geral: " + totalCo2Geral);

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
    }
}