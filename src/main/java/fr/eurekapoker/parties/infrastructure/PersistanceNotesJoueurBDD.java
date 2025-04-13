package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.persistance.PersistanceNotesJoueur;
import fr.eurekapoker.parties.domaine.annotations.NotesJoueur;
import fr.eurekapoker.parties.infrastructure.parties.entites.JoueurJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.JoueurRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistanceNotesJoueurBDD implements PersistanceNotesJoueur {
    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Override
    public UtilisateurIdentifie getProprietaireNotes(String idJoueur) {
        JoueurJpa joueurJpa = joueurRepository.findByIdGenere(idJoueur);
        UtilisateurJpa proprietaire = joueurJpa.getUtilisateur();

        UtilisateurAuthentifie utilisateurAuthentifie = new UtilisateurAuthentifie(
                proprietaire.getIdGenere(),
                proprietaire.getMailUtilisateur(),
                proprietaire.isMailVerifie(),
                UtilisateurAuthentifie.StatutMembre.valueOf(proprietaire.getStatutMembre()),
                proprietaire.getNomFamille(),
                proprietaire.getPrenom()
        );

        return new UtilisateurIdentifie(
                utilisateurAuthentifie,
                joueurJpa.getIdSessionGenere()
        );
    }

    @Transactional
    @Override
    public void modifierNotesJoueur(UtilisateurIdentifie utilisateurIdentifie, String idJoueur, NotesJoueur notesJoueur) {
        JoueurJpa joueurJpa = joueurRepository.findByIdGenere(idJoueur);
        joueurJpa.setNotesJoueur(notesJoueur.getNotes());
        UtilisateurAuthentifie utilisateurAuthentifie = utilisateurIdentifie.getUtilisateurAuthentifie();

        if (utilisateurAuthentifie != null) {
            UtilisateurJpa utilisateurJpa = utilisateurRepository.findByMailUtilisateur(
                    utilisateurAuthentifie.getEmailUtilisateur()
            );
            joueurJpa.setUtilisateur(utilisateurJpa);

            utilisateurJpa.ajouterJoueur(joueurJpa);
        }

    }
}
