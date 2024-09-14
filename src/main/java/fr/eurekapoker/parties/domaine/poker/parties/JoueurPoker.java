package fr.eurekapoker.parties.domaine.poker.parties;

import java.math.BigDecimal;
import java.util.Objects;

public class JoueurPoker {
    private final String nomJoueur;
    public JoueurPoker(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }
    public String obtNom() {
        return nomJoueur;
    }

    public void ajouterStackDepart(BigDecimal bigDecimal) {
    }

    public void ajouterBounty(BigDecimal bigDecimal) {
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomJoueur);
    }

    @Override
    public boolean equals(Object autre) {
        if (autre == this) return true;
        if (!(autre instanceof JoueurPoker)) return false;
        return Objects.equals(((JoueurPoker) autre).nomJoueur, this.nomJoueur);
    }
}
