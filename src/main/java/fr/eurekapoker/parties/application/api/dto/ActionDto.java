package fr.eurekapoker.parties.application.api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Getter
public class ActionDto {
    private final String idAction;
    private final String nomJoueur;
    private final String action;
    private final BigDecimal montant;
    private final int numeroAction;
    private final BigDecimal stackActuel;
    private final BigDecimal montantInvesti;
    private final boolean estAllIn;
    private BigDecimal montantPot;
    public ActionDto(String idAction,
                    String nomJoueur,
                     String action,
                     BigDecimal montant,
                     int numeroAction,
                     BigDecimal stackActuel,
                     BigDecimal montantInvesti,
                     boolean estAllIn,
                     BigDecimal montantPot
                     ) {
        this.idAction = idAction;
        this.nomJoueur = nomJoueur;
        this.action = action;
        this.montant = montant;
        this.numeroAction = numeroAction;
        this.stackActuel = stackActuel;
        this.montantInvesti = montantInvesti;
        this.estAllIn = estAllIn;
        this.montantPot = montantPot;
    }

}
