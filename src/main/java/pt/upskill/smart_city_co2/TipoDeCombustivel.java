package pt.upskill.smart_city_co2;

public enum TipoDeCombustivel {
    ELETRICO(0.0, 0.0),
    HIBRIDO(90.0, 0.010),
    GASOLINA(150.0, 0.015),
    DIESEL(130.0, 0.008),
    GPL(115.0, 0.020);

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
