package fr.eurekapoker.parties.domaine.poker.parties.builders;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.infos.InfosPartieWinamax;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BuilderInfosPartieWinamax extends BuilderInfosPartie {
    // todo vérifier le rake cash game
    // todo ajouter les différentes variantes
    // todo ajouter des TU
    private static final BigDecimal RAKE_CASH_GAME =
            new BigDecimal("0.0525").setScale(4, RoundingMode.HALF_UP);

    public InfosPartieWinamax build() throws ErreurLectureFichier {
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
                this.rake
        );
    }


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
