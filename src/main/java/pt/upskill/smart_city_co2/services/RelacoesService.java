package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;
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

    @PostConstruct
    @Transactional
    public void init() {
        popularRelacionamentosCidadaoVeiculo();
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

    private void popularRelacionamentosCidadaoVeiculo() {
        // Buscar cidadão 3 para verificar se já tem veículos (sem acessar a coleção lazy fora da transação)
        Cidadao cidadao3 = cidadaoRepository.findById(3L).orElse(null);
        if (cidadao3 != null) {
            // Tentar acessar dentro da transação - se já tiver veículos, retorna
            try {
                if (cidadao3.getListaDeVeiculos() != null && !cidadao3.getListaDeVeiculos().isEmpty()) {
                    return; // Já populado
                }
            } catch (Exception e) {
                // Se der erro, continua para popular
            }
        }

        // Buscar cidadãos
        cidadao3 = cidadaoRepository.findById(3L).orElse(null);
        Cidadao cidadao4 = cidadaoRepository.findById(4L).orElse(null);
        Cidadao cidadao5 = cidadaoRepository.findById(5L).orElse(null);
        Cidadao cidadao6 = cidadaoRepository.findById(6L).orElse(null);

        // Buscar veículos por ID
        Veiculo veiculo1 = veiculoRepository.findById(1L).orElse(null);
        Veiculo veiculo2 = veiculoRepository.findById(2L).orElse(null);
        Veiculo veiculo3 = veiculoRepository.findById(3L).orElse(null);
        Veiculo veiculo4 = veiculoRepository.findById(4L).orElse(null);
        Veiculo veiculo5 = veiculoRepository.findById(5L).orElse(null);
        Veiculo veiculo6 = veiculoRepository.findById(6L).orElse(null);
        Veiculo veiculo7 = veiculoRepository.findById(7L).orElse(null);
        Veiculo veiculo8 = veiculoRepository.findById(8L).orElse(null);

        // Popular cidadão 3 com veículos 1 e 2
        if (cidadao3 != null && veiculo1 != null && veiculo2 != null) {
            cidadao3.setListaDeVeiculos(Arrays.asList(veiculo1, veiculo2));
            cidadaoRepository.save(cidadao3);
        }

        // Popular cidadão 4 com veículos 3 e 4
        if (cidadao4 != null && veiculo3 != null && veiculo4 != null) {
            cidadao4.setListaDeVeiculos(Arrays.asList(veiculo3, veiculo4));
            cidadaoRepository.save(cidadao4);
        }

        // Popular cidadão 5 com veículos 5 e 6
        if (cidadao5 != null && veiculo5 != null) {
            cidadao5.setListaDeVeiculos(Arrays.asList(veiculo5, veiculo6));
            cidadaoRepository.save(cidadao5);
        }

        // Popular cidadão 6 com veículos 7 e 8
        if (cidadao6 != null && veiculo6 != null) {
            cidadao6.setListaDeVeiculos(Arrays.asList(veiculo7, veiculo8));
            cidadaoRepository.save(cidadao6);
        }
    }
}