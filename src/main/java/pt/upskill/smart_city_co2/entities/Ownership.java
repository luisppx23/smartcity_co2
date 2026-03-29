package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Ownership {

    @Id
    @GeneratedValue
    private Long id;

    private String matricula;
    private Integer anoRegisto;

    //Relação 1paraMUITOS Ownership-RegistoKms
    @OneToMany(mappedBy = "ownership", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistoKms> registos;

    //Relação 1para1 Ownership-Veiculo
    @OneToOne
    private Veiculo veiculo;


    //CONSTRUTOR VAZIO
    public Ownership() {}

    // CONSTRUTOR COM PARÂMETRO - ATUALIZADO
    public Ownership(String matricula) {
        this.matricula = matricula;
    }

    //GETTERS e SETTERS
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getMatricula() {return matricula;}
    public void setMatricula(String matricula) {this.matricula = matricula;}

    public Integer getAnoRegisto() {return anoRegisto;}
    public void setAnoRegisto(Integer anoRegisto) {this.anoRegisto = anoRegisto;}

    public List<RegistoKms> getRegistos() {return registos;}
    public void setRegistos(List<RegistoKms> registos) {this.registos = registos;}

    public Veiculo getVeiculo() {return veiculo;}
    public void setVeiculo(Veiculo veiculo) {this.veiculo = veiculo;}
}
