package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.models.AdicionarVeiculoModel;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    VeiculoRepository veiculoRepository;

    @PostConstruct
    public void init() {
        if (veiculoRepository.count() > 0) return;

        // ELÉTRICOS
        salvarVeiculo("AA-00-EV", "Tesla", "Model 3", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2025, 1, 1));
        salvarVeiculo("BB-11-EV", "Nissan", "Leaf", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2026, 1, 1));
        salvarVeiculo("EV-01-AA", "Tesla", "Model S", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("EV-02-AA", "Tesla", "Model X", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-03-AA", "Tesla", "Model Y", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2024, 1, 1));
        salvarVeiculo("EV-04-AA", "Nissan", "Leaf e+", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("EV-05-AA", "BMW", "i3", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2021, 1, 1));
        salvarVeiculo("EV-06-AA", "BMW", "i4", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2024, 1, 1));
        salvarVeiculo("EV-07-AA", "BMW", "iX", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-08-AA", "Audi", "e-tron", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-09-AA", "Audi", "Q4 e-tron", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2024, 1, 1));
        salvarVeiculo("EV-10-AA", "Hyundai", "Kona Electric", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("EV-11-AA", "Hyundai", "Ioniq 5", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-12-AA", "Hyundai", "Ioniq 6", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2024, 1, 1));
        salvarVeiculo("EV-13-AA", "Kia", "EV6", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-14-AA", "Kia", "e-Niro", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("EV-15-AA", "Volkswagen", "ID.3", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("EV-16-AA", "Volkswagen", "ID.4", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-17-AA", "Volkswagen", "ID.5", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2024, 1, 1));
        salvarVeiculo("EV-18-AA", "Mercedes", "EQC", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-19-AA", "Mercedes", "EQS", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2024, 1, 1));
        salvarVeiculo("EV-20-AA", "Renault", "Zoe", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2021, 1, 1));
        salvarVeiculo("EV-21-AA", "Peugeot", "e-208", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("EV-22-AA", "Peugeot", "e-2008", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-23-AA", "Fiat", "500e", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("EV-24-AA", "Volvo", "XC40 Recharge", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("EV-25-AA", "Polestar", "Polestar 2", TipoDeCombustivel.ELETRICO, 0.0, LocalDate.of(2024, 1, 1));

        // HÍBRIDOS
        salvarVeiculo("CC-22-HY", "Toyota", "Prius", TipoDeCombustivel.HIBRIDO, 4.3, LocalDate.of(1998, 1, 1));
        salvarVeiculo("DD-33-HY", "Hyundai", "Ioniq", TipoDeCombustivel.HIBRIDO, 4.0, LocalDate.of(1997, 1, 1));
        salvarVeiculo("HY-01-BB", "Toyota", "Prius", TipoDeCombustivel.HIBRIDO, 4.3, LocalDate.of(2020, 1, 1));
        salvarVeiculo("HY-02-BB", "Toyota", "Corolla Hybrid", TipoDeCombustivel.HIBRIDO, 4.0, LocalDate.of(2021, 1, 1));
        salvarVeiculo("HY-03-BB", "Toyota", "C-HR Hybrid", TipoDeCombustivel.HIBRIDO, 4.2, LocalDate.of(2022, 1, 1));
        salvarVeiculo("HY-04-BB", "Toyota", "RAV4 Hybrid", TipoDeCombustivel.HIBRIDO, 5.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-05-BB", "Honda", "Insight", TipoDeCombustivel.HIBRIDO, 4.5, LocalDate.of(2020, 1, 1));
        salvarVeiculo("HY-06-BB", "Honda", "CR-V Hybrid", TipoDeCombustivel.HIBRIDO, 5.2, LocalDate.of(2022, 1, 1));
        salvarVeiculo("HY-07-BB", "Hyundai", "Ioniq Hybrid", TipoDeCombustivel.HIBRIDO, 4.1, LocalDate.of(2021, 1, 1));
        salvarVeiculo("HY-08-BB", "Hyundai", "Tucson Hybrid", TipoDeCombustivel.HIBRIDO, 5.3, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-09-BB", "Kia", "Niro Hybrid", TipoDeCombustivel.HIBRIDO, 4.4, LocalDate.of(2022, 1, 1));
        salvarVeiculo("HY-10-BB", "Kia", "Sportage Hybrid", TipoDeCombustivel.HIBRIDO, 5.1, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-11-BB", "Ford", "Kuga Hybrid", TipoDeCombustivel.HIBRIDO, 5.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("HY-12-BB", "Ford", "Mondeo Hybrid", TipoDeCombustivel.HIBRIDO, 4.8, LocalDate.of(2020, 1, 1));
        salvarVeiculo("HY-13-BB", "BMW", "330e", TipoDeCombustivel.HIBRIDO, 6.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-14-BB", "BMW", "X5 xDrive45e", TipoDeCombustivel.HIBRIDO, 6.5, LocalDate.of(2024, 1, 1));
        salvarVeiculo("HY-15-BB", "Mercedes", "C300e", TipoDeCombustivel.HIBRIDO, 5.8, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-16-BB", "Mercedes", "E300e", TipoDeCombustivel.HIBRIDO, 6.0, LocalDate.of(2024, 1, 1));
        salvarVeiculo("HY-17-BB", "Volvo", "XC60 Recharge", TipoDeCombustivel.HIBRIDO, 6.2, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-18-BB", "Volvo", "XC90 Recharge", TipoDeCombustivel.HIBRIDO, 6.5, LocalDate.of(2024, 1, 1));
        salvarVeiculo("HY-19-BB", "Peugeot", "3008 Hybrid", TipoDeCombustivel.HIBRIDO, 5.5, LocalDate.of(2022, 1, 1));
        salvarVeiculo("HY-20-BB", "Peugeot", "508 Hybrid", TipoDeCombustivel.HIBRIDO, 5.3, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-21-BB", "Renault", "Clio E-Tech", TipoDeCombustivel.HIBRIDO, 4.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("HY-22-BB", "Renault", "Captur E-Tech", TipoDeCombustivel.HIBRIDO, 4.5, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-23-BB", "Opel", "Astra Hybrid", TipoDeCombustivel.HIBRIDO, 5.0, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-24-BB", "Jeep", "Compass 4xe", TipoDeCombustivel.HIBRIDO, 5.7, LocalDate.of(2023, 1, 1));
        salvarVeiculo("HY-25-BB", "Jeep", "Renegade 4xe", TipoDeCombustivel.HIBRIDO, 5.5, LocalDate.of(2022, 1, 1));

        // GASOLINA
        salvarVeiculo("EE-44-GA", "Volkswagen", "Golf", TipoDeCombustivel.GASOLINA, 6.0, LocalDate.of(1998, 1, 1));
        salvarVeiculo("FF-55-GA", "Ford", "Focus", TipoDeCombustivel.GASOLINA, 6.5, LocalDate.of(2000, 1, 1));
        salvarVeiculo("GA-01-CC", "Volkswagen", "Golf", TipoDeCombustivel.GASOLINA, 6.0, LocalDate.of(2018, 1, 1));
        salvarVeiculo("GA-02-CC", "Volkswagen", "Polo", TipoDeCombustivel.GASOLINA, 5.5, LocalDate.of(2019, 1, 1));
        salvarVeiculo("GA-03-CC", "Ford", "Focus", TipoDeCombustivel.GASOLINA, 6.5, LocalDate.of(2017, 1, 1));
        salvarVeiculo("GA-04-CC", "Ford", "Fiesta", TipoDeCombustivel.GASOLINA, 5.8, LocalDate.of(2018, 1, 1));
        salvarVeiculo("GA-05-CC", "Opel", "Corsa", TipoDeCombustivel.GASOLINA, 5.6, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-06-CC", "Opel", "Astra", TipoDeCombustivel.GASOLINA, 6.2, LocalDate.of(2019, 1, 1));
        salvarVeiculo("GA-07-CC", "Renault", "Clio", TipoDeCombustivel.GASOLINA, 5.4, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-08-CC", "Renault", "Megane", TipoDeCombustivel.GASOLINA, 6.1, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-09-CC", "Peugeot", "208", TipoDeCombustivel.GASOLINA, 5.3, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-10-CC", "Peugeot", "308", TipoDeCombustivel.GASOLINA, 6.0, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-11-CC", "Seat", "Ibiza", TipoDeCombustivel.GASOLINA, 5.5, LocalDate.of(2019, 1, 1));
        salvarVeiculo("GA-12-CC", "Seat", "Leon", TipoDeCombustivel.GASOLINA, 6.1, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-13-CC", "Skoda", "Fabia", TipoDeCombustivel.GASOLINA, 5.4, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-14-CC", "Skoda", "Octavia", TipoDeCombustivel.GASOLINA, 6.2, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-15-CC", "Hyundai", "i20", TipoDeCombustivel.GASOLINA, 5.3, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-16-CC", "Hyundai", "i30", TipoDeCombustivel.GASOLINA, 6.0, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-17-CC", "Kia", "Rio", TipoDeCombustivel.GASOLINA, 5.4, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-18-CC", "Kia", "Ceed", TipoDeCombustivel.GASOLINA, 6.1, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-19-CC", "Toyota", "Yaris", TipoDeCombustivel.GASOLINA, 5.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("GA-20-CC", "Toyota", "Corolla", TipoDeCombustivel.GASOLINA, 5.8, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-21-CC", "Mazda", "Mazda2", TipoDeCombustivel.GASOLINA, 5.4, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-22-CC", "Mazda", "Mazda3", TipoDeCombustivel.GASOLINA, 6.2, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-23-CC", "Honda", "Civic", TipoDeCombustivel.GASOLINA, 6.5, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GA-24-CC", "Honda", "Jazz", TipoDeCombustivel.GASOLINA, 5.2, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GA-25-CC", "Dacia", "Sandero", TipoDeCombustivel.GASOLINA, 5.6, LocalDate.of(2022, 1, 1));

        // DIESEL
        salvarVeiculo("GG-66-DI", "BMW", "320d", TipoDeCombustivel.DIESEL, 5.0, LocalDate.of(1998, 1, 1));
        salvarVeiculo("HH-77-DI", "Mercedes", "C220", TipoDeCombustivel.DIESEL, 5.2, LocalDate.of(1997, 1, 1));
        salvarVeiculo("DI-01-DD", "BMW", "320d", TipoDeCombustivel.DIESEL, 5.0, LocalDate.of(2018, 1, 1));
        salvarVeiculo("DI-02-DD", "BMW", "520d", TipoDeCombustivel.DIESEL, 5.5, LocalDate.of(2019, 1, 1));
        salvarVeiculo("DI-03-DD", "Mercedes", "C220d", TipoDeCombustivel.DIESEL, 5.2, LocalDate.of(2008, 1, 1));
        salvarVeiculo("DI-04-DD", "Mercedes", "E220d", TipoDeCombustivel.DIESEL, 5.6, LocalDate.of(2009, 1, 1));
        salvarVeiculo("DI-05-DD", "Audi", "A3 TDI", TipoDeCombustivel.DIESEL, 4.8, LocalDate.of(2020, 1, 1));
        salvarVeiculo("DI-06-DD", "Audi", "A4 TDI", TipoDeCombustivel.DIESEL, 5.3, LocalDate.of(2019, 1, 1));
        salvarVeiculo("DI-07-DD", "Volkswagen", "Golf TDI", TipoDeCombustivel.DIESEL, 4.7, LocalDate.of(2020, 1, 1));
        salvarVeiculo("DI-08-DD", "Volkswagen", "Passat TDI", TipoDeCombustivel.DIESEL, 5.4, LocalDate.of(2019, 1, 1));
        salvarVeiculo("DI-09-DD", "Peugeot", "308 BlueHDi", TipoDeCombustivel.DIESEL, 4.6, LocalDate.of(2021, 1, 1));
        salvarVeiculo("DI-10-DD", "Peugeot", "508 BlueHDi", TipoDeCombustivel.DIESEL, 5.2, LocalDate.of(2020, 1, 1));
        salvarVeiculo("DI-11-DD", "Renault", "Megane dCi", TipoDeCombustivel.DIESEL, 4.7, LocalDate.of(2020, 1, 1));
        salvarVeiculo("DI-12-DD", "Renault", "Talisman dCi", TipoDeCombustivel.DIESEL, 5.3, LocalDate.of(2019, 1, 1));
        salvarVeiculo("DI-13-DD", "Ford", "Focus TDCi", TipoDeCombustivel.DIESEL, 4.8, LocalDate.of(2020, 1, 1));
        salvarVeiculo("DI-14-DD", "Ford", "Mondeo TDCi", TipoDeCombustivel.DIESEL, 5.5, LocalDate.of(2019, 1, 1));
        salvarVeiculo("DI-15-DD", "Opel", "Astra CDTI", TipoDeCombustivel.DIESEL, 4.7, LocalDate.of(2021, 1, 1));
        salvarVeiculo("DI-16-DD", "Opel", "Insignia CDTI", TipoDeCombustivel.DIESEL, 5.4, LocalDate.of(2010, 1, 1));
        salvarVeiculo("DI-17-DD", "Seat", "Leon TDI", TipoDeCombustivel.DIESEL, 4.6, LocalDate.of(2021, 1, 1));
        salvarVeiculo("DI-18-DD", "Seat", "Toledo TDI", TipoDeCombustivel.DIESEL, 5.2, LocalDate.of(2019, 1, 1));
        salvarVeiculo("DI-19-DD", "Skoda", "Octavia TDI", TipoDeCombustivel.DIESEL, 4.7, LocalDate.of(2021, 1, 1));
        salvarVeiculo("DI-20-DD", "Skoda", "Superb TDI", TipoDeCombustivel.DIESEL, 5.5, LocalDate.of(2020, 1, 1));
        salvarVeiculo("DI-21-DD", "Hyundai", "i30 CRDi", TipoDeCombustivel.DIESEL, 4.8, LocalDate.of(2012, 1, 1));
        salvarVeiculo("DI-22-DD", "Kia", "Ceed CRDi", TipoDeCombustivel.DIESEL, 4.7, LocalDate.of(2015, 1, 1));
        salvarVeiculo("DI-23-DD", "Mazda", "Mazda3 Skyactiv-D", TipoDeCombustivel.DIESEL, 4.6, LocalDate.of(2020, 1, 1));
        salvarVeiculo("DI-24-DD", "Volvo", "V60 D4", TipoDeCombustivel.DIESEL, 5.3, LocalDate.of(2016, 1, 1));
        salvarVeiculo("DI-25-DD", "Volvo", "S90 D5", TipoDeCombustivel.DIESEL, 5.6, LocalDate.of(2014, 1, 1));

        // GPL
        salvarVeiculo("II-88-GPL", "Dacia", "Sandero", TipoDeCombustivel.GPL, 7.2, LocalDate.of(1998, 1, 1));
        salvarVeiculo("JJ-99-GPL", "Opel", "Corsa", TipoDeCombustivel.GPL, 7.5, LocalDate.of(1997, 1, 1));
        salvarVeiculo("GP-01-EE", "Dacia", "Sandero GPL", TipoDeCombustivel.GPL, 7.2, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-02-EE", "Dacia", "Duster GPL", TipoDeCombustivel.GPL, 7.8, LocalDate.of(2022, 1, 1));
        salvarVeiculo("GP-03-EE", "Renault", "Clio GPL", TipoDeCombustivel.GPL, 7.0, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-04-EE", "Renault", "Captur GPL", TipoDeCombustivel.GPL, 7.5, LocalDate.of(2022, 1, 1));
        salvarVeiculo("GP-05-EE", "Fiat", "Panda GPL", TipoDeCombustivel.GPL, 6.8, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GP-06-EE", "Fiat", "500 GPL", TipoDeCombustivel.GPL, 6.5, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-07-EE", "Fiat", "Tipo GPL", TipoDeCombustivel.GPL, 7.2, LocalDate.of(2022, 1, 1));
        salvarVeiculo("GP-08-EE", "Opel", "Corsa GPL", TipoDeCombustivel.GPL, 7.0, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-09-EE", "Opel", "Astra GPL", TipoDeCombustivel.GPL, 7.5, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GP-10-EE", "Hyundai", "i10 GPL", TipoDeCombustivel.GPL, 6.7, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-11-EE", "Hyundai", "i20 GPL", TipoDeCombustivel.GPL, 7.1, LocalDate.of(2022, 1, 1));
        salvarVeiculo("GP-12-EE", "Kia", "Picanto GPL", TipoDeCombustivel.GPL, 6.6, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-13-EE", "Kia", "Rio GPL", TipoDeCombustivel.GPL, 7.0, LocalDate.of(2022, 1, 1));
        salvarVeiculo("GP-14-EE", "Chevrolet", "Aveo GPL", TipoDeCombustivel.GPL, 7.3, LocalDate.of(2019, 1, 1));
        salvarVeiculo("GP-15-EE", "Chevrolet", "Spark GPL", TipoDeCombustivel.GPL, 6.5, LocalDate.of(2018, 1, 1));
        salvarVeiculo("GP-16-EE", "Toyota", "Yaris GPL", TipoDeCombustivel.GPL, 7.1, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GP-17-EE", "Toyota", "Corolla GPL", TipoDeCombustivel.GPL, 7.6, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-18-EE", "Ford", "Fiesta GPL", TipoDeCombustivel.GPL, 7.2, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GP-19-EE", "Ford", "Focus GPL", TipoDeCombustivel.GPL, 7.8, LocalDate.of(2019, 1, 1));
        salvarVeiculo("GP-20-EE", "Seat", "Ibiza GPL", TipoDeCombustivel.GPL, 7.0, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-21-EE", "Seat", "Leon GPL", TipoDeCombustivel.GPL, 7.5, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GP-22-EE", "Skoda", "Fabia GPL", TipoDeCombustivel.GPL, 7.1, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-23-EE", "Skoda", "Octavia GPL", TipoDeCombustivel.GPL, 7.6, LocalDate.of(2020, 1, 1));
        salvarVeiculo("GP-24-EE", "Citroen", "C3 GPL", TipoDeCombustivel.GPL, 7.2, LocalDate.of(2021, 1, 1));
        salvarVeiculo("GP-25-EE", "Citroen", "C4 GPL", TipoDeCombustivel.GPL, 7.7, LocalDate.of(2022, 1, 1));
    }

    private void salvarVeiculo(String matricula, String marca, String modelo,
                               TipoDeCombustivel tipo, double consumo, LocalDate anoRegisto) {
        Veiculo veiculo = new Veiculo(marca, modelo, tipo, consumo);
        veiculo.setMatricula(matricula);
        veiculo.setAnoRegisto(anoRegisto);
        veiculoRepository.save(veiculo);
    }

    public Veiculo adicionarVeiculo(AdicionarVeiculoModel model) {
        Veiculo veiculo = new Veiculo(
                model.getMarca(),
                model.getModelo(),
                (TipoDeCombustivel) model.getTipoDeCombustivel(),
                model.getConsumo()
        );
        veiculo.setMatricula(model.getMatricula());
        veiculo.setAnoRegisto(model.getAnoRegisto());

        return veiculoRepository.save(veiculo);
    }

    public List<Veiculo> getAllVeiculos() {
        return veiculoRepository.findAll();
    }

    public Veiculo getVeiculoByMatricula(String matricula) {
        return veiculoRepository.findByMatricula(matricula);
    }
}