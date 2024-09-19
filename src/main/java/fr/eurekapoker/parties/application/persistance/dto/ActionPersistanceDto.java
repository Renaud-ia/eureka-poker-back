package fr.eurekapoker.parties.application.persistance.dto;

import java.math.BigDecimal;

public class ActionPersistanceDto {
    private final String nomJoueur;
    private final String nomAction;
    private final long identifiantSituation;
    private final BigDecimal montantAction;

    public ActionPersistanceDto(String nomJoueur, String nomAction, long identifiantSituation, BigDecimal montantAction) {
        this.nomJoueur = nomJoueur;
        this.nomAction = nomAction;
        this.identifiantSituation = identifiantSituation;
        this.montantAction = montantAction;
    }

    public String obtNomJoueur() {
        return nomJoueur;
    }

    public String obtNomAction() {
        return nomAction;
    }

    public BigDecimal obtMontant() {
        return montantAction;
    }

    public long obtIdSituation() {
        return identifiantSituation;
    }
}
