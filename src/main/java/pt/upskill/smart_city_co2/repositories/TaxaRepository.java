package pt.upskill.smart_city_co2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.upskill.smart_city_co2.entities.Taxa;

import java.util.List;

public interface TaxaRepository extends JpaRepository<Taxa, Long> {

    @Query(value = "SELECT * FROM Taxa", nativeQuery = true)
    List<Taxa> customQuery(double taxa);

}