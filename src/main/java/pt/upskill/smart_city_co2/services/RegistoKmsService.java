package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.OwnershipRepository;
import pt.upskill.smart_city_co2.repositories.RegistoKmsRepository;

import java.time.LocalDate;
import java.util.Date;

@Service
@DependsOn({"ownershipService"})
public class RegistoKmsService {

    @Autowired
    private RegistoKmsRepository registoKmsRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private EmissaoCO2Service emissaoCO2Service;

    @PostConstruct
    @Transactional
    public void init() {
        popularRegistos();
    }

    private void popularRegistos() {
        // Evitar duplicados se a base de dados já tiver dados
        if (registoKmsRepository.count() > 0) {
            return;
        }

        // Definir os meses (Janeiro, Fevereiro, Março de 2024, por exemplo)
        int ano = 2024;
        int[] meses = {0, 1, 2}; // Calendar.JANUARY é 0
        double kmsPadrao = 500.0; // Valor base para kms

        // Iterar pelos IDs de Ownership de 1 a 8
        for (long i = 1; i <= 8; i++) {
            final long currentId = i;
            Ownership ownership = ownershipRepository.findById(currentId).orElse(null);

            if (ownership != null) {
                for (int mes : meses) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(ano, mes, 1, 0, 0);
                    Date dataRegisto = cal.getTime();

                    // Criar o registo
                    RegistoKms novoRegisto = new RegistoKms();
                    // Pequena variação de kms para não ser tudo igual
                    double kmsGerados = kmsPadrao + (currentId * 10) + (mes * 5);

                    novoRegisto.setKms_mes(kmsGerados);
                    novoRegisto.setMes_ano(dataRegisto);
                    novoRegisto.setOwnership(ownership);

                    // Cálculo de emissões usando o seu serviço existente
                    double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, ano);
                    double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(ownership, kmsGerados, ano);

                    novoRegisto.setEmissaoGPorKm(emissaoGPorKm);
                    novoRegisto.setEmissaoEfetivaKg(emissaoEfetivaKg);

                    registoKmsRepository.save(novoRegisto);
                }
            }
        }
    }

    @Transactional
    public RegistoKms salvarRegisto(Cidadao cidadao, Ownership ownership, double kms) {
        // Criar o registo
        RegistoKms novoRegisto = new RegistoKms();
        novoRegisto.setKms_mes(kms);
        novoRegisto.setMes_ano(new Date());
        novoRegisto.setOwnership(ownership);

        int anoReferencia = LocalDate.now().getYear();

        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);
        double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(ownership, kms, anoReferencia);

        novoRegisto.setEmissaoGPorKm(emissaoGPorKm);
        novoRegisto.setEmissaoEfetivaKg(emissaoEfetivaKg);

        // Salvar o registo
        RegistoKms registoSalvo = registoKmsRepository.save(novoRegisto);

        // Adicionar à lista do ownership (se necessário)
        if (ownership.getRegistos() == null) {
            ownership.setRegistos(new java.util.ArrayList<>());
        }
        ownership.getRegistos().add(registoSalvo);

        // Salvar o ownership para persistir a lista atualizada
        ownershipRepository.save(ownership);

        // Nota: Não precisa salvar o cidadão, pois não há alteração direta nele
        // cidadaoRepository.saveAndFlush(cidadao); // REMOVA ESTA LINHA

        return registoSalvo;
    }
}