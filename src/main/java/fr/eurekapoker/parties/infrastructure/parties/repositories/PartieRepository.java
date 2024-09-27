package fr.eurekapoker.parties.infrastructure.parties.repositories;

import fr.eurekapoker.parties.infrastructure.parties.entites.PartieJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PartieRepository extends JpaRepository<PartieJpa, Long> {
    @Query("SELECT p FROM PartieJpa p JOIN FETCH p.mainsJpa m WHERE p.identifiantGenere = :id AND m.indexMain BETWEEN :x AND :y")
    PartieJpa trouverParIdGenereAvecIndexMains(@Param("id") String id, @Param("x") int indexMainMinimum, @Param("y") int indexMainMaximum);
}