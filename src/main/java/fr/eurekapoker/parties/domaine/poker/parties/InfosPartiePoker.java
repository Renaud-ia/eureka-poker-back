package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class InfosPartiePoker {
    protected FormatPoker formatPoker;
    private long identifiantPartie;
    private LocalDateTime date;
    private BigDecimal buyIn;
    private BigDecimal ante;
    protected BigDecimal rake;
    private Integer nombreJoueurs;
    protected String nomPartie;
    public InfosPartiePoker(
            FormatPoker formatPoker,
            long idPartie,
            LocalDateTime localDateTime,
            BigDecimal buyIn,
            BigDecimal ante,
            BigDecimal rake) {
        this.formatPoker = formatPoker;
        this.identifiantPartie = idPartie;
        this.date = localDateTime;
        this.buyIn = buyIn;
        this.ante = ante;
        this.rake = rake;
    }

    public void fixFormatPoker(FormatPoker formatPoker) {
        this.formatPoker = formatPoker;
    }

    public void fixNombreJoueurs(int nombreJoueurs) throws ErreurLectureFichier {
        if (this.nombreJoueurs != null) {
            throw new ErreurLectureFichier("Le nombre de joueurs a déjà été fixé");
        }
        this.nombreJoueurs = nombreJoueurs;
    }

    public void fixNomPartie(String nomPartie) throws ErreurLectureFichier {
        if (this.nomPartie != null) {
            throw new ErreurLectureFichier("Le nombre de joueurs a déjà été fixé");
        }
        this.nomPartie = nomPartie;

        standardiserInfosPartie();
    }

    protected abstract void standardiserInfosPartie();
    public int obtNombreJoueurs() {
        return nombreJoueurs;
    }

    public boolean estInitialise() {
        return false;
    }
}
