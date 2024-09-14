package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ResultatJoueur {
    private final String nomJoueur;
    private final BigDecimal gains;
    private final List<CartePoker> cartes;
    public ResultatJoueur(String nomJoueur, float gains, List<CartePoker> cartes) {
        this.nomJoueur = nomJoueur;
        this.gains = new BigDecimal(gains).setScale(2, RoundingMode.HALF_UP);
        this.cartes = cartes;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }
    public List<CartePoker> obtCartesJoueur() {
        return cartes;
    }
    public BigDecimal obtMontantGagne() {
        return gains;
    }
}
