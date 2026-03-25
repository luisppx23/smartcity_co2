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

    // NOVOS ATRIBUTOS BASEADOS NO EXCEL
    private double consumo; // Consumo em L/100km
    private double fatorBase; // Fator Base (kgCO2/L)
    private double degradacaoAnual; // Degradação anual do consumo (%)
    private Integer anoRegisto; // Ano de registo do veículo

    // Campos calculados
    private Integer numeroAnos; // Número de anos desde o registo
    private double degradacaoTotal; // Degradação total acumulada (%)
    private double consumoAtual; // Consumo atual considerando degradação
    private double co2Calculado; // CO2 Calculado (g/km)

    //Relação 1paraMUITOS Veiculo-RegistoKms
    @OneToMany
    private List<RegistoKms> registos;

    //CONSTRUTOR VAZIO
    public Veiculo(){}

    //CONSTRUTOR PARA SERVICE (ATUALIZADO)
    public Veiculo(String matricula, String marca, String modelo, TipoDeCombustivel tipoDeCombustivel,
                   double CO2_kg_km, double consumo, double fatorBase,
                   double degradacaoAnual, Integer anoRegisto) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipoDeCombustivel = tipoDeCombustivel;
        this.CO2_kg_km = CO2_kg_km;
        this.consumo = consumo;
        this.fatorBase = fatorBase;
        this.degradacaoAnual = degradacaoAnual;
        this.anoRegisto = anoRegisto;
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

    public double getCO2_kg_km() {return CO2_kg_km;}
    public void setCO2_kg_km(double CO2_kg_km) {this.CO2_kg_km = CO2_kg_km;}

    public List<RegistoKms> getRegistos() {return registos;}
    public void setRegistos(List<RegistoKms> registos) {this.registos = registos;}

    public double getConsumo() {return consumo;}
    public void setConsumo(double consumo) {this.consumo = consumo;}

    public double getFatorBase() {return fatorBase;}
    public void setFatorBase(double fatorBase) {this.fatorBase = fatorBase;}

    public double getDegradacaoAnual() {return degradacaoAnual;}
    public void setDegradacaoAnual(double degradacaoAnual) {this.degradacaoAnual = degradacaoAnual;}

    public Integer getAnoRegisto() {return anoRegisto;}
    public void setAnoRegisto(Integer anoRegisto) {this.anoRegisto = anoRegisto;}

    public Integer getNumeroAnos() {return numeroAnos;}
    public void setNumeroAnos(Integer numeroAnos) {this.numeroAnos = numeroAnos;}

    public double getDegradacaoTotal() {return degradacaoTotal;}
    public void setDegradacaoTotal(double degradacaoTotal) {this.degradacaoTotal = degradacaoTotal;}

    public double getConsumoAtual() {return consumoAtual;}
    public void setConsumoAtual(double consumoAtual) {this.consumoAtual = consumoAtual;}

    public double getCo2Calculado() {return co2Calculado;}
    public void setCo2Calculado(double co2Calculado) {this.co2Calculado = co2Calculado;}
}
