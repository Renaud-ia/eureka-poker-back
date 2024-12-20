package fr.eurekapoker.parties.integration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiImportBetclicTest extends ApiImportTestModele {
    public ApiImportBetclicTest() {
        super("betclic");
    }

    @Test
    void creationConsultationTournoiKO() throws Exception {
        String nomFichier = "5359260503.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("MTT 6-max 3€ (KO)", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(4, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("800");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    void creationConsultationCashGame() throws Exception {
        String nomFichier = "5359262849.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Cash-Game 6-max NL2 ([FR] Albi)", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(22, jsonConsultation.get("nombreMains"));
        assertEquals(true, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("0.02");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    void creationConsultationCashGame2() throws Exception {
        String nomFichier = "5359263028.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Cash-Game 2-max NL2 (Adelaide)", jsonConsultation.get("nomPartie"));
        assertEquals(2, jsonConsultation.get("nombreSieges"));
        assertEquals(54, jsonConsultation.get("nombreMains"));
        assertEquals(true, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("0.02");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    void creationConsultationTwister() throws Exception {
        String nomFichier = "5420696596.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 5€ (Twister)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(27, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("20");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void donneesSortiesApiActionSontBonnes() throws Exception {
        String nomFichier = "5359263028.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        JSONObject main28 = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(28);
        JSONObject tourPreflop = main28.getJSONArray("tours").getJSONObject(0);
        assertEquals("[]", tourPreflop.getJSONArray("board").toString());

        JSONArray actionsPreflop = tourPreflop.getJSONArray("actions");

        JSONObject premiereActionPf = actionsPreflop.getJSONObject(0);
        verifierAction(premiereActionPf, "thomaslensois80", "RAISE", 0.04, 0.04, 4.75, 0.06, false);

        JSONObject secondeActionPf = actionsPreflop.getJSONObject(1);
        verifierAction(secondeActionPf, "paricilamone", "CALL", 0.04, 0.04, 2.37, 0.08, false);

        // FLOP
        JSONObject tourFlop = main28.getJSONArray("tours").getJSONObject(1);
        assertEquals("[\"4c\",\"Td\",\"2h\"]", tourFlop.getJSONArray("board").toString());
        JSONArray actionsFlop = tourFlop.getJSONArray("actions");

        JSONObject premiereActionFlop = actionsFlop.getJSONObject(0);
        verifierAction(premiereActionFlop, "paricilamone", "CHECK", 0, 0, 2.37, 0.08, false);

        JSONObject secondeActionFlop = actionsFlop.getJSONObject(1);
        verifierAction(secondeActionFlop, "thomaslensois80", "CHECK", 0, 0, 4.75, 0.08, false);

        JSONArray actionsRiver = main28.getJSONArray("tours").getJSONObject(3).getJSONArray("actions");

        JSONObject premiereActionRiver = actionsRiver.getJSONObject(0);
        verifierAction(premiereActionRiver, "paricilamone", "RAISE", 0.12, 0.12, 2.25, 0.20, false);

        JSONObject secondeActionRiver = actionsRiver.getJSONObject(1);
        verifierAction(secondeActionRiver, "thomaslensois80", "RAISE", 2.12, 2.12, 2.63, 2.32, false);

        JSONObject troisiemeActionRiver = actionsRiver.getJSONObject(2);
        verifierAction(troisiemeActionRiver, "paricilamone", "FOLD", 0, 0.12, 2.25, 2.32, false);
    }

    @Test
    public void donneesSortiesApiJoueursSontBonnes() throws Exception {
        String nomFichier = "5359262849.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(0);
        JSONArray joueurs = premiereMain.getJSONArray("joueurs");

        assertEquals(5, joueurs.length());

        verifierJoueurPresent(joueurs, "Player 1", 1.23, true);
        verifierJoueurPresent(joueurs, "Player 3", 1.33, false);
        verifierJoueurPresent(joueurs, "Player 5", 1.76, false);
        verifierJoueurPresent(joueurs, "Player 8", 1.05, false);
        verifierJoueurPresent(joueurs, "Player 10", 2.03, false);
    }

    @Test
    void donneesCartesCommunesSontBonnes() throws Exception {
        String nomFichier = "5362734143.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(0);

        JSONObject preflop = premiereMain.getJSONArray("tours").getJSONObject(0);
        assertEquals("[]", preflop.getString("board"));

        JSONObject flop = premiereMain.getJSONArray("tours").getJSONObject(1);
        assertEquals("[\"7c\",\"4s\",\"9d\"]", flop.getString("board"));

        JSONObject turn = premiereMain.getJSONArray("tours").getJSONObject(2);
        assertEquals("[\"7c\",\"4s\",\"9d\",\"Jc\"]", turn.getString("board"));

        JSONObject river = premiereMain.getJSONArray("tours").getJSONObject(3);
        assertEquals("[\"7c\",\"4s\",\"9d\",\"Jc\",\"Qh\"]", river.getString("board"));
    }


 }
