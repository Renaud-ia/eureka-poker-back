package fr.eurekapoker.parties.domaine.poker.mains;

import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import org.junit.jupiter.api.Test;

public class TourPokerTest {
    @Test
    void methodeSuivantRenvoieProchainRound()  {
        TourPoker.RoundPoker roundPoker = TourPoker.RoundPoker.PREFLOP;

        roundPoker = roundPoker.suivant();
        assert roundPoker == TourPoker.RoundPoker.FLOP;

        roundPoker = roundPoker.suivant();
        assert roundPoker == TourPoker.RoundPoker.TURN;

        roundPoker = roundPoker.suivant();
        assert roundPoker == TourPoker.RoundPoker.RIVER;

        roundPoker = roundPoker.suivant();
        assert roundPoker == TourPoker.RoundPoker.BLINDES;
    }
}
