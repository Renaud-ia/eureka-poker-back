package fr.eurekapoker.parties.domaine.parsing.xml.extracteur;

import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ExtracteurBetclicTest {
    private ExtracteurBetclic extracteurBetclic;
    @BeforeEach
    void initialisation() {
        extracteurBetclic = new ExtracteurBetclic();
    }

    @Test
    void extraireNombreSieges() throws Exception {
        Document documentCG = obtDocumentCashGame();
        int nombreSieges = extracteurBetclic.extraireNombreSieges(documentCG);
        assertEquals(6, nombreSieges);
    }

    @Test
    void obtenirTypeJeu() throws Exception {
        Document documentCG = obtDocumentCashGame();
        FormatPoker.Variante typeJeu = extracteurBetclic.obtenirTypeJeu(documentCG);

        assertEquals(FormatPoker.Variante.HOLDEM_NO_LIMIT, typeJeu);
    }

    @Test
    void obtenirNomTournoi() throws Exception {
        Document documentCG = obtDocumentCashGame();
        FormatPoker.TypeTable typeTableCG = extracteurBetclic.obtenirTypeTable(documentCG);

        assertEquals(FormatPoker.TypeTable.CASH_GAME, typeTableCG);

        Document documentTKO = obtDocumentTKO();
        FormatPoker.TypeTable typeTableTKO = extracteurBetclic.obtenirTypeTable(documentTKO);
        assertEquals(FormatPoker.TypeTable.MTT, typeTableTKO);

        Document documentTwister = obtDocumentTwister();
        FormatPoker.TypeTable typeTableTwister = extracteurBetclic.obtenirTypeTable(documentTwister);
        assertEquals(FormatPoker.TypeTable.SPIN, typeTableTwister);
    }

    @Test
    void extraireNomPartie() throws Exception {
        Document documentCG = obtDocumentCashGame();
        NomIdPartieBetclic nomIdPartieCG = extracteurBetclic.extraireNomIdPartie(documentCG);

        assertEquals("[FR]  Albi", nomIdPartieCG.obtNomPartie());
        assertEquals(560237916, nomIdPartieCG.obtIdPartie());

        Document documentTKO = obtDocumentTKO();
        NomIdPartieBetclic nomIdPartieTKO = extracteurBetclic.extraireNomIdPartie(documentTKO);
        assertEquals("KO", nomIdPartieTKO.obtNomPartie());
        assertEquals(892334356, nomIdPartieTKO.obtIdPartie());
    }

    @Test
    void extraireBigBlinde() throws Exception {
        Document documentCG = obtDocumentCashGame();
        BigDecimal montantBB = extracteurBetclic.extraireBigBlind(documentCG);

        BigDecimal montantAttendu = new BigDecimal("0.02");
        assertEquals(0, montantAttendu.compareTo(montantBB));
    }

    @Test
    void extraireTotalBuyIn() throws Exception {
        Document documentTKO = obtDocumentTKO();
        BigDecimal buyInTko = extracteurBetclic.extraireTotalBuyIn(documentTKO);

        BigDecimal montantAttendu = new BigDecimal("3");
        assertEquals(0, montantAttendu.compareTo(buyInTko));
    }

    @Test
    void extraireDate() throws Exception {
        Document documentTKO = obtDocumentTKO();
        LocalDateTime dateTKO = extracteurBetclic.extraireDate(documentTKO);

        LocalDateTime dateAttendue = LocalDateTime.of(2024, 1, 25, 20, 44, 45);
        assertEquals(dateAttendue, dateTKO);
    }

    @Test
    void extraireNomHero() throws Exception {
        Document documentTKO = obtDocumentTKO();
        String nomHero = extracteurBetclic.extraireNomHero(documentTKO);

        assertEquals("paricilamone", nomHero);
    }

    @Test
    void extraireIdMain() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element main = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        long idMain = extracteurBetclic.extraireIdMain(main);
        assertEquals(7242221545L, idMain);
    }

    @Test
    void extraireMontantBB() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element mainTKO = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        BigDecimal montantBBTKO = extracteurBetclic.extraireMontantBB(documentTKO, mainTKO);
        BigDecimal montantAttendu = new BigDecimal("800");
        assertEquals(0, montantAttendu.compareTo(montantBBTKO));

        Document documentCG = obtDocumentCashGame();
        Element mainCG = (Element) extracteurBetclic.extraireMains(documentCG).item(0);
        BigDecimal montantCG = extracteurBetclic.extraireMontantBB(documentCG, mainCG);
        BigDecimal montantAttenduCG = new BigDecimal("0.02");
        assertEquals(0, montantAttenduCG.compareTo(montantCG));
    }

    @Test
    void extraireInfosJoueurs() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element mainTKO = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        Element joueurTKO = (Element) extracteurBetclic.extraireJoueurs(mainTKO).item(0);
        InfosJoueurBetclic infosJoueurBetclic = extracteurBetclic.extraireInfoJoueurs(joueurTKO);

        assertEquals("Miclau21", infosJoueurBetclic.obtJoueur());
        BigDecimal stackAttendu = new BigDecimal(8215);
        assertEquals(0, stackAttendu.compareTo(infosJoueurBetclic.obtStack()));
        BigDecimal gainAttendu = new BigDecimal(0);
        assertEquals(0, gainAttendu.compareTo(infosJoueurBetclic.obtGains()));
        assertEquals(5, infosJoueurBetclic.obtSiege());
        assertFalse(infosJoueurBetclic.obtDealer());
    }

    @Test
    void extraireInfoTour() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element premiereMain = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        NodeList tours = extracteurBetclic.extraireTours(premiereMain);

        Element blindeTour = (Element) tours.item(0);
        InfosTourBetclic infosBlinde = extracteurBetclic.extraireInfoTour(blindeTour);
        assertEquals(TourPoker.RoundPoker.BLINDES, infosBlinde.obtRoundPoker());
        assertTrue(infosBlinde.obtCartesExtraites().isEmpty());

        Element flopTour = (Element) tours.item(2);
        InfosTourBetclic infosFlop = extracteurBetclic.extraireInfoTour(flopTour);
        assertEquals(TourPoker.RoundPoker.FLOP, infosFlop.obtRoundPoker());

        List<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('7', 'd'));
        cartesAttendues.add(new CartePoker('4', 'c'));
        cartesAttendues.add(new CartePoker('3', 'h'));

        assertEquals(cartesAttendues, infosFlop.obtCartesExtraites());
    }

    @Test
    void extraireBlindeOuAnte() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element premiereMain = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        NodeList tours = extracteurBetclic.extraireTours(premiereMain);

        Element blindeTour = (Element) tours.item(0);
        NodeList actionsElements = extracteurBetclic.extraireBlindesouAntes(blindeTour);

        Element ante = (Element) actionsElements.item(0);
        BlindeOuAnte anteExtraite = extracteurBetclic.extraireBlindeOuAnte(ante);
        assertTrue(anteExtraite.isAnte());
        assertEquals("Braklare2107", anteExtraite.getNomJoueur());
        BigDecimal montantAnte = new BigDecimal(90);
        assertEquals(0, montantAnte.compareTo(anteExtraite.obtMontant()));

        Element blinde = (Element) actionsElements.item(4);
        BlindeOuAnte blindeExtraire = extracteurBetclic.extraireBlindeOuAnte(blinde);
        assertTrue(blindeExtraire.isBlinde());
        assertEquals("Braklare2107", blindeExtraire.getNomJoueur());
        BigDecimal montantBlinde = new BigDecimal(400);
        assertEquals(0, montantBlinde.compareTo(blindeExtraire.obtMontant()));
    }

    @Test
    void extraireCartes() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element secondeMain = (Element) extracteurBetclic.extraireMains(documentTKO).item(1);
        NodeList tours = extracteurBetclic.extraireTours(secondeMain);

        Element preflopElement = (Element) tours.item(1);
        NodeList cartesNoeuds = extracteurBetclic.extraireNoeudsCartes(preflopElement);

        Element cartesVides = (Element) cartesNoeuds.item(0);
        CartesJoueur cartesJoueur1 = extracteurBetclic.extraireCartes(cartesVides);
        assertEquals("BABAT13", cartesJoueur1.obtNomJoueur());
        assertTrue(cartesJoueur1.obtCartes().isEmpty());

        Element cartesExistantes = (Element) cartesNoeuds.item(1);
        CartesJoueur cartesJoueur2 = extracteurBetclic.extraireCartes(cartesExistantes);
        assertEquals("Miclau21", cartesJoueur2.obtNomJoueur());

        ArrayList<CartePoker> cartesAttendues = new ArrayList<>();
        cartesAttendues.add(new CartePoker('K', 's'));
        cartesAttendues.add(new CartePoker('3', 's'));

        assertEquals(cartesAttendues, cartesJoueur2.obtCartes());
    }

    @Test
    void extraireActionFold() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element premiereMain = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        NodeList tours = extracteurBetclic.extraireTours(premiereMain);

        Element preflopTour = (Element) tours.item(1);
        NodeList listeActions = extracteurBetclic.extraireListeActions(preflopTour);

        Element actionFold = (Element) listeActions.item(1);
        ActionPokerJoueur actionPokerJoueur = extracteurBetclic.extraireAction(actionFold);

        assertEquals(ActionPoker.TypeAction.FOLD, actionPokerJoueur.getTypeAction());
        BigDecimal montantAttendu = new BigDecimal(0);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));
        assertFalse(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionCall() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element premiereMain = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        NodeList tours = extracteurBetclic.extraireTours(premiereMain);

        Element preflopTour = (Element) tours.item(1);
        NodeList listeActions = extracteurBetclic.extraireListeActions(preflopTour);

        Element actionCall = (Element) listeActions.item(0);
        ActionPokerJoueur actionPokerJoueur = extracteurBetclic.extraireAction(actionCall);

        assertEquals(ActionPoker.TypeAction.CALL, actionPokerJoueur.getTypeAction());
        BigDecimal montantAttendu = new BigDecimal(800);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));
        assertFalse(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionCheck() throws Exception {
        Document documentTKO = obtDocumentTKO();
        Element premiereMain = (Element) extracteurBetclic.extraireMains(documentTKO).item(0);
        NodeList tours = extracteurBetclic.extraireTours(premiereMain);

        Element preflopTour = (Element) tours.item(1);
        NodeList listeActions = extracteurBetclic.extraireListeActions(preflopTour);

        Element actionCheck = (Element) listeActions.item(3);
        ActionPokerJoueur actionPokerJoueur = extracteurBetclic.extraireAction(actionCheck);

        assertEquals(ActionPoker.TypeAction.CHECK, actionPokerJoueur.getTypeAction());
        BigDecimal montantAttendu = new BigDecimal(0);
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));
        assertFalse(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionRaise() throws Exception {
        Document documentCG = obtDocumentCashGame2();
        Element premiereMain = (Element) extracteurBetclic.extraireMains(documentCG).item(0);
        NodeList tours = extracteurBetclic.extraireTours(premiereMain);

        Element preflopTour = (Element) tours.item(1);
        NodeList listeActions = extracteurBetclic.extraireListeActions(preflopTour);

        Element actionCheck = (Element) listeActions.item(0);
        ActionPokerJoueur actionPokerJoueur = extracteurBetclic.extraireAction(actionCheck);

        assertEquals("thomaslensois80", actionPokerJoueur.getNomJoueur());
        assertEquals(ActionPoker.TypeAction.RAISE, actionPokerJoueur.getTypeAction());
        BigDecimal montantAttendu = new BigDecimal("0.04");
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));
        assertTrue(actionPokerJoueur.estMontantTotal());
    }

    @Test
    void extraireActionPremierRaise() throws Exception {
        Document documentCG = obtDocumentCashGame2();
        Element secondeMain = (Element) extracteurBetclic.extraireMains(documentCG).item(1);
        NodeList tours = extracteurBetclic.extraireTours(secondeMain);

        Element flopTour = (Element) tours.item(2);
        NodeList listeActions = extracteurBetclic.extraireListeActions(flopTour);

        Element actionCheck = (Element) listeActions.item(0);
        ActionPokerJoueur actionPokerJoueur = extracteurBetclic.extraireAction(actionCheck);

        assertEquals("thomaslensois80", actionPokerJoueur.getNomJoueur());
        assertEquals(ActionPoker.TypeAction.RAISE, actionPokerJoueur.getTypeAction());
        BigDecimal montantAttendu = new BigDecimal("0.14");
        assertEquals(0, montantAttendu.compareTo(actionPokerJoueur.obtMontantAction()));
        assertTrue(actionPokerJoueur.estMontantTotal());
    }


    private Document obtDocumentTKO() throws Exception {
        String nomFichier = "5359260503.xml";
        return ouvrirDocumentXml(nomFichier);
    }

    private Document obtDocumentCashGame() throws Exception {
        String nomFichier = "5359262849.xml";
        return ouvrirDocumentXml(nomFichier);
    }

    private Document obtDocumentTwister() throws Exception {
        String nomFichier = "5420696596.xml";
        return ouvrirDocumentXml(nomFichier);
    }

    private Document obtDocumentCashGame2() throws Exception {
        String nomFichier = "5359263028.xml";
        return ouvrirDocumentXml(nomFichier);
    }

    private Document ouvrirDocumentXml(String nomFichier) throws Exception {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/betclic/" + nomFichier)).toURI());
        File xmlFile = new File(String.valueOf(cheminRepertoire));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(xmlFile);
    }
}
