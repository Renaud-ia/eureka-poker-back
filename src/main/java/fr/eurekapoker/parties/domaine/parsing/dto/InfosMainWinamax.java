package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class InfosMainWinamax extends  InfosMain {
    private final FormatPoker.Variante variante;
    private final FormatPoker.TypeTable typeTable;
    private final BigDecimal buyIn;
    private final LocalDateTime date;
    private final long numeroTable;
    private final BigDecimal ante;
    private final BigDecimal rake;
    private final BigDecimal montantBB;

    public InfosMainWinamax(
            FormatPoker.Variante variantePoker,
            FormatPoker.TypeTable typeTable,
            double buyIn,
            LocalDateTime date,
            long numeroTable,
            long numeroMain,
            float ante,
            double rake,
            double montantBB) {
        super(numeroMain);
        this.variante = variantePoker;
        this.typeTable = typeTable;
        this.buyIn = new BigDecimal(buyIn).setScale(2, RoundingMode.HALF_UP);
        this.date = date;
        this.numeroTable = numeroTable;
        this.ante = new BigDecimal(ante).setScale(2, RoundingMode.HALF_UP);
        this.rake = new BigDecimal(rake).setScale(4, RoundingMode.HALF_UP);
        this.montantBB = new BigDecimal(montantBB).setScale(2, RoundingMode.HALF_UP);
    }

    public final FormatPoker.Variante obtVariante() {
        return variante;
    }

    public final FormatPoker.TypeTable obtTypeTable() {
        return typeTable;
    }

    public final BigDecimal obtBuyIn() {
        return buyIn;
    }

    public final LocalDateTime obtDate() {
        return date;
    }

    public final long obtNumeroTable() {
        return numeroTable;
    }

    public final BigDecimal obtAnte() {
        return ante;
    }

    public final BigDecimal obtRake() {
        return rake;
    }

    @Override
    public BigDecimal obtMontantBb() {
        return montantBB;
    }
}
