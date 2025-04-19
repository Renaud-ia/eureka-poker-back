package fr.eurekapoker.parties.e2e.imports;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiImportParionsSportTest extends ApiImportTestModele {
    public ApiImportParionsSportTest() {
        super("parionssport");
    }

    @Test
    void creationConsultationFlashTwister() throws Exception {
        String nomFichier = "5658765427.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 5€ (Flash Twister)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(17, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("20");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }

    @Test
    void creationConsultationFlashTwisterBis() throws Exception {
        String nomFichier = "5658768241.xml";
        JSONObject jsonCreation = creerPartie(nomFichier, false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 5€ (Flash Twister)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(17, jsonConsultation.get("nombreMains"));
        assertEquals(false, jsonConsultation.get("stackEnEuros"));

        JSONArray mains = jsonConsultation.getJSONArray("mainsExtraites");
        BigDecimal montantBBPremiereMain = new BigDecimal("20");
        JSONObject premiereMain = mains.getJSONObject(0);
        assertEquals(0, montantBBPremiereMain.compareTo(new BigDecimal(premiereMain.get("montantBB").toString())));
    }
}
