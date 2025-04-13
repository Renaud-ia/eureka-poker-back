package fr.eurekapoker.parties.domaine.annotations;

public class NotesJoueur {
    private final String notes;

    public NotesJoueur(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return this.notes;
    }
}
