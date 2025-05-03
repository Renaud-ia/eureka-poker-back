package fr.eurekapoker.parties.domaine.poker.ranges;


import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.utils.Combinations;

import java.util.*;

public class PreflopRange extends PokerRange {
    private final static List<Character> LISTE_TYPES_MAINS;
    public final static List<String> LISTE_MAINS_POSSIBLES;

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
    }

    public PreflopRange() {
        super();
    }

    public PreflopRange(HashMap<String, Float> mains) {
        super();

        for (String main: mains.keySet()) {
            float frequence = mains.get(main);
            try {
                this.ajouterMain(main, frequence);
            }
            catch (IllegalArgumentException e) {
                this.ajouterCombo(main, frequence);
            }
        }
    }

    public void ajouterMain(String main, float frequence) {
        main = main.strip();
        if (!LISTE_MAINS_POSSIBLES.contains(main)) throw new IllegalArgumentException("Main non valide");

        for (String combo : convertirMainEnCombo(main)) {
            this.ajouterCombo(combo, frequence);
        }
    }

    @Override
    public Map<String, Float> obtenirMains() {
        HashMap<String, Float> mains = new HashMap<>();

        for (String main: PreflopRange.LISTE_MAINS_POSSIBLES) {
            String premierCombo = this.convertirMainEnCombo(main).getFirst();

            if (!this.combos.containsKey(premierCombo)) continue;
            float frequence = this.combos.get(premierCombo);

            mains.put(main, frequence);
        }

        return mains;
    }


}
