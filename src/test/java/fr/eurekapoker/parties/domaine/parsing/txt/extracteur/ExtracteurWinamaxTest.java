package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.InfosMainWinamax;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.InfosTableWinamax;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.ResultatJoueurWinamax;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        BigDecimal expectedValue = new BigDecimal("0.05").setScale(2, RoundingMode.HALF_UP);
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }
    @Test
    void doitExtraireNomJoueurAvecEspace() throws ErreurRegex {
        String ligne = "cypress will bets 30";
        ActionPokerJoueur actionPokerJoueur = extracteurWinamax.extraireAction(ligne);
        assertEquals("cypress will", actionPokerJoueur.getNomJoueur());
        assertTrue(actionPokerJoueur.estMontantTotal());
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
    void doitExtraireActionCall() throws ErreurRegex {
        String ligne = "_ NEXT_ calls 1800";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.CALL, actionPoker.getTypeAction());
        assertEquals("_ NEXT_", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("1800");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertFalse(actionPoker.estMontantTotal());
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
    void testDoitExtraireRaiseAllInGrosMontant() throws ErreurRegex {
        String ligne = "benedicimus raises 29859658 to 34259658 and is all-in";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.RAISE, actionPoker.getTypeAction());
        assertEquals("benedicimus", actionPoker.getNomJoueur());
        BigDecimal expectedValue = new BigDecimal("34259658");
        assertEquals(0, expectedValue.compareTo(actionPoker.obtMontantAction()));
        assertTrue(actionPoker.estMontantTotal());
    }

    @Test
    void testDoitExtraireReraiseAllIn() throws ErreurRegex {
        String ligne = "LaMissDu33 raises 1266 to 1326 and is all-in";
        ActionPokerJoueur actionPoker = extracteurWinamax.extraireAction(ligne);
        assertEquals(ActionPoker.TypeAction.RAISE, actionPoker.getTypeAction());
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
    void doitExtraireInfosJoueurCashGame() throws ErreurRegex {
        String ligne = "Seat 3: Nakata80 (2.12€)";
        InfosJoueur infosJoueur = extracteurWinamax.extraireStackJoueur(ligne);
        assertEquals("Nakata80", infosJoueur.obtJoueur());
        assertEquals(3, infosJoueur.obtSiege());
        BigDecimal expectedValue = new BigDecimal("2.12");
        assertEquals(0, expectedValue.compareTo(infosJoueur.obtStack()));
        assertFalse(infosJoueur.aBounty());
    }

    @Test
    void doitExtraireInfosJoueurExpresso() throws ErreurRegex {
        String ligne = "Seat 2: wmx-i5o0yy6 (500)";
        InfosJoueur infosJoueur = extracteurWinamax.extraireStackJoueur(ligne);
        assertEquals(2, infosJoueur.obtSiege());
        assertEquals("wmx-i5o0yy6", infosJoueur.obtJoueur());
        BigDecimal expectedValue = new BigDecimal("500");
        assertEquals(0, expectedValue.compareTo(infosJoueur.obtStack()));
        assertFalse(infosJoueur.aBounty());
    }

    @Test
    void doitExtraireInfosJoueurAvecBountyMtt() throws ErreurRegex {
        String ligne = "Seat 6: KABB.99 (20000, 1.80€ bounty)";
        InfosJoueur infosJoueur = extracteurWinamax.extraireStackJoueur(ligne);
        assertEquals("KABB.99", infosJoueur.obtJoueur());
        assertEquals(6, infosJoueur.obtSiege());
        BigDecimal stackAttendu = new BigDecimal("20000");
        assertEquals(0, stackAttendu.compareTo(infosJoueur.obtStack()));
        assertTrue(infosJoueur.aBounty());
        BigDecimal bountyAttendu = new BigDecimal("1.8");
        assertEquals(0, bountyAttendu.compareTo(infosJoueur.obtBounty()));
    }

    @Test
    void doitExtraireInfosJoueurAvecGrosBountyMtt() throws ErreurRegex {
        String ligne = "Seat 5: menphiscom (18193515, 11.22€ bounty)";
        InfosJoueur infosJoueur = extracteurWinamax.extraireStackJoueur(ligne);
        assertEquals("menphiscom", infosJoueur.obtJoueur());
        assertEquals(5, infosJoueur.obtSiege());
        BigDecimal stackAttendu = new BigDecimal("18193515");
        assertEquals(0, stackAttendu.compareTo(infosJoueur.obtStack()));
        assertTrue(infosJoueur.aBounty());
        BigDecimal bountyAttendu = new BigDecimal("11.22");
        assertEquals(0, bountyAttendu.compareTo(infosJoueur.obtBounty()));
    }

    @Test
    void doitExtraireBountyInfosJoueurAvecEspace() throws ErreurRegex {
        String ligne = "Seat 1: Bastos Papin (548787, 4.26€ bounty)";
        InfosJoueur infosJoueur = extracteurWinamax.extraireStackJoueur(ligne);
        assertEquals("Bastos Papin", infosJoueur.obtJoueur());
        assertEquals(1, infosJoueur.obtSiege());
        BigDecimal stackAttendu = new BigDecimal("548787");
        assertEquals(0, stackAttendu.compareTo(infosJoueur.obtStack()));
        assertTrue(infosJoueur.aBounty());
        BigDecimal bountyAttendu = new BigDecimal("4.26");
        assertEquals(0, bountyAttendu.compareTo(infosJoueur.obtBounty()));
    }

    // TEST EXTRAIRE INFOS MAIN

    @Test
    void doitExtraireInfosMainExpresso() throws ErreurImport {
        String ligne = "Winamax Poker - Tournament \"Expresso\" buyIn: 1.86€ + 0.14€ level: 1 - HandId: #2563429635522035713-1-1667484424 - Holdem no limit (10/20) - 2022/11/03 14:07:04 UTC\n";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.SPIN, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2022, 11, 3, 14, 7, 4);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("2");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(2563429635522035713L, infosMainWinamax.obtNumeroTable());
        assertEquals(1667484424L, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("0");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0.07");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal(20);
        System.out.println(infosMainWinamax.obtMontantBb());
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }

    @Test
    void doitExtraireInfosMainTableExpresso2() throws ErreurImport {
        String ligne = "Winamax Poker - Tournament \"Expresso\" buyIn: 1.86€ + 0.14€ level: 1 - HandId: #2563429635522035713-1-1667484424 - Holdem no limit (10/20) - 2022/11/03 14:07:04 UTC\n";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.SPIN, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2022, 11, 3, 14, 7, 4);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("2");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(2563429635522035713L, infosMainWinamax.obtNumeroTable());
        assertEquals(1667484424L, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("0");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0.07");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal(20);
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }

    @Test
    void doitExtraireInfosMainTableMtt() throws ErreurImport {
        String ligne = "Winamax Poker - Tournament \"WESTERN\" buyIn: 0.90€ + 0.10€ level: 1 - HandId: #2536694125529399325-1-1668368706 - Holdem no limit (25/100/200) - 2022/11/13 19:45:06 UTC\n";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.MTT, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2022, 11, 13, 19, 45, 6);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("1");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(2536694125529399325L, infosMainWinamax.obtNumeroTable());
        assertEquals(1668368706, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("25");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0.10");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal(200);
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }

    @Test
    void doitExtraireInfosMainCashGame() throws ErreurImport {
        String ligne = "Winamax Poker - CashGame - HandId: #15638288-29-1612884230 - Holdem no limit (0.01€/0.02€) - 2021/02/09 15:23:50 UTC";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.CASH_GAME, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2021, 2, 9, 15, 23, 50);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("0.02");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(15638288, infosMainWinamax.obtNumeroTable());
        assertEquals(1612884230, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("0");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal("0.02");
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }

    @Test
    void doitExtraireInfosMainShortTrack() throws ErreurImport {
        String ligne = "Winamax Poker - CashGame - HandId: #15453614-1031-1609705548 - Holdem no limit (0.01€/0.02€) - 2021/01/03 20:25:48 UTC";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.CASH_GAME, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2021, 1, 3, 20, 25, 48);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("0.02");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(15453614, infosMainWinamax.obtNumeroTable());
        assertEquals(1609705548, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("0");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal("0.02");
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }

    @Test
    void extraitInfosMainPartieGratuite() throws ErreurImport {
        String ligne = "Winamax Poker - Tournament \"Starting Block WiPT - Déglingos\" buyIn: 0€ + 0€ level: 1 - HandId: #2991658018329853953-1-1694274624 - Holdem no limit (10/20) - 2023/09/09 15:50:24 UTC";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.MTT, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2023, 9, 9, 15, 50, 24);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("0");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(2991658018329853953L, infosMainWinamax.obtNumeroTable());
        assertEquals(1694274624, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("0");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal(20);
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }


    @Test
    void extraitInfosMainPartieExpressoFreeroll() throws ErreurImport {
        String ligne = "Winamax Poker - Tournament \"Expresso Freeroll\" buyIn: 0€ + 0€ level: 1 - HandId: #3799916446201413633-1-1736033657 - Holdem no limit (10/20) - 2025/01/04 23:34:17 UTC";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.SPIN, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2025, 1, 4, 23, 34, 17);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("0");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(3799916446201413633L, infosMainWinamax.obtNumeroTable());
        assertEquals(1736033657, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("0");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal(20);
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }

    @Test
    void extraitInfosMainPartieColorado() throws ErreurImport {
        String ligne = "Winamax Poker - Go Fast \"Colorado\" - HandId: #13995056-9255063-1588276003 - Holdem no limit (0.01€/0.02€) - 2020/04/30 19:46:43 UTC";
        InfosMainWinamax infosMainWinamax = extracteurWinamax.extraireInfosMain(ligne);
        assertEquals(FormatPoker.TypeTable.CASH_GAME, infosMainWinamax.obtTypeTable());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainWinamax.obtVariante());
        LocalDateTime dateAttendue = LocalDateTime.of(2020, 4, 30, 19, 46, 43);
        assertEquals(dateAttendue, infosMainWinamax.obtDate());
        BigDecimal buyInAttendu = new BigDecimal("0.02");
        assertEquals(0, buyInAttendu.compareTo(infosMainWinamax.obtBuyIn()));
        assertEquals(13995056, infosMainWinamax.obtNumeroTable());
        assertEquals(1588276003, infosMainWinamax.obtIdentifiantMain());
        BigDecimal anteAttendue = new BigDecimal("0");
        assertEquals(0, anteAttendue.compareTo(infosMainWinamax.obtAnte()));
        BigDecimal rakeAttendu = new BigDecimal("0");
        assertEquals(0, rakeAttendu.compareTo(infosMainWinamax.obtRake()));
        BigDecimal bbAttendue = new BigDecimal("0.02");
        assertEquals(0, bbAttendue.compareTo(infosMainWinamax.obtMontantBb()));
    }

    // TESTS EXTRACTION INFOS TABLE

    @Test
    void extraitInfosTableExpresso() throws ErreurRegex {
        String ligne = "Table: 'Expresso(434726065)#0' 3-max (real money) Seat #1 is the button";
        InfosTableWinamax infosTable = extracteurWinamax.extraireInfosTable(ligne);
        assertEquals("Expresso(434726065)#0", infosTable.obtNomTable());
        assertEquals(3, infosTable.obtNombreJoueurs());
        assertEquals(1, infosTable.obtPositionDealer());
    }

    @Test
    void extraitInfosTableShortTrack() throws ErreurRegex {
        String ligne = "Table: 'SHORT TRACK 0,25 € 05' 5-max (real money) Seat #2 is the button";
        InfosTableWinamax infosTable = extracteurWinamax.extraireInfosTable(ligne);
        assertEquals("SHORT TRACK 0,25 € 05", infosTable.obtNomTable());
        assertEquals(5, infosTable.obtNombreJoueurs());
        assertEquals(2, infosTable.obtPositionDealer());
    }

    @Test
    void extraitInfosTableMtt() throws ErreurRegex {
        String ligne = "Table: 'Kill The Fish(429861217)#047' 6-max (real money) Seat #2 is the button";
        InfosTableWinamax infosTable = extracteurWinamax.extraireInfosTable(ligne);
        assertEquals("Kill The Fish(429861217)#047", infosTable.obtNomTable());
        assertEquals(6, infosTable.obtNombreJoueurs());
        assertEquals(2, infosTable.obtPositionDealer());
    }

    @Test
    void extraitInfosTableCashGame() throws ErreurRegex {
        String ligne = "Table: 'Vienna 03' 5-max (real money) Seat #2 is the button";
        InfosTableWinamax infosTable = extracteurWinamax.extraireInfosTable(ligne);
        assertEquals("Vienna 03", infosTable.obtNomTable());
        assertEquals(5, infosTable.obtNombreJoueurs());
        assertEquals(2, infosTable.obtPositionDealer());
    }

    // TESTS EXTRACTION TOUR

    @Test
    void extraitCartesPreflop() throws ErreurRegex {
        String ligne = "*** PRE-FLOP *** ";
        NouveauTour nouveauTour = extracteurWinamax.extraireNouveauTour(ligne, null);
        List<CartePoker> cartesExtraites = nouveauTour.obtCartesExtraites();
        assertTrue(cartesExtraites.isEmpty());

        assertEquals(TourPoker.RoundPoker.PREFLOP, nouveauTour.obtRound());
    }

    @Test
    void extraitCartesFlop() throws ErreurRegex {
        String ligne = "*** FLOP *** [Js 3h Ad]";
        NouveauTour nouveauTour = extracteurWinamax.extraireNouveauTour(ligne, null);
        List<CartePoker> cartesExtraites = nouveauTour.obtCartesExtraites();
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('J', 's'));
        cartesAttendues.add(new CartePoker('3', 'h'));
        cartesAttendues.add(new CartePoker('A', 'd'));

        assertEquals(cartesAttendues, cartesExtraites);
        assertEquals(TourPoker.RoundPoker.FLOP, nouveauTour.obtRound());
    }

    @Test
    void extraitCartesTurn() throws ErreurRegex {
        String ligne = "*** TURN *** [8h 3d 7c][7d]";
        NouveauTour nouveauTour = extracteurWinamax.extraireNouveauTour(ligne, null);
        List<CartePoker> cartesExtraites = nouveauTour.obtCartesExtraites();
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('8', 'h'));
        cartesAttendues.add(new CartePoker('3', 'd'));
        cartesAttendues.add(new CartePoker('7', 'c'));
        cartesAttendues.add(new CartePoker('7', 'd'));

        assertEquals(cartesAttendues, cartesExtraites);
        assertEquals(TourPoker.RoundPoker.TURN, nouveauTour.obtRound());
    }

    @Test
    void extraitCartesRiver() throws ErreurRegex {
        String ligne = "*** RIVER *** [9h Tc Ad 9s][6s]";
        NouveauTour nouveauTour = extracteurWinamax.extraireNouveauTour(ligne, null);
        List<CartePoker> cartesExtraites = nouveauTour.obtCartesExtraites();
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('9', 'h'));
        cartesAttendues.add(new CartePoker('T', 'c'));
        cartesAttendues.add(new CartePoker('A', 'd'));
        cartesAttendues.add(new CartePoker('9', 's'));
        cartesAttendues.add(new CartePoker('6', 's'));

        assertEquals(cartesAttendues, cartesExtraites);
        assertEquals(TourPoker.RoundPoker.RIVER, nouveauTour.obtRound());
    }

    // TESTS EXTRAIRE BLINDES OU ANTE

    @Test
    void doitExtraireBlinde() throws ErreurRegex {
        String ligne = "PastilleRosE posts small blind 10";
        BlindeOuAnte blindeOuAnte = extracteurWinamax.extraireBlindeOuAnte(ligne);
        assertTrue(blindeOuAnte.isBlinde());
        assertFalse(blindeOuAnte.isAnte());
        BigDecimal blindeAttendue = new BigDecimal("10");
        assertEquals("PastilleRosE", blindeOuAnte.getNomJoueur());
        assertEquals(0, blindeAttendue.compareTo(blindeOuAnte.obtMontant()));
    }

    @Test
    void doitExtraireBlindeEuro() throws ErreurRegex {
        String ligne = "RendsL4rgent posts big blind 0.02€";
        BlindeOuAnte blindeOuAnte = extracteurWinamax.extraireBlindeOuAnte(ligne);
        assertTrue(blindeOuAnte.isBlinde());
        assertFalse(blindeOuAnte.isAnte());
        BigDecimal blindeAttendue = new BigDecimal("0.02");
        assertEquals("RendsL4rgent", blindeOuAnte.getNomJoueur());
        assertEquals(0, blindeAttendue.compareTo(blindeOuAnte.obtMontant()));
    }

    @Test
    void doitExtraireAnte() throws ErreurRegex {
        String ligne = "K-riet zooo posts ante 25";
        BlindeOuAnte blindeOuAnte = extracteurWinamax.extraireBlindeOuAnte(ligne);
        assertFalse(blindeOuAnte.isBlinde());
        assertTrue(blindeOuAnte.isAnte());
        BigDecimal blindeAttendue = new BigDecimal("25");
        assertEquals("K-riet zooo", blindeOuAnte.getNomJoueur());
        assertEquals(0, blindeAttendue.compareTo(blindeOuAnte.obtMontant()));
    }


    // TESTS EXTRACTION RESULTATS
    @Test
    void doitExtraireResultatJoueur() throws ErreurRegex {
        String ligne = "Seat 4: ALAVERDURE (big blind) won 425";
        ResultatJoueurWinamax resultatJoueur = extracteurWinamax.extraireResultat(ligne);
        assertEquals("ALAVERDURE", resultatJoueur.getNomJoueur());
        BigDecimal montantAttendu = new BigDecimal("425");
        assertEquals(0, montantAttendu.compareTo(resultatJoueur.obtMontantGagne()));
        List<CartePoker> cartesAttendues = new ArrayList<>();
        assertEquals(cartesAttendues, resultatJoueur.obtCartesJoueur());
    }

    @Test
    void doitExtraireResultatJoueurAvecSeat() throws ErreurRegex {
        String ligne = "Seat 2: RendsL4rgent (small blind) showed [4h 4s] and lost with Quads of 8";
        ResultatJoueurWinamax resultatJoueur = extracteurWinamax.extraireResultat(ligne);
        assertEquals("RendsL4rgent", resultatJoueur.getNomJoueur());
        BigDecimal montantAttendu = new BigDecimal("0");
        assertEquals(0, montantAttendu.compareTo(resultatJoueur.obtMontantGagne()));
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('4', 'h'));
        cartesAttendues.add(new CartePoker('4', 's'));
        assertEquals(cartesAttendues, resultatJoueur.obtCartesJoueur());
    }

    @Test
    void doitExtraireResultatJoueurAvecCartes () throws ErreurRegex {
        String ligne = "Seat 6: Cello33 showed [5h 5c] and won 11553 with Quads of 8";
        ResultatJoueurWinamax resultatJoueur = extracteurWinamax.extraireResultat(ligne);
        assertEquals("Cello33", resultatJoueur.getNomJoueur());
        BigDecimal montantAttendu = new BigDecimal("11553");
        assertEquals(0, montantAttendu.compareTo(resultatJoueur.obtMontantGagne()));
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('5', 'h'));
        cartesAttendues.add(new CartePoker('5', 'c'));
        assertEquals(cartesAttendues, resultatJoueur.obtCartesJoueur());
    }

    @Test
    void doitExtraireResultatJoueurEnEuros() throws ErreurRegex {
        String ligne = "Seat 1: Ivenwicht (big blind) showed [Kd Ah] and won 1.90€ with High card : Ace";
        ResultatJoueurWinamax resultatJoueur = extracteurWinamax.extraireResultat(ligne);
        assertEquals("Ivenwicht", resultatJoueur.getNomJoueur());
        BigDecimal montantAttendu = new BigDecimal("1.9");
        assertEquals(0, montantAttendu.compareTo(resultatJoueur.obtMontantGagne()));
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('K', 'd'));
        cartesAttendues.add(new CartePoker('A', 'h'));
        assertEquals(cartesAttendues, resultatJoueur.obtCartesJoueur());
    }

    // TESTS EXTRAIRE CARTES HERO

    @Test
    void doitExtraireInfosHero() throws ErreurRegex {
        String ligne = "Dealt to RendsL4rgent [3h Td]";
        InfosHero infosHero = extracteurWinamax.extraireInfosHero(ligne);
        assertEquals("RendsL4rgent", infosHero.obtNomHero());
        List<CartePoker> cartes = infosHero.obtCartesHero();
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('3', 'h'));
        cartesAttendues.add(new CartePoker('T', 'd'));
        assertEquals(cartesAttendues, cartes);
    }

    @Test
    void doitExtraireInfosHeroAvecEspace() throws ErreurRegex {
        String ligne = "Dealt to Rends Moi Largent [5s 6d]";
        InfosHero infosHero = extracteurWinamax.extraireInfosHero(ligne);
        assertEquals("Rends Moi Largent", infosHero.obtNomHero());
        List<CartePoker> cartes = infosHero.obtCartesHero();
        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('5', 's'));
        cartesAttendues.add(new CartePoker('6', 'd'));
        assertEquals(cartesAttendues, cartes);
    }
}
