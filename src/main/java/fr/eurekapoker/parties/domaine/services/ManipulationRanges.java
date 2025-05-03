package fr.eurekapoker.parties.domaine.services;

import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;

import java.util.HashMap;
import java.util.List;

public class ManipulationRanges {
    private final HashMap<String, Float> frequencesMax;

    public ManipulationRanges() {
        this.frequencesMax = new HashMap<>();
    }
    public List<PokerRange> controleDiminutionFrequence(List<PokerRange> rangesOrdonnees) {
        /*
        assure que la fréquence de chaque combo diminue
         */
        for (PokerRange pokerRange: rangesOrdonnees) {
            if (frequencesMax.isEmpty()) {
                frequencesMax.putAll(pokerRange.obtenirCombos());
                continue;
            }

            for (String combo: pokerRange.obtenirCombos().keySet()) {
                float frequence = pokerRange.obtenirCombos().get(combo);
                float frequenceMax = frequencesMax.getOrDefault(combo, 0f);

                if (frequence > frequenceMax) {
                    pokerRange.ajouterCombo(combo, frequenceMax);
                }

                else {
                    frequencesMax.put(combo, frequence);
                }
            }
        }

        return rangesOrdonnees;
    }
}
