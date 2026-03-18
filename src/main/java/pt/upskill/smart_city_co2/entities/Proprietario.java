package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Proprietario {
    @Id
    @GeneratedValue
    private Long id; // Como o Country!

    @ManyToOne
    private Cidadao cidadaoP;

    @OneToOne
    private Veiculo veiculoP;

    private Date data_aquisicao;

    //CONSTRUTOR VAZIO
    public Proprietario(){}

    //GETTERS e SETTERS
    public Cidadao getCidadaoP() {return cidadaoP;}
    public void setCidadaoP(Cidadao cidadaoP) {this.cidadaoP = cidadaoP;}

    public Veiculo getVeiculoP() {return veiculoP;}
    public void setVeiculoP(Veiculo veiculoP) {this.veiculoP = veiculoP;}

    public Date getData_aquisicao() {
        return data_aquisicao;
    }
    public void setData_aquisicao(Date data_aquisicao) {
        this.data_aquisicao = data_aquisicao;
    }
}
