package pt.upskill.smart_city_co2.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.upskill.smart_city_co2.entities.Proprietario;

import java.util.List;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {

    // Query nativa para obter todos os proprietários
    @Query(value = "SELECT * FROM Proprietario", nativeQuery = true)
    List<Proprietario> customQuery(@Param("id") Long id);

    List<Proprietario> findProprietarioById(Long id);
}