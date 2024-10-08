package fr.eurekapoker.parties.application.persistance.dto;

import java.math.BigDecimal;

public class ActionPersistanceDto {
    private final String nomJoueur;
    private final String nomAction;
    private final long identifiantSituation;
    private final BigDecimal montantAction;
    private final BigDecimal pot;
    private final BigDecimal potBounty;
    private final BigDecimal stackEffectif;
    private final int numeroAction;
    private final Boolean allIn;

    // utilisé pour récupération depuis persistance
    public ActionPersistanceDto(
            String nomJoueur,
            String nomAction,
            long identifiantSituation,
            BigDecimal montantAction,
            int numeroAction) {
        this.nomJoueur = nomJoueur;
        this.nomAction = nomAction;
        this.identifiantSituation = identifiantSituation;
        this.montantAction = montantAction;
        this.pot = null;
        this.potBounty = null;
        this.stackEffectif = null;
        this.numeroAction = numeroAction;
        this.allIn = null;
    }

    // utilise depuis parsing
    public ActionPersistanceDto(
            String nomJoueur,
            String nomAction,
            long identifiantSituation,
            BigDecimal montantAction,
            BigDecimal pot,
            BigDecimal potBounty,
            BigDecimal stackEffectif,
            boolean allIn,
            int numeroAction) {
        this.nomJoueur = nomJoueur;
        this.nomAction = nomAction;
        this.identifiantSituation = identifiantSituation;
        this.montantAction = montantAction;
        this.pot = pot;
        this.potBounty = potBounty;
        this.stackEffectif = stackEffectif;
        this.allIn = allIn;
        this.numeroAction = numeroAction;
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

    public BigDecimal obtStackEffectif() {
        return stackEffectif;
    }

    public Boolean estAllIn() {
        return allIn;
    }

    public int getNumeroAction() {
        return numeroAction;
    }
}
