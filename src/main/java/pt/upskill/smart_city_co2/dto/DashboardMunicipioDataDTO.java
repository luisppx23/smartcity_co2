package pt.upskill.smart_city_co2.dto;

import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Municipio;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.entities.Veiculo;

import java.util.ArrayList;
import java.util.List;

// Alimenta o dashboard de visualização dos gráficos e dados processados do Município
// Armazena APENAS dados brutos - todos os cálculos são feitos pelo Service
public class DashboardMunicipioDataDTO {

    // Dados de entrada (brutos)
    private Municipio municipio;
    private List<Cidadao> cidadaos = new ArrayList<>();
    private List<RegistoKms> registos = new ArrayList<>();
    private List<Veiculo> veiculos = new ArrayList<>();

    // Metadados
    private int numeroHabitantes;
    private int quantidadeVeiculosTotais;

    // Construtores
    public DashboardMunicipioDataDTO() {}

    public DashboardMunicipioDataDTO(Municipio municipio) {
        this.municipio = municipio;
    }

    // Método para DTO vazio (usado quando não há dados)
    public static DashboardMunicipioDataDTO empty(Municipio municipio) {
        DashboardMunicipioDataDTO dto = new DashboardMunicipioDataDTO(municipio);
        dto.setCidadaos(new ArrayList<>());
        dto.setRegistos(new ArrayList<>());
        dto.setVeiculos(new ArrayList<>());
        dto.setNumeroHabitantes(0);
        dto.setQuantidadeVeiculosTotais(0);
        return dto;
    }

    // Getters e Setters (apenas para dados brutos)
    public Municipio getMunicipio() { return municipio; }
    public void setMunicipio(Municipio municipio) { this.municipio = municipio; }

    public List<Cidadao> getCidadaos() { return cidadaos; }
    public void setCidadaos(List<Cidadao> cidadaos) { this.cidadaos = cidadaos; }

    public List<RegistoKms> getRegistos() { return registos; }
    public void setRegistos(List<RegistoKms> registos) { this.registos = registos; }

    public List<Veiculo> getVeiculos() { return veiculos; }
    public void setVeiculos(List<Veiculo> veiculos) { this.veiculos = veiculos; }

    public int getNumeroHabitantes() { return numeroHabitantes; }
    public void setNumeroHabitantes(int numeroHabitantes) { this.numeroHabitantes = numeroHabitantes; }

    public int getQuantidadeVeiculosTotais() { return quantidadeVeiculosTotais; }
    public void setQuantidadeVeiculosTotais(int quantidadeVeiculosTotais) {
        this.quantidadeVeiculosTotais = quantidadeVeiculosTotais;
    }
}