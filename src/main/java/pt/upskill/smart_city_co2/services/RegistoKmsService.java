package pt.upskill.smart_city_co2.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.OwnershipRepository;
import pt.upskill.smart_city_co2.repositories.RegistoKmsRepository;

import java.time.LocalDate;
import java.util.Date;

@Service
public class RegistoKmsService {

    @Autowired
    private RegistoKmsRepository registoKmsRepository;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private OwnershipRepository ownershipRepository;  // ADICIONADO

    @Autowired
    private EmissaoCO2Service emissaoCO2Service;

    @Transactional
    public RegistoKms salvarRegisto(Cidadao cidadao, Ownership ownership, double kms) {
        // Criar o registo
        RegistoKms novoRegisto = new RegistoKms();
        novoRegisto.setKms_mes(kms);
        novoRegisto.setMes_ano(new Date());
        novoRegisto.setOwnership(ownership);  // ADICIONADO - seta o ownership

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