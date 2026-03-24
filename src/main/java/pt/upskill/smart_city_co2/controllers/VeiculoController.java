package pt.upskill.smart_city_co2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.upskill.smart_city_co2.entities.Veiculo;
import pt.upskill.smart_city_co2.models.AdicionarVeiculoModel;
import pt.upskill.smart_city_co2.services.VeiculoService;

@Controller
@RequestMapping("/cidadao")
public class VeiculoController {

    @Autowired
    private VeiculoService veiculoService;

    @PostMapping(value = "/registoVeiculo")
    public Veiculo adicionarVeiculo(@RequestBody AdicionarVeiculoModel model) {
        return veiculoService.adicionarVeiculo(model);
    }
}
