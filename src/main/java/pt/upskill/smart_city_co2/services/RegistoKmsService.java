package pt.upskill.smart_city_co2.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.RegistoKmsRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.RegistoKmsRepository;

import java.util.ArrayList;
import java.util.Date;

@Service
public class RegistoKmsService {

    @Autowired
    private RegistoKmsRepository registoKmsRepository;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Transactional
    public void salvarRegisto(Cidadao cidadao, double kms) {
        RegistoKms novoRegisto = new RegistoKms();
        novoRegisto.setKms_mes(kms);
        novoRegisto.setMes_ano(new Date());

        // Importante: Inicializar a lista se estiver nula
        if (cidadao.getListaDeRegistosKms() == null) {
            cidadao.setListaDeRegistosKms(new ArrayList<>());
        }

        // Adiciona à lista do cidadão
        cidadao.getListaDeRegistosKms().add(novoRegisto);

        // Salva o cidadão e força a escrita imediata
        cidadaoRepository.saveAndFlush(cidadao);
    }
}
