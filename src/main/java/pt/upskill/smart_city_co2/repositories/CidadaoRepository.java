package pt.upskill.smart_city_co2.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.upskill.smart_city_co2.entities.Cidadao;

import java.util.List;
import java.util.Optional;

@Repository
public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {

    @Query(value = "SELECT * FROM Cidadao", nativeQuery = true)
    List<Cidadao> customQuery(@Param("email") String email);

    @Query("""
        SELECT DISTINCT c
        FROM Cidadao c
        LEFT JOIN FETCH c.listaDeVeiculos o
        LEFT JOIN FETCH o.veiculo v
        WHERE c.municipio.id = :municipioId
    """)
    List<Cidadao> buscarCidadaosDoMunicipioComVeiculos(@Param("municipioId") Long municipioId);

    //Optional<Cidadao> findByUsername(String username);

    Optional<Cidadao> findByUsername(String username);

    Optional<Cidadao> findByEmail(String email);

}
