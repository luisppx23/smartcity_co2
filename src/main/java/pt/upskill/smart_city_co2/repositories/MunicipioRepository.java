package pt.upskill.smart_city_co2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.upskill.smart_city_co2.entities.Municipio;

import java.util.Optional;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {
    Optional<Municipio> findByUsername(String username);
}
