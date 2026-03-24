package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Proprietario {

    //ATRIBUTOS
    @Id
    @GeneratedValue
    private Long id;

    private Date data_aquisicao;

    //CONSTRUTOR VAZIO
    public Proprietario(){}

    //GETTERS e SETTERS
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Date getData_aquisicao() {return data_aquisicao;}
    public void setData_aquisicao(Date data_aquisicao) {this.data_aquisicao = data_aquisicao;}
}
