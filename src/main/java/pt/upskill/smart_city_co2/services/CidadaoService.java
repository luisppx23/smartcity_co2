package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.RegistoKms;
import pt.upskill.smart_city_co2.repositories.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CidadaoService {
    @Autowired
    CidadaoRepository cidadaoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private OwnershipRepository ownershipRepository;

    @Autowired
    private RegistoKmsRepository registoKmsRepository;

    @Autowired
    private TaxaRepository taxaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    //Popular a base de dados com contas de Cidadão
    @PostConstruct
    public void init() {
        if (cidadaoRepository.count() > 0) return;
        {
            cidadaoRepository.save(new Cidadao("João", "Castor", "joaocastor", LocalDateTime.now(), "joao@gmail.com", passwordEncoder.encode("joao123"), 123456789, "cidadao", true, "911888333","Rua Um"));
            cidadaoRepository.save(new Cidadao("Maria", "Silva", "mariasilva", LocalDateTime.now(), "maria@gmail.com", passwordEncoder.encode("maria123"), 987654321, "cidadao", true, "911888332","Rua Dois"));
            cidadaoRepository.save(new Cidadao("Carlos", "Ferreira", "carlosferreira", LocalDateTime.now(), "carlos@gmail.com", passwordEncoder.encode("carlos123"), 456789123, "cidadao", true, "911888331","Rua Três"));
            cidadaoRepository.save(new Cidadao("Ana", "Costa", "anacosta", LocalDateTime.now(), "ana@gmail.com", passwordEncoder.encode("ana123"), 789123456, "cidadao", true, "911888330","Rua Quatro"));
            cidadaoRepository.save(new Cidadao("Pedro", "Oliveira", "pedrooliveira", LocalDateTime.now(), "pedro@gmail.com", passwordEncoder.encode("pedro123"), 934567890, "cidadao", true, "933444555","Rua Cinco"));
            cidadaoRepository.save(new Cidadao("Carla", "Santos", "carlasantos", LocalDateTime.now(), "carla@gmail.com", passwordEncoder.encode("carla123"), 945678901, "cidadao", true, "944555666","Rua Seis"));
            cidadaoRepository.save(new Cidadao("Rui", "Mendes", "ruimendes", LocalDateTime.now(), "rui@gmail.com", passwordEncoder.encode("rui123"), 956789012, "cidadao", true, "955666777","Rua Sete"));
            cidadaoRepository.save(new Cidadao("Sofia", "Rocha", "sofiarocha", LocalDateTime.now(), "sofia@gmail.com", passwordEncoder.encode("sofia123"), 967890123, "cidadao", true, "966777888","Rua Oito"));
            cidadaoRepository.save(new Cidadao("Tiago", "Nunes", "tiagonunes", LocalDateTime.now(), "tiago@gmail.com", passwordEncoder.encode("tiago123"), 978901234, "cidadao", true, "977888999","Rua Nove"));
            cidadaoRepository.save(new Cidadao("Ines", "Martins", "inesmartins", LocalDateTime.now(), "ines@gmail.com", passwordEncoder.encode("ines123"), 989012345, "cidadao", true, "988999000","Rua Dez"));
            cidadaoRepository.save(new Cidadao("Miguel", "Sousa", "miguelsousa", LocalDateTime.now(), "miguel@gmail.com", passwordEncoder.encode("miguel123"), 901123456, "cidadao", true, "911000111","Rua Onze"));
            cidadaoRepository.save(new Cidadao("Beatriz", "Lima", "beatrizlima", LocalDateTime.now(), "beatriz@gmail.com", passwordEncoder.encode("beatriz123"), 912234567, "cidadao", true, "922111222","Rua Doze"));
            cidadaoRepository.save(new Cidadao("Andre", "Pinto", "andrepinto", LocalDateTime.now(), "andre@gmail.com", passwordEncoder.encode("andre123"), 923345678, "cidadao", true, "933222333","Rua Treze"));
            cidadaoRepository.save(new Cidadao("Mariana", "Teixeira", "marianateixeira", LocalDateTime.now(), "mariana@gmail.com", passwordEncoder.encode("mariana123"), 934456789, "cidadao", true, "944333444","Rua Catorze"));
            cidadaoRepository.save(new Cidadao("Bruno", "Alves", "brunoalves", LocalDateTime.now(), "bruno@gmail.com", passwordEncoder.encode("bruno123"), 945567890, "cidadao", true, "955444555","Rua Quinze"));
            cidadaoRepository.save(new Cidadao("Diana", "Correia", "dianacorreia", LocalDateTime.now(), "diana@gmail.com", passwordEncoder.encode("diana123"), 956678901, "cidadao", true, "966555666","Rua Dezasseis"));
            cidadaoRepository.save(new Cidadao("Filipe", "Gomes", "filipegomes", LocalDateTime.now(), "filipe@gmail.com", passwordEncoder.encode("filipe123"), 967789012, "cidadao", true, "977666777","Rua Dezassete"));
            cidadaoRepository.save(new Cidadao("Patricia", "Ribeiro", "patriciaribeiro", LocalDateTime.now(), "patricia@gmail.com", passwordEncoder.encode("patricia123"), 978890123, "cidadao", true, "988777888","Rua Dezoito"));
            cidadaoRepository.save(new Cidadao("Nelson", "Carvalho", "nelsoncarvalho", LocalDateTime.now(), "nelson@gmail.com", passwordEncoder.encode("nelson123"), 989901234, "cidadao", true, "999888777","Rua Dezanove"));
            cidadaoRepository.save(new Cidadao("Helena", "Araujo", "helenaaraujo", LocalDateTime.now(), "helena@gmail.com", passwordEncoder.encode("helena123"), 990012345, "cidadao", true, "900999888","Rua Vinte"));
        }
    }

    public List<Cidadao> getNome() {
        return cidadaoRepository.findAll();
    }
    public Cidadao getUserC(Long id) {
        return cidadaoRepository.findById(id).orElse(null);
    }

    //Funcionalidade de editar Perfil de Cidadão
    public void guardarAlteracoes(Cidadao cidadao) {
        try {
            cidadaoRepository.save(cidadao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar os dados do cidadão: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteCidadao(Long id) {
        Cidadao cidadao = cidadaoRepository.findById(id).orElse(null);
        if (cidadao == null) return;

        for (Ownership ownership : cidadao.getListaDeVeiculos()) {
            if (ownership.getRegistosKms() != null) {
                for (RegistoKms registo : ownership.getRegistosKms()) {
                    if (registo.getTaxa() != null) {
                        taxaRepository.delete(registo.getTaxa());
                    }
                }
                registoKmsRepository.deleteAll(ownership.getRegistosKms());
            }
            if (ownership.getVeiculo() != null) {
                veiculoRepository.delete(ownership.getVeiculo());
            }
        }

        if (cidadao.getListaDeVeiculos() != null) {
            ownershipRepository.deleteAll(cidadao.getListaDeVeiculos());
        }

        cidadaoRepository.delete(cidadao);
    }
}