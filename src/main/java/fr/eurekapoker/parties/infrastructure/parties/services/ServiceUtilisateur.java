package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurAuthentification;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class ServiceUtilisateur {
    @Autowired
    UtilisateurRepository utilisateurRepository;

    UtilisateurJpa trouverOuCreerUtilisateur(UtilisateurIdentifie utilisateurIdentifie) {
        return null;
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
