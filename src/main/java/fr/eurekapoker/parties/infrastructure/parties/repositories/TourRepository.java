package fr.eurekapoker.parties.infrastructure.parties.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<TourJpa, Long> {
}
