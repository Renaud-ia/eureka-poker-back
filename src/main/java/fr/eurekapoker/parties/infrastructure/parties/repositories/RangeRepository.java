package fr.eurekapoker.parties.infrastructure.parties.repositories;


import fr.eurekapoker.parties.infrastructure.parties.entites.PokerRangeJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RangeRepository extends JpaRepository<PokerRangeJpa, Long> {
    @Query("""
        SELECT r FROM PokerRangeJpa r
        JOIN r.actions a
        WHERE a.id = :idAction
        AND r.methodeGeneration = :methodeGeneration
        """)
    PokerRangeJpa trouverParIdActionEtMethodeGeneration(
            String idAction,
            ServiceRange.MethodeGeneration methodeGeneration
    );

    @Query("""
        SELECT r FROM PokerRangeJpa r
        JOIN r.actions a
        WHERE a.id = :idAction
        """)
    List<PokerRangeJpa> trouverParIdAction(
            String idAction
    );
}
