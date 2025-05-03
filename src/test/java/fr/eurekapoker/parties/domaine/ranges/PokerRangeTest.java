package fr.eurekapoker.parties.domaine.ranges;

import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PokerRangeTest {
    private String standardiser(String nomCombo) throws Exception {
        Method m = PokerRange.class.getDeclaredMethod("standardiserNomCombo", String.class);
        m.setAccessible(true);
        return (String) m.invoke(null, nomCombo);
    }

    @Test
    void testStandardisationComboOrdre() throws Exception {
        // Ordre inversé → doit être remis dans l’ordre
        assertEquals("AhKh", standardiser("KhAh"));
        assertEquals("As2c", standardiser("2cAs"));
        assertEquals("Jd3s", standardiser("3sJd"));
    }

    @Test
    void testStandardisationMêmeRangOrdreCouleur() throws Exception {
        // Même rang, couleur plus basse doit être devant
        assertEquals("AdAh", standardiser("AhAd"));
        assertEquals("2c2d", standardiser("2d2c"));
        assertEquals("JcJs", standardiser("JsJc"));
    }

    @Test
    void testComboDéjàBienFormé() throws Exception {
        // Déjà bien ordonné
        assertEquals("AhKh", standardiser("AhKh"));
        assertEquals("Kc2s", standardiser("Kc2s"));
    }

    @Test
    void testComboInvalideTaille() {
        assertThrows(Exception.class, () -> standardiser("AhK"));
        assertThrows(Exception.class, () -> standardiser("AhKh3"));
    }

    @Test
    void testComboInvalideCarte() {
        assertThrows(Exception.class, () -> standardiser("AxKh"));
        assertThrows(Exception.class, () -> standardiser("AhXh"));
    }

    @Test
    void testMêmeCarteDeuxFois() {
        assertThrows(Exception.class, () -> standardiser("AhAh"));
    }
}