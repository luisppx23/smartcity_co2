package pt.upskill.smart_city_co2.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.upskill.smart_city_co2.entities.Municipio;

import java.util.List;

public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    // Query nativa para obter todos os municipios
    @Query(value = "SELECT * FROM Municipio", nativeQuery = true)
    List<Municipio> customQuery(@Param("name") String nome);

    List<Municipio> findMunicipioByNome(String nome);
}
