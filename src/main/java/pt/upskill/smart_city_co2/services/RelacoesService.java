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
        // Buscar municípios
        Municipio lisboa = municipioRepository.findById(1L).orElse(null);
        Municipio vilaVerde = municipioRepository.findById(2L).orElse(null);

        // Buscar cidadãos
        Cidadao cidadao3 = cidadaoRepository.findById(3L).orElse(null);
        Cidadao cidadao4 = cidadaoRepository.findById(4L).orElse(null);
        Cidadao cidadao5 = cidadaoRepository.findById(5L).orElse(null);
        Cidadao cidadao6 = cidadaoRepository.findById(6L).orElse(null);

        // Verificar se já está populado (verificando se algum cidadão já tem município)
        boolean jaPopulado = false;
        if (cidadao3 != null && cidadao3.getMunicipio() != null) {
            jaPopulado = true;
        }

        if (jaPopulado) {
            return;
        }

        // Lisboa com cidadãos 3 e 4
        if (lisboa != null) {
            List<Cidadao> cidadaosLisboa = new ArrayList<>();
            if (cidadao3 != null) {
                cidadaosLisboa.add(cidadao3);
                cidadao3.setMunicipio(lisboa);
            }
            if (cidadao4 != null) {
                cidadaosLisboa.add(cidadao4);
                cidadao4.setMunicipio(lisboa);
            }

            lisboa.setListaDeCidadaos(cidadaosLisboa);
            municipioRepository.save(lisboa);

            // Salvar os cidadãos com o município atualizado
            if (cidadao3 != null) {
                cidadaoRepository.save(cidadao3);
            }
            if (cidadao4 != null) {
                cidadaoRepository.save(cidadao4);
            }
        }

        // Vila Verde com cidadãos 5 e 6
        if (vilaVerde != null) {
            List<Cidadao> cidadaosVilaVerde = new ArrayList<>();
            if (cidadao5 != null) {
                cidadaosVilaVerde.add(cidadao5);
                cidadao5.setMunicipio(vilaVerde);
            }
            if (cidadao6 != null) {
                cidadaosVilaVerde.add(cidadao6);
                cidadao6.setMunicipio(vilaVerde);
            }

            vilaVerde.setListaDeCidadaos(cidadaosVilaVerde);
            municipioRepository.save(vilaVerde);

            // Salvar os cidadãos com o município atualizado
            if (cidadao5 != null) {
                cidadaoRepository.save(cidadao5);
            }
            if (cidadao6 != null) {
                cidadaoRepository.save(cidadao6);
            }
        }
    }
}