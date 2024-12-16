package fr.eurekapoker.parties.domaine.parsing.dto.pmu;

import fr.eurekapoker.parties.domaine.parsing.dto.InfosMain;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.time.LocalDateTime;

public class InfosMainPmu extends InfosMain {
    public InfosMainPmu(FormatPoker.Variante variante, double buyIn, LocalDateTime date) {
        super(variante, buyIn, date);
    }
}
