package fr.eurekapoker.parties.domaine.poker.mains;

import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.BoardPoker;

/**
 * attribue un code unique à chaque action ajoutée
 */
public class TourPoker {

    public TourPoker(BoardPoker board) {
    }

    public void ajouterAction(ActionPoker actionPoker) {
    }

    /**
     * utilisé pour garantir la validité de l'encodage des actions
     */
    public void ajouterJoueurNonPresent() {
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
