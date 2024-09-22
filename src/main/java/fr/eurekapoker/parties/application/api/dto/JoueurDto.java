package fr.eurekapoker.parties.application.api.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class JoueurDto {
    private final String nomJoueur;
    private final BigDecimal stackDepart;
    private final List<String> cartesJoueurs;
    private final int siege;
    private final BigDecimal ante;
    private final BigDecimal blinde;

    public JoueurDto(String nomJoueur,
                     BigDecimal stackDepart,
                     List<String> cartesJoueurs,
                     int siege,
                     BigDecimal ante,
                     BigDecimal blinde) {
        this.nomJoueur = nomJoueur;
        this.stackDepart = stackDepart;
        this.cartesJoueurs = cartesJoueurs;
        this.siege = siege;
        this.ante = ante;
        this.blinde = blinde;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public final BigDecimal getStackDepart() {
        return stackDepart;
    }

    public List<String> getCartesJoueurs() {
        return cartesJoueurs;
    }

    public int getSiege() {
        return siege;
    }
    public BigDecimal getAnte() {
        return ante;
    }
    public BigDecimal blinde() {
        return blinde;
    }
}
