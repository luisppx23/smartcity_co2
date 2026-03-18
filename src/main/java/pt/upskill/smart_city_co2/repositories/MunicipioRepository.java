package pt.upskill.smart_city_co2.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.upskill.smart_city_co2.entities.Municipio;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    @Query(value = "SELECT * FROM Municipio", nativeQuery = true)
    List<Municipio> customQuery(String name);

}
