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
        veiculoRepository.save(new Veiculo("AA-00-EV", "Tesla", "Model 3", TipoDeCombustivel.ELETRICO,
                0.0, 0.0, 0.0, 0.0, 2025));
        veiculoRepository.save(new Veiculo("BB-11-EV", "Nissan", "Leaf", TipoDeCombustivel.ELETRICO,
                0.0, 0.0, 0.0, 0.0, 2026));
        veiculoRepository.save(new Veiculo("EV-01-AA", "Tesla", "Model S", TipoDeCombustivel.ELETRICO, 0,0,0,0,2022));
        veiculoRepository.save(new Veiculo("EV-02-AA", "Tesla", "Model X", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-03-AA", "Tesla", "Model Y", TipoDeCombustivel.ELETRICO, 0,0,0,0,2024));
        veiculoRepository.save(new Veiculo("EV-04-AA", "Nissan", "Leaf e+", TipoDeCombustivel.ELETRICO, 0,0,0,0,2022));
        veiculoRepository.save(new Veiculo("EV-05-AA", "BMW", "i3", TipoDeCombustivel.ELETRICO, 0,0,0,0,2021));
        veiculoRepository.save(new Veiculo("EV-06-AA", "BMW", "i4", TipoDeCombustivel.ELETRICO, 0,0,0,0,2024));
        veiculoRepository.save(new Veiculo("EV-07-AA", "BMW", "iX", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-08-AA", "Audi", "e-tron", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-09-AA", "Audi", "Q4 e-tron", TipoDeCombustivel.ELETRICO, 0,0,0,0,2024));
        veiculoRepository.save(new Veiculo("EV-10-AA", "Hyundai", "Kona Electric", TipoDeCombustivel.ELETRICO, 0,0,0,0,2022));
        veiculoRepository.save(new Veiculo("EV-11-AA", "Hyundai", "Ioniq 5", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-12-AA", "Hyundai", "Ioniq 6", TipoDeCombustivel.ELETRICO, 0,0,0,0,2024));
        veiculoRepository.save(new Veiculo("EV-13-AA", "Kia", "EV6", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-14-AA", "Kia", "e-Niro", TipoDeCombustivel.ELETRICO, 0,0,0,0,2022));
        veiculoRepository.save(new Veiculo("EV-15-AA", "Volkswagen", "ID.3", TipoDeCombustivel.ELETRICO, 0,0,0,0,2022));
        veiculoRepository.save(new Veiculo("EV-16-AA", "Volkswagen", "ID.4", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-17-AA", "Volkswagen", "ID.5", TipoDeCombustivel.ELETRICO, 0,0,0,0,2024));
        veiculoRepository.save(new Veiculo("EV-18-AA", "Mercedes", "EQC", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-19-AA", "Mercedes", "EQS", TipoDeCombustivel.ELETRICO, 0,0,0,0,2024));
        veiculoRepository.save(new Veiculo("EV-20-AA", "Renault", "Zoe", TipoDeCombustivel.ELETRICO, 0,0,0,0,2021));
        veiculoRepository.save(new Veiculo("EV-21-AA", "Peugeot", "e-208", TipoDeCombustivel.ELETRICO, 0,0,0,0,2022));
        veiculoRepository.save(new Veiculo("EV-22-AA", "Peugeot", "e-2008", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-23-AA", "Fiat", "500e", TipoDeCombustivel.ELETRICO, 0,0,0,0,2022));
        veiculoRepository.save(new Veiculo("EV-24-AA", "Volvo", "XC40 Recharge", TipoDeCombustivel.ELETRICO, 0,0,0,0,2023));
        veiculoRepository.save(new Veiculo("EV-25-AA", "Polestar", "Polestar 2", TipoDeCombustivel.ELETRICO, 0,0,0,0,2024));

        // HÍBRIDOS
        veiculoRepository.save(new Veiculo("CC-22-HY", "Toyota", "Prius", TipoDeCombustivel.HIBRIDO,
                0.1235, 4.3, 2.31, 0.7, 1998));
        veiculoRepository.save(new Veiculo("DD-33-HY", "Hyundai", "Ioniq", TipoDeCombustivel.HIBRIDO,
                0.1159, 4.0, 2.31, 0.7, 1997));
        veiculoRepository.save(new Veiculo("HY-01-BB", "Toyota", "Prius", TipoDeCombustivel.HIBRIDO, 0.12,4.3,2.31,0.7,2020));
        veiculoRepository.save(new Veiculo("HY-02-BB", "Toyota", "Corolla Hybrid", TipoDeCombustivel.HIBRIDO, 0.11,4.0,2.31,0.7,2021));
        veiculoRepository.save(new Veiculo("HY-03-BB", "Toyota", "C-HR Hybrid", TipoDeCombustivel.HIBRIDO, 0.12,4.2,2.31,0.7,2022));
        veiculoRepository.save(new Veiculo("HY-04-BB", "Toyota", "RAV4 Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.0,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-05-BB", "Honda", "Insight", TipoDeCombustivel.HIBRIDO, 0.12,4.5,2.31,0.7,2020));
        veiculoRepository.save(new Veiculo("HY-06-BB", "Honda", "CR-V Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.2,2.31,0.7,2022));
        veiculoRepository.save(new Veiculo("HY-07-BB", "Hyundai", "Ioniq Hybrid", TipoDeCombustivel.HIBRIDO, 0.11,4.1,2.31,0.7,2021));
        veiculoRepository.save(new Veiculo("HY-08-BB", "Hyundai", "Tucson Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.3,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-09-BB", "Kia", "Niro Hybrid", TipoDeCombustivel.HIBRIDO, 0.12,4.4,2.31,0.7,2022));
        veiculoRepository.save(new Veiculo("HY-10-BB", "Kia", "Sportage Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.1,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-11-BB", "Ford", "Kuga Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.0,2.31,0.7,2022));
        veiculoRepository.save(new Veiculo("HY-12-BB", "Ford", "Mondeo Hybrid", TipoDeCombustivel.HIBRIDO, 0.12,4.8,2.31,0.7,2020));
        veiculoRepository.save(new Veiculo("HY-13-BB", "BMW", "330e", TipoDeCombustivel.HIBRIDO, 0.14,6.0,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-14-BB", "BMW", "X5 xDrive45e", TipoDeCombustivel.HIBRIDO, 0.15,6.5,2.31,0.7,2024));
        veiculoRepository.save(new Veiculo("HY-15-BB", "Mercedes", "C300e", TipoDeCombustivel.HIBRIDO, 0.14,5.8,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-16-BB", "Mercedes", "E300e", TipoDeCombustivel.HIBRIDO, 0.14,6.0,2.31,0.7,2024));
        veiculoRepository.save(new Veiculo("HY-17-BB", "Volvo", "XC60 Recharge", TipoDeCombustivel.HIBRIDO, 0.15,6.2,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-18-BB", "Volvo", "XC90 Recharge", TipoDeCombustivel.HIBRIDO, 0.16,6.5,2.31,0.7,2024));
        veiculoRepository.save(new Veiculo("HY-19-BB", "Peugeot", "3008 Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.5,2.31,0.7,2022));
        veiculoRepository.save(new Veiculo("HY-20-BB", "Peugeot", "508 Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.3,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-21-BB", "Renault", "Clio E-Tech", TipoDeCombustivel.HIBRIDO, 0.11,4.0,2.31,0.7,2022));
        veiculoRepository.save(new Veiculo("HY-22-BB", "Renault", "Captur E-Tech", TipoDeCombustivel.HIBRIDO, 0.12,4.5,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-23-BB", "Opel", "Astra Hybrid", TipoDeCombustivel.HIBRIDO, 0.13,5.0,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-24-BB", "Jeep", "Compass 4xe", TipoDeCombustivel.HIBRIDO, 0.14,5.7,2.31,0.7,2023));
        veiculoRepository.save(new Veiculo("HY-25-BB", "Jeep", "Renegade 4xe", TipoDeCombustivel.HIBRIDO, 0.14,5.5,2.31,0.7,2022));
        // GASOLINA
        veiculoRepository.save(new Veiculo("EE-44-GA", "Volkswagen", "Golf", TipoDeCombustivel.GASOLINA,
                0.1925, 6.0, 2.31, 1.0, 1998));
        veiculoRepository.save(new Veiculo("FF-55-GA", "Ford", "Focus", TipoDeCombustivel.GASOLINA,
                0.2029, 6.5, 2.31, 1.0, 2000));
        veiculoRepository.save(new Veiculo("GA-01-CC", "Volkswagen", "Golf", TipoDeCombustivel.GASOLINA, 0.19,6.0,2.31,1.0,2018));
        veiculoRepository.save(new Veiculo("GA-02-CC", "Volkswagen", "Polo", TipoDeCombustivel.GASOLINA, 0.17,5.5,2.31,1.0,2019));
        veiculoRepository.save(new Veiculo("GA-03-CC", "Ford", "Focus", TipoDeCombustivel.GASOLINA, 0.20,6.5,2.31,1.0,2017));
        veiculoRepository.save(new Veiculo("GA-04-CC", "Ford", "Fiesta", TipoDeCombustivel.GASOLINA, 0.18,5.8,2.31,1.0,2018));
        veiculoRepository.save(new Veiculo("GA-05-CC", "Opel", "Corsa", TipoDeCombustivel.GASOLINA, 0.18,5.6,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-06-CC", "Opel", "Astra", TipoDeCombustivel.GASOLINA, 0.19,6.2,2.31,1.0,2019));
        veiculoRepository.save(new Veiculo("GA-07-CC", "Renault", "Clio", TipoDeCombustivel.GASOLINA, 0.17,5.4,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-08-CC", "Renault", "Megane", TipoDeCombustivel.GASOLINA, 0.19,6.1,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-09-CC", "Peugeot", "208", TipoDeCombustivel.GASOLINA, 0.17,5.3,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-10-CC", "Peugeot", "308", TipoDeCombustivel.GASOLINA, 0.19,6.0,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-11-CC", "Seat", "Ibiza", TipoDeCombustivel.GASOLINA, 0.17,5.5,2.31,1.0,2019));
        veiculoRepository.save(new Veiculo("GA-12-CC", "Seat", "Leon", TipoDeCombustivel.GASOLINA, 0.19,6.1,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-13-CC", "Skoda", "Fabia", TipoDeCombustivel.GASOLINA, 0.17,5.4,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-14-CC", "Skoda", "Octavia", TipoDeCombustivel.GASOLINA, 0.19,6.2,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-15-CC", "Hyundai", "i20", TipoDeCombustivel.GASOLINA, 0.17,5.3,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-16-CC", "Hyundai", "i30", TipoDeCombustivel.GASOLINA, 0.19,6.0,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-17-CC", "Kia", "Rio", TipoDeCombustivel.GASOLINA, 0.17,5.4,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-18-CC", "Kia", "Ceed", TipoDeCombustivel.GASOLINA, 0.19,6.1,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-19-CC", "Toyota", "Yaris", TipoDeCombustivel.GASOLINA, 0.16,5.0,2.31,1.0,2022));
        veiculoRepository.save(new Veiculo("GA-20-CC", "Toyota", "Corolla", TipoDeCombustivel.GASOLINA, 0.18,5.8,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-21-CC", "Mazda", "Mazda2", TipoDeCombustivel.GASOLINA, 0.17,5.4,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-22-CC", "Mazda", "Mazda3", TipoDeCombustivel.GASOLINA, 0.19,6.2,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-23-CC", "Honda", "Civic", TipoDeCombustivel.GASOLINA, 0.20,6.5,2.31,1.0,2020));
        veiculoRepository.save(new Veiculo("GA-24-CC", "Honda", "Jazz", TipoDeCombustivel.GASOLINA, 0.17,5.2,2.31,1.0,2021));
        veiculoRepository.save(new Veiculo("GA-25-CC", "Dacia", "Sandero", TipoDeCombustivel.GASOLINA, 0.18,5.6,2.31,1.0,2022));
        // DIESEL
        veiculoRepository.save(new Veiculo("GG-66-DI", "BMW", "320d", TipoDeCombustivel.DIESEL,
                0.1701, 5.0, 2.64, 0.8, 1998));
        veiculoRepository.save(new Veiculo("HH-77-DI", "Mercedes", "C220", TipoDeCombustivel.DIESEL,
                0.1788, 5.2, 2.64, 0.8, 1997));
        veiculoRepository.save(new Veiculo("DI-01-DD", "BMW", "320d", TipoDeCombustivel.DIESEL, 0.17,5.0,2.64,0.8,2018));
        veiculoRepository.save(new Veiculo("DI-02-DD", "BMW", "520d", TipoDeCombustivel.DIESEL, 0.18,5.5,2.64,0.8,2019));
        veiculoRepository.save(new Veiculo("DI-03-DD", "Mercedes", "C220d", TipoDeCombustivel.DIESEL, 0.18,5.2,2.64,0.8,2008));
        veiculoRepository.save(new Veiculo("DI-04-DD", "Mercedes", "E220d", TipoDeCombustivel.DIESEL, 0.19,5.6,2.64,0.8,2009));
        veiculoRepository.save(new Veiculo("DI-05-DD", "Audi", "A3 TDI", TipoDeCombustivel.DIESEL, 0.17,4.8,2.64,0.8,2020));
        veiculoRepository.save(new Veiculo("DI-06-DD", "Audi", "A4 TDI", TipoDeCombustivel.DIESEL, 0.18,5.3,2.64,0.8,2019));
        veiculoRepository.save(new Veiculo("DI-07-DD", "Volkswagen", "Golf TDI", TipoDeCombustivel.DIESEL, 0.17,4.7,2.64,0.8,2020));
        veiculoRepository.save(new Veiculo("DI-08-DD", "Volkswagen", "Passat TDI", TipoDeCombustivel.DIESEL, 0.18,5.4,2.64,0.8,2019));
        veiculoRepository.save(new Veiculo("DI-09-DD", "Peugeot", "308 BlueHDi", TipoDeCombustivel.DIESEL, 0.17,4.6,2.64,0.8,2021));
        veiculoRepository.save(new Veiculo("DI-10-DD", "Peugeot", "508 BlueHDi", TipoDeCombustivel.DIESEL, 0.18,5.2,2.64,0.8,2020));
        veiculoRepository.save(new Veiculo("DI-11-DD", "Renault", "Megane dCi", TipoDeCombustivel.DIESEL, 0.17,4.7,2.64,0.8,2020));
        veiculoRepository.save(new Veiculo("DI-12-DD", "Renault", "Talisman dCi", TipoDeCombustivel.DIESEL, 0.18,5.3,2.64,0.8,2019));
        veiculoRepository.save(new Veiculo("DI-13-DD", "Ford", "Focus TDCi", TipoDeCombustivel.DIESEL, 0.17,4.8,2.64,0.8,2020));
        veiculoRepository.save(new Veiculo("DI-14-DD", "Ford", "Mondeo TDCi", TipoDeCombustivel.DIESEL, 0.18,5.5,2.64,0.8,2019));
        veiculoRepository.save(new Veiculo("DI-15-DD", "Opel", "Astra CDTI", TipoDeCombustivel.DIESEL, 0.17,4.7,2.64,0.8,2021));
        veiculoRepository.save(new Veiculo("DI-16-DD", "Opel", "Insignia CDTI", TipoDeCombustivel.DIESEL, 0.18,5.4,2.64,0.8,2010));
        veiculoRepository.save(new Veiculo("DI-17-DD", "Seat", "Leon TDI", TipoDeCombustivel.DIESEL, 0.17,4.6,2.64,0.8,2021));
        veiculoRepository.save(new Veiculo("DI-18-DD", "Seat", "Toledo TDI", TipoDeCombustivel.DIESEL, 0.18,5.2,2.64,0.8,2019));
        veiculoRepository.save(new Veiculo("DI-19-DD", "Skoda", "Octavia TDI", TipoDeCombustivel.DIESEL, 0.17,4.7,2.64,0.8,2021));
        veiculoRepository.save(new Veiculo("DI-20-DD", "Skoda", "Superb TDI", TipoDeCombustivel.DIESEL, 0.18,5.5,2.64,0.8,2020));
        veiculoRepository.save(new Veiculo("DI-21-DD", "Hyundai", "i30 CRDi", TipoDeCombustivel.DIESEL, 0.17,4.8,2.64,0.8,2012));
        veiculoRepository.save(new Veiculo("DI-22-DD", "Kia", "Ceed CRDi", TipoDeCombustivel.DIESEL, 0.17,4.7,2.64,0.8,2015));
        veiculoRepository.save(new Veiculo("DI-23-DD", "Mazda", "Mazda3 Skyactiv-D", TipoDeCombustivel.DIESEL, 0.17,4.6,2.64,0.8,2020));
        veiculoRepository.save(new Veiculo("DI-24-DD", "Volvo", "V60 D4", TipoDeCombustivel.DIESEL, 0.18,5.3,2.64,0.8,2016));
        veiculoRepository.save(new Veiculo("DI-25-DD", "Volvo", "S90 D5", TipoDeCombustivel.DIESEL, 0.19,5.6,2.64,0.8,2014));

        // GPL
        veiculoRepository.save(new Veiculo("II-88-GPL", "Dacia", "Sandero", TipoDeCombustivel.GPL,
                0.151, 7.2, 1.51, 1.0, 1998));
        veiculoRepository.save(new Veiculo("JJ-99-GPL", "Opel", "Corsa", TipoDeCombustivel.GPL,
                0.1595, 7.5, 1.51, 1.0, 1997));
        veiculoRepository.save(new Veiculo("GP-01-EE", "Dacia", "Sandero GPL", TipoDeCombustivel.GPL, 0.15,7.2,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-02-EE", "Dacia", "Duster GPL", TipoDeCombustivel.GPL, 0.16,7.8,1.51,1.0,2022));
        veiculoRepository.save(new Veiculo("GP-03-EE", "Renault", "Clio GPL", TipoDeCombustivel.GPL, 0.15,7.0,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-04-EE", "Renault", "Captur GPL", TipoDeCombustivel.GPL, 0.16,7.5,1.51,1.0,2022));
        veiculoRepository.save(new Veiculo("GP-05-EE", "Fiat", "Panda GPL", TipoDeCombustivel.GPL, 0.14,6.8,1.51,1.0,2020));
        veiculoRepository.save(new Veiculo("GP-06-EE", "Fiat", "500 GPL", TipoDeCombustivel.GPL, 0.14,6.5,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-07-EE", "Fiat", "Tipo GPL", TipoDeCombustivel.GPL, 0.15,7.2,1.51,1.0,2022));
        veiculoRepository.save(new Veiculo("GP-08-EE", "Opel", "Corsa GPL", TipoDeCombustivel.GPL, 0.15,7.0,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-09-EE", "Opel", "Astra GPL", TipoDeCombustivel.GPL, 0.16,7.5,1.51,1.0,2020));
        veiculoRepository.save(new Veiculo("GP-10-EE", "Hyundai", "i10 GPL", TipoDeCombustivel.GPL, 0.14,6.7,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-11-EE", "Hyundai", "i20 GPL", TipoDeCombustivel.GPL, 0.15,7.1,1.51,1.0,2022));
        veiculoRepository.save(new Veiculo("GP-12-EE", "Kia", "Picanto GPL", TipoDeCombustivel.GPL, 0.14,6.6,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-13-EE", "Kia", "Rio GPL", TipoDeCombustivel.GPL, 0.15,7.0,1.51,1.0,2022));
        veiculoRepository.save(new Veiculo("GP-14-EE", "Chevrolet", "Aveo GPL", TipoDeCombustivel.GPL, 0.15,7.3,1.51,1.0,2019));
        veiculoRepository.save(new Veiculo("GP-15-EE", "Chevrolet", "Spark GPL", TipoDeCombustivel.GPL, 0.14,6.5,1.51,1.0,2018));
        veiculoRepository.save(new Veiculo("GP-16-EE", "Toyota", "Yaris GPL", TipoDeCombustivel.GPL, 0.15,7.1,1.51,1.0,2020));
        veiculoRepository.save(new Veiculo("GP-17-EE", "Toyota", "Corolla GPL", TipoDeCombustivel.GPL, 0.16,7.6,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-18-EE", "Ford", "Fiesta GPL", TipoDeCombustivel.GPL, 0.15,7.2,1.51,1.0,2020));
        veiculoRepository.save(new Veiculo("GP-19-EE", "Ford", "Focus GPL", TipoDeCombustivel.GPL, 0.16,7.8,1.51,1.0,2019));
        veiculoRepository.save(new Veiculo("GP-20-EE", "Seat", "Ibiza GPL", TipoDeCombustivel.GPL, 0.15,7.0,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-21-EE", "Seat", "Leon GPL", TipoDeCombustivel.GPL, 0.16,7.5,1.51,1.0,2020));
        veiculoRepository.save(new Veiculo("GP-22-EE", "Skoda", "Fabia GPL", TipoDeCombustivel.GPL, 0.15,7.1,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-23-EE", "Skoda", "Octavia GPL", TipoDeCombustivel.GPL, 0.16,7.6,1.51,1.0,2020));
        veiculoRepository.save(new Veiculo("GP-24-EE", "Citroen", "C3 GPL", TipoDeCombustivel.GPL, 0.15,7.2,1.51,1.0,2021));
        veiculoRepository.save(new Veiculo("GP-25-EE", "Citroen", "C4 GPL", TipoDeCombustivel.GPL, 0.16,7.7,1.51,1.0,2022));
    }

    public List<Veiculo> getAllVeiculos() {
        return veiculoRepository.findAll();
    }

    public Veiculo getVeiculoByMatricula(String matricula) {
        return veiculoRepository.findById(matricula).orElse(null);
    }
}