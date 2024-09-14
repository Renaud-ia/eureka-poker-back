package fr.eurekapoker.parties.domaine.poker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartePokerTest {
    @Test
    void chaqueCarteDoitAvoirUnCodeUnique() {
        List<Integer> listeCodes = new ArrayList<>();
        for (Character rank : CartePoker.STR_RANKS) {
            for (Character suit : CartePoker.STR_SUITS) {
                CartePoker cartePoker = new CartePoker(rank, suit);
                int code = cartePoker.toInt();
                assertTrue(code > 0);
                assertFalse(listeCodes.contains(code));
                listeCodes.add(code);
            }
        }
    }

    @Test
    void codeRecreeLaMemeCarte() {
        for (Character rank : CartePoker.STR_RANKS) {
            for (Character suit : CartePoker.STR_SUITS) {
                CartePoker cartePoker = new CartePoker(rank, suit);
                int code = cartePoker.toInt();
                CartePoker carteRecree = new CartePoker(code);
                assertEquals(cartePoker, carteRecree);
                assertEquals(cartePoker.getIntRank(), carteRecree.getIntRank());
                assertEquals(cartePoker.getIntSuit(), carteRecree.getIntSuit());
            }
        }
    }
}
