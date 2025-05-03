package fr.eurekapoker.parties.application.persistance.dto;

import java.util.Objects;

public class JoueurPersistenceDto {
    private final String idUnique;
    private final String nomJoueur;
    private final String notesJoueur;
    public JoueurPersistenceDto(String idUnique, String nomJoueur, String notesJoueur) {
        this.idUnique = idUnique;
        this.nomJoueur = nomJoueur;
        this.notesJoueur = notesJoueur;
    }

    public String obtNomJoueur() {
        return nomJoueur;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomJoueur);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof JoueurPersistenceDto)) return false;
        return Objects.equals(((JoueurPersistenceDto) other).nomJoueur, this.nomJoueur);
    }

    public String obtNotesJoueur() {
        return notesJoueur;
    }

    public String obtIdUnique() {
        return idUnique;
    }
}
