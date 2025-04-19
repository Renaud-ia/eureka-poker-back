package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurAuthentification;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ServiceUtilisateur {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    UtilisateurJpa trouverOuCreerUtilisateur(UtilisateurIdentifie utilisateurIdentifie) {
        UtilisateurAuthentifie utilisateurAuthentifie = utilisateurIdentifie.getUtilisateurAuthentifie();

        if (utilisateurAuthentifie != null) {
            UtilisateurJpa utilisateurJpa = utilisateurRepository.findByMailUtilisateur(utilisateurAuthentifie.getEmailUtilisateur());

            if (utilisateurJpa != null) return utilisateurJpa;

            utilisateurJpa = UtilisateurJpa.builder()
                    .idGenere(utilisateurAuthentifie.getIdUtilisateur())
                    .mailUtilisateur(utilisateurAuthentifie.getEmailUtilisateur())
                    .dateCreation(LocalDateTime.now())
                    .statutMembre(utilisateurAuthentifie.getStatutMembre().toString())
                    .mailUtilisateur(utilisateurAuthentifie.getEmailUtilisateur())
                    .mailVerifie(utilisateurAuthentifie.isEmailVerifie())
                    .prenom(utilisateurAuthentifie.getPrenom())
                    .nomFamille(utilisateurAuthentifie.getNomFamille())
                    .build();

            utilisateurRepository.save(utilisateurJpa);

            return utilisateurJpa;
        }

        UtilisateurJpa utilisateurJpa = utilisateurRepository.findByIdGenere(utilisateurIdentifie.getIdentifiantSession());

        if (utilisateurJpa != null) return utilisateurJpa;

        utilisateurJpa = UtilisateurJpa.builder()
                .idGenere(utilisateurIdentifie.getIdentifiantSession())
                .dateCreation(LocalDateTime.now())
                .statutMembre(UtilisateurAuthentifie.StatutMembre.TEMPORAIRE.toString())
                .build();

        utilisateurRepository.save(utilisateurJpa);

        return utilisateurJpa;
    }

    @Transactional
    void associerUtilisateurSession(UtilisateurIdentifie utilisateurIdentifie) {
        UtilisateurJpa utilisateurJpa = utilisateurRepository.findByIdGenere(utilisateurIdentifie.getIdentifiantSession());

        if (utilisateurJpa == null) {
            throw new ErreurAuthentification("Aucun utilisateur avec cette session");
        }

        if (!Objects.equals(utilisateurJpa.getStatutMembre(), UtilisateurAuthentifie.StatutMembre.TEMPORAIRE.toString())) {
            throw new ErreurAuthentification("Un utilisateur a déjà été associé");
        }

        UtilisateurAuthentifie utilisateurAuthentifie = utilisateurIdentifie.getUtilisateurAuthentifie();

        utilisateurJpa.setMailUtilisateur(utilisateurAuthentifie.getEmailUtilisateur());
        utilisateurJpa.setPrenom(utilisateurAuthentifie.getPrenom());
        utilisateurJpa.setNomFamille(utilisateurAuthentifie.getNomFamille());
        utilisateurJpa.setMailVerifie(utilisateurAuthentifie.isEmailVerifie());
        utilisateurJpa.setStatutMembre(utilisateurAuthentifie.getStatutMembre().toString());
    }
}
