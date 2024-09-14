package fr.eurekapoker.parties.domaine.poker;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BlindeOuAnte {
    private final String nomJoueur;
    private final TypeTaxe typeTaxe;
    private final BigDecimal montant;
    public BlindeOuAnte(String nomJoueur, TypeTaxe typeTaxe, float montant) {
        this.nomJoueur = nomJoueur;
        this.typeTaxe = typeTaxe;
        this.montant = new BigDecimal(montant).setScale(2, RoundingMode.HALF_UP);
    }
    public String getNomJoueur() {
        return nomJoueur;
    }

    public boolean isBlinde() {
        return typeTaxe == TypeTaxe.BLINDE;
    }
    public boolean isAnte() {
        return typeTaxe == TypeTaxe.ANTE;
    }

    public BigDecimal obtMontant() {
        return montant;
    }

    public enum TypeTaxe {
        BLINDE,
        ANTE
    }
}
