package pt.upskill.smart_city_co2.models;


import pt.upskill.smart_city_co2.TipoDeCombustivel;

public class AdicionarVeiculoModel {

    private String matricula;
    private String marca;
    private String modelo;
    private TipoDeCombustivel tipoDeCombustivel;
    private double CO2_kg_km;

    public AdicionarVeiculoModel() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public TipoDeCombustivel getTipoDeCombustivel() {
        return tipoDeCombustivel;
    }

    public void setTipoDeCombustivel(TipoDeCombustivel tipoDeCombustivel) {
        this.tipoDeCombustivel = tipoDeCombustivel;
    }

    public double getCO2_kg_km() {
        return CO2_kg_km;
    }

    public void setCO2_kg_km(double CO2_kg_km) {
        this.CO2_kg_km = CO2_kg_km;
    }
}
