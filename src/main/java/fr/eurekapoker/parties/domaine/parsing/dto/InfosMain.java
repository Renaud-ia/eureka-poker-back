package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class InfosMain {
    private final FormatPoker.Variante variante;
    private final BigDecimal buyIn;
    private final LocalDateTime date;

    public InfosMain(FormatPoker.Variante variante, double buyIn, LocalDateTime date) {
        this.variante = variante;
        this.buyIn = new BigDecimal(buyIn).setScale(2, RoundingMode.HALF_UP);;
        this.date = date;
    }

    public FormatPoker.Variante obtVariante() {
        return variante;
    }

    public BigDecimal obtBuyIn() {
        return buyIn;
    }

    public LocalDateTime obtDate() {
        return date;
    }
}
