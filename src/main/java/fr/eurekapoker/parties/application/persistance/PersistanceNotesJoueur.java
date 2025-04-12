package fr.eurekapoker.parties.application.persistance;

import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.domaine.annotations.NotesJoueur;

public interface PersistanceNotesJoueur {
    public UtilisateurIdentifie getProprietaireNotes(String idNote);
    public String modifierNotesJoueur(UtilisateurIdentifie utilisateurIdentifie, String idNote, NotesJoueur notesJoueur);
}
