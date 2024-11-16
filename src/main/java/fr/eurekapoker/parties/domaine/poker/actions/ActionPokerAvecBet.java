package fr.eurekapoker.parties.domaine.poker.actions;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ActionPokerAvecBet extends ActionPokerJoueur {
    // todo gérer le paramètre TotalBet
    private final BigDecimal montantAction;
    private final boolean totalBet;
    public ActionPokerAvecBet(String nomJoueur, TypeAction typeAction, float montantAction, boolean totalBet) {
        super(nomJoueur, typeAction);
        this.montantAction = new BigDecimal(montantAction).setScale(2, RoundingMode.HALF_UP);
        this.totalBet = totalBet;
    }

    @Override
    public BigDecimal obtMontantAction() {
        return montantAction;
    }

    @Override
    public boolean estMontantTotal() {
        return totalBet;
    }

    public boolean montantPositif() {
        return montantAction.compareTo(new BigDecimal(0)) > 0;
    }
}
