package fr.eurekapoker.parties.domaine.poker.cartes;

import java.util.ArrayList;
import java.util.List;

public class BoardPoker {
    private final List<CartePoker> cartes;

    //utile pour calculer des valeurs préflop
    public BoardPoker() {this.cartes = new ArrayList<>();}
    public BoardPoker(List<CartePoker> cartesBoard) {
        cartes = cartesBoard;
    }

    public BoardPoker(String cartesBoard) {
        cartesBoard = cartesBoard.replace(" ", "");
        this.cartes = new ArrayList<>();

        // Vérifier que la longueur de la chaîne est un nombre pair
        if (cartesBoard.length() % 2 != 0) {
            throw new IllegalArgumentException("Format de board saisi invalide");
        }

        for (int i = 0; i < cartesBoard.length(); i += 2) {
            char currentRank = cartesBoard.charAt(i);
            char currentSuit = cartesBoard.charAt(i + 1);
            cartes.add(new CartePoker(currentRank, currentSuit));
        }
    }

    //retrouve le board à partir de son Int
    public BoardPoker(long longBoard) {
        //important les cartes ne sont jamais à zéro

        // on retrouve le nombre de cartes encodées
        int nCartes = 0;
        long codeBoard = longBoard;
        while (codeBoard != 0) {
            codeBoard >>= CartePoker.N_BITS_CARTE;
            nCartes++;
        }

        cartes = new ArrayList<>();
        for (int cartesRestantes = nCartes; cartesRestantes > 0; cartesRestantes--) {
            int masque = (1 << CartePoker.N_BITS_CARTE) - 1;  // Crée un masque pour isoler les bits de la carte
            int intCard = (int) (longBoard >> ((cartesRestantes - 1) * CartePoker.N_BITS_CARTE)) & masque;
            cartes.add(new CartePoker(intCard));
        }
    }

    // ne pas convertir en int => overflow qui génère des bugs
    public long asLong() {
        long boardInt = 0;
        for (CartePoker carte : cartes) {
            boardInt = (boardInt << CartePoker.N_BITS_CARTE) | carte.toInt();
        }
        return boardInt;
    }

    public List<CartePoker> getCartes() {
        // retourne une référence directe car besoin de performances
        return cartes;
    }

    public void ajouterCarte(CartePoker carte) {
        this.cartes.add(carte);
    }

    public int taille() {
        return cartes.size();
    }

    public BoardPoker copie() {
        List<CartePoker> copieBoard = new ArrayList<>();
        for (CartePoker carte : this.cartes) {
            CartePoker carteCopiee = carte.copie();
            copieBoard.add(carteCopiee);
        }

        return new BoardPoker(copieBoard);
    }

    @Override
    public String toString() {
        StringBuilder repr = new StringBuilder();
        for (CartePoker carte : cartes) {
            repr.append(carte);
        }
        return repr.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        else if (!(o instanceof BoardPoker)) return false;
        else return ((BoardPoker) o).asLong() == this.asLong();
    }
}
