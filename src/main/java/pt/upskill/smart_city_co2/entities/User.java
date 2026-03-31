package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private LocalDateTime data_registo;
    private String email;
    private String password;
    private int nif;
    private String tipo;
    private boolean ativo;

    public User() {}

    public User(String username, LocalDateTime data_registo, String email, String password, int nif, String tipo, boolean ativo) {
        this.username = username;
        this.data_registo = data_registo;
        this.email = email;
        this.password = password;
        this.nif = nif;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getData_registo() { return data_registo; }
    public void setData_registo(LocalDateTime data_registo) { this.data_registo = data_registo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public int getNif() { return nif; }
    public void setNif(int nif) { this.nif = nif; }
}