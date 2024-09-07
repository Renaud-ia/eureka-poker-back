package fr.eurekapoker.parties.domaine.poker;

public class TourPoker {

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
