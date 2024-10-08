package fr.eurekapoker.parties.application.api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ActionDto {
    private final String nomJoueur;
    private final String action;
    private final BigDecimal montant;
    private final int numeroAction;
    public ActionDto(String nomJoueur, String action, BigDecimal montant, int numeroAction) {
        this.nomJoueur = nomJoueur;
        this.action = action;
        this.montant = montant;
        this.numeroAction = numeroAction;
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

    public int getNumeroAction() {
        return numeroAction;
    }
}
