package fr.eurekapoker.parties.application.api.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
public class JoueurDto {
    private final String idUniqueJoueur;
    private final String nomJoueur;
    private final BigDecimal stackDepart;
    private final List<String> cartesJoueurs;
    private final int siege;
    private final BigDecimal ante;
    private final BigDecimal blinde;
    private final BigDecimal resultat;
    private final boolean dealer;
    private final BigDecimal bounty;
    private boolean desactive;
    public JoueurDto(
            String idUniqueJoueur,
            String nomJoueur,
            BigDecimal stackDepart,
            List<String> cartesJoueurs,
            int siege,
            BigDecimal ante,
            BigDecimal blinde,
            BigDecimal resultat,
            boolean dealer,
            BigDecimal bounty
    ) {
        this.idUniqueJoueur = idUniqueJoueur;
        this.nomJoueur = nomJoueur;
        this.stackDepart = stackDepart;
        this.cartesJoueurs = cartesJoueurs;
        this.siege = siege;
        this.ante = ante;
        this.blinde = blinde;
        this.resultat = resultat;
        this.dealer = dealer;
        this.bounty = bounty;
    }

    public void estDesactive(boolean desactive) {
        this.desactive = desactive;
    }
}
