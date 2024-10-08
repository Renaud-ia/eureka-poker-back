package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuilderInfosPartieWinamax extends BuilderInfosPartie {
    // todo vérifier le rake cash game
    // todo ajouter les différentes variantes
    // todo ajouter des TU
    private static final BigDecimal RAKE_CASH_GAME =
            new BigDecimal("0.0525").setScale(4, RoundingMode.HALF_UP);
    @Override
    public InfosPartiePoker build() throws ErreurLectureFichier {
        if (donneesIncompletes()) throw new ErreurLectureFichier("Toutes les infos ne sont pas complètes");
        standardiserInfosPartie();

        return new InfosPartieWinamax(
            this.formatPoker,
            this.numeroTable,
            this.nomPartie,
            this.nombreJoueurs,
            this.date,
            this.buyIn,
            this.ante,
            this.rake,
            this.montantBB
        );
    }

    @Override
    public boolean donneesIncompletes() {
        if (formatPoker == null) return true;
        if (numeroTable == 0) return true;
        if (nomPartie == null) return true;
        if (nombreJoueurs == 0) return true;
        if (date == null) return true;
        if (buyIn == null) return true;
        if (ante == null) return true;
        if (rake == null) return true;
        if (montantBB == null) return true;

        return false;
    }

    private void standardiserInfosPartie() {
        attribuerRake();
    }

    private void attribuerRake() {
        if (formatPoker.obtTypeTable() == FormatPoker.TypeTable.CASH_GAME) {
            this.rake = RAKE_CASH_GAME;
        }
    }
}
