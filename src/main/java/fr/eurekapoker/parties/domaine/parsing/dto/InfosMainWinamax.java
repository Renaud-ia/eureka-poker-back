package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.FormatPoker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class InfosMainWinamax extends  InfosMain {
    // todo vérifier le rake cash game
    // todo ajouter les différentes variantes
    private static final BigDecimal RAKE_CASH_GAME = new BigDecimal("0.0525").setScale(4, RoundingMode.HALF_UP);
    private final FormatPoker.Variante variante;
    private final FormatPoker.TypeTable typeTable;
    private final BigDecimal buyIn;
    private final LocalDateTime date;
    private final long numeroTable;
    private final float ante;
    private final BigDecimal rake;

    public InfosMainWinamax(
            FormatPoker.Variante variantePoker,
            FormatPoker.TypeTable typeTable,
            double buyIn,
            LocalDateTime date,
            long numeroTable,
            long numeroMain,
            float ante,
            double rake) {
        super(numeroMain);
        this.variante = variantePoker;
        this.typeTable = typeTable;
        this.buyIn = new BigDecimal(buyIn).setScale(2, RoundingMode.HALF_UP);
        this.date = date;
        this.numeroTable = numeroTable;
        this.ante = ante;

        if (typeTable == FormatPoker.TypeTable.CASH_GAME) {
            this.rake = RAKE_CASH_GAME;
        }
        else this.rake = new BigDecimal(rake).setScale(4, RoundingMode.HALF_UP);
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

    public final float obtAnte() {
        return ante;
    }

    public final BigDecimal obtRake() {
        return rake;
    }
}
