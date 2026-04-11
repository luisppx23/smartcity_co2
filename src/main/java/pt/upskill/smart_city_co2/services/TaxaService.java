package pt.upskill.smart_city_co2.services;

import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.EscalaTaxaEmissao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Taxa;
import pt.upskill.smart_city_co2.entities.Veiculo;

import java.time.LocalDate;

@Service
public class TaxaService {

    public Taxa criarTaxa(RegistoKms registoKms) {
        if (registoKms == null) {
            throw new IllegalArgumentException("O registo de kms não pode ser nulo.");
        }

        Veiculo veiculo = registoKms.getOwnership().getVeiculo();
        if (veiculo == null) {
            throw new IllegalArgumentException("O registo não tem veículo associado.");
        }

        double kms = registoKms.getKms_mes();
        double emissaoGPorKm = registoKms.getEmissaoGPorKm();

        EscalaTaxaEmissao escala = EscalaTaxaEmissao.fromEmissao(emissaoGPorKm);
        double taxaPorKm = escala.getTaxaPorKm();
        double valorTaxa = kms * taxaPorKm;

        Taxa taxa = new Taxa();
        taxa.setValor(valorTaxa);
        taxa.setMes_ano(LocalDate.now());
        taxa.setRegistoKms(registoKms);
        taxa.setTaxaPorKmAplicada(taxaPorKm);
        taxa.setEmissaoGPorKmReferencia(emissaoGPorKm);
        taxa.setNivelEscala(escala);

        return taxa;
    }

    public Taxa criarTaxa(RegistoKms registoKms, Municipio municipio) {
        if (registoKms == null) {
            throw new IllegalArgumentException("O registo de kms não pode ser nulo.");
        }

        double kms = registoKms.getKms_mes();
        double emissaoGPorKm = registoKms.getEmissaoGPorKm();

        EscalaTaxaEmissao escala = EscalaTaxaEmissao.fromEmissao(emissaoGPorKm);
        double taxaPorKm = obterTaxaPorKmDoMunicipio(escala, municipio);
        double valorTaxa = kms * taxaPorKm;

        Taxa taxa = new Taxa();
        taxa.setValor(valorTaxa);
        taxa.setMes_ano(LocalDate.now());
        taxa.setRegistoKms(registoKms);
        taxa.setTaxaPorKmAplicada(taxaPorKm);
        taxa.setEmissaoGPorKmReferencia(emissaoGPorKm);
        taxa.setNivelEscala(escala);

        return taxa;
    }

    public double simularTaxa(double emissaoGPorKm, double kms) {
        EscalaTaxaEmissao escala = EscalaTaxaEmissao.fromEmissao(emissaoGPorKm);
        return kms * escala.getTaxaPorKm();
    }

    public double simularTaxa(double emissaoGPorKm, double kms, Municipio municipio) {
        EscalaTaxaEmissao escala = EscalaTaxaEmissao.fromEmissao(emissaoGPorKm);
        double taxaPorKm = obterTaxaPorKmDoMunicipio(escala, municipio);
        return kms * taxaPorKm;
    }

    public EscalaTaxaEmissao obterEscala(double emissaoGPorKm) {
        return EscalaTaxaEmissao.fromEmissao(emissaoGPorKm);
    }

    public double obterTaxaPorKm(double emissaoGPorKm) {
        return EscalaTaxaEmissao.fromEmissao(emissaoGPorKm).getTaxaPorKm();
    }

    private double obterTaxaPorKmDoMunicipio(EscalaTaxaEmissao escala, Municipio municipio) {
        if (municipio == null) {
            return escala.getTaxaPorKm(); // fallback para valores fixos
        }
        switch (escala) {
            case NIVEL_1: return municipio.getTaxaNivel1();
            case NIVEL_2: return municipio.getTaxaNivel2();
            case NIVEL_3: return municipio.getTaxaNivel3();
            case NIVEL_4: return municipio.getTaxaNivel4();
            case NIVEL_5: return municipio.getTaxaNivel5();
            case NIVEL_6: return municipio.getTaxaNivel6();
            case NIVEL_7: return municipio.getTaxaNivel7();
            default: return 0.0;
        }
    }
}