package fr.eurekapoker.parties.application.persistance.dto;

import java.math.BigDecimal;

public class ActionPersistanceDto {
    private final String nomAction;
    private final long identifiantSituation;
    private final BigDecimal montantAction;

    public ActionPersistanceDto(String nomAction, long identifiantSituation, BigDecimal montantAction) {
        this.nomAction = nomAction;
        this.identifiantSituation = identifiantSituation;
        this.montantAction = montantAction;
    }
}
