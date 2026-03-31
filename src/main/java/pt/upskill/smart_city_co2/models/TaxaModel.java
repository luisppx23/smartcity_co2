package pt.upskill.smart_city_co2.models;

import java.time.LocalDate;

public class TaxaModel {

    private Long id;
    private LocalDate mesAno;
    private double valor;

    // Construtor vazio
    public TaxaModel() {}

    // Construtor com parâmetros
    public TaxaModel(LocalDate mesAno, double valor) {
        this.mesAno = mesAno;
        this.valor = valor;
    }

    // Getters e Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public LocalDate getMesAno() {return mesAno;}
    public void setMesAno(LocalDate mesAno) {this.mesAno = mesAno;}

    public double getValor() {return valor;}
    public void setValor(double valor) {this.valor = valor;}
}