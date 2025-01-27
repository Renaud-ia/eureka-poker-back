package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ResultatJoueur {
    private final BigDecimal gains;
    private final String nomJoueur;

    public ResultatJoueur(String nomJoueur, double gains) {
        this.nomJoueur = nomJoueur;
        this.gains = new BigDecimal(gains).setScale(2, RoundingMode.HALF_UP);
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public BigDecimal obtMontantGagne() {
        return gains;
    }
}
