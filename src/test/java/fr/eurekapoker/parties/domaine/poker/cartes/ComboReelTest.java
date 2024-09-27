package fr.eurekapoker.parties.domaine.poker.cartes;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComboReelTest {
    @Test
    void identifiantUniquePourChaqueCombo() {
        HashMap<Integer, ComboReel> codesDejaVus = new HashMap<>();

        for(Character rank1 : CartePoker.STR_RANKS) {
            for(Character rank2 : CartePoker.STR_RANKS) {
                for (Character suit1 : CartePoker.STR_SUITS) {
                    for (Character suit2 : CartePoker.STR_SUITS) {
                        ComboReel comboReel = new ComboReel(rank1, suit1, rank2, suit2);
                        int code = comboReel.toInt();
                        if (codesDejaVus.containsKey(code)) {
                            ComboReel memeCombo = codesDejaVus.get(code);
                            assertTrue(memeCombo.getCartes().contains(comboReel.getCartes().get(0)));
                            assertTrue(memeCombo.getCartes().contains(comboReel.getCartes().get(1)));
                        }
                        else codesDejaVus.put(code, comboReel);
                    }
                }
            }
        }
    }

    /**
     * garantit qu'un combo Réel est toujours égal quelque soit l'ordre de saisie des cartes
     */
    @Test
    void changerOrdreDesCartesNeChangePasIdentifiantUnique() {
        for(Character rank1 : CartePoker.STR_RANKS) {
            for(Character rank2 : CartePoker.STR_RANKS) {
                for (Character suit1 : CartePoker.STR_SUITS) {
                    for (Character suit2 : CartePoker.STR_SUITS) {
                        ComboReel comboReel = new ComboReel(rank1, suit1, rank2, suit2);
                        ComboReel comboInverse = new ComboReel(rank2, suit2, rank1, suit1);
                        assertEquals(comboInverse.toInt(), comboReel.toInt());
                    }
                }
            }
        }
    }

    @Test
    void bonAffichageString() {
        for(Character rank1 : CartePoker.STR_RANKS) {
            for(Character rank2 : CartePoker.STR_RANKS) {
                for (Character suit1 : CartePoker.STR_SUITS) {
                    for (Character suit2 : CartePoker.STR_SUITS) {
                        ComboReel comboReel = new ComboReel(rank1, suit1, rank2, suit2);
                        assertTrue((String.valueOf(rank2) + suit2 + rank1 + suit1).equals(comboReel.toString()) ||
                                (String.valueOf(rank1) + suit1 + rank2 + suit2).equals(comboReel.toString()));
                    }
                }
            }
        }
    }

}

