package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MunicipioService {
    @Autowired
    MunicipioRepository municipioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (municipioRepository.count() > 0) return;
        {
            municipioRepository.save(new Municipio("Lisboa", 50.0, "lisboa", LocalDateTime.now(), "co2@cm-vilaverde.pt", passwordEncoder.encode("lisboa123"), 111222333, "municipio", true));
            municipioRepository.save(new Municipio("Vila Verde", 45.0, "vilaverde", LocalDateTime.now(), "co2@cm-vilaverde.pt", passwordEncoder.encode("vilaverde123"), 444555666, "municipio", true));
        }
    }

    public List<Municipio> getNome() {
        return municipioRepository.findAll();
    }

    public Municipio getUserM(Long id) {
        return municipioRepository.getReferenceById(id);
    }
}