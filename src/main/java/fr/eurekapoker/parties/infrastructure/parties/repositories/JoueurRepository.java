package fr.eurekapoker.parties.infrastructure.parties.repositories;

import fr.eurekapoker.parties.infrastructure.parties.entites.JoueurJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoueurRepository extends JpaRepository<JoueurJpa, Long> {
    JoueurJpa findByNomJoueurAndNomRoom(String nomJoueur, String nomRoom);
}
