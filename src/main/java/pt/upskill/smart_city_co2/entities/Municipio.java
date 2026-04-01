package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Municipio extends User {

    private String nome;
    private double objetivo_co2_mes_hab;

    @OneToMany(mappedBy = "municipio")
    private List<Cidadao> listaDeCidadaos;

    @OneToMany(mappedBy = "municipio")
    private List<RelatorioMensal> relatoriosMensais;

    public Municipio() {
    }

    public Municipio(String nome, double objetivo_co2_mes_hab, String username,
                     LocalDateTime data_registo, String email, String password,
                     int nif, String tipo, boolean ativo) {
        super(username, data_registo, email, password, nif, tipo, ativo);
        this.nome = nome;
        this.objetivo_co2_mes_hab = objetivo_co2_mes_hab;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getObjetivo_co2_mes_hab() {
        return objetivo_co2_mes_hab;
    }

    public void setObjetivo_co2_mes_hab(double objetivo_co2_mes_hab) {
        this.objetivo_co2_mes_hab = objetivo_co2_mes_hab;
    }

    public List<Cidadao> getListaDeCidadaos() {
        return listaDeCidadaos;
    }

    public void setListaDeCidadaos(List<Cidadao> listaDeCidadaos) {
        this.listaDeCidadaos = listaDeCidadaos;
    }

    public List<RelatorioMensal> getRelatoriosMensais() {
        return relatoriosMensais;
    }

    public void setRelatoriosMensais(List<RelatorioMensal> relatoriosMensais) {
        this.relatoriosMensais = relatoriosMensais;
    }
}