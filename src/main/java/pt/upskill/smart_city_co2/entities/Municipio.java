package pt.upskill.smart_city_co2.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Municipio extends User {

    private String nome;
    @Column(columnDefinition = "LONGTEXT")
    private String fotoUrl;

    // Taxas aplicadas de acordo com o nível de emissões do veículo.
    private double objetivo_co2_mes_hab;
    private double taxaNivel1 = 0.10; // >250 g/km
    private double taxaNivel2 = 0.08; // 200-250
    private double taxaNivel3 = 0.06; // 150-200
    private double taxaNivel4 = 0.04; // 100-150
    private double taxaNivel5 = 0.02; // 50-100
    private double taxaNivel6 = 0.01; // 0-50
    private double taxaNivel7 = 0.00; // 0 g/km

    // Relação com os cidadãos pertencentes a este município
    @OneToMany(mappedBy = "municipio")
    private List<Cidadao> listaDeCidadaos;

    // Relação com os relatórios mensais associados ao município.
    @OneToMany(mappedBy = "municipio")
    private List<RelatorioMensal> relatoriosMensais;

    // Construtor vazio
    public Municipio() {
    }

    // Construtor principal para criar um objeto município com os dados herdados de User e meta CO2
    public Municipio(String nome, double objetivo_co2_mes_hab, String username,
                     LocalDateTime data_registo, String email, String password,
                     int nif, String tipo, boolean ativo) {
        super(username, data_registo, email, password, nif, tipo, ativo);
        this.nome = nome;
        this.objetivo_co2_mes_hab = objetivo_co2_mes_hab;
    }

    //Getters e Setters
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

    public double getTaxaNivel1() {
        return taxaNivel1;
    }

    public void setTaxaNivel1(double taxaNivel1) {
        this.taxaNivel1 = taxaNivel1;
    }

    public double getTaxaNivel2() {
        return taxaNivel2;
    }

    public void setTaxaNivel2(double taxaNivel2) {
        this.taxaNivel2 = taxaNivel2;
    }

    public double getTaxaNivel3() {
        return taxaNivel3;
    }

    public void setTaxaNivel3(double taxaNivel3) {
        this.taxaNivel3 = taxaNivel3;
    }

    public double getTaxaNivel4() {
        return taxaNivel4;
    }

    public void setTaxaNivel4(double taxaNivel4) {
        this.taxaNivel4 = taxaNivel4;
    }

    public double getTaxaNivel5() {
        return taxaNivel5;
    }

    public void setTaxaNivel5(double taxaNivel5) {
        this.taxaNivel5 = taxaNivel5;
    }

    public double getTaxaNivel6() {
        return taxaNivel6;
    }

    public void setTaxaNivel6(double taxaNivel6) {
        this.taxaNivel6 = taxaNivel6;
    }

    public double getTaxaNivel7() {
        return taxaNivel7;
    }

    public void setTaxaNivel7(double taxaNivel7) {
        this.taxaNivel7 = taxaNivel7;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}