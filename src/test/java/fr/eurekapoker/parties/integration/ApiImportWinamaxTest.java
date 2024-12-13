package fr.eurekapoker.parties.integration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ApiImportWinamaxTest extends ApiImportTestModele {
    @Autowired
    private MockMvc mockMvc;

    public ApiImportWinamaxTest() {
        super("winamax");
    }

    @Test
    public void testSimpleAjouterPartie() throws Exception {
        String nomFichier = "20201207_Aalen 47_real_holdem_no-limit.txt";
        JSONObject result = creerPartie(nomFichier, false);
        assertNotNull(result.get("idUniquePartie"), "idUniquePartie ne doit pas être nul après création");
    }

    @Test
    public void consultationPartieCreeCashGame() throws Exception {
        String nomFichier = "20201207_Aalen 47_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Cash-Game 5-max NL2 (Aalen 47)", jsonConsultation.get("nomPartie"));
        assertEquals(5, jsonConsultation.get("nombreSieges"));
        assertEquals(7, jsonConsultation.get("nombreMains"));
        assertEquals(true, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("0.02");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieCreeMtt() throws Exception {
        String nomFichier = "20200901_WESTERN(384468080)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("MTT 6-max 1€ (WESTERN)", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(21, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("600");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieCreeShortTrack() throws Exception {
        String nomFichier = "20200513_SHORT TRACK 0,25 € 05_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        System.out.println(jsonConsultation);

        assertEquals("Cash-Game 5-max 0.25€ (Short Track)", jsonConsultation.get("nomPartie"));
        assertEquals(5, jsonConsultation.get("nombreSieges"));
        assertEquals(9, jsonConsultation.get("nombreMains"));
        assertEquals(true, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("0.05");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieCreeExpresso() throws Exception {
        String nomFichier = "20230318_Expresso(645339353)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 2€ (Expresso)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(23, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("20");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieCreeExpressoNitro() throws Exception {
        String nomFichier = "20210302_Expresso Nitro(442878131)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 1€ (Expresso Nitro)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(2, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("20");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieCreeStartingBlock() throws Exception {
        String nomFichier = "20230909_Starting Block WiPT - Déglingos(696549662)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("MTT 6-max 0€ (Starting Block WiPT - Déglingos)", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(11, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("20");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieJoueursAnonymes() throws Exception {
        String nomFichier = "20210302_Expresso Nitro(442878131)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, true);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 1€ (Expresso Nitro)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(2, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(0);
        JSONArray joueurs = premiereMain.getJSONArray("joueurs");

        for (int i = 0; i < (int) jsonConsultation.get("nombreSieges"); i++) {
            JSONObject joueur = joueurs.getJSONObject(i);
            String nomJoueur = joueur.getString("nomJoueur");
            assertTrue(nomJoueur.startsWith("Hero") || nomJoueur.startsWith("Villain"), nomJoueur);
        }
    }

    @Test
    public void donneesSortiesApiActionSontBonnes() throws Exception {
        String nomFichier = "tournoi_3_bet.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(0);
        JSONArray actionsPreflop = premiereMain.getJSONArray("tours").getJSONObject(0).getJSONArray("actions");

        JSONObject premiereActionPf = actionsPreflop.getJSONObject(0);
        verifierAction(premiereActionPf, "AlexisSR7", "RAISE", 875, 875, 16327, 1640, false);

        JSONObject secondeActionPf = actionsPreflop.getJSONObject(1);
        verifierAction(secondeActionPf, "Fonkytilta", "CALL", 875, 875, 17027, 2515, false);

        JSONObject troisiemeActionPf = actionsPreflop.getJSONObject(2);
        verifierAction(troisiemeActionPf, "juste1doigt", "FOLD", 0, 0, 19114, 2515, false);

        JSONObject sixiemeActionPf = actionsPreflop.getJSONObject(5);
        verifierAction(sixiemeActionPf, "jveudlamone", "FOLD", 0, 350, 29045, 2515, false);


        JSONArray actionsFlop = premiereMain.getJSONArray("tours").getJSONObject(1).getJSONArray("actions");

        JSONObject premiereActionFlop = actionsFlop.getJSONObject(0);
        verifierAction(premiereActionFlop, "AlexisSR7", "CHECK", 0, 0, 16327, 2515, false);

        JSONObject secondeActionFlop = actionsFlop.getJSONObject(1);
        verifierAction(secondeActionFlop, "Fonkytilta", "RAISE", 1258, 1258, 15769, 3773, false);

        JSONObject troisiemeActionFlop = actionsFlop.getJSONObject(2);
        verifierAction(troisiemeActionFlop, "AlexisSR7", "RAISE", 2641, 2641, 13686, 6414, false);

        JSONObject quatriemeActionFlop = actionsFlop.getJSONObject(3);
        verifierAction(quatriemeActionFlop, "Fonkytilta", "RAISE", 17027, 17027, 0, 22183, true);

        JSONObject cinquiemeActionFlop = actionsFlop.getJSONObject(4);
        verifierAction(cinquiemeActionFlop, "AlexisSR7", "CALL", 16327, 16327, 0, 35869, true);
    }


}