package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RegistoKms {

    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Importante para H2/MySQL
    private Long id;

    private Date mes_ano;
    private double kms_mes;

    @OneToOne
    private Taxa taxa;

    //CONSTRUTOR VAZIO
    public RegistoKms(){}

    //GETTERS e SETTERS
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Date getMes_ano() {return mes_ano;}
    public void setMes_ano(Date mes_ano) {this.mes_ano = mes_ano;}

    public double getKms_mes() {return kms_mes;}
    public void setKms_mes(double kms_mes) {this.kms_mes = kms_mes;}

    public Taxa getTaxa() {return taxa;}
    public void setTaxa(Taxa taxa) {this.taxa = taxa;}
}
