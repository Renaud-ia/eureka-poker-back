package fr.eurekapoker.parties.application.persistance.dto;

import java.util.Objects;

public class JoueurPersistenceDto {
    private final String nomJoueur;
    public JoueurPersistenceDto(String nomJoueur) {
        this.nomJoueur = nomJoueur;
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
}
