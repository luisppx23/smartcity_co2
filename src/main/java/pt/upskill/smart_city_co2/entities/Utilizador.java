package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Utilizador {

    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private Date data_registo;
    private String email;
    private String password;
    private String tipo;
    private Boolean ativo;


    //Relação 1para1 Utilizador-Cidadão
    @OneToOne(mappedBy = "utilizadorC")
    private Cidadao cidadao;

    //Relação 1para1 Utilizador-Município
    @OneToOne(mappedBy = "utilizadorM")
    private Municipio municipio;

    //CONSTRUTOR VAZIO
    public Utilizador(){}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Cidadao getCidadao() {return cidadao;}
    public void setCidadao(Cidadao cidadao) {this.cidadao = cidadao;}

    public Municipio getMunicipio() {return municipio;}
    public void setMunicipio(Municipio municipio) {this.municipio = municipio;}

    public Date getData_registo() {
        return data_registo;
    }
    public void setData_registo(Date data_registo) {
        this.data_registo = data_registo;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
