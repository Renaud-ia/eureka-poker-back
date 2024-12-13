package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;

import java.util.Collection;
import java.util.List;

public class InfosTourBetclic {
    private final TourPoker.RoundPoker roundPoker;
    private final List<CartePoker> nouvellesCartes;

    public InfosTourBetclic(TourPoker.RoundPoker roundPoker, List<CartePoker> nouvellesCartes) {
        this.roundPoker = roundPoker;
        this.nouvellesCartes = nouvellesCartes;
    }

    public TourPoker.RoundPoker obtRoundPoker() {
        return roundPoker;
    }

    public List<CartePoker> obtCartesExtraites() {
        return nouvellesCartes;
    }
}
