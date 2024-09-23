package fr.eurekapoker.parties.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eurekapoker.parties.api.RequeteImport;
import fr.eurekapoker.parties.application.api.dto.ContenuMainDto;
import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.JoueurDto;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiImportEndToEndWinamaxTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSimpleAjouterPartie() throws Exception {
        String nomFichier = "20201207_Aalen 47_real_holdem_no-limit.txt";
        JSONObject result = creerPartie(nomFichier);
        assertNotNull(result.get("idUniquePartie"), "idUniquePartie ne doit pas être nul après création");
    }

    @Test
    public void consultationPartieCreeCashGame() throws Exception {
        String nomFichier = "20201207_Aalen 47_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Cash-Game 5-max NL2 (Aalen 47)", jsonConsultation.get("nomPartie"));
        assertEquals(5, jsonConsultation.get("nombreSieges"));
        assertEquals(7, jsonConsultation.get("nombreMains"));
    }

    @Test
    public void consultationPartieCreeMtt() throws Exception {
        String nomFichier = "20200901_WESTERN(384468080)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("MTT 6-max 1€ (WESTERN)", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(21, jsonConsultation.get("nombreMains"));
    }

    @Test
    public void consultationPartieCreeShortTrack() throws Exception {
        String nomFichier = "20200513_SHORT TRACK 0,25 € 05_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Cash-Game 5-max 0.25€ (Short Track)", jsonConsultation.get("nomPartie"));
        assertEquals(5, jsonConsultation.get("nombreSieges"));
        assertEquals(9, jsonConsultation.get("nombreMains"));
    }

    @Test
    public void consultationPartieCreeExpresso() throws Exception {
        String nomFichier = "20230318_Expresso(645339353)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 2€ (Expresso)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(23, jsonConsultation.get("nombreMains"));
    }

    @Test
    public void consultationPartieCreeExpressoNitro() throws Exception {
        String nomFichier = "20210302_Expresso Nitro(442878131)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("Spin&Go 3-max 1€ (Expresso Nitro)", jsonConsultation.get("nomPartie"));
        assertEquals(3, jsonConsultation.get("nombreSieges"));
        assertEquals(2, jsonConsultation.get("nombreMains"));
    }

    /*
    @Test
    public void consultationPartieCreeStartingBlock() throws Exception {
        String nomFichier = "20230909_Starting Block WiPT - Déglingos(696549662)_real_holdem_no-limit.txt";
        JSONObject jsonCreation = creerPartie(nomFichier);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        assertEquals("MTT Starting block WiPT - Déglingos", jsonConsultation.get("nomPartie"));
        assertEquals(6, jsonConsultation.get("nombreSieges"));
        assertEquals(11, jsonConsultation.get("nombreMains"));
    }

     */

    private JSONObject consulterPartie(String idUniquePartie) throws Exception {
        int indexPremiereMain = 0;
        int fenetreConsultation = 10;
        MvcResult result = mockMvc.perform(get("/parties/" + idUniquePartie)
                        .param("indexPremiereMain", String.valueOf(indexPremiereMain))
                        .param("fenetreConsultation", String.valueOf(fenetreConsultation)))
                .andExpect(status().isOk()) // Vérifier que la réponse est 200 OK
                .andReturn(); // Retourner le résultat

        // Traitement du contenu de la réponse
        String content = result.getResponse().getContentAsString();

        return new JSONObject(content);
    }

    private JSONObject creerPartie(String nomFichier) throws Exception {
        String contenuPartie = lireContenuFichier(nomFichier);

        ParametresImport parametresImport = new ParametresImport(false);
        RequeteImport requeteImport = new RequeteImport(contenuPartie, parametresImport);

        ObjectMapper objectMapper = new ObjectMapper();
        String requeteImportJson = objectMapper.writeValueAsString(requeteImport);

        MvcResult result = mockMvc.perform(post("/parties/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requeteImportJson))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return new JSONObject(content);
    }

    private String lireContenuFichier(String nomFichier) throws URISyntaxException, IOException {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/winamax/" + nomFichier)).toURI());
        return Files.readString(cheminRepertoire);
    }
}