package fr.eurekapoker.parties.domaine.poker;

import java.util.List;

/**
 * attribue un code unique à chaque action ajoutée
 */
public class TourPoker {

    public TourPoker(List<CartePoker> board) {
    }

    public void ajouterAction(ActionPoker actionPoker) {
    }

    public enum RoundPoker {
        PREFLOP,
        FLOP,
        TURN,
        RIVER;

        public RoundPoker suivant() {
            int newIndex = (this.ordinal() + 1) % RoundPoker.values().length;
            return RoundPoker.values()[newIndex];
        }
    }
}
