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


        // Buscar cidadãos (IDs 3, 4, 5, 6)
        Cidadao cidadao3 = cidadaoRepository.findById(3L).orElse(null);
        Cidadao cidadao4 = cidadaoRepository.findById(4L).orElse(null);
        Cidadao cidadao5 = cidadaoRepository.findById(5L).orElse(null);
        Cidadao cidadao6 = cidadaoRepository.findById(6L).orElse(null);

        if (cidadao3 == null || cidadao4 == null || cidadao5 == null || cidadao6 == null) {
            return;
        }

        // Buscar veículos base
        List<Veiculo> veiculosBase = veiculoRepository.findAll();

        if (veiculosBase.size() < 8) {
            return;
        }

        // CRIAR VEÍCULOS PRIMEIRO (separadamente)
        List<Veiculo> veiculosCriados = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Veiculo veiculoBase = veiculosBase.get(i);
            Veiculo novoVeiculo = new Veiculo();
            novoVeiculo.setMarca(veiculoBase.getMarca());
            novoVeiculo.setModelo(veiculoBase.getModelo());
            novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
            novoVeiculo.setConsumo(veiculoBase.getConsumo());
            // Salvar cada veículo individualmente
            Veiculo veiculoSalvo = veiculoRepository.save(novoVeiculo);
            veiculosCriados.add(veiculoSalvo);
           }

        // Agora criar os ownerships com os veículos já persistidos
        List<Ownership> ownershipsParaSalvar = new ArrayList<>();

        // Ownership 1 - Cidadão 3
        Ownership own1 = new Ownership();
        own1.setMatricula("AB-12-CD");
        own1.setAnoRegisto(2022);
        own1.setVeiculo(veiculosCriados.get(0));
        ownershipsParaSalvar.add(own1);

        // Ownership 2 - Cidadão 3
        Ownership own2 = new Ownership();
        own2.setMatricula("EF-34-GH");
        own2.setAnoRegisto(2021);
        own2.setVeiculo(veiculosCriados.get(1));
        ownershipsParaSalvar.add(own2);

        // Ownership 3 - Cidadão 4
        Ownership own3 = new Ownership();
        own3.setMatricula("IJ-56-KL");
        own3.setAnoRegisto(2020);
        own3.setVeiculo(veiculosCriados.get(2));
        ownershipsParaSalvar.add(own3);

        // Ownership 4 - Cidadão 4
        Ownership own4 = new Ownership();
        own4.setMatricula("MN-78-OP");
        own4.setAnoRegisto(2023);
        own4.setVeiculo(veiculosCriados.get(3));
        ownershipsParaSalvar.add(own4);

        // Ownership 5 - Cidadão 5
        Ownership own5 = new Ownership();
        own5.setMatricula("QR-90-ST");
        own5.setAnoRegisto(2019);
        own5.setVeiculo(veiculosCriados.get(4));
        ownershipsParaSalvar.add(own5);

        // Ownership 6 - Cidadão 5
        Ownership own6 = new Ownership();
        own6.setMatricula("UV-12-WX");
        own6.setAnoRegisto(2024);
        own6.setVeiculo(veiculosCriados.get(5));
        ownershipsParaSalvar.add(own6);

        // Ownership 7 - Cidadão 6
        Ownership own7 = new Ownership();
        own7.setMatricula("YZ-34-AB");
        own7.setAnoRegisto(2022);
        own7.setVeiculo(veiculosCriados.get(6));
        ownershipsParaSalvar.add(own7);

        // Ownership 8 - Cidadão 6
        Ownership own8 = new Ownership();
        own8.setMatricula("CD-56-EF");
        own8.setAnoRegisto(2020);
        own8.setVeiculo(veiculosCriados.get(7));
        ownershipsParaSalvar.add(own8);

        // Salvar todos os ownerships
        List<Ownership> ownershipsSalvos = ownershipRepository.saveAll(ownershipsParaSalvar);

        // Agora associar os ownerships aos cidadãos
        // Cidadão 3 recebe ownerships 1 e 2
        List<Ownership> listaCidadao3 = new ArrayList<>();
        listaCidadao3.add(ownershipsSalvos.get(0));
        listaCidadao3.add(ownershipsSalvos.get(1));
        cidadao3.setListaDeVeiculos(listaCidadao3);
        cidadaoRepository.save(cidadao3);

        // Cidadão 4 recebe ownerships 3 e 4
        List<Ownership> listaCidadao4 = new ArrayList<>();
        listaCidadao4.add(ownershipsSalvos.get(2));
        listaCidadao4.add(ownershipsSalvos.get(3));
        cidadao4.setListaDeVeiculos(listaCidadao4);
        cidadaoRepository.save(cidadao4);

        // Cidadão 5 recebe ownerships 5 e 6
        List<Ownership> listaCidadao5 = new ArrayList<>();
        listaCidadao5.add(ownershipsSalvos.get(4));
        listaCidadao5.add(ownershipsSalvos.get(5));
        cidadao5.setListaDeVeiculos(listaCidadao5);
        cidadaoRepository.save(cidadao5);

        // Cidadão 6 recebe ownerships 7 e 8
        List<Ownership> listaCidadao6 = new ArrayList<>();
        listaCidadao6.add(ownershipsSalvos.get(6));
        listaCidadao6.add(ownershipsSalvos.get(7));
        cidadao6.setListaDeVeiculos(listaCidadao6);
        cidadaoRepository.save(cidadao6);

    }

    private Veiculo criarCopiaVeiculo(Veiculo veiculoBase) {
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMarca(veiculoBase.getMarca());
        novoVeiculo.setModelo(veiculoBase.getModelo());
        novoVeiculo.setTipoDeCombustivel(veiculoBase.getTipoDeCombustivel());
        novoVeiculo.setConsumo(veiculoBase.getConsumo());
        return veiculoRepository.save(novoVeiculo);
    }

    @Transactional
    public Ownership criarOwnership(AdicionarOwnershipModel model) {
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
        Veiculo novoVeiculo = criarCopiaVeiculo(veiculoBase);

        // Criar a Ownership associada ao veículo
        Ownership ownership = new Ownership();
        ownership.setMatricula(model.getMatricula());
        ownership.setAnoRegisto(model.getAnoRegisto());
        ownership.setVeiculo(novoVeiculo);

        // Salvar a ownership
        return ownershipRepository.save(ownership);
    }
}