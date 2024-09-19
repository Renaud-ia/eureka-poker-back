package fr.eurekapoker.parties.application.persistance.dto;

import java.math.BigDecimal;

public class ActionPersistanceDto {
    private final String nomJoueur;
    private final String nomAction;
    private final long identifiantSituation;
    private final BigDecimal montantAction;
    private final BigDecimal pot;
    private final BigDecimal potBounty;

    public ActionPersistanceDto(
            String nomJoueur,
            String nomAction,
            long identifiantSituation,
            BigDecimal montantAction,
            BigDecimal pot,
            BigDecimal potBounty) {
        this.nomJoueur = nomJoueur;
        this.nomAction = nomAction;
        this.identifiantSituation = identifiantSituation;
        this.montantAction = montantAction;
        this.pot = pot;
        this.potBounty = potBounty;
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

    public BigDecimal obtPot() {
        return pot;
    }

    public BigDecimal obtPotBounty() {
        return potBounty;
    }
}
