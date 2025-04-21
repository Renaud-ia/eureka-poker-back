package fr.eurekapoker.parties.domaine.services;

import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PostflopRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PreflopRange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManipulationRangesTest {
    @Test
    void controleDiminutionFrequencePreflop() {
        PreflopRange preflopRange1 = new PreflopRange();
        preflopRange1.ajouterMain("33", 0.5f);

        PreflopRange preflopRange2 = new PreflopRange();
        preflopRange2.ajouterMain("33", 0.7f);

        List<PokerRange> listeRangesInput = new ArrayList<>();
        listeRangesInput.add(preflopRange1);
        listeRangesInput.add(preflopRange2);

        ManipulationRanges manipulationRanges = new ManipulationRanges();
        List<PokerRange> rangesSorties = manipulationRanges.controleDiminutionFrequence(listeRangesInput);

        assertEquals(2, rangesSorties.size());

        HashMap<String, Float> combos = rangesSorties.getLast().obtenirCombos();
        assertEquals(0.5f, combos.get("3d3s"));
    }

    @Test
    void controleDiminutionFrequenceTransitionPostflop() {
        PreflopRange preflopRange = new PreflopRange();
        preflopRange.ajouterMain("33", 0.5f);

        PostflopRange postflopRange = new PostflopRange();
        postflopRange.ajouterCombo("3d3s", 0.7f);

        List<PokerRange> listeRangesInput = new ArrayList<>();
        listeRangesInput.add(preflopRange);
        listeRangesInput.add(postflopRange);

        ManipulationRanges manipulationRanges = new ManipulationRanges();
        List<PokerRange> rangesSorties = manipulationRanges.controleDiminutionFrequence(listeRangesInput);

        assertEquals(2, rangesSorties.size());

        HashMap<String, Float> combos = rangesSorties.getLast().obtenirCombos();
        assertEquals(0.5f, combos.get("3d3s"));
    }
}
