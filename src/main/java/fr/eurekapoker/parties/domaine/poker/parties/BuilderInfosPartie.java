package fr.eurekapoker.parties.domaine.poker.parties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * classe qui a pour but de standardiser les infos de partie
 * détermine le rake en fonction du format détecté
 */
public class BuilderInfosPartie {
    // todo
    public InfosPartiePoker build() {
        return null;
    }

    public boolean donneesIncompletes() {
        return true;
    }

    public void fixFormatPoker(FormatPoker formatPoker) {
    }

    public void fixNumeroTable(long l) {
    }

    public void fixDate(LocalDateTime localDateTime) {
    }

    public void fixBuyIn(BigDecimal bigDecimal) {
    }

    public void fixAnte(BigDecimal bigDecimal) {
    }

    public void fixRake(BigDecimal bigDecimal) {
    }

    public void fixNombreJoueurs(int i) {
    }

    public void fixNomPartie(String s) {
    }
}
