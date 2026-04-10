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
    private EmissaoCO2Service emissaoCO2Service;

    @Autowired
    private TaxaService taxaService;

    @Autowired
    private TaxaRepository taxaRepository;

   /*Método executado no arranque da aplicação para popular a base de dados
   com registos de exemplo, caso ainda não existam registos.
   Esta estratégia facilita testes, demonstração da aplicação e construção de dashboards.*/
    @PostConstruct
    @Transactional
    public void popularRegistos() {
        if (registoKmsRepository.count() > 0) {
            return;
        }

        Random random = new Random();

        // Percorre os ownerships previamente criados para gerar histórico de registos
        for (long i = 1; i <= 8; i++) {
            Ownership ownership = ownershipRepository.findById(i).orElse(null);

            if (ownership == null) {
                continue;
            }

            // Gera registos mensais entre 2024 e 2026
            for (int ano = 2024; ano <= 2026; ano++) {

                // Em 2026 apenas são gerados registos até abril
                int ultimoMes = (ano == 2026) ? 3 : 11;

                for (int mes = 0; mes <= ultimoMes; mes++) {

                    Calendar cal = Calendar.getInstance();
                    cal.set(ano, mes, 1, 0, 0, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    Date dataRegisto = cal.getTime();

                    // Geração aleatória de quilómetros mensais entre 0 e 1000
                    double kmsGerados = random.nextInt(1001);

                    RegistoKms registo = new RegistoKms();
                    registo.setKms_mes(kmsGerados);
                    registo.setMes_ano(dataRegisto);
                    registo.setOwnership(ownership);

                    // Cálculo das emissões com base no veículo, no ano e nos kms percorridos
                    double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, ano);
                    double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(ownership, kmsGerados, ano);

                    registo.setEmissaoGPorKm(emissaoGPorKm);
                    registo.setEmissaoEfetivaKg(emissaoEfetivaKg);

                    // Guarda o registo e cria a taxa correspondente
                    RegistoKms registoSalvo = registoKmsRepository.save(registo);

                    Taxa taxa = taxaService.criarTaxa(registoSalvo);
                    taxaRepository.save(taxa);
                }
            }
        }
    }

    // Guarda um novo registo mensal de quilómetros para um determinado veículo do cidadão.
    // Para além dos kms, este método calcula automaticamente as emissões e a taxa associada.
    @Transactional
    public RegistoKms salvarRegisto(Cidadao cidadao, Ownership ownership, double kms) {
        RegistoKms registo = new RegistoKms();
        registo.setKms_mes(kms);
        registo.setMes_ano(new Date());
        registo.setOwnership(ownership);

        // O ano atual é usado como referência para aplicar a degradação anual
        // e restantes regras de cálculo de emissões.
        int anoReferencia = LocalDate.now().getYear();

        // Cálculo das emissões reais do registo
        double emissaoGPorKm = emissaoCO2Service.calcularEmissaoGPorKm(ownership, anoReferencia);
        double emissaoEfetivaKg = emissaoCO2Service.calcularEmissaoEfetivaKg(ownership, kms, anoReferencia);

        registo.setEmissaoGPorKm(emissaoGPorKm);
        registo.setEmissaoEfetivaKg(emissaoEfetivaKg);

        // Guarda o registo na base de dados
        RegistoKms registoSalvo = registoKmsRepository.save(registo);

        // Após guardar o registo, é criada a taxa associada ao nível de emissões
        Taxa taxa = taxaService.criarTaxa(registoSalvo);
        taxaRepository.save(taxa);

        // Atualiza a lista de registos do ownership para manter a associação consistente
        if (ownership.getRegistosKms() == null) {
            ownership.setRegistosKms(new ArrayList<>());
        }
        ownership.getRegistosKms().add(registoSalvo);
        ownershipRepository.save(ownership);

        return registoSalvo;
    }
}