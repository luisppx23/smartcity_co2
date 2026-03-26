package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CidadaoService {
    @Autowired
    CidadaoRepository cidadaoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
        return cidadaoRepository.getReferenceById(id);
    }
}