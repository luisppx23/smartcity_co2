package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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
    private boolean ativo;

    //Construtor para UserService
    public User(String firstName,String lastName, String username, LocalDateTime data_registo, String email, String password,String tipo,boolean ativo){
        this.firstName=firstName;
        this.lastName=lastName;
        this.username=username;
        this.data_registo=data_registo;
        this.email=email;
        this.password=password;
        this.tipo=tipo;
        this.ativo=ativo;
    }

    //CONSTRUTOR VAZIO
    public User(){}

    //GETTERS E SETTERS
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

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

    public boolean isAtivo() {return ativo;}
    public void setAtivo(boolean ativo) {this.ativo = ativo;}
}
