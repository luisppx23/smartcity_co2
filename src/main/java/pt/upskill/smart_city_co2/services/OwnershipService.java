package pt.upskill.smart_city_co2.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import pt.upskill.smart_city_co2.entities.Cidadao;
import pt.upskill.smart_city_co2.entities.Ownership;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.models.AdicionarOwnershipModel;
import pt.upskill.smart_city_co2.repositories.CidadaoRepository;
import pt.upskill.smart_city_co2.repositories.OwnershipRepository;
import pt.upskill.smart_city_co2.repositories.VeiculoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@DependsOn({"veiculoService"})
public class OwnershipService {

    @Autowired
    OwnershipRepository ownershipRepository;

    @Autowired
    CidadaoRepository cidadaoRepository;

    @Autowired
    VeiculoRepository veiculoRepository;

    @PostConstruct
    @Transactional
    public void init() {
        popularOwnerships();
    }

    private void popularOwnerships() {
        if (ownershipRepository.count() > 0) {
            return;
        }

        List<Cidadao> cidadaos = new ArrayList<>();
        for (long i = 3L; i <= 22L; i++) {
            Cidadao cidadao = cidadaoRepository.findById(i).orElse(null);
            if (cidadao != null) {
                cidadaos.add(cidadao);
            }
        }

        if (cidadaos.size() < 20) {
            return;
        }

        List<Veiculo> veiculosBase = veiculoRepository.findAll();

        if (veiculosBase.size() < 10) {
            return;
        }

        Random random = new Random();
        List<Ownership> ownershipsParaSalvar = new ArrayList<>();

        for (int i = 0; i < 40; i++) {
            int indiceAleatorio = random.nextInt(veiculosBase.size());
            Veiculo veiculoBase = veiculosBase.get(indiceAleatorio);

            // Criar novo veículo com os dados do veículo base
            Veiculo novoVeiculo = new Veiculo();
            novoVeiculo.setMarca(veiculoBase.getMarca());
            novoVeiculo.setModelo(veiculoBase.getModelo());
            novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
            novoVeiculo.setConsumo(veiculoBase.getConsumo());

            Veiculo veiculoSalvo = veiculoRepository.save(novoVeiculo);

            Ownership ownership = new Ownership();
            ownership.setMatricula(getMatriculaByIndex(i));
            ownership.setAnoRegisto(getAnoRegistoByIndex(i));
            ownership.setVeiculo(veiculoSalvo);

            int indiceCidadao = i / 2;
            ownership.setCidadao(cidadaos.get(indiceCidadao));

            ownershipsParaSalvar.add(ownership);
        }

        List<Ownership> ownershipsSalvos = ownershipRepository.saveAll(ownershipsParaSalvar);
        atualizarListasCidadaos(cidadaos, ownershipsSalvos);
    }

    private void atualizarListasCidadaos(List<Cidadao> cidadaos, List<Ownership> ownerships) {
        for (int i = 0; i < cidadaos.size(); i++) {
            List<Ownership> listaOwnerships = new ArrayList<>();
            listaOwnerships.add(ownerships.get(i * 2));
            listaOwnerships.add(ownerships.get(i * 2 + 1));

            cidadaos.get(i).setListaDeVeiculos(listaOwnerships);
            cidadaoRepository.save(cidadaos.get(i));
        }
    }

    private String getMatriculaByIndex(int i) {
        String[] matriculas = {
                "AA-10-BC", "AD-11-EF", "AG-12-HI", "AJ-13-KL", "AM-14-NO",
                "AP-15-QR", "AS-16-TU", "AV-17-WX", "AY-18-ZA", "BA-19-CD",
                "BE-20-FG", "BI-21-JK", "BO-22-MN", "BR-23-PQ", "BT-24-RS",
                "BU-25-TV", "BX-26-YZ", "CA-27-DE", "CE-28-GH", "CI-29-JL",
                "CO-30-MP", "CR-31-QT", "CU-32-VX", "DA-33-EG", "DI-34-HJ",
                "DO-35-LM", "DU-36-PR", "EA-37-FH", "EC-38-GL", "ED-39-HN",
                "FA-40-JP", "FE-41-KR", "FI-42-LT", "FO-43-MV", "GA-44-NX",
                "GE-45-PZ", "GI-46-RB", "GO-47-TD", "GU-48-VF", "HA-49-XH"
        };
        return matriculas[i];
    }

    private int getAnoRegistoByIndex(int i) {
        int[] anos = {
                2019, 2020, 2021, 2022, 2023,
                2024, 2018, 2019, 2020, 2021,
                2022, 2023, 2024, 2018, 2019,
                2020, 2021, 2022, 2023, 2024,
                2018, 2019, 2020, 2021, 2022,
                2023, 2024, 2018, 2019, 2020,
                2021, 2022, 2023, 2024, 2018,
                2019, 2020, 2021, 2022, 2023
        };
        return anos[i];
    }

    @Transactional
    public Ownership criarOwnership(AdicionarOwnershipModel model, Cidadao cidadao) {
        String[] partes = model.getModeloReferencia().split(":");
        if (partes.length < 2) {
            throw new IllegalArgumentException("Formato de veículo inválido");
        }

        String marca = partes[0];
        String modelo = partes[1];

        Veiculo veiculoBase = veiculoRepository.findAll().stream()
                .filter(v -> v.getMarca().equals(marca) && v.getModelo().equals(modelo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMarca(veiculoBase.getMarca());
        novoVeiculo.setModelo(veiculoBase.getModelo());
        novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
        novoVeiculo.setConsumo(veiculoBase.getConsumo());

        Veiculo veiculoSalvo = veiculoRepository.save(novoVeiculo);

        Ownership ownership = new Ownership();
        ownership.setMatricula(model.getMatricula());
        ownership.setAnoRegisto(model.getAnoRegisto());
        ownership.setVeiculo(veiculoSalvo);
        ownership.setCidadao(cidadao);

        return ownershipRepository.save(ownership);
    }
}