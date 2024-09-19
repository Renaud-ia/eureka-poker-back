package fr.eurekapoker.parties.domaine.poker.moteur;

import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerAvecBet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

public class MoteurJeuTest {
    private MoteurJeu moteurJeu;

    @BeforeEach
    void initialisation() {
        this.moteurJeu = new MoteurJeu();
    }

    @Test
    void ajoutActionIncrementePot() {
        String fauxNomJoueur = "Fake";
        float montantAction = 56.34f;

        moteurJeu.ajouterJoueur(fauxNomJoueur, new BigDecimal(1000), new BigDecimal(0));
        ActionPokerAvecBet actionPokerJoueur =
                new ActionPokerAvecBet(fauxNomJoueur, ActionPoker.TypeAction.FOLD, montantAction, true);
        moteurJeu.ajouterAction(actionPokerJoueur);

        BigDecimal montantAttendu = new BigDecimal(montantAction).setScale(2, RoundingMode.HALF_UP);
        assertEquals(0, montantAttendu.compareTo(moteurJeu.obtPot()));
    }

    @Test
    void ajoutBlindeIncrementePot() {
        String fauxNomJoueur = "Fake";
        BigDecimal montantBlinde = new BigDecimal("56.34");

        moteurJeu.ajouterJoueur(fauxNomJoueur, new BigDecimal(1000), new BigDecimal(0));
        moteurJeu.ajouterBlinde(fauxNomJoueur, montantBlinde);

        assertEquals(0, montantBlinde.compareTo(moteurJeu.obtPot()));
    }

    @Test
    void ajoutAnteIncrementePot() {
        String fauxNomJoueur = "Fake";
        BigDecimal montantAnte = new BigDecimal("56.34");

        moteurJeu.ajouterJoueur(fauxNomJoueur, new BigDecimal(1000), new BigDecimal(0));
        moteurJeu.ajouterAnte(fauxNomJoueur, montantAnte);

        assertEquals(0, montantAnte.compareTo(moteurJeu.obtPot()));
    }

    @Test
    void calculeBienPotBounty() {
        String nomJoueur1 = "Joueur1";
        BigDecimal stackInitialJoueur1 = new BigDecimal("25.34").setScale(2, RoundingMode.HALF_UP);
        BigDecimal bountyJoueur1 = new BigDecimal("3.45").setScale(2, RoundingMode.HALF_UP);
        moteurJeu.ajouterJoueur(nomJoueur1, stackInitialJoueur1, bountyJoueur1);

        String nomJoueur2 = "Joueur2";
        BigDecimal stackInitialJoueur2 = new BigDecimal("36.66").setScale(2, RoundingMode.HALF_UP);
        BigDecimal bountyJoueur2 = new BigDecimal("5.8").setScale(2, RoundingMode.HALF_UP);
        moteurJeu.ajouterJoueur(nomJoueur2, stackInitialJoueur2, bountyJoueur2);

        BigDecimal montantBlindeJoueur1 = new BigDecimal("0.2").setScale(2, RoundingMode.HALF_UP);
        this.moteurJeu.ajouterBlinde(nomJoueur1, montantBlindeJoueur1);
        BigDecimal montantAnteJoueur2 = new BigDecimal("0.44").setScale(2, RoundingMode.HALF_UP);
        this.moteurJeu.ajouterAnte(nomJoueur2, montantAnteJoueur2);

        float montantActionJoueur1 = 2.56f;
        ActionPokerAvecBet actionPokerJoueur1 = new ActionPokerAvecBet(
                nomJoueur1,
                ActionPoker.TypeAction.RAISE,
                montantActionJoueur1,
                true
        );
        this.moteurJeu.ajouterAction(actionPokerJoueur1);

        BigDecimal partBountyJoueur1 =
                new BigDecimal(montantActionJoueur1)
                        .add(montantBlindeJoueur1)
                        .divide(stackInitialJoueur1, RoundingMode.HALF_UP)
                        .multiply(bountyJoueur1).setScale(2, RoundingMode.HALF_UP);

        BigDecimal partBountyJoueur2 =
                montantAnteJoueur2
                        .divide(stackInitialJoueur2, RoundingMode.HALF_UP)
                        .multiply(bountyJoueur2).setScale(2, RoundingMode.HALF_UP);

        BigDecimal montantAttendu = partBountyJoueur1.add(partBountyJoueur2).setScale(2, RoundingMode.HALF_UP);
        assertEquals(0, montantAttendu.compareTo(moteurJeu.obtPotBounty()));
    }
}
