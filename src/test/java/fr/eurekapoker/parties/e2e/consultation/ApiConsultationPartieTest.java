package fr.eurekapoker.parties.e2e.consultation;

import fr.eurekapoker.parties.builders.UtilisateurAuthentifieTestBuilder;
import fr.eurekapoker.parties.builders.UtilisateurIdentifieTestBuilder;
import fr.eurekapoker.parties.e2e.BaseTestE2E;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiConsultationPartieTest extends BaseTestE2E {
    @Test
    void consultationProprietaire() throws Exception {
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().avecUtilisateurAuthentifie(
                        new UtilisateurAuthentifieTestBuilder().avecEmail("autorized_ranges@toto.fr").build()
                ).build());

        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);
        Boolean proprietaire = jsonConsultation.getBoolean("estProprietaire");

        assertEquals(true, proprietaire);
    }

    @Test
    void consultationNonProprietaire() throws Exception {
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().avecUtilisateurAuthentifie(
                        new UtilisateurAuthentifieTestBuilder().avecEmail("autorized_partie@toto.fr").build()
                ).build());

        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().avecUtilisateurAuthentifie(
                        new UtilisateurAuthentifieTestBuilder().avecEmail("unauthorized_partie@toto.fr").build()
                ).build());

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);
        Boolean proprietaire = jsonConsultation.getBoolean("estProprietaire");

        assertEquals(false, proprietaire);
    }
}
