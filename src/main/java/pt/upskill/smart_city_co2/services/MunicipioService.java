package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.User;
import pt.upskill.smart_city_co2.repositories.MunicipioRepository;
import pt.upskill.smart_city_co2.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MunicipioService {
    @Autowired
    MunicipioRepository municipioRepository;

    @PostConstruct
    public void init() {
        if (municipioRepository.count() > 0) return;
        {
            municipioRepository.save(new Municipio("Lisboa",2.5,"123456781"));
            municipioRepository.save(new Municipio("Vila Verde",1.5,"123456782"));

        }
    }

    public List<Municipio> getNome() {
        return municipioRepository.findAll();
    }

    public Municipio getUserM(Long id) {
        return municipioRepository.getReferenceById(id);
    }

}
