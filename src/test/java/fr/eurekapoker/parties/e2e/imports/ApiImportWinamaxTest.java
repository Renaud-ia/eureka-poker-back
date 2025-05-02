package fr.eurekapoker.parties.e2e.imports;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ApiImportWinamaxTest extends ApiImportTestModele {
    public ApiImportWinamaxTest() {
        super("winamax");
    }

    @Test
    public void testSimpleAjouterPartie() throws Exception {
        for (String nomFichier: this.parcourirLesFichiers()) {
            JSONObject result = creerPartie(nomFichier, false);
            assertNotNull(result.get("idUniquePartie"), "idUniquePartie ne doit pas être nul après création");
        }
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
    public void donneesSortiesApiActionSontBonnes3bet() throws Exception {
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

    @Test
    public void donneesSortiesApiActionSontBonnesExpressoNitro() throws Exception {
        String nomFichier = "20210302_Expresso Nitro(442878131)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(0);
        JSONArray actionsPreflop = premiereMain.getJSONArray("tours").getJSONObject(0).getJSONArray("actions");

        JSONObject premiereActionPf = actionsPreflop.getJSONObject(0);
        verifierAction(premiereActionPf, "Mouguy29", "CALL", 20, 20, 280, 50, false);

        JSONObject secondeActionPf = actionsPreflop.getJSONObject(1);
        verifierAction(secondeActionPf, "Olichebo2308", "CALL", 20, 20, 280, 60, false);

        JSONObject troisiemeActionPf = actionsPreflop.getJSONObject(2);
        verifierAction(troisiemeActionPf, "RendsL4rgent", "CHECK", 0, 20, 280, 60, false);
    }

    @Test
    public void donneesSortiesApiSontBonnesAllInMtt() throws Exception {
        String nomFichier = "20210409_LE SUD VS LE RESTE DU MONDE(447551158)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(0);
        assertEquals(1500000, Float.parseFloat(String.valueOf((Double) premiereMain.get("potInitial"))));
        JSONArray actionsPreflop = premiereMain.getJSONArray("tours").getJSONObject(0).getJSONArray("actions");

        JSONObject premiereActionPf = actionsPreflop.getJSONObject(0);
        verifierAction(premiereActionPf, "Hiddenwig14", "FOLD", 0, 0, 51048294, 1500000, false);

        JSONObject secondeActionPf = actionsPreflop.getJSONObject(1);
        verifierAction(secondeActionPf, "RendsL4rgent", "RAISE", 4400000, 4400000, 8172048, 5500000, false);

        JSONObject troisiemeActionPf = actionsPreflop.getJSONObject(2);
        verifierAction(troisiemeActionPf, "benedicimus", "RAISE", 34259658, 34259658, 0, 38959658, true);

        JSONObject quatriemeActionPf = actionsPreflop.getJSONObject(3);
        verifierAction(quatriemeActionPf, "RendsL4rgent", "CALL", 12572048, 12572048, 0, 47131706, true);

        JSONArray tours = premiereMain.getJSONArray("tours");
        assertEquals(4, tours.length());
    }

    @Test
    public void donneesSortiesApiSontBonnesAllInMttAnonymes() throws Exception {
        String nomFichier = "20210409_LE SUD VS LE RESTE DU MONDE(447551158)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, true);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(0);
        assertEquals(Float.parseFloat(String.valueOf((Double) premiereMain.get("potInitial"))), 1500000);
        JSONArray actionsPreflop = premiereMain.getJSONArray("tours").getJSONObject(0).getJSONArray("actions");

        JSONObject premiereActionPf = actionsPreflop.getJSONObject(0);
        verifierAction(premiereActionPf, "Villain1", "FOLD", 0, 0, 51048294, 1500000, false);

        JSONObject secondeActionPf = actionsPreflop.getJSONObject(1);
        verifierAction(secondeActionPf, "Hero", "RAISE", 4400000, 4400000, 8172048, 5500000, false);

        JSONObject troisiemeActionPf = actionsPreflop.getJSONObject(2);
        verifierAction(troisiemeActionPf, "Villain2", "RAISE", 34259658, 34259658, 0, 38959658, true);

        JSONObject quatriemeActionPf = actionsPreflop.getJSONObject(3);
        verifierAction(quatriemeActionPf, "Hero", "CALL", 12572048, 12572048, 0, 47131706, true);
    }


}