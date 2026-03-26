package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.models.AdicionarVeiculoModel;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;

import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    VeiculoRepository veiculoRepository;

    @PostConstruct
    public void init() {
        if (veiculoRepository.count() > 0) return;

        // ELÉTRICOS (0.0 L/km)
        veiculoRepository.save(new Veiculo("Tesla", "Model 3", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Nissan", "Leaf", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Tesla", "Model S", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Tesla", "Model X", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Tesla", "Model Y", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Nissan", "Leaf e+", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("BMW", "i3", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("BMW", "i4", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("BMW", "iX", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Audi", "e-tron", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Audi", "Q4 e-tron", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Hyundai", "Kona Electric", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Hyundai", "Ioniq 5", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Hyundai", "Ioniq 6", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Kia", "EV6", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Kia", "e-Niro", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Volkswagen", "ID.3", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Volkswagen", "ID.4", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Volkswagen", "ID.5", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Mercedes", "EQC", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Mercedes", "EQS", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Renault", "Zoe", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Peugeot", "e-208", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Peugeot", "e-2008", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Fiat", "500e", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Volvo", "XC40 Recharge", TipoDeCombustivel.ELETRICO, 0.0));
        veiculoRepository.save(new Veiculo("Polestar", "Polestar 2", TipoDeCombustivel.ELETRICO, 0.0));


// HÍBRIDOS (≈ 4.0 – 6.5 L/100km → 0.040 – 0.065 L/km)
        veiculoRepository.save(new Veiculo("Toyota", "Prius", TipoDeCombustivel.HIBRIDO, 0.043));
        veiculoRepository.save(new Veiculo("Hyundai", "Ioniq", TipoDeCombustivel.HIBRIDO, 0.040));
        veiculoRepository.save(new Veiculo("Toyota", "Prius", TipoDeCombustivel.HIBRIDO, 0.043));
        veiculoRepository.save(new Veiculo("Toyota", "Corolla Hybrid", TipoDeCombustivel.HIBRIDO, 0.040));
        veiculoRepository.save(new Veiculo("Toyota", "C-HR Hybrid", TipoDeCombustivel.HIBRIDO, 0.042));
        veiculoRepository.save(new Veiculo("Toyota", "RAV4 Hybrid", TipoDeCombustivel.HIBRIDO, 0.050));
        veiculoRepository.save(new Veiculo("Honda", "Insight", TipoDeCombustivel.HIBRIDO, 0.045));
        veiculoRepository.save(new Veiculo("Honda", "CR-V Hybrid", TipoDeCombustivel.HIBRIDO, 0.052));
        veiculoRepository.save(new Veiculo("Hyundai", "Ioniq Hybrid", TipoDeCombustivel.HIBRIDO, 0.041));
        veiculoRepository.save(new Veiculo("Hyundai", "Tucson Hybrid", TipoDeCombustivel.HIBRIDO, 0.053));
        veiculoRepository.save(new Veiculo("Kia", "Niro Hybrid", TipoDeCombustivel.HIBRIDO, 0.044));
        veiculoRepository.save(new Veiculo("Kia", "Sportage Hybrid", TipoDeCombustivel.HIBRIDO, 0.051));
        veiculoRepository.save(new Veiculo("Ford", "Kuga Hybrid", TipoDeCombustivel.HIBRIDO, 0.050));
        veiculoRepository.save(new Veiculo("Ford", "Mondeo Hybrid", TipoDeCombustivel.HIBRIDO, 0.048));
        veiculoRepository.save(new Veiculo("BMW", "330e", TipoDeCombustivel.HIBRIDO, 0.060));
        veiculoRepository.save(new Veiculo("BMW", "X5 xDrive45e", TipoDeCombustivel.HIBRIDO, 0.065));
        veiculoRepository.save(new Veiculo("Mercedes", "C300e", TipoDeCombustivel.HIBRIDO, 0.058));
        veiculoRepository.save(new Veiculo("Mercedes", "E300e", TipoDeCombustivel.HIBRIDO, 0.060));
        veiculoRepository.save(new Veiculo("Volvo", "XC60 Recharge", TipoDeCombustivel.HIBRIDO, 0.062));
        veiculoRepository.save(new Veiculo("Volvo", "XC90 Recharge", TipoDeCombustivel.HIBRIDO, 0.065));
        veiculoRepository.save(new Veiculo("Peugeot", "3008 Hybrid", TipoDeCombustivel.HIBRIDO, 0.055));
        veiculoRepository.save(new Veiculo("Peugeot", "508 Hybrid", TipoDeCombustivel.HIBRIDO, 0.053));
        veiculoRepository.save(new Veiculo("Renault", "Clio E-Tech", TipoDeCombustivel.HIBRIDO, 0.040));
        veiculoRepository.save(new Veiculo("Renault", "Captur E-Tech", TipoDeCombustivel.HIBRIDO, 0.045));
        veiculoRepository.save(new Veiculo("Opel", "Astra Hybrid", TipoDeCombustivel.HIBRIDO, 0.050));
        veiculoRepository.save(new Veiculo("Jeep", "Compass 4xe", TipoDeCombustivel.HIBRIDO, 0.057));
        veiculoRepository.save(new Veiculo("Jeep", "Renegade 4xe", TipoDeCombustivel.HIBRIDO, 0.055));


// GASOLINA
        veiculoRepository.save(new Veiculo("Volkswagen", "Golf", TipoDeCombustivel.GASOLINA, 0.060));
        veiculoRepository.save(new Veiculo("Ford", "Focus", TipoDeCombustivel.GASOLINA, 0.065));
        veiculoRepository.save(new Veiculo("Volkswagen", "Golf", TipoDeCombustivel.GASOLINA, 0.060));
        veiculoRepository.save(new Veiculo("Volkswagen", "Polo", TipoDeCombustivel.GASOLINA, 0.055));
        veiculoRepository.save(new Veiculo("Ford", "Focus", TipoDeCombustivel.GASOLINA, 0.065));
        veiculoRepository.save(new Veiculo("Ford", "Fiesta", TipoDeCombustivel.GASOLINA, 0.058));
        veiculoRepository.save(new Veiculo("Opel", "Corsa", TipoDeCombustivel.GASOLINA, 0.056));
        veiculoRepository.save(new Veiculo("Opel", "Astra", TipoDeCombustivel.GASOLINA, 0.062));
        veiculoRepository.save(new Veiculo("Renault", "Clio", TipoDeCombustivel.GASOLINA, 0.054));
        veiculoRepository.save(new Veiculo("Renault", "Megane", TipoDeCombustivel.GASOLINA, 0.061));
        veiculoRepository.save(new Veiculo("Peugeot", "208", TipoDeCombustivel.GASOLINA, 0.053));
        veiculoRepository.save(new Veiculo("Peugeot", "308", TipoDeCombustivel.GASOLINA, 0.060));
        veiculoRepository.save(new Veiculo("Seat", "Ibiza", TipoDeCombustivel.GASOLINA, 0.055));
        veiculoRepository.save(new Veiculo("Seat", "Leon", TipoDeCombustivel.GASOLINA, 0.061));
        veiculoRepository.save(new Veiculo("Skoda", "Fabia", TipoDeCombustivel.GASOLINA, 0.054));
        veiculoRepository.save(new Veiculo("Skoda", "Octavia", TipoDeCombustivel.GASOLINA, 0.062));
        veiculoRepository.save(new Veiculo("Hyundai", "i20", TipoDeCombustivel.GASOLINA, 0.053));
        veiculoRepository.save(new Veiculo("Hyundai", "i30", TipoDeCombustivel.GASOLINA, 0.060));
        veiculoRepository.save(new Veiculo("Kia", "Rio", TipoDeCombustivel.GASOLINA, 0.054));
        veiculoRepository.save(new Veiculo("Kia", "Ceed", TipoDeCombustivel.GASOLINA, 0.061));
        veiculoRepository.save(new Veiculo("Toyota", "Yaris", TipoDeCombustivel.GASOLINA, 0.050));
        veiculoRepository.save(new Veiculo("Toyota", "Corolla", TipoDeCombustivel.GASOLINA, 0.058));
        veiculoRepository.save(new Veiculo("Mazda", "Mazda2", TipoDeCombustivel.GASOLINA, 0.054));
        veiculoRepository.save(new Veiculo("Mazda", "Mazda3", TipoDeCombustivel.GASOLINA, 0.062));
        veiculoRepository.save(new Veiculo("Honda", "Civic", TipoDeCombustivel.GASOLINA, 0.065));
        veiculoRepository.save(new Veiculo("Honda", "Jazz", TipoDeCombustivel.GASOLINA, 0.052));
        veiculoRepository.save(new Veiculo("Dacia", "Sandero", TipoDeCombustivel.GASOLINA, 0.056));


// DIESEL
        veiculoRepository.save(new Veiculo("BMW", "320d", TipoDeCombustivel.DIESEL, 0.050));
        veiculoRepository.save(new Veiculo("Mercedes", "C220", TipoDeCombustivel.DIESEL, 0.052));
        veiculoRepository.save(new Veiculo("BMW", "320d", TipoDeCombustivel.DIESEL, 0.050));
        veiculoRepository.save(new Veiculo("BMW", "520d", TipoDeCombustivel.DIESEL, 0.055));
        veiculoRepository.save(new Veiculo("Mercedes", "C220d", TipoDeCombustivel.DIESEL, 0.052));
        veiculoRepository.save(new Veiculo("Mercedes", "E220d", TipoDeCombustivel.DIESEL, 0.056));
        veiculoRepository.save(new Veiculo("Audi", "A3 TDI", TipoDeCombustivel.DIESEL, 0.048));
        veiculoRepository.save(new Veiculo("Audi", "A4 TDI", TipoDeCombustivel.DIESEL, 0.053));
        veiculoRepository.save(new Veiculo("Volkswagen", "Golf TDI", TipoDeCombustivel.DIESEL, 0.047));
        veiculoRepository.save(new Veiculo("Volkswagen", "Passat TDI", TipoDeCombustivel.DIESEL, 0.054));
        veiculoRepository.save(new Veiculo("Peugeot", "308 BlueHDi", TipoDeCombustivel.DIESEL, 0.046));
        veiculoRepository.save(new Veiculo("Peugeot", "508 BlueHDi", TipoDeCombustivel.DIESEL, 0.052));
        veiculoRepository.save(new Veiculo("Renault", "Megane dCi", TipoDeCombustivel.DIESEL, 0.047));
        veiculoRepository.save(new Veiculo("Renault", "Talisman dCi", TipoDeCombustivel.DIESEL, 0.053));
        veiculoRepository.save(new Veiculo("Ford", "Focus TDCi", TipoDeCombustivel.DIESEL, 0.048));
        veiculoRepository.save(new Veiculo("Ford", "Mondeo TDCi", TipoDeCombustivel.DIESEL, 0.055));
        veiculoRepository.save(new Veiculo("Opel", "Astra CDTI", TipoDeCombustivel.DIESEL, 0.047));
        veiculoRepository.save(new Veiculo("Opel", "Insignia CDTI", TipoDeCombustivel.DIESEL, 0.054));
        veiculoRepository.save(new Veiculo("Seat", "Leon TDI", TipoDeCombustivel.DIESEL, 0.046));
        veiculoRepository.save(new Veiculo("Seat", "Toledo TDI", TipoDeCombustivel.DIESEL, 0.052));
        veiculoRepository.save(new Veiculo("Skoda", "Octavia TDI", TipoDeCombustivel.DIESEL, 0.047));
        veiculoRepository.save(new Veiculo("Skoda", "Superb TDI", TipoDeCombustivel.DIESEL, 0.055));
        veiculoRepository.save(new Veiculo("Hyundai", "i30 CRDi", TipoDeCombustivel.DIESEL, 0.048));
        veiculoRepository.save(new Veiculo("Kia", "Ceed CRDi", TipoDeCombustivel.DIESEL, 0.047));
        veiculoRepository.save(new Veiculo("Mazda", "Mazda3 Skyactiv-D", TipoDeCombustivel.DIESEL, 0.046));
        veiculoRepository.save(new Veiculo("Volvo", "V60 D4", TipoDeCombustivel.DIESEL, 0.053));
        veiculoRepository.save(new Veiculo("Volvo", "S90 D5", TipoDeCombustivel.DIESEL, 0.056));


// GPL
        veiculoRepository.save(new Veiculo("Dacia", "Sandero", TipoDeCombustivel.GPL, 0.072));
        veiculoRepository.save(new Veiculo("Opel", "Corsa", TipoDeCombustivel.GPL, 0.075));
        veiculoRepository.save(new Veiculo("Dacia", "Sandero GPL", TipoDeCombustivel.GPL, 0.072));
        veiculoRepository.save(new Veiculo("Dacia", "Duster GPL", TipoDeCombustivel.GPL, 0.078));
        veiculoRepository.save(new Veiculo("Renault", "Clio GPL", TipoDeCombustivel.GPL, 0.070));
        veiculoRepository.save(new Veiculo("Renault", "Captur GPL", TipoDeCombustivel.GPL, 0.075));
        veiculoRepository.save(new Veiculo("Fiat", "Panda GPL", TipoDeCombustivel.GPL, 0.068));
        veiculoRepository.save(new Veiculo("Fiat", "500 GPL", TipoDeCombustivel.GPL, 0.065));
        veiculoRepository.save(new Veiculo("Fiat", "Tipo GPL", TipoDeCombustivel.GPL, 0.072));
        veiculoRepository.save(new Veiculo("Opel", "Corsa GPL", TipoDeCombustivel.GPL, 0.070));
        veiculoRepository.save(new Veiculo("Opel", "Astra GPL", TipoDeCombustivel.GPL, 0.075));
        veiculoRepository.save(new Veiculo("Hyundai", "i10 GPL", TipoDeCombustivel.GPL, 0.067));
        veiculoRepository.save(new Veiculo("Hyundai", "i20 GPL", TipoDeCombustivel.GPL, 0.071));
        veiculoRepository.save(new Veiculo("Kia", "Picanto GPL", TipoDeCombustivel.GPL, 0.066));
        veiculoRepository.save(new Veiculo("Kia", "Rio GPL", TipoDeCombustivel.GPL, 0.070));
        veiculoRepository.save(new Veiculo("Chevrolet", "Aveo GPL", TipoDeCombustivel.GPL, 0.073));
        veiculoRepository.save(new Veiculo("Chevrolet", "Spark GPL", TipoDeCombustivel.GPL, 0.065));
        veiculoRepository.save(new Veiculo("Toyota", "Yaris GPL", TipoDeCombustivel.GPL, 0.071));
        veiculoRepository.save(new Veiculo("Toyota", "Corolla GPL", TipoDeCombustivel.GPL, 0.076));
        veiculoRepository.save(new Veiculo("Ford", "Fiesta GPL", TipoDeCombustivel.GPL, 0.072));
        veiculoRepository.save(new Veiculo("Ford", "Focus GPL", TipoDeCombustivel.GPL, 0.078));
        veiculoRepository.save(new Veiculo("Seat", "Ibiza GPL", TipoDeCombustivel.GPL, 0.070));
        veiculoRepository.save(new Veiculo("Seat", "Leon GPL", TipoDeCombustivel.GPL, 0.075));
        veiculoRepository.save(new Veiculo("Skoda", "Fabia GPL", TipoDeCombustivel.GPL, 0.071));
        veiculoRepository.save(new Veiculo("Skoda", "Octavia GPL", TipoDeCombustivel.GPL, 0.076));
        veiculoRepository.save(new Veiculo("Citroen", "C3 GPL", TipoDeCombustivel.GPL, 0.072));
        veiculoRepository.save(new Veiculo("Citroen", "C4 GPL", TipoDeCombustivel.GPL, 0.077));
    }

    public Veiculo adicionarVeiculo(AdicionarVeiculoModel model) {
        Veiculo veiculo = new Veiculo();

        veiculo.setMatricula(model.getMatricula());
        veiculo.setMarca(model.getMarca());
        veiculo.setModelo(model.getModelo());
        veiculo.setAnoRegisto((model.getAnoRegisto()));


        return veiculoRepository.save(veiculo);
    }

    public List<Veiculo> getAllVeiculos() {
        return veiculoRepository.findAll();
    }

    public Veiculo getVeiculoByMatricula(String matricula) {
        return veiculoRepository.findById(matricula).orElse(null);
    }

    public Veiculo getVeiculoByMarcaEModelo(String marca, String modelo) {
        return veiculoRepository.findAll().stream()
                .filter(v -> v.getMarca().equals(marca) && v.getModelo().equals(modelo))
                .findFirst()
                .orElse(null);
    }
}