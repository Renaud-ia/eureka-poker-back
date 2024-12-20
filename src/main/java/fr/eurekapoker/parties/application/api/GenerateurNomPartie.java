package fr.eurekapoker.parties.application.api;

import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.util.Objects;

public class GenerateurNomPartie {
    public static String genererNomPartie(PartiePersistanceDto partiePersistanceDto) {
        String typeJeu = partiePersistanceDto.obtTypeTable();
        String nombreJoueurs = String.valueOf(partiePersistanceDto.obtNombreSieges()) + "-max";
        String nomPartieSansCaracteresSpeciaux = partiePersistanceDto.obtNomPartie();
        BigDecimal buyIn = partiePersistanceDto.obtBuyIn();

        String buyInString;

        if (buyIn == null) buyInString = "";

        else if (Objects.equals(partiePersistanceDto.obtTypeTable(), FormatPoker.TypeTable.CASH_GAME.toString())
        && !Objects.equals(partiePersistanceDto.obtNomPartie(), "Short Track")) {
            buyInString = "NL" + buyIn.multiply(new BigDecimal(100)).stripTrailingZeros();
        }
        else buyInString = buyIn.stripTrailingZeros().toPlainString() + "€";

        return typeJeu + " "
                + nombreJoueurs + " "
                + buyInString + " "
                + "(" + nomPartieSansCaracteresSpeciaux + ")";
    }
}
