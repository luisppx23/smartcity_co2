package pt.upskill.smart_city_co2.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.upskill.smart_city_co2.entities.Cidadao;

import java.util.List;

@Repository
public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {

    // Query nativa para obter todos os cidadãos
    @Query(value = "SELECT * FROM Cidadao", nativeQuery = true)
    List<Cidadao> customQuery(@Param("nome") String nome);

    List<Cidadao>findCidadaoByNome(String nome);
}
