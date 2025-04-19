package fr.eurekapoker.parties.e2e.imports;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiImportPmuTest extends ApiImportTestModele {
    public ApiImportPmuTest() {
        super("pmu");
    }

    @Test
    public void consultationPartieCreeCashGame() throws Exception {
        String nomFichier = "Caen.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Cash-Game 6-max NL2 (Caen)", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(57, jsonConsultation.get("nombreMains"));
        assertEquals(true, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("0.02");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieCreeMtt() throws Exception {
        String nomFichier = "The Major Phased Sat. 25 x 50€ Gtd (Phase 1, Hyper) (399524782) Table #1.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("MTT 6-max 1€ (The Major Phased Sat. 25 x 50€ Gtd)", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(5, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("800");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    public void consultationPartieCreeSpin() throws Exception {
        String nomFichier = "10€ SPINS (402535770) Table #1.txt";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 10€ (SPINS)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(24, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("20");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }
}
