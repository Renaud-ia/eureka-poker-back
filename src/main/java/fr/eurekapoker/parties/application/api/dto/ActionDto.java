package fr.eurekapoker.parties.application.api.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ActionDto {
    private final String nomJoueur;
    private final String action;
    private final BigDecimal montant;
    public ActionDto(String nomJoueur, String action, float montant) {
        this.nomJoueur = nomJoueur;
        this.action = action;
        this.montant = new BigDecimal(montant).setScale(2, RoundingMode.HALF_UP);
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public String getAction() {
        return action;
    }

    public BigDecimal getMontant() {
        return montant;
    }
}
