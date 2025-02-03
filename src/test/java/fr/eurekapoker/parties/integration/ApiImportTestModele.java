package fr.eurekapoker.parties.integration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiImportTestModele {
    @Autowired
    private MockMvc mockMvc;

    protected final String nomDossierRoom;

    public ApiImportTestModele(String nomDossierRoom) {
        this.nomDossierRoom = nomDossierRoom;
    }

    public List<String> parcourirLesFichiers() throws Exception {
        String cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/" + nomDossierRoom + "/")).toURI()).toString();

        return Files.list(Paths.get(cheminRepertoire))
                .map(path -> path.getFileName().toString()) // Récupérer juste le nom du fichier
                .collect(Collectors.toList());
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

    protected JSONObject creerPartie(String nomFichier, boolean joueursAnonymes) throws Exception {
        String contenuPartie = lireContenuFichier(nomFichier);
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

    protected String lireContenuFichier(String nomFichier) throws URISyntaxException, IOException {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/" + nomDossierRoom + "/" + nomFichier)).toURI());
        return Files.readString(cheminRepertoire);
    }

    protected void verifierAction(JSONObject action,
                                String nomJoueur,
                                String nomAction,
                                  double montant,
                                double montantInvesti,
                                  double stackActuel,
                                  double montantPot,
                                boolean estAllIn) throws JSONException {
        assertEquals(nomJoueur, action.getString("nomJoueur"));
        assertEquals(nomAction, action.getString("action"));
        assertEquals(montant, action.getDouble("montant"),  1e-3);
        assertEquals(montantInvesti, action.getDouble("montantInvesti"),  1e-3);
        assertEquals(stackActuel, action.getDouble("stackActuel"),  1e-3);
        assertEquals(montantPot, action.getDouble("montantPot"),  1e-3);
        assertEquals(estAllIn, action.getBoolean("estAllIn"));
    }

    protected void verifierJoueurPresent(JSONArray joueurs, String nomJoueur, double stackDepart, boolean dealer) throws Exception {
        for (int i = 0; i < joueurs.length(); i++) {
            JSONObject joueur = joueurs.getJSONObject(i);
            if (Objects.equals(joueur.getString("nomJoueur"), nomJoueur)) {
                assertEquals(stackDepart, joueur.getDouble("stackDepart"));
                assertEquals(dealer, joueur.getBoolean("dealer"));
                return;
            }
        }

        throw new Exception("Joueur non trouvé :" + nomJoueur);
    }
}
