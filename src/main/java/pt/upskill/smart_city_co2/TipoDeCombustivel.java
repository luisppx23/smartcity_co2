package pt.upskill.smart_city_co2;

public enum TipoDeCombustivel {
    ELETRICO(0.0, 0.0),
    HIBRIDO(120.0, 0.010),
    GASOLINA(192.0, 0.015),
    DIESEL(171.0, 0.02),
    GPL(145.0, 0.025);

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
