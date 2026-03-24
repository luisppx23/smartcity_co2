package pt.upskill.smart_city_co2.models;

import java.util.Date;

public class SignUpModel {

    private String firstName;
    private String lastName;
    private String email;
    private String tipo;
    private Date data_registo;
    private String username;
    private String password;
    private int nif;

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    public Date getData_registo() {return data_registo;}
    public void setData_registo(Date data_registo) {this.data_registo = data_registo;}

    public int getNif() {return nif;}
    public void setNif(int nif) {this.nif = nif;}
}