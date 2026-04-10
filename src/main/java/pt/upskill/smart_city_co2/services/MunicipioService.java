package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.dto.DashboardMunicipioDataDTO;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class MunicipioService {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DashboardMunicipioAnalyticsService dashboardMunicipioAnalyticsService;

    @PostConstruct
    public void init() {
        if (municipioRepository.count() > 0) {
            return;
        }

        municipioRepository.save(
                new Municipio(
                        "Lisboa",
                        300.0,
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
                        250.0,
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

    @Transactional(readOnly = true)
    public List<Cidadao> buscarCidadaosDoMunicipio(Municipio municipio) {
        if (municipio == null) {
            return Collections.emptyList();
        }

        return cidadaoRepository.buscarCidadaosDoMunicipioComVeiculos(municipio.getId());
    }

    @Transactional(readOnly = true)
    public DashboardMunicipioDataDTO gerarRelatorioMunicipio(Municipio municipio) {
        return dashboardMunicipioAnalyticsService.prepararDadosDashboard(municipio);
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