package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.util.Date;

@Entity
public class Taxa {

    //ATRIBUTOS
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate mes_ano;
    private double valor;

    public Taxa(LocalDate mes_ano, double valor){
        this.mes_ano=mes_ano;
        this.valor=valor;
    }

    //CONSTRUTOR VAZIO
    public Taxa(){}

    //GETTERS e SETTERS
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public LocalDate getMes_ano() {return mes_ano;}
    public void setMes_ano(LocalDate mes_ano) {this.mes_ano = mes_ano;}


    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
