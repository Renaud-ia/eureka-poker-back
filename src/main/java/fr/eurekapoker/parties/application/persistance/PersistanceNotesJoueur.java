package fr.eurekapoker.parties.application.persistance;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.domaine.annotations.NotesJoueur;

public interface PersistanceNotesJoueur {
    public UtilisateurAuthentifie getProprietaireNotes(String idNote);
    public void modifierNotesJoueur(UtilisateurAuthentifie utilisateurAuthentifie, String idNote, NotesJoueur notesJoueur) throws ModificationNonAutorisee;
}
