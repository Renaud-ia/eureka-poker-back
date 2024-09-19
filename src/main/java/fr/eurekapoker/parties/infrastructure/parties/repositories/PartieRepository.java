package fr.eurekapoker.parties.infrastructure.parties.repositories;

import fr.eurekapoker.parties.infrastructure.parties.entites.PartieJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartieRepository extends JpaRepository<PartieJpa, Long> {

}