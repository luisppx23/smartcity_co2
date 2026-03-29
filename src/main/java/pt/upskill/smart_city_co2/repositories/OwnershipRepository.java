package pt.upskill.smart_city_co2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.upskill.smart_city_co2.entities.Ownership;

import java.util.List;

public interface OwnershipRepository extends JpaRepository<Ownership, Long> {

    @Query(value = "SELECT * FROM Ownership", nativeQuery = true)
    List<Ownership> customQuery(@Param("id") Long id);

    Ownership findByMatricula(String matricula);

}
