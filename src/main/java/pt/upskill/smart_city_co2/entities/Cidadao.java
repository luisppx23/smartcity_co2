package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cidadao extends User {

    private String nome;
    private String contacto;
    private int nif;
    private String morada;

    //Relação 1paraMUITOS Cidadão-Proprietario
    @OneToMany(mappedBy = "cidadaoP")
    private List<Proprietario> propriedades;


    //CONSTRUTOR VAZIO
    public Cidadao(){}

    //GETTERS e SETTERS
    public List<Proprietario> getPropriedades() {return propriedades;}
    public void setPropriedades(List<Proprietario> propriedades) {this.propriedades = propriedades;}


    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContacto() {
        return contacto;
    }
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public int getNIF() {
        return nif;
    }
    public void setNIF(int nif) {
        this.nif = nif;
    }

    public String getMorada() {
        return morada;
    }
    public void setMorada(String morada) {
        this.morada = morada;
    }
}
