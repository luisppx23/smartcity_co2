package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CidadaoService {
    @Autowired
    CidadaoRepository cidadaoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private RegistoKmsRepository registoKmsRepository;

    @Autowired
    private TaxaRepository taxaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @PostConstruct
    public void init() {
        if (cidadaoRepository.count() > 0) return;
        {
            cidadaoRepository.save(new Cidadao("João", "Castor", "joaocastor", LocalDateTime.now(), "joao@gmail.com", passwordEncoder.encode("joao123"), 123456789, "cidadao", true, "911888333","Rua Um"));
            cidadaoRepository.save(new Cidadao("Maria", "Silva", "mariasilva", LocalDateTime.now(), "maria@gmail.com", passwordEncoder.encode("maria123"), 987654321, "cidadao", true, "911888332","Rua Dois"));
            cidadaoRepository.save(new Cidadao("Carlos", "Ferreira", "carlosferreira", LocalDateTime.now(), "carlos@gmail.com", passwordEncoder.encode("carlos123"), 456789123, "cidadao", true, "911888331","Rua Três"));
            cidadaoRepository.save(new Cidadao("Ana", "Costa", "anacosta", LocalDateTime.now(), "ana@gmail.com", passwordEncoder.encode("ana123"), 789123456, "cidadao", true, "911888330","Rua Quatro"));
        }
    }

    public List<Cidadao> getNome() {
        return cidadaoRepository.findAll();
    }

    public Cidadao getUserC(Long id) {
        return cidadaoRepository.findById(id).orElse(null);
    }

    public void salvarAlteracoes(Cidadao cidadao) {
        try {
            cidadaoRepository.save(cidadao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar os dados do cidadão: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCidadao(Long id) {
        Cidadao cidadao = cidadaoRepository.findById(id).orElse(null);
        if (cidadao == null) return;

        // Percorrer os ownerships para apagar as taxas e registos
        for (Ownership ownership : cidadao.getListaDeVeiculos()) {
            if (ownership.getRegistosKms() != null) {
                for (RegistoKms registo : ownership.getRegistosKms()) {
                    if (registo.getTaxa() != null) {
                        taxaRepository.delete(registo.getTaxa());
                    }
                }
                registoKmsRepository.deleteAll(ownership.getRegistosKms());
            }
            // Apagar o veículo associado (cada ownership tem o seu próprio veículo)
            if (ownership.getVeiculo() != null) {
                veiculoRepository.delete(ownership.getVeiculo());
            }
        }
        // Apagar todas as ownerships do cidadão
        if (cidadao.getListaDeVeiculos() != null) {
            ownershipRepository.deleteAll(cidadao.getListaDeVeiculos());
        }
        // Finalmente, apagar o cidadão (que é uma subclasse de User)
        cidadaoRepository.delete(cidadao);
    }

}