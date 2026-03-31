package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.*;

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
    private EmissaoCO2Service emissaoCO2Service;  // ← MANTER ESTE

    @Autowired
    private TaxaService taxaService;

    @Autowired
    private TaxaRepository taxaRepository;

    @PostConstruct
    @Transactional
    public void init() {
        popularRegistos();
    }

    private void popularRegistos() {
        if (registoKmsRepository.count() > 0) {
            return;
        }

        int ano = 2024;
        int[] meses = {0, 1, 2};
        double kmsPadrao = 1000.0;

        for (long i = 1; i <= 8; i++) {
            Ownership ownership = ownershipRepository.findById(i).orElse(null);

            if (ownership != null) {
                for (int mes : meses) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(ano, mes, 1, 0, 0);
                    Date dataRegisto = cal.getTime();

                    RegistoKms registo = new RegistoKms();
                    double kmsGerados = kmsPadrao + (i * 10) + (mes * 5);

                    registo.setKms_mes(kmsGerados);
                    registo.setMes_ano(dataRegisto);
                    registo.setOwnership(ownership);

                    // USAR CÁLCULOS REAIS
                    double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, ano);
                    double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(ownership, kmsGerados, ano);

                    registo.setEmissaoGPorKm(emissaoGPorKm);
                    registo.setEmissaoEfetivaKg(emissaoEfetivaKg);

                    RegistoKms registoSalvo = registoKmsRepository.save(registo);

                    Taxa taxa = taxaService.criarTaxa(registoSalvo);
                    taxaRepository.save(taxa);
                }
            }
        }
    }

    @Transactional
    public RegistoKms salvarRegisto(Cidadao cidadao, Ownership ownership, double kms) {
        RegistoKms registo = new RegistoKms();
        registo.setKms_mes(kms);
        registo.setMes_ano(new Date());
        registo.setOwnership(ownership);

        int anoReferencia = LocalDate.now().getYear();

        // USAR CÁLCULOS REAIS
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);
        double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(ownership, kms, anoReferencia);

        registo.setEmissaoGPorKm(emissaoGPorKm);
        registo.setEmissaoEfetivaKg(emissaoEfetivaKg);

        RegistoKms registoSalvo = registoKmsRepository.save(registo);

        Taxa taxa = taxaService.criarTaxa(registoSalvo);
        taxaRepository.save(taxa);

        if (ownership.getRegistosKms() == null) {
            ownership.setRegistosKms(new java.util.ArrayList<>());
        }
        ownership.getRegistosKms().add(registoSalvo);
        ownershipRepository.save(ownership);

        return registoSalvo;
    }
}