package fr.eurekapoker.parties.e2e;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.builders.UtilisateurIdentifieTestBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BaseTestE2E {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtDecoder jwtDecoder;

    @MockBean
    protected AuthService authService;

    @BeforeEach
    void setUp() {
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().build());
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
