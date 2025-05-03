package fr.eurekapoker.parties.infrastructure.parties.repositories;

import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<UtilisateurJpa, Long> {
    UtilisateurJpa findByMailUtilisateur(String email);
    UtilisateurJpa findByIdGenere(String idGenere);
}
