package fr.eurekapoker.parties.domaine.parsing.dto.pmu;

import fr.eurekapoker.parties.domaine.parsing.dto.InfosMain;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.time.LocalDateTime;

public class InfosMainPmu extends InfosMain {
    private final FormatPoker.TypeTable typeTable;
    public InfosMainPmu(FormatPoker.Variante variante, FormatPoker.TypeTable typeTable, double buyIn, LocalDateTime date) {
        super(variante, buyIn, date);
        this.typeTable = typeTable;
    }

    public FormatPoker.TypeTable obtTypeJeu() {
        return typeTable;
    }
}
