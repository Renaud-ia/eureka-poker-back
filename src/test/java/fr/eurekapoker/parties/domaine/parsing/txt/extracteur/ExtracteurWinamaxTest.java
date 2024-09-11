package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.poker.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.JoueurPoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ExtracteurWinamaxTest {
    private ExtracteurWinamax extracteurWinamax;
    @BeforeEach
    void initialisation() {
        extracteurWinamax = new ExtracteurWinamax();
    }


    // TESTS EXTRAIRE ACTION
    @Test
    void doitExtraireMontantRaiseEnEuros() throws ErreurRegex {
        String ligne = "GregossTR raises 0.03€ to 0.05€";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.RAISE, actionPoker.getTypeAction());
        assertEquals("GregossTR", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("0.05");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }
    @Test
    void doitExtraireNomJoueurAvecEspace() throws ErreurRegex {
        String ligne = "cypress will bets 30";
        ActionPokerJoueur actionPokerJoueur = extracteurWinamax.extraireAction(ligne);
        assertEquals("cypress will", actionPokerJoueur.getNomJoueur());
    }
    @Test
    void doitExtraireActionFold() throws ErreurRegex {
        String ligne = "Patounet3184 folds";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.FOLD, actionPoker.getTypeAction());
        assertEquals("Patounet3184", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("0");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }

    @Test
    void doitExtraireActionCheck() throws ErreurRegex {
        String ligne = "Y.O.F. checks";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.CHECK, actionPoker.getTypeAction());
        assertEquals("Y.O.F.", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("0");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }

    @Test
    void doitExtraireActionPremierBet() throws ErreurRegex {
        String ligne = "Acasu bets 20";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.RAISE, actionPoker.getTypeAction());
        assertEquals("Acasu", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("20");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }

    @Test
    void testDoitExtraireMontantReraise() throws ErreurRegex {
        String ligne = "MarkRent0n raises 30 to 50";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.RAISE, actionPoker.getTypeAction());
        assertEquals("MarkRent0n", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("50");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }

    @Test
    void testDoitExtrairePremierBetAllIn() throws ErreurRegex {
        //todo trouver un premier bet all-in
    }

    @Test
    void testDoitExtraireReraiseAllIn() throws ErreurRegex {
        String ligne = "LaMissDu33 raises 1266 to 1326 and is all-in";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.RAISE_ALL_IN, actionPoker.getTypeAction());
        assertEquals("LaMissDu33", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("1326");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }

    @Test
    void testDoitExtraireCallAllIn() throws ErreurRegex {
        String ligne = "cypress will calls 114 and is all-in";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.CALL, actionPoker.getTypeAction());
        assertEquals("cypress will", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("114");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertFalse(actionPoker.estMontantTotal());
    }

    @Test
    void doitExtraireNomJoueurAvecCaracteresChinois() throws ErreurRegex {
        String ligne = "Loisea夥0 folds";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.FOLD, actionPoker.getTypeAction());
        assertEquals("Loisea夥0", actionPoker.getNomJoueur());
    }

    // TESTS EXTRAIRE STACK ET JOUEUR
    @Test
    void doitExtraireStackJoueurCashGame() throws ErreurRegex {
        String ligne = "Seat 3: Nakata80 (2.12€)";
        // todo changer en Stack Joueur
        JoueurPoker joueurPoker = extracteurWinamax.extraireJoueur(ligne);
    }


}
