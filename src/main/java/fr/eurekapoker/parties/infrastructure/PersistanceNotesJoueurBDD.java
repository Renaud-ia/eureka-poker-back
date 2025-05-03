package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
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
    public UtilisateurAuthentifie getProprietaireNotes(String idJoueur) {
        JoueurJpa joueurJpa = joueurRepository.findByIdGenere(idJoueur);
        UtilisateurJpa proprietaire = joueurJpa.getUtilisateur();

        return new UtilisateurAuthentifie(
                proprietaire.getIdGenere(),
                proprietaire.getMailUtilisateur(),
                proprietaire.isMailVerifie(),
                UtilisateurAuthentifie.StatutMembre.valueOf(proprietaire.getStatutMembre()),
                proprietaire.getNomFamille(),
                proprietaire.getPrenom()
        );
    }

    @Transactional
    @Override
    public void modifierNotesJoueur(UtilisateurAuthentifie utilisateurAuthentifie, String idJoueur, NotesJoueur notesJoueur)
            throws ModificationNonAutorisee {
        JoueurJpa joueurJpa = joueurRepository.findByIdGenere(idJoueur);
        joueurJpa.setNotesJoueur(notesJoueur.getNotes());

        if (utilisateurAuthentifie == null) {
            throw new ModificationNonAutorisee("Le joueur doit être authentifié");
        }
        UtilisateurJpa utilisateurJpa = utilisateurRepository.findByMailUtilisateur(
                utilisateurAuthentifie.getEmailUtilisateur()
        );

        if (joueurJpa.getUtilisateur() == null) {
            throw new ModificationNonAutorisee("Aucun joueur associé");
        }
        utilisateurJpa.ajouterJoueur(joueurJpa);
    }
}
