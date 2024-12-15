package fr.eurekapoker.parties.domaine.parsing.dto.pmu;

import fr.eurekapoker.parties.domaine.parsing.dto.InfosMain;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InfosMainPmu extends InfosMain {
    @Override
    public FormatPoker.Variante obtVariante() {
        return null;
    }

    @Override
    public BigDecimal obtBuyIn() {
        return null;
    }

    @Override
    public LocalDateTime obtDate() {
        return null;
    }
}
