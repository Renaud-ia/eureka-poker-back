package fr.eurekapoker.parties.domaine.poker.cartes;

import fr.eurekapoker.parties.domaine.utils.Bits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ComboReel {
    //on va toujours en avoir besoin pour le calcul
    private final List<CartePoker> cartesReelles;
    private static final int MASQUE_CARTE = (int) Bits.creerMasque(CartePoker.N_BITS_CARTE, CartePoker.N_BITS_CARTE);
    private final int comboBits;

    public ComboReel(List<CartePoker> cartesJoueur) {
        assert cartesJoueur.size() == 2;


        // on trie les cartes dans le même sens => aucun impact sur les performances
        if (cartesJoueur.get(0).toInt() > cartesJoueur.get(1).toInt()) {
            comboBits = (cartesJoueur.get(0).toInt()
                    << CartePoker.N_BITS_CARTE) | cartesJoueur.get(1).toInt();
        }
        else {
            comboBits = (cartesJoueur.get(1).toInt()
                    << CartePoker.N_BITS_CARTE) | cartesJoueur.get(0).toInt();
            Collections.reverse(cartesJoueur);
        }
        cartesReelles = cartesJoueur;
    }

    public ComboReel (int comboInt) {
        CartePoker carte1 = new CartePoker(comboInt >> CartePoker.N_BITS_CARTE);
        CartePoker carte2 = new CartePoker(comboInt & MASQUE_CARTE);
        cartesReelles = new ArrayList<>(Arrays.asList(carte1, carte2));
        this.comboBits = comboInt;
    }

    public ComboReel(Character rank1, Character suit1, Character rank2, Character suit2) {
        this(Arrays.asList(new CartePoker(rank1, suit1), new CartePoker(rank2, suit2)));
    }

    public int toInt() {
        return comboBits;
    }


    public List<CartePoker> getCartes() {
        return cartesReelles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        else if (!(o instanceof ComboReel)) return false;
        return (((ComboReel) o).comboBits == this.comboBits);
    }

    @Override
    public int hashCode() {
        return comboBits;
    }

    @Override
    public String toString() {
        return cartesReelles.get(0).toString() + cartesReelles.get(1).toString();
    }
}
