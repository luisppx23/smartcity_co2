package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

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
    public void popularRegistos() {
        if (registoKmsRepository.count() > 0) {
            return;
        }

        Random random = new Random();

        for (long i = 1; i <= 8; i++) {
            Ownership ownership = ownershipRepository.findById(i).orElse(null);

            if (ownership == null) {
                continue;
            }

            for (int ano = 2024; ano <= 2026; ano++) {

                int ultimoMes = (ano == 2026) ? 3 : 11; // 2026 até abril

                for (int mes = 0; mes <= ultimoMes; mes++) {

                    Calendar cal = Calendar.getInstance();
                    cal.set(ano, mes, 1, 0, 0, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    Date dataRegisto = cal.getTime();

                    // Kms aleatórios entre 0 e 1000
                    double kmsGerados = random.nextInt(1001);

                    RegistoKms registo = new RegistoKms();
                    registo.setKms_mes(kmsGerados);
                    registo.setMes_ano(dataRegisto);
                    registo.setOwnership(ownership);

                    double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, ano);
                    double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(ownership, kmsGerados, ano);

                    registo.setEmissaoGPorKm(emissaoGPorKm);
                    registo.setEmissaoEfetivaKg(emissaoEfetivaKg);

                    RegistoKms registoSalvo = registoKmsRepository.save(registo);

                    Cidadao cidadao = ownership.getCidadao();
                    Municipio municipio = (cidadao != null) ? cidadao.getMunicipio() : null;
                    Taxa taxa = taxaService.criarTaxa(registoSalvo, municipio);
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

        Municipio municipio = cidadao.getMunicipio();
        Taxa taxa = taxaService.criarTaxa(registoSalvo, municipio);
        taxaRepository.save(taxa);

        if (ownership.getRegistosKms() == null) {
            ownership.setRegistosKms(new ArrayList<>());
        }
        ownership.getRegistosKms().add(registoSalvo);
        ownershipRepository.save(ownership);

        return registoSalvo;
    }
}