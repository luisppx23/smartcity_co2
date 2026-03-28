package pt.upskill.smart_city_co2.services;

import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.TipoDeCombustivel;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Veiculo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class EmissaoCO2Service {

    public double calcularEmissaoGPorKm(Veiculo veiculo, int anoReferencia) {
        TipoDeCombustivel tipo = veiculo.getTipoDeCombustivel();

        if (tipo == TipoDeCombustivel.ELETRICO) {
            return 0.0;
        }

        int anosVeiculo = Math.max(0, anoReferencia - veiculo.getAnoRegisto());

        double consumo = veiculo.getConsumo(); // L/100km
        double fatorEmissaogPorLitro = tipo.getFatorEmissaogPorLitro(); // g/L
        double fatorDegradacao = tipo.getFatorDegradacaoAnual(); //%/ano

        double degradacao= 1 - (fatorDegradacao * anosVeiculo);

        if (degradacao <= 0.6) { //Ter só em conta uma degradação até 40%
            return 0.6;
        }

        double emissaoBase = (consumo * fatorEmissaogPorLitro ); //g/km

        return emissaoBase/ degradacao; //g/km
    }

    public double calcularEmissaoEfetivaG(Veiculo veiculo, double kms, int anoReferencia) {
        double emissaoGPorKm = calcularEmissaoGPorKm(veiculo, anoReferencia);
        return emissaoGPorKm * kms; //g
    }

    public double calcularEmissaoEfetivaKg(Veiculo veiculo, double kms, int anoReferencia) {
        return calcularEmissaoEfetivaG(veiculo, kms, anoReferencia) / 1000.0; //kg
    }

//    private int extrairAno(Date date) {
//        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        return localDate.getYear();
//    }
}
