package fr.eurekapoker.parties.domaine.parsing.xml.extracteur;

import fr.eurekapoker.parties.domaine.parsing.dto.NomIdPartieBetclic;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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
        BigDecimal montantBBTKO = extracteurBetclic.extraireMontantBB(mainTKO);
        BigDecimal montantAttendu = new BigDecimal("800");
        assertEquals(0, montantAttendu.compareTo(montantBBTKO));

        // pas de montant BB pour CG
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

    private Document ouvrirDocumentXml(String nomFichier) throws Exception {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/betclic/" + nomFichier)).toURI());
        File xmlFile = new File(String.valueOf(cheminRepertoire));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(xmlFile);
    }
}
