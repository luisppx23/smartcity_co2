package pt.upskill.smart_city_co2.models;


public class AdicionarOwnershipModel {
    private String matricula;
    private Integer anoRegisto;
    private String modeloReferencia;  // formato "Marca:Modelo"

    // Getters e Setters
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
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