package fr.eurekapoker.parties.domaine.parsing.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InfosJoueur {
    private final String nomJoueur;
    private final BigDecimal montantStack;
    private final BigDecimal bounty;
    private final int siege;
    public InfosJoueur(String joueur, double montantStack, float bounty, int siege) {
        this.nomJoueur = joueur;
        this.montantStack = new BigDecimal(montantStack).setScale(2, RoundingMode.HALF_UP);
        this.bounty = new BigDecimal(bounty).setScale(2, RoundingMode.HALF_UP);
        this.siege = siege;
    }

    public InfosJoueur(String joueur, double montantStack, int siege) {
        this.nomJoueur = joueur;
        this.montantStack = new BigDecimal(montantStack).setScale(2, RoundingMode.HALF_UP);
        this.bounty = null;
        this.siege = siege;
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

    public int obtSiege() {
        return siege;
    }
  }
