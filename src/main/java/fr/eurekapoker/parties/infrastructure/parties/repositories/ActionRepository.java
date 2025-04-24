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
    SELECT a2
    FROM ActionJpa a2,
         ActionJpa a1
    WHERE a1.idGenere = :idAction
      AND a2.main = a1.main
      AND a2.infosJoueurJpa = a1.infosJoueurJpa
      AND a2.numeroAction < a1.numeroAction
    ORDER BY a2.numeroAction ASC
    """)
    List<ActionJpa> trouverActionsPrecedentes(@Param("idAction") String idAction);
}
