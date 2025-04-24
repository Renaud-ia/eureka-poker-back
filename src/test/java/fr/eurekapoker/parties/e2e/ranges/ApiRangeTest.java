package fr.eurekapoker.parties.e2e.ranges;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eurekapoker.parties.api.requetes.RequeteRange;
import fr.eurekapoker.parties.builders.UtilisateurAuthentifieTestBuilder;
import fr.eurekapoker.parties.builders.UtilisateurIdentifieTestBuilder;
import fr.eurekapoker.parties.e2e.BaseTestE2E;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiRangeTest extends BaseTestE2E {
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().avecUtilisateurAuthentifie(
                        new UtilisateurAuthentifieTestBuilder().avecEmail("autorized_ranges@toto.fr").build()
                ).build());
    }

    @Test
    void autoCreationRangeVide() throws Exception {
        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        String idAction = extraireIdAction(jsonConsultation, 0, 0, 0);

        assertNotNull(idAction);

        MvcResult result = mockMvc.perform(get("/ranges/" + idAction))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject rangeUser = extraireRangeUser(result);
        assertEquals(0, rangeUser.length());
    }

    @Test
    void modifierRangeSimple() throws Exception {
        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        String idAction = extraireIdAction(jsonConsultation, 0, 0, 0);
        assertNotNull(idAction);

        RequeteRange requete = new RequeteRange();
        requete.setTypeRange("PREFLOP");

        HashMap<String, Float> combos = new HashMap<>();
        combos.put("22", 0.8f);

        requete.setCombos(combos);

        // 2. Sérialise en JSON
        String jsonBody = objectMapper.writeValueAsString(requete);

        // 3. Envoie le PUT avec MockMvc
        mockMvc.perform(put("/ranges/" + idAction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/ranges/" + idAction))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject rangeUserMains = extraireRangeUserMains(result);
        assertEquals(1, rangeUserMains.length());

        Double frequence22 = rangeUserMains.getDouble("22");
        assertEquals(0.8, frequence22);
    }

    @Test
    void appliqueDiminutionFrequence() throws Exception {
        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        String idAction = extraireIdAction(jsonConsultation, 2, 0, 0);
        assertNotNull(idAction);
        RequeteRange requete = new RequeteRange();
        requete.setTypeRange("PREFLOP");
        HashMap<String, Float> combos = new HashMap<>();
        combos.put("22", 0.5f);
        requete.setCombos(combos);
        // 2. Sérialise en JSON
        String jsonBody = objectMapper.writeValueAsString(requete);
        // 3. Envoie le PUT avec MockMvc
        mockMvc.perform(put("/ranges/" + idAction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());


        idAction = extraireIdAction(jsonConsultation, 2, 0, 5);
        requete = new RequeteRange();
        requete.setTypeRange("PREFLOP");
        combos = new HashMap<>();
        combos.put("22", 0.8f);
        requete.setCombos(combos);
        // 2. Sérialise en JSON
        jsonBody = objectMapper.writeValueAsString(requete);
        // 3. Envoie le PUT avec MockMvc
        mockMvc.perform(put("/ranges/" + idAction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/ranges/" + idAction))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject rangeUserMains = extraireRangeUserMains(result);
        assertEquals(1, rangeUserMains.length());

        Double frequence22 = rangeUserMains.getDouble("22");
        assertEquals(0.5, frequence22);
    }

    @Test
    void refuseModificationNonProprietaire() throws Exception {
        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        String idAction = extraireIdAction(jsonConsultation, 0, 0, 0);
        assertNotNull(idAction);

        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().avecUtilisateurAuthentifie(
                        new UtilisateurAuthentifieTestBuilder().avecEmail("unautorized_ranges@toto.fr").build()
                ).build());

        RequeteRange requete = new RequeteRange();
        requete.setTypeRange("PREFLOP");

        HashMap<String, Float> combos = new HashMap<>();
        combos.put("22", 0.8f);

        requete.setCombos(combos);

        // 2. Sérialise en JSON
        String jsonBody = objectMapper.writeValueAsString(requete);

        // 3. Envoie le PUT avec MockMvc
        mockMvc.perform(put("/ranges/" + idAction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isUnauthorized());
    }

    private JSONObject extraireRangeUserCombos(MvcResult result) throws JSONException, UnsupportedEncodingException {
        JSONObject rangeUser = extraireRangeUser(result);
        JSONObject rangeUserCombos = rangeUser.getJSONObject("combos");

        return rangeUserCombos;
    }

    private JSONObject extraireRangeUserMains(MvcResult result) throws JSONException, UnsupportedEncodingException {
        JSONObject rangeUser = extraireRangeUser(result);
        JSONObject rangeUserMains = rangeUser.getJSONObject("mains");

        return rangeUserMains;
    }

    private JSONObject extraireRangeUser(MvcResult result) throws JSONException, UnsupportedEncodingException {
        JSONObject contentReponse = new JSONObject(result.getResponse().getContentAsString());
        JSONObject ranges = contentReponse.getJSONObject("ranges");

        assertTrue(ranges.has("SAISIE_USER"));
        JSONObject rangeUser = ranges.getJSONObject("SAISIE_USER");

        return rangeUser;
    }

    private String extraireIdAction(JSONObject jsonConsultation, int indexMain, int indexTour, int indexAction) throws JSONException {
        JSONObject premiereMain = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(indexMain);
        JSONObject premiereAction =
                premiereMain.getJSONArray("tours")
                        .getJSONObject(indexTour)
                        .getJSONArray("actions")
                        .getJSONObject(indexAction);

        String idAction = premiereAction.getString("idAction");

        return idAction;
    }
}
