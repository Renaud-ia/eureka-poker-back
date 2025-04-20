package fr.eurekapoker.parties.api.controleurs;

import fr.eurekapoker.parties.api.requetes.RequeteNotes;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.services.ModifierNote;
import fr.eurekapoker.parties.domaine.annotations.NotesJoueur;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("notes")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class NotesControleur extends BaseControleur {
    private final ModifierNote modifierNote;

    @Autowired
    public NotesControleur(ModifierNote modifierNote) {
        this.modifierNote = modifierNote;
    }

    @PutMapping(value = "/{idJoueur}")
    public ResponseEntity<ResumePartieDto> ajouterPartie(
            @PathVariable String idJoueur,
            @RequestBody RequeteNotes requete,
            @RequestHeader Map<String, String> headers,
            HttpServletResponse response
    ) throws ErreurModificationPartie {
        if (idJoueur == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        UtilisateurIdentifie utilisateurIdentifie = this.extraireUtilisateurIdentifie(
                headers,
                null,
                response
        );

        UtilisateurAuthentifie utilisateurAuthentifie = utilisateurIdentifie.getUtilisateurAuthentifie();

        if (utilisateurAuthentifie == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        NotesJoueur notesJoueur = new NotesJoueur(requete.getNouvellesNotes());

        this.modifierNote.changerNotesEnregistrees(utilisateurAuthentifie, idJoueur, notesJoueur);

        return ResponseEntity.noContent().build();
    }
}
