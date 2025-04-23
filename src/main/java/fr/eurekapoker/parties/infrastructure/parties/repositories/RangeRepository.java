package fr.eurekapoker.parties.infrastructure.parties.repositories;


import fr.eurekapoker.parties.infrastructure.parties.entites.PokerRangeJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            @Param("idAction") String idAction,
            @Param("methodeGeneration") ServiceRange.MethodeGeneration methodeGeneration
    );

    @Query("""
        SELECT r FROM PokerRangeJpa r
        JOIN r.actions a
        WHERE a.id = :idAction
        """)
    List<PokerRangeJpa> trouverParIdAction(
            @Param("idAction") String idAction
    );
}
