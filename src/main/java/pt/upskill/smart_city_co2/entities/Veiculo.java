package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import pt.upskill.smart_city_co2.TipoDeCombustivel;

@Entity
public class Veiculo {

    //ATRIBUTOS
    @Id
    @GeneratedValue
    private Long id;

    private String marca;
    private String modelo;
    private double consumo;

    //Relação 1para1 Veiculo-Ownership
    @OneToOne(cascade = CascadeType.ALL)
    private Ownership ownership;

    @Enumerated
    private TipoDeCombustivel tipoDeCombustivel;

    //CONSTRUTOR VAZIO
    public Veiculo(){}

    //CONSTRUTOR PARA SERVICE
    public Veiculo(String marca, String modelo, TipoDeCombustivel tipoDeCombustivel,double consumo){
        this.marca=marca;
        this.modelo=modelo;
        this.tipoDeCombustivel=tipoDeCombustivel;
        this.consumo=consumo;
    }

    //GETTERS e SETTERS
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public double getConsumo() {
        return consumo;
    }
    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }
}
