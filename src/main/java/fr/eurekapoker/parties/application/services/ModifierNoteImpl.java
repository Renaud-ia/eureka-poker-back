package fr.eurekapoker.parties.application.services;

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
    public void changerNotesEnregistrees(UtilisateurIdentifie utilisateurIdentifie, String idJoueur, NotesJoueur notesJoueur)
            throws ErreurModificationPartie {
        UtilisateurIdentifie proprietairePartie = this.persistanceNotesJoueur.getProprietaireNotes(idJoueur);

        if (!utilisateurIdentifie.equals(proprietairePartie)) {
            throw new ModificationNonAutorisee("La partie n'appartient pas à cet utilisateur");
        }

        this.persistanceNotesJoueur.modifierNotesJoueur(utilisateurIdentifie, idJoueur, notesJoueur);
    }
}
