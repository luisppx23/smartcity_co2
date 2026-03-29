package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;
import pt.upskill.smart_city_co2.repositories.OwnershipRepository;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RelacoesService {

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private OwnershipRepository ownershipRepository;

    @PostConstruct
    @Transactional
    public void init() {
        popularRelacionamentosMunicipioCidadao();
    }

    private void popularRelacionamentosMunicipioCidadao() {
        // Buscar municípios
        Municipio lisboa = municipioRepository.findById(1L).orElse(null);
        Municipio vilaVerde = municipioRepository.findById(2L).orElse(null);

        // Verificar se já existem relacionamentos - usando uma abordagem diferente
        // Se o município já tiver algum cidadão na lista, não faz nada
        if (lisboa != null) {
            try {
                if (lisboa.getListaDeCidadaos() != null && !lisboa.getListaDeCidadaos().isEmpty()) {
                    return; // Já populado
                }
            } catch (Exception e) {
                // Se der erro, continua para popular
            }
        }

        // Buscar cidadãos
        Cidadao cidadao3 = cidadaoRepository.findById(3L).orElse(null);
        Cidadao cidadao4 = cidadaoRepository.findById(4L).orElse(null);
        Cidadao cidadao5 = cidadaoRepository.findById(5L).orElse(null);
        Cidadao cidadao6 = cidadaoRepository.findById(6L).orElse(null);

        // Lisboa com cidadãos 3 e 4
        if (lisboa != null) {
            List<Cidadao> cidadaosLisboa = new ArrayList<>();
            if (cidadao3 != null) cidadaosLisboa.add(cidadao3);
            if (cidadao4 != null) cidadaosLisboa.add(cidadao4);

            lisboa.setListaDeCidadaos(cidadaosLisboa);
            municipioRepository.save(lisboa);
        }

        // Vila Verde com cidadãos 5 e 6
        if (vilaVerde != null) {
            List<Cidadao> cidadaosVilaVerde = new ArrayList<>();
            if (cidadao5 != null) cidadaosVilaVerde.add(cidadao5);
            if (cidadao6 != null) cidadaosVilaVerde.add(cidadao6);

            vilaVerde.setListaDeCidadaos(cidadaosVilaVerde);
            municipioRepository.save(vilaVerde);
        }
    }
}