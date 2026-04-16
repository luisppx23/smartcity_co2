package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import pt.upskill.smart_city_co2.TipoDeCombustivel;

@Entity
public class Veiculo {

    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private double consumo;

    @OneToOne(mappedBy = "veiculo")
    private Ownership ownership;

    @Enumerated(EnumType.STRING)
    private TipoDeCombustivel tipoDeCombustivel;

    //CONSTRUTOR VAZIO
    public Veiculo() {}

    public Veiculo(String marca, String modelo, TipoDeCombustivel tipoDeCombustivel, double consumo) {
        this.marca = marca;
        this.modelo = modelo;
        this.tipoDeCombustivel = tipoDeCombustivel;
        this.consumo = consumo;
    }

    //GETTERS E SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getConsumo() { return consumo; }
    public void setConsumo(double consumo) { this.consumo = consumo; }

    public Ownership getOwnership() { return ownership; }
    public void setOwnership(Ownership ownership) { this.ownership = ownership; }

    public TipoDeCombustivel getTipoDeCombustivel() { return tipoDeCombustivel; }
    public void setTipoDeCombustivel(TipoDeCombustivel tipoDeCombustivel) { this.tipoDeCombustivel = tipoDeCombustivel; }
}