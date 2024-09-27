package fr.eurekapoker.parties.domaine.poker.mains;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.cartes.ComboReel;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * orchestre la récupération des données sur une main de poker
 * expose une interface à la partie application pour récupérer toutes les données
 */
public class MainPoker {
    private final long identifiantMain;

    private List<CartePoker> cartesHero;
    public MainPoker(long identifiantMain) {
        this.identifiantMain = identifiantMain;
    }

    public long obtIdParse() {
        return identifiantMain;
    }
}
