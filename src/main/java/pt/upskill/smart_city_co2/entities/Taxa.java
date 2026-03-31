package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Taxa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double valor;
    private LocalDate mes_ano;

    @OneToOne
    @JoinColumn(name = "registo_kms_id")
    private RegistoKms registoKms;

    public Taxa() {}

    public Taxa(double valor, LocalDate mes_ano) {
        this.valor = valor;
        this.mes_ano = mes_ano;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public LocalDate getMes_ano() { return mes_ano; }
    public void setMes_ano(LocalDate mes_ano) { this.mes_ano = mes_ano; }

    public RegistoKms getRegistoKms() { return registoKms; }
    public void setRegistoKms(RegistoKms registoKms) { this.registoKms = registoKms; }
}