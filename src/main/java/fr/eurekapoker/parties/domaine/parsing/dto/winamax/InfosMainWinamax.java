package fr.eurekapoker.parties.domaine.parsing.dto.winamax;

import fr.eurekapoker.parties.domaine.parsing.dto.InfosMain;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class InfosMainWinamax extends InfosMain {
    private final long identifiantMain;
    private final FormatPoker.TypeTable typeTable;
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
        super(variantePoker, buyIn, date);
        this.identifiantMain = numeroMain;
        this.typeTable = typeTable;
        this.numeroTable = numeroTable;
        this.ante = new BigDecimal(ante).setScale(2, RoundingMode.HALF_UP);
        this.rake = new BigDecimal(rake).setScale(4, RoundingMode.HALF_UP);
        this.montantBB = new BigDecimal(montantBB).setScale(2, RoundingMode.HALF_UP);
    }

    public long obtIdentifiantMain() {
        return identifiantMain;
    }

    public final FormatPoker.TypeTable obtTypeTable() {
        return typeTable;
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

    public BigDecimal obtMontantBb() {
        return montantBB;
    }
}
