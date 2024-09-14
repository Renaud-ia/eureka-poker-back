package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.FormatNonPrisEnCharge;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FabriqueInfosPartie {
    private final RoomPoker roomPoker;
    public FabriqueInfosPartie(RoomPoker roomPoker) {
        this.roomPoker = roomPoker;
    }
    public InfosPartiePoker creerInfosPartie(
            FormatPoker formatPoker,
            long idPartie,
            LocalDateTime localDateTime,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake
    ) throws FormatNonPrisEnCharge {
        if (roomPoker == RoomPoker.WINAMAX) {
            return new InfosPartieWinamax(
                    formatPoker,
                    idPartie,
                    localDateTime,
                    buyIn,
                    ante,
                    rake
            );
        }


        throw new FormatNonPrisEnCharge("Cette room n'est pas prise en charge: " + roomPoker);
    }
}
