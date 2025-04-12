package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.application.persistance.PersistanceNotesJoueur;
import fr.eurekapoker.parties.domaine.annotations.NotesJoueur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifierNoteImpl implements ModifierNote {
    private final PersistanceNotesJoueur persistanceNotesJoueur;

    @Autowired
    public ModifierNoteImpl(PersistanceNotesJoueur persistanceNotesJoueur) {
        this.persistanceNotesJoueur = persistanceNotesJoueur;
    }

    @Override
    public void changerNotesEnregistrees(AuthService authService, String idNote, NotesJoueur notesJoueur)
            throws ErreurModificationPartie {
        if (!authService.userEstIdentifie()) {
            throw new ModificationNonAutorisee("Seul les utilisateurs identifiés peuvent modifier une partie");
        }

        UtilisateurIdentifie proprietairePartie = this.persistanceNotesJoueur.getProprietaireNotes(idNote);

        if (!authService.estUtilisateur(proprietairePartie)) {
            throw new ModificationNonAutorisee("La partie n'appartient pas à cet utilisateur");
        }

        this.persistanceNotesJoueur.modifierNotesJoueur(authService.getUtilisateur(), idNote, notesJoueur);
    }
}
