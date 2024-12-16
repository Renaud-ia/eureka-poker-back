package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.pmu.InfosMainPmu;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExtracteurPmuTest {
    private ExtracteurPmu extracteurPmu;

    @BeforeEach
    void initialisation() {
        extracteurPmu = new ExtracteurPmu();
    }

    @Test
    void extraitNumeroMain() throws Exception {
        String ligne = "***** Hand History for Game 24771887244 *****\n";
        long idMain = extracteurPmu.extraireIdMain(ligne);
        assertEquals(24771887244L, idMain);
    }

    @Test
    void extraitInfosMainsSpin() throws Exception {
        String ligne = "NL Texas Hold'em €2 EUR Buy-in  - Saturday, December 14, 13:45:56 CET 2024\n";
        InfosMainPmu infosMainPmu = extracteurPmu.extraireInfosMain(ligne);

        BigDecimal buyInAttendu = new BigDecimal(2);
        assertEquals(0, buyInAttendu.compareTo(infosMainPmu.obtBuyIn()));
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainPmu.obtVariante());

        LocalDateTime dateAttendue = LocalDateTime.of(2024, 12, 14, 13, 45, 56);
        assertEquals(dateAttendue, infosMainPmu.obtDate());
    }

    @Test
    void extraitInfosMainsCG() throws Exception {
        String ligne = "€0.01/€0.02 EUR NL Texas Hold'em - Saturday, December 14, 13:17:19 CET 2024";
        InfosMainPmu infosMainPmu = extracteurPmu.extraireInfosMain(ligne);

        BigDecimal buyInAttendu = new BigDecimal(2);
        assertEquals(0, buyInAttendu.compareTo(infosMainPmu.obtBuyIn()), "Buy in extrait : " + infosMainPmu.obtBuyIn());
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainPmu.obtVariante());

        LocalDateTime dateAttendue = LocalDateTime.of(2024, 12, 14, 13, 17, 19);
        assertEquals(dateAttendue, infosMainPmu.obtDate());
    }

    @Test
    void extraitInfosMainsMTT() throws Exception {
        String ligne = "NL Texas Hold'em €0.50 EUR Buy-in Trny:399523796 Level:7  Blinds-Antes(1.2K/2.4K -300) - Saturday, December 14, 13:54:20 CET 2024";
        InfosMainPmu infosMainPmu = extracteurPmu.extraireInfosMain(ligne);

        BigDecimal buyInAttendu = new BigDecimal("0.5");
        assertEquals(0, buyInAttendu.compareTo(infosMainPmu.obtBuyIn()));
        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, infosMainPmu.obtVariante());

        LocalDateTime dateAttendue = LocalDateTime.of(2024, 12, 14, 13, 54, 20);
        assertEquals(dateAttendue, infosMainPmu.obtDate());
    }


    @Test
    void extraitInfosTableSpin() throws Exception {
        String ligne = "Table 2€ SPINS (402535347) Table #1 (Real Money)\\n";
        InfosTable infosTable = extracteurPmu.extraireInfosTable(ligne);

        assertEquals("SPINS", infosTable.obtNomTable());
    }

    @Test
    void extraitInfosTableCG() throws Exception {
        String ligne = "Table Caen (Real Money)";
        InfosTable infosTable = extracteurPmu.extraireInfosTable(ligne);
        assertEquals("Caen", infosTable.obtNomTable());
    }

    @Test
    void extraitInfosTableMTT() throws Exception {
        String ligne = "Table Progressive KO 6-Max. 40€ Gtd  (399523796) Table #11 (Real Money)";
        InfosTable infosTable = extracteurPmu.extraireInfosTable(ligne);
        assertEquals("Progressive KO 6-Max. 40€ Gtd", infosTable.obtNomTable());
    }

    @Test
    void extraitNombreJoueurs() throws Exception {
        String ligne = "Total number of players : 2/6 ";
        int nombreJoueurs = extracteurPmu.extraireNombreJoueurs(ligne);
        assertEquals(6, nombreJoueurs);
    }

    @Test
    void extraitInfosJoueurSpin() throws Exception {
        String ligne = "Seat 1: ElLogo76 ( 500 )";
        InfosJoueur infosJoueur = extracteurPmu.extraireStackJoueur(ligne);

        assertEquals("ElLogo76", infosJoueur.obtJoueur());
        assertEquals(1, infosJoueur.obtSiege());

        BigDecimal stackAttendu = new BigDecimal(500);
        assertEquals(0, stackAttendu.compareTo(infosJoueur.obtStack()));

        assertFalse(infosJoueur.aBounty());
    }

    @Test
    void extraitInfosJoueurMtt() throws Exception {
        String ligne = "Seat 1: phil 1340 ( 100,000 )";
        InfosJoueur infosJoueur = extracteurPmu.extraireStackJoueur(ligne);

        assertEquals("phil 1340", infosJoueur.obtJoueur());
        assertEquals(1, infosJoueur.obtSiege());

        BigDecimal stackAttendu = new BigDecimal(100000);
        assertEquals(0, stackAttendu.compareTo(infosJoueur.obtStack()));

        assertFalse(infosJoueur.aBounty());
    }

    @Test
    void extraitInfosJoueurCG() throws Exception {
        String ligne = "Seat 6: Player6 ( €2.01 EUR )";
        InfosJoueur infosJoueur = extracteurPmu.extraireStackJoueur(ligne);

        assertEquals("Player6", infosJoueur.obtJoueur());
        assertEquals(6, infosJoueur.obtSiege());

        BigDecimal stackAttendu = new BigDecimal("2.01");
        assertEquals(0, stackAttendu.compareTo(infosJoueur.obtStack()));

        assertFalse(infosJoueur.aBounty());
    }

    @Test
    void extraitMontantBB() throws Exception {
        String ligne = "Blinds(10/20)\n";
        BigDecimal montantBB = extracteurPmu.extraireBigBlinde(ligne);
        BigDecimal montantAttenduBB = new BigDecimal(20);

        assertEquals(0, montantAttenduBB.compareTo(montantBB));
    }
    @Test
    void extraitMontantBBMTT() throws Exception {
        String ligne = "Blinds-Antes(400/800 -100)";
        BigDecimal montantBB = extracteurPmu.extraireBigBlinde(ligne);
        BigDecimal montantAttenduBB = new BigDecimal(800);

        assertEquals(0, montantAttenduBB.compareTo(montantBB));
    }

    @Test
    void extraireBlindeAnteSpin() throws Exception {
        String ligne = "TIYO95 posts small blind [10].";

        BlindeOuAnte blindeOuAnte = extracteurPmu.extraireBlindeOuAnte(ligne);
        assertEquals("TIYO95", blindeOuAnte.getNomJoueur());
        assertTrue(blindeOuAnte.isBlinde());
        assertFalse(blindeOuAnte.isAnte());

        BigDecimal montantAttendu = new BigDecimal(10);
        assertEquals(0, montantAttendu.compareTo(blindeOuAnte.obtMontant()));
    }

    @Test
    void extraireBlindeAnteMtt() throws Exception {
        String ligne = "phil 1340 posts small blind [1,200].";
        BlindeOuAnte blindeOuAnte = extracteurPmu.extraireBlindeOuAnte(ligne);

        assertEquals("phil 1340", blindeOuAnte.getNomJoueur());
        assertTrue(blindeOuAnte.isBlinde());
        assertFalse(blindeOuAnte.isAnte());

        BigDecimal montantAttendu = new BigDecimal(1200);
        assertEquals(0, montantAttendu.compareTo(blindeOuAnte.obtMontant()));
    }

    @Test
    void extraireBlindeAnteCG() throws Exception {
        String ligne = "rendslargent33 posts big blind [€0.02 EUR].";
        BlindeOuAnte blindeOuAnte = extracteurPmu.extraireBlindeOuAnte(ligne);
        assertEquals("rendslargent33", blindeOuAnte.getNomJoueur());
        assertTrue(blindeOuAnte.isBlinde());
        assertFalse(blindeOuAnte.isAnte());

        BigDecimal montantAttendu = new BigDecimal("0.02");
        assertEquals(0, montantAttendu.compareTo(blindeOuAnte.obtMontant()));
    }

    @Test
    void extraireAnteMtt() throws Exception {
        String ligne = "TRIEN posts ante [100]";
        BlindeOuAnte blindeOuAnte = extracteurPmu.extraireBlindeOuAnte(ligne);
        assertEquals("TRIEN", blindeOuAnte.getNomJoueur());
        assertTrue(blindeOuAnte.isAnte());
        assertFalse(blindeOuAnte.isBlinde());

        BigDecimal montantAttendu = new BigDecimal(100);
        assertEquals(0, montantAttendu.compareTo(blindeOuAnte.obtMontant()));
    }

    @Test
    void extraireCartesHero() throws Exception {
        String ligne = "Dealt to rendslargent33 [  Js 2h ]\n";
        InfosHero infosHero = extracteurPmu.extraireInfosHero(ligne);

        assertEquals("rendslargent33", infosHero.obtNomHero());

        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('J', 's'));
        cartesAttendues.add(new CartePoker('2', 'h'));

        assertEquals(cartesAttendues, infosHero.obtCartesHero());
    }

    @Test
    void extraireActionFold() throws Exception {
        String ligne = "ElLogo76 folds";
        ActionPokerJoueur actionPokerJoueur = extracteurPmu.extraireAction(ligne);

        assertEquals("ElLogo76", actionPokerJoueur.getNomJoueur());
        assertEquals(ActionPoker.TypeAction.FOLD, actionPokerJoueur.getTypeAction());

        BigDecimal montantAttendu = new BigDecimal(0);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));

        assertTrue(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionCall() throws Exception {
        String ligne = "phil 1340 calls [1,200]";
        ActionPokerJoueur actionPokerJoueur = extracteurPmu.extraireAction(ligne);

        assertEquals("phil 1340", actionPokerJoueur.getNomJoueur());
        assertEquals(ActionPoker.TypeAction.CALL, actionPokerJoueur.getTypeAction());

        BigDecimal montantAttendu = new BigDecimal(1200);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));

        assertFalse(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionCheck() throws Exception {
        String ligne = "TIYO95 checks";
        ActionPokerJoueur actionPokerJoueur = extracteurPmu.extraireAction(ligne);

        assertEquals("TIYO95", actionPokerJoueur.getNomJoueur());
        assertEquals(ActionPoker.TypeAction.CHECK, actionPokerJoueur.getTypeAction());

        BigDecimal montantAttendu = new BigDecimal(0);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));

        assertTrue(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionRaises() throws Exception {
        String ligne = "ElLogo76 raises [40]";
        ActionPokerJoueur actionPokerJoueur = extracteurPmu.extraireAction(ligne);

        assertEquals("ElLogo76", actionPokerJoueur.getNomJoueur());
        assertEquals(ActionPoker.TypeAction.RAISE, actionPokerJoueur.getTypeAction());

        BigDecimal montantAttendu = new BigDecimal(40);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));

        assertFalse(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionRaisesAllIn() throws Exception {
        String ligne = "ElLogo76 is all-In  [410]";
        ActionPokerJoueur actionPokerJoueur = extracteurPmu.extraireAction(ligne);

        assertEquals("ElLogo76", actionPokerJoueur.getNomJoueur());
        assertEquals(ActionPoker.TypeAction.RAISE, actionPokerJoueur.getTypeAction());

        BigDecimal montantAttendu = new BigDecimal(410);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));

        assertFalse(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraitCartesPreflop() throws ErreurRegex {
        String ligne = "** Dealing down cards **\n";
        NouveauTour nouveauTour = extracteurPmu.extraireNouveauTour(ligne);
        assertEquals(TourPoker.RoundPoker.PREFLOP, nouveauTour.obtRound());
        assertTrue(nouveauTour.obtCartesExtraites().isEmpty());
    }

    @Test
    void extraitCartesFlop() throws ErreurRegex {
        String ligne = "** Dealing Flop ** [ Kc, 5s, 5d ]\n";
        NouveauTour nouveauTour = extracteurPmu.extraireNouveauTour(ligne);
        assertEquals(TourPoker.RoundPoker.FLOP, nouveauTour.obtRound());

        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('K', 'c'));
        cartesAttendues.add(new CartePoker('5', 's'));
        cartesAttendues.add(new CartePoker('5', 'd'));

        assertEquals(cartesAttendues, nouveauTour.obtCartesExtraites());
    }

    @Test
    void extraitCartesTurn() throws ErreurRegex {
        String ligne = "** Dealing Turn ** [ 4c ]\n";;
        NouveauTour nouveauTour = extracteurPmu.extraireNouveauTour(ligne);
        assertEquals(TourPoker.RoundPoker.TURN, nouveauTour.obtRound());

        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('4', 'c'));

        assertEquals(cartesAttendues, nouveauTour.obtCartesExtraites());
    }

    @Test
    void extraitCartesRiver() throws ErreurRegex {
        String ligne = "** Dealing River ** [ 3s ]\n";;;
        NouveauTour nouveauTour = extracteurPmu.extraireNouveauTour(ligne);
        assertEquals(TourPoker.RoundPoker.RIVER, nouveauTour.obtRound());

        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('3', 's'));

        assertEquals(cartesAttendues, nouveauTour.obtCartesExtraites());
    }

    @Test
    void extraitCartes() throws Exception {
        String ligne = "SACPALSA shows [ 2s, 8d ]three of a kind.";
        CartesJoueur cartesJoueur = extracteurPmu.extraireCartes(ligne);

        assertEquals("SACPALSA", cartesJoueur.obtNomJoueur());

        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('2', 's'));
        cartesAttendues.add(new CartePoker('8', 'd'));

        assertEquals(cartesAttendues, cartesJoueur.obtCartes());
    }

    @Test
    void extraitResultat() throws Exception {
        String ligne = "rendslargent33 wins 100 chips from the main pot with three of a kind.";
        ResultatJoueur resultatExtrait = extracteurPmu.extraireResultat(ligne);

        assertEquals("rendslargent33", resultatExtrait.getNomJoueur());

        BigDecimal resultatAttendu = new BigDecimal(100);
        assertEquals(0, resultatAttendu.compareTo(resultatExtrait.obtMontantGagne()));
    }

    @Test
    void extraitResultatEnEuros() throws Exception {
        String ligne = "ShalashaskaFOX wins €0.16 EUR";
        ResultatJoueur resultatExtrait = extracteurPmu.extraireResultat(ligne);

        assertEquals("ShalashaskaFOX", resultatExtrait.getNomJoueur());

        BigDecimal resultatAttendu = new BigDecimal("0.16");
        assertEquals(0, resultatAttendu.compareTo(resultatExtrait.obtMontantGagne()));
    }


}
