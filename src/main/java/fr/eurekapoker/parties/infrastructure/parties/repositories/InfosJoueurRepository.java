package fr.eurekapoker.parties.infrastructure.parties.repositories;

import fr.eurekapoker.parties.infrastructure.parties.entites.InfosJoueurJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfosJoueurRepository extends JpaRepository<InfosJoueurJpa, Long> {
}
