package fr.eurekapoker.parties.domaine.parsing.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InfosJoueurBetclic extends InfosJoueur {
    private final boolean dealer;
    private final BigDecimal gains;
    public InfosJoueurBetclic(String joueur, double montantStack, int siege, boolean dealer, double gains) {
        super(joueur, montantStack, siege);
        this.dealer = dealer;
        this.gains = new BigDecimal(gains).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean obtDealer() {
        return dealer;
    }

    public BigDecimal obtGains() {
        return gains;
    }
}
