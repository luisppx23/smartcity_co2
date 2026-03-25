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

        // ELÉTRICOS
        veiculoRepository.save(new Veiculo("AA-00-EV", "Tesla", "Model 3", TipoDeCombustivel.ELECTRICO,
                0.0, 0.0, 0.0, 0.0, 2025));
        veiculoRepository.save(new Veiculo("BB-11-EV", "Nissan", "Leaf", TipoDeCombustivel.ELECTRICO,
                0.0, 0.0, 0.0, 0.0, 2026));

        // HÍBRIDOS
        veiculoRepository.save(new Veiculo("CC-22-HY", "Toyota", "Prius", TipoDeCombustivel.HIBRIDO,
                0.1235, 4.3, 2.31, 0.7, 1998));
        veiculoRepository.save(new Veiculo("DD-33-HY", "Hyundai", "Ioniq", TipoDeCombustivel.HIBRIDO,
                0.1159, 4.0, 2.31, 0.7, 1997));

        // GASOLINA
        veiculoRepository.save(new Veiculo("EE-44-GA", "Volkswagen", "Golf", TipoDeCombustivel.GASOLINA,
                0.1925, 6.0, 2.31, 1.0, 1998));
        veiculoRepository.save(new Veiculo("FF-55-GA", "Ford", "Focus", TipoDeCombustivel.GASOLINA,
                0.2029, 6.5, 2.31, 1.0, 2000));

        // DIESEL
        veiculoRepository.save(new Veiculo("GG-66-DI", "BMW", "320d", TipoDeCombustivel.DIESEL,
                0.1701, 5.0, 2.64, 0.8, 1998));
        veiculoRepository.save(new Veiculo("HH-77-DI", "Mercedes", "C220", TipoDeCombustivel.DIESEL,
                0.1788, 5.2, 2.64, 0.8, 1997));

        // GPL
        veiculoRepository.save(new Veiculo("II-88-GPL", "Dacia", "Sandero", TipoDeCombustivel.GPL,
                0.151, 7.2, 1.51, 1.0, 1998));
        veiculoRepository.save(new Veiculo("JJ-99-GPL", "Opel", "Corsa", TipoDeCombustivel.GPL,
                0.1595, 7.5, 1.51, 1.0, 1997));
    }

    public List<Veiculo> getAllVeiculos() {
        return veiculoRepository.findAll();
    }

    public Veiculo getVeiculoByMatricula(String matricula) {
        return veiculoRepository.findById(matricula).orElse(null);
    }
}