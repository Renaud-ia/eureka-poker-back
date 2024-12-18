package fr.eurekapoker.parties.domaine.poker.parties.infos;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InfosPartiePmu extends InfosPartiePoker {
    public InfosPartiePmu(
            FormatPoker formatPoker,
            long numeroTable,
            String nomPartie,
            int nombreJoueurs,
            LocalDateTime date,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake) {

        super(formatPoker, numeroTable, nomPartie, nombreJoueurs, date, buyIn, ante, rake);
    }

    @Override
    public String getNomRoom() {
        return "PMU";
    }
}
