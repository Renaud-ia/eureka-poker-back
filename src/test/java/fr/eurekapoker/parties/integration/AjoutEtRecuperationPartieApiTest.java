package fr.eurekapoker.parties.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.eurekapoker.parties.api.RequeteImport;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AjoutEtRecuperationPartieApiTest {
    @Autowired
    private MockMvc mockMvc;

    private String idUniquePartie;

    @Test
    public void testSimpleAjouterPartie() throws Exception {
        String nomFichier = "20211023_Aalen 13_real_holdem_no-limit.txt";
        String contenuPartie = lireContenuFichier(nomFichier);

        ParametresImport parametresImport = new ParametresImport(false);
        RequeteImport requeteImport = new RequeteImport(
                contenuPartie,
                parametresImport
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String requeteImportJson = objectMapper.writeValueAsString(requeteImport);

        MvcResult result = mockMvc.perform(post("/parties/creer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requeteImportJson))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }

    private String lireContenuFichier(String nomFichier) throws URISyntaxException, IOException {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/winamax/" + nomFichier)).toURI());
        return Files.readString(cheminRepertoire);
    }
}
