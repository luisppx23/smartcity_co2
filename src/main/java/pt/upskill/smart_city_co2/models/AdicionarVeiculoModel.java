package pt.upskill.smart_city_co2.models;

public class AdicionarVeiculoModel {
    private String matricula;
    private String marca;
    private String modelo;
    private Integer anoRegisto;
    private String modeloReferencia;

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

    public Integer getAnoRegisto() {
        return anoRegisto;
    }

    public void setAnoRegisto(Integer anoRegisto) {
        this.anoRegisto = anoRegisto;
    }

    public String getModeloReferencia() {
        return modeloReferencia;
    }

    public void setModeloReferencia(String modeloReferencia) {
        this.modeloReferencia = modeloReferencia;
    }
}
