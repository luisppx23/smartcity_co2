package pt.upskill.smart_city_co2.models;


import pt.upskill.smart_city_co2.TipoDeCombustivel;

import java.time.LocalDate;

public class AdicionarVeiculoModel {

    private String matricula;
    private String marca;
    private String modelo;
    private LocalDate anoRegisto;

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

    public LocalDate getAnoRegisto() {
        return anoRegisto;
    }

    public void setAnoRegisto(LocalDate anoRegisto) {
        this.anoRegisto = anoRegisto;
    }
}
