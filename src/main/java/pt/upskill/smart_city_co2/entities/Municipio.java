package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Municipio {

    //ATRIBUTOS
    @Id
    @GeneratedValue
    private Long id;

    private String nome;
    private double objetivo_co2_mes_hab;
    private String nif;

    //Relação 1para1 Municipio-Utilizador
    @OneToOne
    private User userM;

    //Relação 1para1 Municipio-Utilizador
    @OneToMany(mappedBy = "municipioR")
    private List<RelatorioMensal> relatoriosMensais;

    public Municipio(String nome,double objetivo_co2_mes_hab,String nif){
        this.nome=nome;
        this.objetivo_co2_mes_hab=objetivo_co2_mes_hab;
        this.nif=nif;
    }

    //CONSTRUTOR VAZIO
    public Municipio(){}

    //GETTERS e SETTERS
    public User getUserM() {return userM;}
    public void setUserM(User userM) {this.userM = userM;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getObjetivo_co2_mes_hab() {
        return objetivo_co2_mes_hab;
    }
    public void setObjetivo_co2_mes_hab(double objetivo_co2_mes_hab) {this.objetivo_co2_mes_hab = objetivo_co2_mes_hab;}

    public String getNIF() {
        return nif;
    }
    public void setNIF(String nif) {
        this.nif = nif;
    }
}