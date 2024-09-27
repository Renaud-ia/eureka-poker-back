package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InfosPartieWinamax extends InfosPartiePoker {
    public InfosPartieWinamax(
            FormatPoker formatPoker,
            long idPartie,
            String nomPartie,
            int nombreJoueurs,
            LocalDateTime localDateTime,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake) throws ErreurLectureFichier {
        super(formatPoker, idPartie, nomPartie, nombreJoueurs, localDateTime, buyIn, ante, rake);
        corrigerNomPartie();
    }

    private void corrigerNomPartie() throws ErreurLectureFichier {
        if (this.nomPartie.contains("SHORT TRACK")) {
            String regex = "(?<=\\s)(?<buyIn>\\d+,\\d+)(?=\\s€)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(this.nomPartie);
            if (!matcher.find()) throw new ErreurLectureFichier("Nom de partie non récupéré correctement");
            this.buyIn = BigDecimal.valueOf(Float.parseFloat(matcher.group("buyIn").replace(',', '.')));

            this.nomPartie = "Short Track";
            return;
        }

        if (this.formatPoker.obtTypeTable() == FormatPoker.TypeTable.CASH_GAME) {
            return;
        }

        String regex = "^(?<nomPartie>.*?)(?=\\(\\d+\\)#\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.nomPartie);
        if (!matcher.find()) throw new ErreurLectureFichier("Nom de partie non récupéré correctement");

        this.nomPartie = matcher.group("nomPartie");
    }

    @Override
    public String getNomRoom() {
        return RoomPoker.WINAMAX.toString();
    }
}
