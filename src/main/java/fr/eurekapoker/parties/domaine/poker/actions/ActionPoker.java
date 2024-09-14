package fr.eurekapoker.parties.domaine.poker.actions;

import java.math.BigDecimal;

public class ActionPoker {
    private final TypeAction typeAction;
    public ActionPoker(TypeAction typeAction) {
        this.typeAction = typeAction;
    }

    public TypeAction getTypeAction() {
        return typeAction;
    }

    public BigDecimal obtMontantAction() {
        return new BigDecimal(0);
    }

    public boolean estMontantTotal() {
        return true;
    }

    public enum TypeAction {
        CHECK,
        CALL,
        FOLD,
        RAISE,
        RAISE_ALL_IN

    }
}
