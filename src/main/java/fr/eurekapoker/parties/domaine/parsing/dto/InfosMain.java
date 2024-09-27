package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class InfosMain {
    private final long identifiantMain;
    public InfosMain(long identifiantMain) {
        this.identifiantMain = identifiantMain;
    }

    public long obtIdentifiantMain() {
        return identifiantMain;
    }

    public abstract FormatPoker.Variante obtVariante();

    public abstract FormatPoker.TypeTable obtTypeTable();

    public abstract BigDecimal obtBuyIn();

    public abstract LocalDateTime obtDate();

    public abstract long obtNumeroTable();

    public abstract BigDecimal obtAnte();

    public abstract BigDecimal obtRake();
}
