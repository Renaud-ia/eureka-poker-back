package fr.eurekapoker.parties.domaine.poker.parties.builders;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;

public class BuilderInfosPartieBetclic extends BuilderInfosPartie {
    private static final BigDecimal RAKE_BETCLIC = new BigDecimal("0.05");
    private static final BigDecimal ANTE_BETCLIC = new BigDecimal("0.1");
    @Override
    protected void standardiserInfosPartie() {
        attribuerRake();
        attribuerAnte();
    }

    @Override
    public boolean donneesIncompletes() {
        if (formatPoker == null) return true;
        if (numeroTable == 0) return true;
        if (nomPartie == null) return true;
        if (nombreJoueurs == 0) return true;
        if (date == null) return true;
        if (buyIn == null) return true;

        return false;
    }

    private void attribuerAnte() {
        if (formatPoker.obtTypeTable() == FormatPoker.TypeTable.MTT) {
            this.ante = ANTE_BETCLIC;
        }

        else this.ante = new BigDecimal(0);
    }

    private void attribuerRake() {
        if (formatPoker.obtTypeTable() == FormatPoker.TypeTable.CASH_GAME) {
            this.rake = RAKE_BETCLIC;
        }

        else this.rake = new BigDecimal(0);
    }
}
