package fr.eurekapoker.parties.e2e.notes;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.builders.UtilisateurIdentifieTestBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ModiferNoteTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    protected AuthService authService;

    @BeforeEach
    void setUp() {
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().build());
    }

    @Test
    void modifierNotes() throws Exception {
        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        String nomJoueur = "H-Savoie74";
        String idJoueur = this.extraireIdJoueur(jsonConsultation, 1, nomJoueur);

        assertNotNull(idJoueur);

        String notesNouvelles = "Ce joueur est très passif postflop";
        String json = "{ \"nouvellesNotes\": \"" + notesNouvelles + "\" }";
        mockMvc.perform(put("/notes/" + idJoueur)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent()); // Vérifier que la réponse est 200 OK

        jsonConsultation = consulterPartie(idUniquePartie);

        String notesModifiees = this.extraireNotes(jsonConsultation, 1, nomJoueur);

        assertEquals(notesNouvelles, notesModifiees);
    }

    private String extraireNotes(
            JSONObject jsonConsultation,
            int indexMain,
            String nomJoueur
    ) throws Exception {
        JSONObject joueur = trouverJoueur(jsonConsultation, indexMain, nomJoueur);
        return joueur.getString("notesJoueur");
    }

    private String extraireIdJoueur(JSONObject jsonConsultation, int indexMain, String nomJoueur) throws Exception {
        JSONObject joueur = trouverJoueur(jsonConsultation, indexMain, nomJoueur);
        return joueur.getString("idUniqueJoueur");
    }

    private JSONObject trouverJoueur(
            JSONObject jsonConsultation,
            int indexMain,
            String nomJoueur
    ) throws Exception {
        JSONObject main = jsonConsultation.getJSONArray("mainsExtraites").getJSONObject(indexMain);
        JSONArray joueurs = main.getJSONArray("joueurs");

        for (int i = 0; i < joueurs.length(); i++) {
            JSONObject joueur = joueurs.getJSONObject(i);
            String nom = joueur.getString("nomJoueur");

            if (nomJoueur.equals(nom)) {
                return joueur;
            }
        }

        throw new Exception("Joueur non trouvé");
    }


    protected JSONObject consulterPartie(String idUniquePartie) throws Exception {
        int indexPremiereMain = 0;
        int fenetreConsultation = 10;
        MvcResult result = mockMvc.perform(get("/parties/" + idUniquePartie)
                        .param("indexMain", String.valueOf(indexPremiereMain))
                        .param("fenetreConsultation", String.valueOf(fenetreConsultation)))
                .andExpect(status().isOk()) // Vérifier que la réponse est 200 OK
                .andReturn(); // Retourner le résultat

        // Traitement du contenu de la réponse
        String content = result.getResponse().getContentAsString();

        return new JSONObject(content);
    }

    protected JSONObject creerPartie(String nomDossier, String nomFichier, boolean joueursAnonymes) throws Exception {
        String contenuPartie = lireContenuFichier(nomDossier, nomFichier);
        String valeurJoueursAnonymes = joueursAnonymes ? "on" : "off";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/parties/")
                        .file(new MockMultipartFile("fichierUpload", "fichier.txt",
                                MediaType.TEXT_PLAIN_VALUE, contenuPartie.getBytes()))
                        .param("contenuPartie", contenuPartie)
                        .param("joueursAnonymes", valeurJoueursAnonymes)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        return new JSONObject(content);
    }

    protected String lireContenuFichier(String nomDossierRoom, String nomFichier) throws URISyntaxException, IOException {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/" + nomDossierRoom + "/" + nomFichier)).toURI());
        return Files.readString(cheminRepertoire);
    }
}
