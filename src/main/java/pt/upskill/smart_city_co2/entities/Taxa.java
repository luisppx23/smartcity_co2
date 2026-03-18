package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Date;

@Entity
public class Taxa {

    //ATRIBUTOS
    @Id
    @GeneratedValue
    private Long id;

    private Date mes_ano;
    private double co2_total;
    private double kms_total;

    //Relação 1para1 Taxa-RegistoKms
    @OneToOne(mappedBy = "taxa")
    private RegistoKms registoKms;

    //CONSTRUTOR VAZIO
    public Taxa(){}

    //GETTERS e SETTERS
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Date getMes_ano() {return mes_ano;}
    public void setMes_ano(Date mes_ano) {this.mes_ano = mes_ano;}

    public double getCo2_total() {return co2_total;}
    public void setCo2_total(double co2_total) {this.co2_total = co2_total;}

    public double getKms_total() {return kms_total;}
    public void setKms_total(double kms_total) {this.kms_total = kms_total;}

    public RegistoKms getRegistoKms() {return registoKms;}
    public void setRegistoKms(RegistoKms registoKms) {this.registoKms = registoKms;}
}
