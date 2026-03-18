package pt.upskill.smart_city_co2.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.upskill.smart_city_co2.entities.RegistoKms;


import java.util.List;

public interface RegistoKmsRepository extends JpaRepository<RegistoKms, Long> {

    @Query(value = "SELECT * FROM RegistoKms", nativeQuery = true)
    List<RegistoKms> customQuery(double kms_mes);

}