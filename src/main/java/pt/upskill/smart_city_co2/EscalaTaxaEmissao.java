package pt.upskill.smart_city_co2;

/*
 Enumeração que define a escala de taxação associada às emissões de CO2 por km.

 Cada nível representa um intervalo de emissões em g/km e a respetiva taxa aplicada por km.
 Esta estrutura permite centralizar a lógica de classificação ambiental dos veículos
 e evita espalhar valores fixos pelo código.

 Exemplo:
 - um veículo com 0 g/km pertence ao nível 7
 - um veículo com mais de 250 g/km pertence ao nível 1
 */
public enum EscalaTaxaEmissao {

    // Veículos sem emissões diretas
    NIVEL_7(0, 0, 0.00),          // 0 g/km

    // Emissões muito reduzidas
    NIVEL_6(0.0001, 50, 0.01),    // ]0, 50[

    // Emissões baixas
    NIVEL_5(50, 100, 0.02),       // [50, 100[

    // Emissões moderadas
    NIVEL_4(100, 150, 0.04),      // [100, 150[

    // Emissões médias-altas
    NIVEL_3(150, 200, 0.06),      // [150, 200[

    // Emissões elevadas
    NIVEL_2(200, 250, 0.08),      // [200, 250]

    // Emissões muito elevadas
    NIVEL_1(250, Double.MAX_VALUE, 0.10); // > 250

    // Limite mínimo do intervalo
    private final double min;

    // Limite máximo do intervalo
    private final double max;

    // Taxa aplicada por km para este nível de emissões
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

    /*
     Determina automaticamente o nível da escala a partir do valor de emissões em g/km.
     Este metodo é importante porque centraliza a regra de classificação ambiental
     e garante consistência sempre que for necessário converter emissões num nível.
     */
    public static EscalaTaxaEmissao fromEmissao(double emissaoGPorKm) {

        // Caso especial: veículos com 0 emissões
        if (emissaoGPorKm == 0) {
            return NIVEL_7;

            // Emissões superiores a 250 g/km
        } else if (emissaoGPorKm > 250) {
            return NIVEL_1;

            // Intervalo [200, 250]
        } else if (emissaoGPorKm >= 200) {
            return NIVEL_2;

            // Intervalo [150, 200[
        } else if (emissaoGPorKm >= 150) {
            return NIVEL_3;

            // Intervalo [100, 150[
        } else if (emissaoGPorKm >= 100) {
            return NIVEL_4;

            // Intervalo [50, 100[
        } else if (emissaoGPorKm >= 50) {
            return NIVEL_5;

            // Intervalo ]0, 50[
        } else {
            return NIVEL_6;
        }
    }
}