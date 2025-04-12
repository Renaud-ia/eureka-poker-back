package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.persistance.PersistanceNotesJoueur;
import fr.eurekapoker.parties.domaine.annotations.NotesJoueur;

public class PersistanceNotesJoueurBDD implements PersistanceNotesJoueur {
    @Override
    public UtilisateurIdentifie getProprietaireNotes(String idNote) {
        return null;
    }

    @Override
    public String modifierNotesJoueur(UtilisateurIdentifie utilisateurIdentifie, String idNote, NotesJoueur notesJoueur) {
        return "";
    }
}
