package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RelatorioMensal {

    //ATRIBUTOS
    @Id
    @GeneratedValue
    private Long id;

    private double ajuste;
    private double co2_total_kg;
    private double kms_total;
    private Date mes_ano;

    //CONSTRUTOR VAZIO
    public RelatorioMensal() {}

    //GETTERS e SETTERS
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public double getAjuste() {
        return ajuste;
    }
    public void setAjuste(double ajuste) {
        this.ajuste = ajuste;
    }

    public double getCo2_total_kg() {
        return co2_total_kg;
    }
    public void setCo2_total_kg(double co2_total_kg) {
        this.co2_total_kg = co2_total_kg;
    }

    public double getKms_total() {
        return kms_total;
    }
    public void setKms_total(double kms_total) {
        this.kms_total = kms_total;
    }

    public Date getMes_ano() {
        return mes_ano;
    }
    public void setMes_ano(Date mes_ano) {
        this.mes_ano = mes_ano;
    }
}
