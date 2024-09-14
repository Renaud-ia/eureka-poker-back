package fr.eurekapoker.parties.domaine.poker;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StackJoueur {
    private final JoueurPoker joueurPoker;
    private final BigDecimal montantStack;
    private final BigDecimal bounty;
    public StackJoueur(JoueurPoker joueur, double montantStack, float bounty) {
        this.joueurPoker = joueur;
        this.montantStack = new BigDecimal(montantStack).setScale(2, RoundingMode.HALF_UP);
        this.bounty = new BigDecimal(bounty).setScale(2, RoundingMode.HALF_UP);
    }

    public StackJoueur(JoueurPoker joueur, double montantStack) {
        this.joueurPoker = joueur;
        this.montantStack = new BigDecimal(montantStack).setScale(2, RoundingMode.HALF_UP);
        this.bounty = null;
    }

    public JoueurPoker obtJoueur() {
        return joueurPoker;
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
