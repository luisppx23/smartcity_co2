package pt.upskill.smart_city_co2.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.*;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.RegistoKmsRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.RegistoKmsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@Service
public class RegistoKmsService {

    @Autowired
    private RegistoKmsRepository registoKmsRepository;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private EmissaoCO2Service emissaoCO2Service;

    @Transactional
    public RegistoKms salvarRegisto(Cidadao cidadao, Veiculo veiculo, double kms) {
        RegistoKms novoRegisto = new RegistoKms();
        novoRegisto.setKms_mes(kms);
        novoRegisto.setMes_ano(new Date());
        novoRegisto.setVeiculo(veiculo);

        int anoReferencia = LocalDate.now().getYear();

        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(veiculo,anoReferencia);
        double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(veiculo,kms,anoReferencia);

        novoRegisto.setEmissaoGPorKm(emissaoGPorKm);
        novoRegisto.setEmissaoEfetivaKg(emissaoEfetivaKg);

        if (cidadao.getListaDeRegistosKms() == null) {
            cidadao.setListaDeRegistosKms(new ArrayList<>());
        }

        cidadao.getListaDeRegistosKms().add(novoRegisto);

        registoKmsRepository.save(novoRegisto);
        cidadaoRepository.saveAndFlush(cidadao);

        return novoRegisto;
    }
}
