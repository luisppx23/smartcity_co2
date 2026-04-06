package pt.upskill.smart_city_co2.services;

import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.EscalaTaxaEmissao;
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

    public double simularTaxa(double emissaoGPorKm, double kms) {
        EscalaTaxaEmissao escala = EscalaTaxaEmissao.fromEmissao(emissaoGPorKm);
        return kms * escala.getTaxaPorKm();
    }

    public EscalaTaxaEmissao obterEscala(double emissaoGPorKm) {
        return EscalaTaxaEmissao.fromEmissao(emissaoGPorKm);
    }

    public double obterTaxaPorKm(double emissaoGPorKm) {
        return EscalaTaxaEmissao.fromEmissao(emissaoGPorKm).getTaxaPorKm();
    }
}