package pt.upskill.smart_city_co2;

public enum TipoDeCombustivel {
    ELETRICO(0.0, 0.0),
    HIBRIDO(1700.0, 0.01),   // valor aproximado (1,7 kg/L)
    GASOLINA(2310.0, 0.015),
    DIESEL(2680.0, 0.02),
    GPL(1650.0, 0.025);

    private final double fatorEmissaogPorLitro;
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
