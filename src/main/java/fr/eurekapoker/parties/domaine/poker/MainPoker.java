package fr.eurekapoker.parties.domaine.poker;

import java.math.BigDecimal;
import java.util.List;

public class MainPoker {
    private final long identifiantMain;
    public MainPoker(long identifiantMain) {
        this.identifiantMain = identifiantMain;
    }

    public List<JoueurPoker> obtJoueurs() {
        return null;
    }

    public void ajouterJoueur(JoueurPoker joueurPoker) {
    }

    public void ajouterTour(TourPoker tourPoker) {
    }

    public List<TourPoker> obtTours() {
        return null;
    }

    public void ajouterBlinde(JoueurPoker joueurPoker, BigDecimal montant) {
    }

    public void ajouterAnte(JoueurPoker joueurPoker, BigDecimal montant) {
    }
}
