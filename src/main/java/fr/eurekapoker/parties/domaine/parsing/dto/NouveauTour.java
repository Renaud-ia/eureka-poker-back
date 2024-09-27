package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;

import java.util.List;

public class NouveauTour {
    private final TourPoker.RoundPoker roundPoker;
    private final List<CartePoker> board;

    public NouveauTour(TourPoker.RoundPoker roundPoker, List<CartePoker> board) {
        this.roundPoker = roundPoker;
        this.board = board;
    }

    public List<CartePoker> obtCartesExtraites() {
        return board;
    }


    public TourPoker.RoundPoker obtRound() {
        return roundPoker;
    }
}
