package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;


public class InfosPartieWinamax extends InfosPartiePoker {
    public InfosPartieWinamax(
            FormatPoker formatPoker,
            long idPartie,
            String nomPartie,
            int nombreJoueurs,
            LocalDateTime localDateTime,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake) {
        super(formatPoker, idPartie, nomPartie, nombreJoueurs, localDateTime, buyIn, ante, rake);
    }
}
