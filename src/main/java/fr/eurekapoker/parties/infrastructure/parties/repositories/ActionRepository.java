package fr.eurekapoker.parties.infrastructure.parties.repositories;

import fr.eurekapoker.parties.infrastructure.parties.entites.ActionJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<ActionJpa, Long> {
    ActionJpa findByIdGenere(String idGenere);

    @Query("""
    SELECT a2 FROM ActionJpa a2
    JOIN FETCH a2.ranges
    WHERE a2.main = (
        SELECT a1.main FROM ActionJpa a1 WHERE a1.idGenere = :idAction
    )
    AND a2.infosJoueurJpa = (
        SELECT a1.infosJoueurJpa FROM ActionJpa a1 WHERE a1.idGenere = :idAction
    )
    AND a2.numeroAction < (
        SELECT a1.numeroAction FROM ActionJpa a1 WHERE a1.idGenere = :idAction
    )
    ORDER BY a2.numeroAction ASC
    """)
    List<ActionJpa> trouverActionsPrecedentes(@Param("idAction") String idAction);
}
