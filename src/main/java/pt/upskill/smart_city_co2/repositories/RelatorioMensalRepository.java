package pt.upskill.smart_city_co2.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.upskill.smart_city_co2.entities.RelatorioMensal;

import java.util.List;

public interface RelatorioMensalRepository extends JpaRepository<RelatorioMensal, Long> {

    @Query(value = "SELECT * FROM RelatorioMensal", nativeQuery = true)
    List<RelatorioMensal> customQuery(@Param("name") String name);

}
