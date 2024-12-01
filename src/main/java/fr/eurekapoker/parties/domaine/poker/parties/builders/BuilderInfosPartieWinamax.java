package fr.eurekapoker.parties.domaine.poker.parties.builders;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuilderInfosPartieWinamax extends BuilderInfosPartie {
    // todo vérifier le rake cash game
    // todo ajouter les différentes variantes
    // todo ajouter des TU
    private static final BigDecimal RAKE_CASH_GAME =
            new BigDecimal("0.0525").setScale(4, RoundingMode.HALF_UP);


    @Override
    protected void standardiserInfosPartie() {
        attribuerRake();
    }

    private void attribuerRake() {
        if (formatPoker.obtTypeTable() == FormatPoker.TypeTable.CASH_GAME) {
            this.rake = RAKE_CASH_GAME;
        }
    }
}
