package fr.eurekapoker.parties.domaine.poker.ranges;


import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.utils.Combinations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PreflopRange extends PokerRange {
    private final static List<Character> LISTE_TYPES_MAINS;
    private final static List<String> LISTE_MAINS_POSSIBLES;
    private final static List<List<Character>> COMBINAISONS_COULEURS;

    static {
        LISTE_TYPES_MAINS = List.of('s', 'o');
        LISTE_MAINS_POSSIBLES = new ArrayList<>();

        for (Character rank1: Arrays.stream(CartePoker.STR_RANKS).toList().reversed()) {
            for (Character rank2: Arrays.stream(CartePoker.STR_RANKS).toList().reversed()) {
                if (rank1 == rank2) {
                    LISTE_MAINS_POSSIBLES.add(String.valueOf(rank1) + rank2);
                    continue;
                }

                LISTE_MAINS_POSSIBLES.add(String.valueOf(rank1) + rank2 + LISTE_TYPES_MAINS.getFirst());
                LISTE_MAINS_POSSIBLES.add(String.valueOf(rank1) + rank2 + LISTE_TYPES_MAINS.get(1));
            }
        }

        Combinations<Character> combinations = new Combinations<>(CartePoker.STR_SUITS);
        COMBINAISONS_COULEURS = combinations.getCombinations(2);
    }

    public PreflopRange() {
        super();
    }

    public PreflopRange(HashMap<String, Float> mains) {
        super();

        for (String main: mains.keySet()) {
            float frequence = mains.get(main);
            this.ajouterMain(main, frequence);
        }
    }

    public void ajouterMain(String main, float frequence) {
        main = main.strip();
        if (!LISTE_MAINS_POSSIBLES.contains(main)) throw new IllegalArgumentException("Main non valide");

        for (String combo : convertirMainEnCombo(main)) {
            this.ajouterCombo(combo, frequence);
        }
    }

    private List<String> convertirMainEnCombo(String main) {
        if (main.length() == 2) return this.pocketPaires(main.charAt(0));

        if (main.charAt(2) == 's') return this.combosSuites(main.charAt(0), main.charAt(1));

        return this.combosDepareilles(main.charAt(0), main.charAt(1));
    }

    private List<String> combosDepareilles(char rank1, char rank2) {
        List<String> combosDepareilles = new ArrayList<>();

        for (List<Character> combinaisons : COMBINAISONS_COULEURS) {
            Character suit1 = combinaisons.getFirst();
            Character suit2 = combinaisons.get(1);

            combosDepareilles.add(String.valueOf(rank1) + suit1 + rank2 + suit2);
            combosDepareilles.add(String.valueOf(rank1) + suit2 + rank2 + suit1);
        }
        return combosDepareilles;
    }

    private List<String> combosSuites(char rank1, char rank2) {
        List<String> combosSuites = new ArrayList<>();
        for (Character suit : CartePoker.STR_SUITS) {
            combosSuites.add(String.valueOf(rank1) + suit + rank2 + suit);
        }
        return combosSuites;
    }

    private List<String> pocketPaires(char rank) {
        List<String> pocketPaires = new ArrayList<>();
        for (Character suit1 : CartePoker.STR_SUITS) {
            for (Character suit2 : CartePoker.STR_SUITS) {
                if (suit1 >= suit2) continue;
                pocketPaires.add(String.valueOf(rank) + suit1 + rank + suit2);
            }
        }
        return pocketPaires;
    }
}
