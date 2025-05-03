package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.persistance.PersistanceUtilisateur;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersistanceUtilisateurBDD implements PersistanceUtilisateur {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UtilisateurAuthentifie trouverOuCreer(UtilisateurAuthentifie utilisateurAuthentifie) {
        UtilisateurJpa utilisateurJpa = utilisateurRepository.findByMailUtilisateur(
                utilisateurAuthentifie.getEmailUtilisateur()
        );

        if (utilisateurJpa == null) {
            UUID idUnique = UUID.randomUUID();
            utilisateurJpa = UtilisateurJpa
                    .builder()
                    .idGenere(idUnique.toString())
                    .mailUtilisateur(utilisateurAuthentifie.getEmailUtilisateur())
                    .mailVerifie(utilisateurAuthentifie.isEmailVerifie())
                    .statutMembre(utilisateurAuthentifie.getStatutMembre().toString())
                    .nomFamille(utilisateurAuthentifie.getNomFamille())
                    .prenom(utilisateurAuthentifie.getPrenom())
                    .build();

            utilisateurRepository.save(utilisateurJpa);
        }

        return convertirJpaVersApplication(utilisateurJpa);
    }

    private UtilisateurAuthentifie convertirJpaVersApplication(UtilisateurJpa utilisateurJpa) {
        return new UtilisateurAuthentifie(
                utilisateurJpa.getIdGenere(),
                utilisateurJpa.getMailUtilisateur(),
                utilisateurJpa.isMailVerifie(),
                UtilisateurAuthentifie.StatutMembre.valueOf(utilisateurJpa.getStatutMembre()),
                utilisateurJpa.getNomFamille(),
                utilisateurJpa.getPrenom()
        );
    }
}
