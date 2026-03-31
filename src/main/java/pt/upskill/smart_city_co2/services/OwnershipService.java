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
        // Verificar se já existem ownerships
        if (ownershipRepository.count() > 0) {
            return;
        }

        Cidadao cidadao3 = cidadaoRepository.findById(3L).orElse(null);
        Cidadao cidadao4 = cidadaoRepository.findById(4L).orElse(null);
        Cidadao cidadao5 = cidadaoRepository.findById(5L).orElse(null);
        Cidadao cidadao6 = cidadaoRepository.findById(6L).orElse(null);

        if (cidadao3 == null || cidadao4 == null || cidadao5 == null || cidadao6 == null) {
            return;
        }

        // Buscar veículos base EXISTENTES (já populados pelo VeiculoService)
        List<Veiculo> veiculosBase = veiculoRepository.findAll();

        if (veiculosBase.size() < 8) {
            return;
        }

        Random random = new Random();
        List<Ownership> ownershipsParaSalvar = new ArrayList<>();

        // Criar ownerships USANDO veículos existentes (NÃO criar novos)
        for (int i = 0; i < 8; i++) {
            int indiceAleatorio = random.nextInt(veiculosBase.size());
            Veiculo veiculoExistente = veiculosBase.get(indiceAleatorio);  // ← USAR VEÍCULO EXISTENTE

            // Criar ownership
            Ownership ownership = new Ownership();
            ownership.setMatricula(getMatriculaByIndex(i));
            ownership.setAnoRegisto(getAnoRegistoByIndex(i));
            ownership.setVeiculo(veiculoExistente);  // ← ASSOCIA VEÍCULO EXISTENTE

            // Associar ao cidadão apropriado
            if (i < 2) {
                ownership.setCidadao(cidadao3);
            } else if (i < 4) {
                ownership.setCidadao(cidadao4);
            } else if (i < 6) {
                ownership.setCidadao(cidadao5);
            } else {
                ownership.setCidadao(cidadao6);
            }

            ownershipsParaSalvar.add(ownership);
        }

        // Salvar todos os ownerships
        List<Ownership> ownershipsSalvos = ownershipRepository.saveAll(ownershipsParaSalvar);

        // Atualizar as listas dos cidadãos
        atualizarListasCidadaos(cidadao3, cidadao4, cidadao5, cidadao6, ownershipsSalvos);
    }

    private void atualizarListasCidadaos(Cidadao c3, Cidadao c4, Cidadao c5, Cidadao c6, List<Ownership> ownerships) {
        List<Ownership> listaC3 = new ArrayList<>();
        List<Ownership> listaC4 = new ArrayList<>();
        List<Ownership> listaC5 = new ArrayList<>();
        List<Ownership> listaC6 = new ArrayList<>();

        for (int i = 0; i < ownerships.size(); i++) {
            if (i < 2) listaC3.add(ownerships.get(i));
            else if (i < 4) listaC4.add(ownerships.get(i));
            else if (i < 6) listaC5.add(ownerships.get(i));
            else listaC6.add(ownerships.get(i));
        }

        c3.setListaDeVeiculos(listaC3);
        c4.setListaDeVeiculos(listaC4);
        c5.setListaDeVeiculos(listaC5);
        c6.setListaDeVeiculos(listaC6);

        cidadaoRepository.save(c3);
        cidadaoRepository.save(c4);
        cidadaoRepository.save(c5);
        cidadaoRepository.save(c6);
    }

    private String getMatriculaByIndex(int i) {
        String[] matriculas = {"AB-12-CD", "EF-34-GH", "IJ-56-KL", "MN-78-OP",
                "QR-90-ST", "UV-12-WX", "YZ-34-AB", "CD-56-EF"};
        return matriculas[i];
    }

    private int getAnoRegistoByIndex(int i) {
        int[] anos = {2022, 2021, 2020, 2023, 2019, 2024, 2022, 2020};
        return anos[i];
    }

    @Transactional
    public Ownership criarOwnership(AdicionarOwnershipModel model, Cidadao cidadao) {
        // Processar modelo de referência
        String[] partes = model.getModeloReferencia().split(":");
        if (partes.length < 2) {
            throw new IllegalArgumentException("Formato de veículo inválido");
        }

        String marca = partes[0];
        String modelo = partes[1];

        // Buscar veículo base
        Veiculo veiculoBase = veiculoRepository.findAll().stream()
                .filter(v -> v.getMarca().equals(marca) && v.getModelo().equals(modelo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado"));

        // Criar o Veiculo (cópia com os dados do veículo base)
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMarca(veiculoBase.getMarca());
        novoVeiculo.setModelo(veiculoBase.getModelo());
        novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
        novoVeiculo.setConsumo(veiculoBase.getConsumo());

        Veiculo veiculoSalvo = veiculoRepository.save(novoVeiculo);

        // Criar a Ownership associada ao veículo
        Ownership ownership = new Ownership();
        ownership.setMatricula(model.getMatricula());
        ownership.setAnoRegisto(model.getAnoRegisto());
        ownership.setVeiculo(veiculoSalvo);
        ownership.setCidadao(cidadao);

        // Associar ao cidadão autenticado (será definido pelo controller)
        // O cidadão será definido no controller antes de salvar

        // Salvar a ownership
        return ownershipRepository.save(ownership);
    }
}