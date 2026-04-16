package pt.upskill.smart_city_co2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.upskill.smart_city_co2.entities.RegistoKms;

public interface RegistoKmsRepository extends JpaRepository<RegistoKms, Long> {
}