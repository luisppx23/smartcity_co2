package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Ownership {

    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String matricula;
    private Integer anoRegisto;

    @ManyToOne
    @JoinColumn(name = "cidadao_id")
    private Cidadao cidadao;

    @OneToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @OneToMany(mappedBy = "ownership")
    private List<RegistoKms> registosKms;

    //CONSTRUTOR VAZIO
    public Ownership() {}

    public Ownership(String matricula) {
        this.matricula = matricula;
    }

    //GETTERS E SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Integer getAnoRegisto() { return anoRegisto; }
    public void setAnoRegisto(Integer anoRegisto) { this.anoRegisto = anoRegisto; }

    public Cidadao getCidadao() { return cidadao; }
    public void setCidadao(Cidadao cidadao) { this.cidadao = cidadao; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public List<RegistoKms> getRegistosKms() { return registosKms; }
    public void setRegistosKms(List<RegistoKms> registosKms) { this.registosKms = registosKms; }
}