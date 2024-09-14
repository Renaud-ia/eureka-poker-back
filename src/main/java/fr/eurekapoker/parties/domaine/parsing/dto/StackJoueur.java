package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StackJoueur {
    private final String nomJoueur;
    private final BigDecimal montantStack;
    private final BigDecimal bounty;
    public StackJoueur(String joueur, double montantStack, float bounty) {
        this.nomJoueur = joueur;
        this.montantStack = new BigDecimal(montantStack).setScale(2, RoundingMode.HALF_UP);
        this.bounty = new BigDecimal(bounty).setScale(2, RoundingMode.HALF_UP);
    }

    public StackJoueur(String joueur, double montantStack) {
        this.nomJoueur = joueur;
        this.montantStack = new BigDecimal(montantStack).setScale(2, RoundingMode.HALF_UP);
        this.bounty = null;
    }

    public String obtJoueur() {
        return nomJoueur;
    }

    public BigDecimal obtStack() {
        return montantStack;
    }

    public boolean aBounty() {
        return bounty != null;
    }

    public BigDecimal obtBounty() {
        return bounty;
    }
  }
