package fr.eurekapoker.parties.domaine.poker.ranges;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class PokerRange {
    public static final Set<String> LISTE_CARTES;
    private static final HashSet<String> LISTE_COMBOS;

    static {
        LISTE_CARTES = new HashSet<>();

        for (Character rank: CartePoker.STR_RANKS) {
            for (Character suit: CartePoker.STR_SUITS) {
                LISTE_CARTES.add(String.valueOf(rank) + suit);
            }
        }

        LISTE_COMBOS = new HashSet<>();

        for (String carte1 : LISTE_CARTES) {
            for (String carte2: LISTE_CARTES) {
                if (Objects.equals(carte1, carte2)) continue;
                LISTE_COMBOS.add(standardiserNomCombo(carte1 + carte2));
            }
        }
    }
    private final HashMap<String, Float> combos;

    protected PokerRange() {
        this.combos = new HashMap<>();
    }

    protected PokerRange(HashMap<String, Float> combos) {
        this.combos = combos;
    }

    public HashMap<String, Float> obtenirCombos() {
        return combos;
    }

    public void ajouterCombo(String nomCombo, float frequence) {
        String nomStandardise = standardiserNomCombo(nomCombo);

        this.combos.put(nomStandardise, frequence);
    }

    private static String standardiserNomCombo(String nomCombo) {
        nomCombo = nomCombo.strip();
        if (nomCombo.length() != 4) {
            throw new IllegalArgumentException("Combo non valide");
        }

        String carte1 = nomCombo.substring(0, 2);
        if (!LISTE_CARTES.contains(carte1)) throw new IllegalArgumentException("Combo non valide");
        int rank1 = CartePoker.CHAR_RANK_TO_INT_RANK.get(carte1.charAt(0));
        int suit1 = CartePoker.CHAR_SUIT_TO_INT_SUIT.get(carte1.charAt(1));

        String carte2 = nomCombo.substring(2, 4);
        if (!LISTE_CARTES.contains(carte2)) throw new IllegalArgumentException("Combo non valide");
        int rank2 = CartePoker.CHAR_RANK_TO_INT_RANK.get(carte2.charAt(0));
        int suit2 = CartePoker.CHAR_SUIT_TO_INT_SUIT.get(carte2.charAt(1));

        if (carte1.equals(carte2)) throw new IllegalArgumentException("Combo non valide");

        if (rank1 < rank2) {
            return carte2 + carte1;
        }

        if (rank1 > rank2) {
            return carte1 + carte2;
        }

        if (suit1 < suit2) {
            return carte2 + carte1;
        }

        return carte1 + carte2;
    }

    public void initialiser() {
        for (String nomCombo : LISTE_COMBOS) {
            this.combos.put(nomCombo, 0f);
        }
    }
}
