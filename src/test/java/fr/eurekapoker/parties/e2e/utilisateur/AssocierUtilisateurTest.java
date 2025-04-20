package fr.eurekapoker.parties.e2e.utilisateur;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.builders.UtilisateurIdentifieTestBuilder;
import fr.eurekapoker.parties.e2e.BaseTestE2E;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AssocierUtilisateurTest extends BaseTestE2E {
    @Test
    void associerUtilisateurSession() throws Exception {
        String tokenSession = "session test";
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder()
                        .avecIdentifiantSession(tokenSession)
                        .avecUtilisateurAuthentifie(
                                null
                        ).build());

        creerPartie("pmu", "Caen.txt", false);

        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder()
                        .avecIdentifiantSession(tokenSession)
                        .build());

        mockMvc.perform(patch("/utilisateur/" + tokenSession)
                        .header("Authorization", "Bearer xxx") // ou ce que tu attends dans les headers
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void modificationNonAutoriseeDejaAssocie() throws Exception {
        String tokenSession = "session test";
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder()
                        .avecIdentifiantSession(tokenSession)
                        .avecUtilisateurAuthentifie(
                                null
                        ).build());

        creerPartie("pmu", "Caen.txt", false);

        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder()
                        .avecIdentifiantSession(tokenSession)
                        .build());

        mockMvc.perform(patch("/utilisateur/" + tokenSession)
                        .header("Authorization", "Bearer xxx") // ou ce que tu attends dans les headers
                )
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/utilisateur/" + tokenSession)
                        .header("Authorization", "Bearer xxx") // ou ce que tu attends dans les headers
                )
                .andExpect(status().isUnauthorized());
    }
}
