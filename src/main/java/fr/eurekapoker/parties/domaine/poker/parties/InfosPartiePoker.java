package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class InfosPartiePoker {
    protected final FormatPoker formatPoker;
    private final long identifiantPartie;
    protected String nomPartie;
    private final Integer nombreJoueurs;
    private final LocalDateTime date;
    protected  BigDecimal buyIn;
    private final BigDecimal ante;
    protected final BigDecimal rake;
    protected String formatSpecialRoom;
    public InfosPartiePoker(
            FormatPoker formatPoker,
            long idPartie,
            String nomPartie,
            int nombreJoueurs,
            LocalDateTime localDateTime,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake) {
        this.formatPoker = formatPoker;
        this.identifiantPartie = idPartie;
        this.nomPartie = nomPartie;
        this.nombreJoueurs = nombreJoueurs;
        this.date = localDateTime;
        this.buyIn = buyIn;
        this.ante = ante;
        this.rake = rake;
        this.formatSpecialRoom = "";
    }
    public int obtNombreJoueurs() {
        return nombreJoueurs;
    }

    public long getIdParse() {
        return identifiantPartie;
    }

    public abstract String getNomRoom();

    public String getFormatPoker() {
        return formatPoker.obtVariante();
    }

    public String getTypeJeu() {
        return formatPoker.obtTypeTable().toString();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getNomPartie() {
        return nomPartie;
    }

    public int getNombreSieges() {
        return nombreJoueurs;
    }

    public String getFormatSpecialRoom() {
        return formatSpecialRoom;
    }

    public BigDecimal getBuyIn() {
        return buyIn;
    }
}
