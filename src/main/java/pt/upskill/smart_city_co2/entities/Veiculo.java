package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import pt.upskill.smart_city_co2.TipoDeCombustivel;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Veiculo {

    //ATRIBUTOS
    @Id
    @GeneratedValue
    private Long id;

    private String matricula;

    private String marca;
    private String modelo;
    private TipoDeCombustivel tipoDeCombustivel;
    private double consumo;
    private LocalDate anoRegisto;

    //Relação 1paraMUITOS Veiculo-RegistoKms
    @OneToMany
    private List<RegistoKms> registos;

    //CONSTRUTOR VAZIO
    public Veiculo(){}

    //CONSTRUTOR PARA SERVICE
    public Veiculo( String marca, String modelo, TipoDeCombustivel tipoDeCombustivel,double consumo ){
        this.marca=marca;
        this.modelo=modelo;
        this.tipoDeCombustivel=tipoDeCombustivel;
        this.consumo=consumo;
    }

    //GETTERS e SETTERS
    public String getMatricula() {return matricula;}
    public void setMatricula(String matricula) {this.matricula = matricula;}

    public String getMarca() {return marca;}
    public void setMarca(String marca) {this.marca = marca;}

    public String getModelo() {return modelo;}
    public void setModelo(String modelo) {this.modelo = modelo;}

    public TipoDeCombustivel getTipoDeCombustivel() {return tipoDeCombustivel;}
    public void setTipoDeCombustivel(TipoDeCombustivel tipoDeCombustivel) {this.tipoDeCombustivel = tipoDeCombustivel;}

    public List<RegistoKms> getRegistos() {return registos;}
    public void setRegistos(List<RegistoKms> registos) {this.registos = registos;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    public LocalDate getAnoRegisto() {
        return anoRegisto;
    }

    public void setAnoRegisto(LocalDate anoRegisto) {
        this.anoRegisto = anoRegisto;
    }
}
