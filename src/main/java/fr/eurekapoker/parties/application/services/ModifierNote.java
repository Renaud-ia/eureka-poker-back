package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.domaine.annotations.NotesJoueur;

public interface ModifierNote {
    public void changerNotesEnregistrees(
            AuthService authService,
            String idNote,
            NotesJoueur notesJoueur
            ) throws ErreurModificationPartie;
}
