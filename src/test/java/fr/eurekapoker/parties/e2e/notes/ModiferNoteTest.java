package fr.eurekapoker.parties.e2e.notes;

import fr.eurekapoker.parties.builders.UtilisateurAuthentifieTestBuilder;
import fr.eurekapoker.parties.builders.UtilisateurIdentifieTestBuilder;
import fr.eurekapoker.parties.e2e.BaseTestE2E;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ModiferNoteTest extends BaseTestE2E {

    @Test
    void modifierNotesUserAuthentifie() throws Exception {
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().avecUtilisateurAuthentifie(
                        new UtilisateurAuthentifieTestBuilder().avecEmail("autorized@toto.fr").build()
                ).build());

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

        String notesModifiees = this.extraireNotes(jsonConsultation, idJoueur);

        assertEquals(notesNouvelles, notesModifiees);
    }

    @Test
    void modificationNonAutoriseeAuthentificationDifferente() throws Exception {
        JSONObject jsonCreation = creerPartie("pmu", "Caen.txt", false);
        String idUniquePartie = jsonCreation.get("idUniquePartie").toString();

        JSONObject jsonConsultation = consulterPartie(idUniquePartie);

        String nomJoueur = "H-Savoie74";
        String idJoueur = this.extraireIdJoueur(jsonConsultation, 1, nomJoueur);

        assertNotNull(idJoueur);

        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder().avecUtilisateurAuthentifie(
                        new UtilisateurAuthentifieTestBuilder().avecEmail("test@toto.fr").build()
                ).build());

        String notesNouvelles = "Ce joueur est très passif postflop";
        String json = "{ \"nouvellesNotes\": \"" + notesNouvelles + "\" }";
        mockMvc.perform(put("/notes/" + idJoueur)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void modificationNonAutoriseeNonAuthentifie() throws Exception {
        when(authService.getUtilisateurIdentifie(any(), any()))
                .thenReturn(new UtilisateurIdentifieTestBuilder()
                        .avecIdentifiantSession("session1")
                        .avecUtilisateurAuthentifie(
                        null
                ).build());

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
                .andExpect(status().isUnauthorized());
    }

    private String extraireNotes(
            JSONObject jsonConsultation,
            String idJoueur
    ) throws Exception {
        JSONArray profilsJoueurs = jsonConsultation.getJSONArray("profilsJoueurs");

        for (int i = 0; i < profilsJoueurs.length(); i++) {
            JSONObject profilJoueur = profilsJoueurs.getJSONObject(i);
            String idTrouve = profilJoueur.getString("idUnique");

            if (idTrouve.equals(idJoueur)) {
                return profilJoueur.getString("notes");
            }
        }

        throw new Exception("Joueur non trouvé");
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



}
