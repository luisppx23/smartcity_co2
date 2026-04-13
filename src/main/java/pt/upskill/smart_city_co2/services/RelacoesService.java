package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelacoesService {

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @PostConstruct
    @Transactional
    public void init() {
        popularRelacionamentosMunicipioCidadao();
    }

    @Transactional
    public void popularRelacionamentosMunicipioCidadao() {
        Municipio lisboa = municipioRepository.findById(1L).orElse(null);
        Municipio vilaVerde = municipioRepository.findById(2L).orElse(null);

        List<Cidadao> cidadaosLisboa = new ArrayList<>();
        List<Cidadao> cidadaosVilaVerde = new ArrayList<>();

        for (long i = 3L; i <= 12L; i++) {
            Cidadao cidadao = cidadaoRepository.findById(i).orElse(null);
            if (cidadao != null) {
                cidadaosLisboa.add(cidadao);
            }
        }

        for (long i = 13L; i <= 22L; i++) {
            Cidadao cidadao = cidadaoRepository.findById(i).orElse(null);
            if (cidadao != null) {
                cidadaosVilaVerde.add(cidadao);
            }
        }

        boolean jaPopulado = false;
        if (!cidadaosLisboa.isEmpty() && cidadaosLisboa.get(0).getMunicipio() != null) {
            jaPopulado = true;
        }

        if (jaPopulado) {
            return;
        }

        if (lisboa != null) {
            for (Cidadao cidadao : cidadaosLisboa) {
                cidadao.setMunicipio(lisboa);
            }
            lisboa.setListaDeCidadaos(cidadaosLisboa);
            municipioRepository.save(lisboa);

            for (Cidadao cidadao : cidadaosLisboa) {
                cidadaoRepository.save(cidadao);
            }
        }

        if (vilaVerde != null) {
            for (Cidadao cidadao : cidadaosVilaVerde) {
                cidadao.setMunicipio(vilaVerde);
            }
            vilaVerde.setListaDeCidadaos(cidadaosVilaVerde);
            municipioRepository.save(vilaVerde);

            for (Cidadao cidadao : cidadaosVilaVerde) {
                cidadaoRepository.save(cidadao);
            }
        }
    }
}