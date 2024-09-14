package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


public class InfosPartieWinamax extends InfosPartiePoker {
    // todo vérifier le rake cash game
    // todo ajouter les différentes variantes
    // todo ajouter des TU
    private static final BigDecimal RAKE_CASH_GAME =
            new BigDecimal("0.0525").setScale(4, RoundingMode.HALF_UP);
    public InfosPartieWinamax(
            FormatPoker formatPoker,
            long idPartie,
            LocalDateTime localDateTime,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake) {
        super(formatPoker, idPartie, localDateTime, buyIn, ante, rake);
    }


    @Override
    protected void standardiserInfosPartie() {
        if (formatPoker.obtTypeTable() == FormatPoker.TypeTable.CASH_GAME) {
            this.rake = RAKE_CASH_GAME;
        }
    }
}
