package fr.eurekapoker.parties.domaine.poker.ranges;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.utils.Combinations;

import java.util.*;

public abstract class PokerRange {
    public static final Set<String> LISTE_CARTES;
    private static final HashSet<String> LISTE_COMBOS;
    private final static List<List<Character>> COMBINAISONS_COULEURS;

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

        Combinations<Character> combinations = new Combinations<>(CartePoker.STR_SUITS);
        COMBINAISONS_COULEURS = combinations.getCombinations(2);
    }
    protected final HashMap<String, Float> combos;

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

    protected List<String> convertirMainEnCombo(String main) {
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

    public Map<String, Float> obtenirMains() {
        HashMap<String, Float> mains = new HashMap<>();

        for (String main: PreflopRange.LISTE_MAINS_POSSIBLES) {
            float frequence = 0f;
            int nombreCombos = 0;

            for (String nomCombo : this.convertirMainEnCombo(main)) {
                frequence += this.combos.getOrDefault(nomCombo, 0f);
                nombreCombos += 1;
            }

            frequence /= nombreCombos;

            if (frequence == 0f) continue;

            mains.put(main, frequence);
        }

        return mains;
    }
}
