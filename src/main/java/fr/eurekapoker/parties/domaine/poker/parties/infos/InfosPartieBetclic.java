package fr.eurekapoker.parties.domaine.poker.parties.infos;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

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
            BigDecimal rake) {
        super(formatPoker, idPartie, nomPartie, nombreJoueurs, localDateTime, buyIn, ante, rake);
    }

    @Override
    public String getNomRoom() {
        return "Betclic";
    }
}
