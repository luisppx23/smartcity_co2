package pt.upskill.smart_city_co2;

public enum EscalaTaxaEmissao {

    NIVEL_7(0, 0, 0.00),          // 0 g/km
    NIVEL_6(0.0001, 50, 0.01),    // ]0, 50[
    NIVEL_5(50, 100, 0.02),       // [50, 100[
    NIVEL_4(100, 150, 0.04),      // [100, 150[
    NIVEL_3(150, 200, 0.06),      // [150, 200[
    NIVEL_2(200, 250, 0.08),      // [200, 250]
    NIVEL_1(250, Double.MAX_VALUE, 0.10); // > 250

    private final double min;
    private final double max;
    private final double taxaPorKm;

    EscalaTaxaEmissao(double min, double max, double taxaPorKm) {
        this.min = min;
        this.max = max;
        this.taxaPorKm = taxaPorKm;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getTaxaPorKm() {
        return taxaPorKm;
    }

    public static EscalaTaxaEmissao fromEmissao(double emissaoGPorKm) {
        if (emissaoGPorKm == 0) {
            return NIVEL_7;
        } else if (emissaoGPorKm > 250) {
            return NIVEL_1;
        } else if (emissaoGPorKm >= 200) {
            return NIVEL_2;
        } else if (emissaoGPorKm >= 150) {
            return NIVEL_3;
        } else if (emissaoGPorKm >= 100) {
            return NIVEL_4;
        } else if (emissaoGPorKm >= 50) {
            return NIVEL_5;
        } else {
            return NIVEL_6;
        }
    }
}
