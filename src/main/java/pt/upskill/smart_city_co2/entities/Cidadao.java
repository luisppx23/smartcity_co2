package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Cidadao extends User {

    private String firstName;
    private String lastName;
    private String contacto;
    private String morada;

    //Relação 1paraMUITOS Cidadão-Proprietario
    @OneToMany
    private List<Veiculo> listaDeVeiculos;

    //CONSTRUTOR VAZIO
    public Cidadao() {}

    // CONSTRUTOR COM PARÂMETROS - ATUALIZADO
    public Cidadao(String firstName, String lastName, String username,
                   LocalDateTime data_registo, String email, String password,
                   int nif, String tipo, boolean ativo, String contacto, String morada) {
        super(username, data_registo, email, password, nif, tipo, ativo);
        this.firstName = firstName;
        this.lastName = lastName;
        this.contacto = contacto;
        this.morada = morada;
    }

    //GETTERS e SETTERS
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public List<Veiculo> getListaDeVeiculos() {return listaDeVeiculos;}
    public void setListaDeVeiculos(List<Veiculo> listaDeVeiculos) {this.listaDeVeiculos = listaDeVeiculos;}

    public String getContacto() {return contacto;}
    public void setContacto(String contacto) {this.contacto = contacto;}

    public String getMorada() {return morada;}
    public void setMorada(String morada) {this.morada = morada;}
}