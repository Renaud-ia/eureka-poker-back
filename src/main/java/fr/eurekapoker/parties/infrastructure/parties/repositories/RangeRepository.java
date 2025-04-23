package fr.eurekapoker.parties.infrastructure.parties.repositories;


import fr.eurekapoker.parties.infrastructure.parties.entites.PokerRangeJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RangeRepository extends JpaRepository<PokerRangeJpa, Long> {
}
