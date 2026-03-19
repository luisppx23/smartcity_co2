package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cidadao {

    @Id
    @GeneratedValue
    private Long id;

    private String nome;
    private String contacto;
    private int nif;
    private String morada;
    private long id_municipio;

    //Relação 1paraMUITOS Cidadão-Proprietario
    @OneToMany(mappedBy = "cidadaoP")
    private List<Proprietario> propriedades;

    //Relação 1para1 Utilizador-Cidadão
    @OneToOne
    private User userC;

    //CONSTRUTOR VAZIO
    public Cidadao(){}

    //GETTERS e SETTERS
    public List<Proprietario> getPropriedades() {return propriedades;}
    public void setPropriedades(List<Proprietario> propriedades) {this.propriedades = propriedades;}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public User getUserC() {return userC;}
    public void setUserC(User userC) {this.userC = userC;}

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

    public long getId_municipio() {
        return id_municipio;
    }
    public void setId_municipio(long id_municipio) {
        this.id_municipio = id_municipio;
    }
}
