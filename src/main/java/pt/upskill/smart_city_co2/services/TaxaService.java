package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Taxa;
import pt.upskill.smart_city_co2.repositories.TaxaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaxaService {

    @Autowired
    TaxaRepository taxaRepository;

    @PostConstruct
    public void init() {
        if (taxaRepository.count() > 0) return;
        {
            taxaRepository.save(new Taxa(LocalDate.of(2025, 1, 1), 0.0));
            taxaRepository.save(new Taxa(LocalDate.of(2025, 1, 1), 0.045));
            taxaRepository.save(new Taxa(LocalDate.of(2025, 1, 1), 0.055));
            taxaRepository.save(new Taxa(LocalDate.of(2025, 1, 1), 0.065));
            taxaRepository.save(new Taxa(LocalDate.of(2025, 1, 1), 0.075));
            taxaRepository.save(new Taxa(LocalDate.of(2025, 1, 1), 0.090));
            taxaRepository.save(new Taxa(LocalDate.of(2025, 1, 1), 0.110));
        }
    }

    public List<Taxa> getTaxas() {
        return taxaRepository.findAll();
    }

    public Taxa getTaxa(Long id) {
        return taxaRepository.getReferenceById(id);
    }
}
