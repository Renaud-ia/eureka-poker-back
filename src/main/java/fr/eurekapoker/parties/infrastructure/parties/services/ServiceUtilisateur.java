package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurAuthentification;
import fr.eurekapoker.parties.infrastructure.parties.entites.ActionJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.JoueurJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.PartieJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    public void associerUtilisateurSession(UtilisateurIdentifie utilisateurIdentifie) {
        UtilisateurAuthentifie utilisateurAuthentifie = utilisateurIdentifie.getUtilisateurAuthentifie();
        UtilisateurJpa utilisateurJpaAuthentifie = utilisateurRepository.findByMailUtilisateur(utilisateurAuthentifie.getEmailUtilisateur());
        UtilisateurJpa utilisateurJpaSession = utilisateurRepository.findByIdGenere(utilisateurIdentifie.getIdentifiantSession());

        if (utilisateurJpaSession == null) {
            throw new ErreurAuthentification("Aucun utilisateur avec cette session");
        }

        if (!Objects.equals(utilisateurJpaSession.getStatutMembre(), UtilisateurAuthentifie.StatutMembre.TEMPORAIRE.toString())) {
            throw new ErreurAuthentification("Un utilisateur a déjà été associé");
        }

        if (utilisateurJpaAuthentifie != null) {
            this.fusionnerUtilisateursJpa(utilisateurJpaAuthentifie, utilisateurJpaSession);
            return;
        }

        utilisateurJpaSession.setMailUtilisateur(utilisateurAuthentifie.getEmailUtilisateur());
        utilisateurJpaSession.setPrenom(utilisateurAuthentifie.getPrenom());
        utilisateurJpaSession.setNomFamille(utilisateurAuthentifie.getNomFamille());
        utilisateurJpaSession.setMailVerifie(utilisateurAuthentifie.isEmailVerifie());
        utilisateurJpaSession.setStatutMembre(utilisateurAuthentifie.getStatutMembre().toString());
    }

    private void fusionnerUtilisateursJpa(
            UtilisateurJpa utilisateurJpaAuthentifie,
            UtilisateurJpa utilisateurJpaSession) {

        List<ActionJpa> actionJpaList = utilisateurJpaSession.getActions();
        for (ActionJpa actionJpa: actionJpaList) {
            actionJpa.setUtilisateur(utilisateurJpaAuthentifie);
            utilisateurJpaAuthentifie.ajouterAction(actionJpa);
        }

        List<PartieJpa> partieJpaList = utilisateurJpaSession.getParties();
        for (PartieJpa partieJpa: partieJpaList) {
            partieJpa.setUtilisateur(utilisateurJpaAuthentifie);
            utilisateurJpaAuthentifie.ajouterPartie(partieJpa);
        }

        List<JoueurJpa> joueurJpaList = utilisateurJpaSession.getJoueurs();
        for (JoueurJpa joueurJpa: joueurJpaList) {
            joueurJpa.setUtilisateur(utilisateurJpaAuthentifie);
            utilisateurJpaAuthentifie.ajouterJoueur(joueurJpa);
        }

        this.utilisateurRepository.save(utilisateurJpaAuthentifie);
        this.utilisateurRepository.delete(utilisateurJpaSession);
    }
}
