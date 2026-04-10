package pt.upskill.smart_city_co2;

/*
 Enumeração que representa os diferentes tipos de combustível suportados pelo sistema.
 Cada tipo de combustível possui:
 - um fator de emissão em gramas por litro, usado no cálculo de emissões de CO2
 - um fator de degradação anual, usado para simular o agravamento do desempenho
 ambiental do veículo ao longo do tempo

 Esta abordagem permite centralizar estas constantes num único ponto,
 tornando os cálculos mais organizados, consistentes e fáceis de manter.
 */
public enum TipoDeCombustivel {
    ELETRICO(0.0, 0.0),
    HIBRIDO(1700.0, 0.01),   // valor aproximado (1,7 kg/L)
    GASOLINA(2310.0, 0.015),
    DIESEL(2680.0, 0.02),
    GPL(1650.0, 0.025);

    // Fator base de emissão do combustível, expresso em gramas por litro
    private final double fatorEmissaogPorLitro;

    // Fator de agravamento anual das emissões do veículo
    private final double fatorDegradacaoAnual;

    TipoDeCombustivel(double fatorEmissaogPorLitro, double fatorDegradacaoAnual) {
        this.fatorEmissaogPorLitro = fatorEmissaogPorLitro;
        this.fatorDegradacaoAnual = fatorDegradacaoAnual;
    }

    public double getFatorEmissaogPorLitro() {
        return fatorEmissaogPorLitro;
    }

    public double getFatorDegradacaoAnual() {
        return fatorDegradacaoAnual;
    }
}
