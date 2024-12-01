package fr.eurekapoker.parties.domaine.poker.parties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InfosPartieBetclic extends InfosPartiePoker {
    public InfosPartieBetclic(
            FormatPoker formatPoker,
            long idPartie,
            String nomPartie,
            int nombreJoueurs,
            LocalDateTime localDateTime,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake,
            BigDecimal montantBB) {
        super(formatPoker, idPartie, nomPartie, nombreJoueurs, localDateTime, buyIn, ante, rake, montantBB);
    }

    @Override
    public String getNomRoom() {
        return "Betclic";
    }
}
