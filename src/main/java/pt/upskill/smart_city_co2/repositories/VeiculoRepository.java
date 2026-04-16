package pt.upskill.smart_city_co2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.upskill.smart_city_co2.entities.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
}