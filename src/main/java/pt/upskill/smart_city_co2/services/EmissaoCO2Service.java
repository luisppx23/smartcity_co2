package pt.upskill.smart_city_co2.services;

import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.entities.Ownership;


@Service
public class EmissaoCO2Service {

    public double calcularEmissaoGPorKm(Ownership ownership, int anoReferencia) {
        TipoDeCombustivel tipo = ownership.getVeiculo().getTipoDeCombustivel();

        if (tipo == TipoDeCombustivel.ELETRICO) {
            return 0.0;
        }

        int anosVeiculo = Math.max(0, anoReferencia - ownership.getAnoRegisto());

        double consumo = ownership.getVeiculo().getConsumo(); // L/100km
        double fatorEmissaogPorLitro = tipo.getFatorEmissaogPorLitro(); // g/L
        double fatorDegradacao = tipo.getFatorDegradacaoAnual(); //%/ano

        double degradacao= 1 - (fatorDegradacao * anosVeiculo);

        if (degradacao <= 0.7) { //Ter só em conta uma degradação até 40%
            degradacao = 0.7;
        }

        double consumoLporKm = ownership.getVeiculo().getConsumo(); // já em L/km
        double emissaoBase = consumoLporKm * fatorEmissaogPorLitro;

        return emissaoBase/ degradacao; //g/km
    }

    public double calcularEmissaoEfetivaG(Ownership ownership, double kms, int anoReferencia) {
        double emissaoGPorKm = calcularEmissaoGPorKm(ownership, anoReferencia);
        return emissaoGPorKm * kms; //g
    }

    public double calcularEmissaoEfetivaKg(Ownership ownership, double kms, int anoReferencia) {
        return calcularEmissaoEfetivaG(ownership, kms, anoReferencia) / 1000.0;
    }
}
