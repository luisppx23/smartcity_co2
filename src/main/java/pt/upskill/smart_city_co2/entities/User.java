package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class User {

    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime data_registo;
    private String email;
    private String password;
    private String tipo;
    private Boolean ativo;


    //Relação 1para1 Utilizador-Cidadão
    @OneToOne(mappedBy = "userC")
    private Cidadao cidadao;

    //Relação 1para1 Utilizador-Município
    @OneToOne(mappedBy = "userM")
    private Municipio municipio;

    //CONSTRUTOR VAZIO
    public User(){}


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

    public LocalDateTime getData_registo() {
        return data_registo;
    }
    public void setData_registo(LocalDateTime data_registo) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
