package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import pt.upskill.smart_city_co2.EscalaTaxaEmissao;

import java.time.LocalDate;

@Entity
public class Taxa {

    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double valor;
    private LocalDate mes_ano;

    private double taxaPorKmAplicada;
    private double emissaoGPorKmReferencia;

    @Enumerated(EnumType.STRING)
    private EscalaTaxaEmissao nivelEscala;

    @OneToOne
    @JoinColumn(name = "registo_kms_id")
    private RegistoKms registoKms;

    //CONSTRUTOR VAZIO
    public Taxa() {}

    public Taxa(double valor, LocalDate mes_ano) {
        this.valor = valor;
        this.mes_ano = mes_ano;
    }

    //GETTERS E SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public LocalDate getMes_ano() { return mes_ano; }
    public void setMes_ano(LocalDate mes_ano) { this.mes_ano = mes_ano; }

    public double getTaxaPorKmAplicada() { return taxaPorKmAplicada; }
    public void setTaxaPorKmAplicada(double taxaPorKmAplicada) { this.taxaPorKmAplicada = taxaPorKmAplicada; }

    public double getEmissaoGPorKmReferencia() { return emissaoGPorKmReferencia; }
    public void setEmissaoGPorKmReferencia(double emissaoGPorKmReferencia) { this.emissaoGPorKmReferencia = emissaoGPorKmReferencia; }

    public EscalaTaxaEmissao getNivelEscala() { return nivelEscala; }
    public void setNivelEscala(EscalaTaxaEmissao nivelEscala) { this.nivelEscala = nivelEscala; }

    public RegistoKms getRegistoKms() { return registoKms; }
    public void setRegistoKms(RegistoKms registoKms) { this.registoKms = registoKms; }
}