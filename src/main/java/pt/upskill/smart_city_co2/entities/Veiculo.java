package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Veiculo {

    //ATRIBUTOS
    @Id
    private String matricula;

    private String marca;
    private String modelo;
    private String combustivel;
    private Double CO2_kg_km;

    //Relação 1para1 Veiculo-Proprietario
    @OneToOne(mappedBy = "veiculoP")
    private Proprietario propriedade;

    //Relação 1paraMUITOS Veiculo-RegistoKms
    @OneToMany
    private List<RegistoKms> registos;

    //CONSTRUTOR VAZIO
    public Veiculo(){}

    //GETTERS e SETTERS
    public Proprietario getPropriedade() {return propriedade;}
    public void setPropriedade(Proprietario propriedade) {this.propriedade = propriedade;}

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

    public String getCombustivel() {
        return combustivel;
    }
    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public Double getCO2_kg_km() {
        return CO2_kg_km;
    }
    public void setCO2_kg_km(Double CO2_kg_km) {
        this.CO2_kg_km = CO2_kg_km;
    }

    public List<RegistoKms> getRegistos() {return registos;}
    public void setRegistos(List<RegistoKms> registos) {this.registos = registos;}
}
