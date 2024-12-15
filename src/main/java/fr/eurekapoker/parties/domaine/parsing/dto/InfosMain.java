package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class InfosMain {

    public InfosMain() {

    }



    public abstract FormatPoker.Variante obtVariante();

    public abstract BigDecimal obtBuyIn();

    public abstract LocalDateTime obtDate();
}
