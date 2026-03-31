package pt.upskill.smart_city_co2.services;

import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Taxa;

import java.time.LocalDate;

@Service
public class TaxaService {

    public Taxa criarTaxa(RegistoKms registoKms) {
        double emissaoKg = registoKms.getEmissaoEfetivaKg();
        double valorTaxa = emissaoKg * 0.10; // valor fixo para teste

        Taxa taxa = new Taxa();
        taxa.setValor(valorTaxa);
        taxa.setMes_ano(LocalDate.now());
        taxa.setRegistoKms(registoKms);

        return taxa;
    }

    public double simularTaxa(double emissaoGPorKm, double kms) {
        double emissaoKg = (kms * emissaoGPorKm) / 1000;
        return emissaoKg * 0.10; // valor fixo para teste (depois substituir pela tabela real)
    }
}