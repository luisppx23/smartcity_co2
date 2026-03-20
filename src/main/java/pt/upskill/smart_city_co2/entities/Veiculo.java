package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import pt.upskill.smart_city_co2.TipoDeCombustivel;

import java.util.List;

@Entity
public class Veiculo {

    //ATRIBUTOS
    @Id
    private String matricula;

    private String marca;
    private String modelo;
    private TipoDeCombustivel tipoDeCombustivel;
    private double CO2_kg_km;

    //Relação 1para1 Veiculo-Proprietario
    @OneToOne(mappedBy = "veiculoP")
    private Proprietario propriedade;

    //Relação 1paraMUITOS Veiculo-RegistoKms
    @OneToMany
    private List<RegistoKms> registos;

    //CONSTRUTOR VAZIO
    public Veiculo(){}

    //CONSTRUTOR PARA SERVICE
    public Veiculo(String matricula, String marca, String modelo, TipoDeCombustivel tipoDeCombustivel,double CO2_kg_km ){
        this.matricula=matricula;
        this.marca=marca;
        this.modelo=modelo;
        this.tipoDeCombustivel=tipoDeCombustivel;
        this.CO2_kg_km=CO2_kg_km;
    }

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

    public TipoDeCombustivel getTipoDeCombustivel() {
        return tipoDeCombustivel;
    }
    public void setTipoDeCombustivel(TipoDeCombustivel tipoDeCombustivel) {this.tipoDeCombustivel = tipoDeCombustivel;}

    public double getCO2_kg_km() {
        return CO2_kg_km;
    }
    public void setCO2_kg_km(double CO2_kg_km) {
        this.CO2_kg_km = CO2_kg_km;
    }

    public List<RegistoKms> getRegistos() {return registos;}
    public void setRegistos(List<RegistoKms> registos) {this.registos = registos;}
}
