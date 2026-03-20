package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;

import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    VeiculoRepository veiculoRepository;

    @PostConstruct
    public void init() {
        if (veiculoRepository.count() > 0) return;
        {
            // ELECTRICO
            veiculoRepository.save(new Veiculo("AA-00-EV", "Tesla", "Model 3", TipoDeCombustivel.ELECTRICO, 0.0));
            veiculoRepository.save(new Veiculo("BB-11-EV", "Nissan", "Leaf", TipoDeCombustivel.ELECTRICO, 0.0));

            // HIBRIDO
            veiculoRepository.save(new Veiculo("CC-22-HY", "Toyota", "Prius", TipoDeCombustivel.HIBRIDO, 0.09));
            veiculoRepository.save(new Veiculo("DD-33-HY", "Hyundai", "Ioniq", TipoDeCombustivel.HIBRIDO, 0.08));

// GASOLINA
            veiculoRepository.save(new Veiculo("EE-44-GA", "Volkswagen", "Golf", TipoDeCombustivel.GASOLINA, 0.12));
            veiculoRepository.save(new Veiculo("FF-55-GA", "Ford", "Focus", TipoDeCombustivel.GASOLINA, 0.13));

// DIESEL
            veiculoRepository.save(new Veiculo("GG-66-DI", "BMW", "320d", TipoDeCombustivel.DIESEL, 0.11));
            veiculoRepository.save(new Veiculo("HH-77-DI", "Mercedes", "C220", TipoDeCombustivel.DIESEL, 0.10));

// GPL
            veiculoRepository.save(new Veiculo("II-88-GPL", "Dacia", "Sandero", TipoDeCombustivel.GPL, 0.09));
            veiculoRepository.save(new Veiculo("JJ-99-GPL", "Opel", "Corsa", TipoDeCombustivel.GPL, 0.10));
        }
    }

    public List<Veiculo> getMatricula() {
        return veiculoRepository.findAll();
    }

    public Veiculo getPropriedade(Long id) {
        return veiculoRepository.getReferenceById(id);
    }
}
