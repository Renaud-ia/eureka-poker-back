package fr.eurekapoker.parties.domaine.poker.parties;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * classe qui a pour but de standardiser les infos de partie
 * détermine le rake en fonction du format détecté
 */
public abstract class BuilderInfosPartie {
    protected FormatPoker formatPoker;
    protected long numeroTable;
    protected String nomPartie;
    protected int nombreJoueurs;
    protected LocalDateTime date;
    protected BigDecimal buyIn;
    protected BigDecimal ante;
    protected BigDecimal rake;
    protected BigDecimal montantBB;
    public abstract InfosPartiePoker build() throws ErreurLectureFichier;

    public abstract boolean donneesIncompletes();
    public void fixFormatPoker(FormatPoker formatPoker) {
        this.formatPoker = formatPoker;
    }

    public void fixNumeroTable(long numeroTable) {
        this.numeroTable = numeroTable;
    }

    public void fixDate(LocalDateTime date) {
        this.date = date;
    }

    public void fixBuyIn(BigDecimal buyIn) {
        this.buyIn = buyIn;
    }

    public void fixAnte(BigDecimal ante) {
        this.ante = ante;
    }

    public void fixRake(BigDecimal rake) {
        this.rake = rake;
    }

    public void fixNombreJoueurs(int nombreJoueurs) {
        this.nombreJoueurs = nombreJoueurs;
    }

    public void fixNomPartie(String nomPartie) {
        this.nomPartie = nomPartie;
    }

    public void fixMontantBB(BigDecimal montantBB) {
        this.montantBB = montantBB;
    }
}
