package fr.eurekapoker.parties.domaine.poker.cartes;


import fr.eurekapoker.parties.domaine.poker.cartes.BoardPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.utils.Combinations;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardPokerTest {
    @Test
    void idenfifiantBoardEstUnique() {
        List<CartePoker> toutesLesCartes = genererToutesLesCartes();
        Collections.shuffle(toutesLesCartes);
        List<Long> codesDejaVus = new ArrayList<>();

        Combinations<CartePoker> combinator = new Combinations<>(toutesLesCartes);
        for (List<CartePoker> cartesBoard : combinator.getCombinations(3)) {
            BoardPoker boardRandom = new BoardPoker(cartesBoard);
            long code = boardRandom.asLong();

            assertFalse(codesDejaVus.contains(code));
            codesDejaVus.add(code);
        }

        List<CartePoker> echantillonCartes = toutesLesCartes.subList(0, Math.min(toutesLesCartes.size(), 20));
        Combinations<CartePoker> combinatorReduit = new Combinations<>(echantillonCartes);
        for (List<CartePoker> cartesBoard : combinatorReduit.getCombinations(4)) {
            BoardPoker boardRandom = new BoardPoker(cartesBoard);
            long code = boardRandom.asLong();

            assertFalse(codesDejaVus.contains(code));
            codesDejaVus.add(code);
        }

        for (List<CartePoker> cartesBoard : combinatorReduit.getCombinations(5)) {
            BoardPoker boardRandom = new BoardPoker(cartesBoard);
            long code = boardRandom.asLong();

            assertFalse(codesDejaVus.contains(code), "Code déjà vu pour: " + boardRandom + new BoardPoker(code));
            codesDejaVus.add(code);
        }
    }

    @Test
    void boardReconstruitAvecIdEstLeMeme() {
        List<CartePoker> toutesLesCartes = genererToutesLesCartes();
        Collections.shuffle(toutesLesCartes);

        List<CartePoker> echantillonCartes = toutesLesCartes.subList(0, Math.min(toutesLesCartes.size(), 20));

        Combinations<CartePoker> combinator = new Combinations<>(echantillonCartes);
        for (List<CartePoker> cartesBoard : combinator.getCombinations(3)) {
            BoardPoker boardRandom = new BoardPoker(cartesBoard);
            BoardPoker boardRecree = new BoardPoker(boardRandom.asLong());
            assertEquals(boardRecree, boardRandom, "Problème : le board n'est pas le même");
        }

        for (List<CartePoker> cartesBoard : combinator.getCombinations(4)) {
            BoardPoker boardRandom = new BoardPoker(cartesBoard);
            BoardPoker boardRecree = new BoardPoker(boardRandom.asLong());
            assertEquals(boardRecree, boardRandom, "Problème : le board n'est pas le même");
        }

        for (List<CartePoker> cartesBoard : combinator.getCombinations(5)) {
            BoardPoker boardRandom = new BoardPoker(cartesBoard);
            BoardPoker boardRecree = new BoardPoker(boardRandom.asLong());
            assertEquals(boardRecree, boardRandom, "Problème : le board n'est pas le même: " + boardRecree + boardRandom);
        }
    }

    private List<CartePoker> genererToutesLesCartes() {
        List<CartePoker> toutesLesCartes = new ArrayList<>();
        for (Character rank : CartePoker.STR_RANKS) {
            for (Character suit : CartePoker.STR_SUITS) {
                CartePoker cartePoker = new CartePoker(rank, suit);
                toutesLesCartes.add(cartePoker);
            }
        }

        return toutesLesCartes;    }
}

