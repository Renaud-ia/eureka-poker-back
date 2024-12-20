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
        corrigerNomPartie();
    }

    private void corrigerNomPartie() {
        this.nomPartie = supprimerDoubleEspace(this.nomPartie);
        this.nomPartie = supprimerNomFormat(this.nomPartie);
        this.nomPartie = retirerBuyIn(this.nomPartie);
    }

    private String retirerBuyIn(String nomPartie) {
        return nomPartie.replaceAll("\\s\\d€", "");
    }

    private String supprimerNomFormat(String nomPartie) {
        return nomPartie.replace("Sit’n’Go ", "");
    }

    private String supprimerDoubleEspace(String nomPartie) {
        return nomPartie.replaceAll("  +", " ");
    }

    @Override
    public String getNomRoom() {
        return "IPoker";
    }
}
