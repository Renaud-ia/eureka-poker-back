package fr.eurekapoker.parties.domaine.poker.moteur;

import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.utils.Combinations;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncodageSituationTest {
    int MAX_ACTIONS = EncodageSituation.MAX_ACTIONS;

    @Test
    void nombreDeJoueursDifferentsDonneDesNoeudsInitiauxDifferents() {
        EncodageSituation rootHU = new EncodageSituation(2, TourPoker.RoundPoker.PREFLOP);
        EncodageSituation root3WAY = new EncodageSituation(3, TourPoker.RoundPoker.PREFLOP);

        assertNotEquals(rootHU.toLong(), root3WAY.toLong());
    }

    /**
     * vérifie si un EncodageSituation généré à partir de son id long est identique
     */
    @Test
    void reconstructionDuMemeObjetAPartirDuLong() throws Exception {
        ActionPoker.TypeAction[] actionsPossibles = ActionPoker.TypeAction.values();
        TourPoker.RoundPoker[] roundsPossibles = TourPoker.RoundPoker.values();
        Random random = new Random();

        TourPoker.RoundPoker randomRound = roundsPossibles[random.nextInt(roundsPossibles.length)];
        int randomJoueurs = random.nextInt(10) + 2;

        int N_TESTS = 100000;

        for (int i = 0; i < N_TESTS; i++) {
            EncodageSituation noeudTest = new EncodageSituation(randomJoueurs, randomRound);

            int nombre_actions = random.nextInt(2, MAX_ACTIONS);

            for (int j = 0; j < nombre_actions; j++) {
                int randomIndex = random.nextInt(actionsPossibles.length);
                ActionPoker.TypeAction randomActionPoker = ActionPoker.TypeAction.values()[randomIndex];
                noeudTest.ajouterAction(randomActionPoker);
            }

            EncodageSituation noeudLong = new EncodageSituation(noeudTest.toLong());

            // test valable car méthode recalcule le long à partir des actions
            assertEquals(noeudLong.toLong(), noeudTest.toLong());

        }
    }

    @Test
    void ajouterTropDactionsLanceException() throws Exception {
        ActionPoker.TypeAction[] actionsPossibles = ActionPoker.TypeAction.values();
        TourPoker.RoundPoker[] roundsPossibles = TourPoker.RoundPoker.values();
        Random random = new Random();

        TourPoker.RoundPoker randomRound = roundsPossibles[random.nextInt(roundsPossibles.length)];
        int randomJoueurs = random.nextInt(10) + 2;

        EncodageSituation noeudTest = new EncodageSituation(randomJoueurs, randomRound);

        int nombre_actions = MAX_ACTIONS + 1;

        assertThrows(Exception.class, () -> {
            for (int j = 0; j < nombre_actions; j++) {
                int randomIndex = random.nextInt(actionsPossibles.length);
                ActionPoker.TypeAction randomActionPoker = ActionPoker.TypeAction.values()[randomIndex];
                noeudTest.ajouterAction(randomActionPoker);
            }
        });
    }

    @Test
    void chaqueSituationIdentifiantUnique() throws Exception {
        List<Long> situationsDejaVues = new ArrayList<>();

        ActionPoker.TypeAction[] actionsPossibles = ActionPoker.TypeAction.values();
        TourPoker.RoundPoker[] roundsPossibles = TourPoker.RoundPoker.values();

        for (TourPoker.RoundPoker roundPoker: roundsPossibles) {
            for (int nombreJoueurs = 2; nombreJoueurs < EncodageSituation.MAX_JOUEURS; nombreJoueurs++) {
                for (int nombreActions = 0; nombreActions < EncodageSituation.MAX_ACTIONS; nombreActions++) {
                    Combinations<ActionPoker.TypeAction> combinationsActions = new Combinations<>(actionsPossibles);
                    List<List<ActionPoker.TypeAction>> combinaisons = combinationsActions.getCombinations(nombreActions);

                    for (List<ActionPoker.TypeAction> listeActions: combinaisons) {
                        EncodageSituation encodageSituation = new EncodageSituation(nombreJoueurs, roundPoker);

                        for (ActionPoker.TypeAction action: listeActions) {
                            encodageSituation.ajouterAction(action);
                        }

                        assertFalse(situationsDejaVues.contains(encodageSituation.toLong()));
                        situationsDejaVues.add(encodageSituation.toLong());
                    }
                }
            }
        }
    }

}
