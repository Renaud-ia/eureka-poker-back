package fr.eurekapoker.parties.domaine.ranges;

import fr.eurekapoker.parties.domaine.poker.ranges.PreflopRange;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PreflopRangeTest {
    @Test
    void ajouterPocketPaire() {
        PreflopRange preflopRange = new PreflopRange();
        preflopRange.ajouterMain("44", 0.5f);

        HashMap<String, Float> combos = preflopRange.obtenirCombos();

        assertEquals(6, combos.size());

        List<String> combosAttendus = List.of(
                "4c4d", "4c4h", "4c4s",
                "4d4h", "4d4s",
                "4h4s"
        );
        for (String combo : combosAttendus) {
            assertTrue(combos.containsKey(combo), "Combo attendu manquant : " + combo);
        }
    }

    @Test
    void ajouterMainSuitee() {
        PreflopRange preflopRange = new PreflopRange();
        preflopRange.ajouterMain("32s", 0.5f);

        HashMap<String, Float> combos = preflopRange.obtenirCombos();

        assertEquals(4, combos.size());

        List<String> combosAttendus = List.of(
                "3c2c", "3d2d", "3h2h", "3s2s"
        );
        for (String combo : combosAttendus) {
            assertTrue(combos.containsKey(combo), "Combo attendu manquant : " + combo);
        }
    }

    @Test
    void ajouterMainDepareillee() {
        PreflopRange preflopRange = new PreflopRange();
        preflopRange.ajouterMain("56o", 0.5f);

        HashMap<String, Float> combos = preflopRange.obtenirCombos();

        assertEquals(12, combos.size());

        List<String> combosAttendus = List.of(
                "6c5d", "6c5h", "6c5s",
                "6d5c", "6d5h", "6d5s",
                "6h5c", "6h5d", "6h5s",
                "6s5c", "6s5d", "6s5h"
        );
        for (String combo : combosAttendus) {
            assertTrue(combos.containsKey(combo), "Combo attendu manquant : " + combo);
        }
    }
}
