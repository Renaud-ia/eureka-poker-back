package fr.eurekapoker.parties.api.controleurs;

import fr.eurekapoker.parties.api.requetes.RequeteRange;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.services.ServiceRangeApi;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PostflopRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PreflopRange;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceRange;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("ranges")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class RangeControleur extends BaseControleur {
    private final ServiceRangeApi modifierRange;

    @Autowired
    public RangeControleur(ServiceRangeApi modifierRange) {
        this.modifierRange = modifierRange;
    }

    @PutMapping(value = "/{idAction}")
    public ResponseEntity<ResumePartieDto> ajouterPartie(
            @PathVariable String idAction,
            @RequestBody RequeteRange requete,
            @RequestHeader Map<String, String> headers,
            HttpServletResponse response
    ) throws ErreurModificationPartie {
        if (idAction == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        UtilisateurIdentifie utilisateurIdentifie = this.extraireUtilisateurIdentifie(
                headers,
                null,
                response
        );

        UtilisateurAuthentifie utilisateurAuthentifie = utilisateurIdentifie.getUtilisateurAuthentifie();

        if (utilisateurAuthentifie == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        PokerRange pokerRange = this.extraireRange(requete);
        if (pokerRange == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        this.modifierRange.changerRangeEnregistree(
                utilisateurAuthentifie,
                idAction,
                pokerRange
        );

        return ResponseEntity.noContent().build();
    }



    @GetMapping(value = "/{idAction}")
    public ResponseEntity<HashMap<ServiceRange.MethodeGeneration, PokerRange>> getRanges(
            @PathVariable String idAction,
            @RequestHeader Map<String, String> headers,
            HttpServletResponse response
    ) throws ErreurModificationPartie {
        if (idAction == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        UtilisateurIdentifie utilisateurIdentifie = this.extraireUtilisateurIdentifie(
                headers,
                null,
                response
        );
        UtilisateurAuthentifie utilisateurAuthentifie = utilisateurIdentifie.getUtilisateurAuthentifie();

        if (utilisateurAuthentifie == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        HashMap<ServiceRange.MethodeGeneration, PokerRange> ranges = this.modifierRange.recupererRanges(
                utilisateurAuthentifie,
                idAction
        );

        return ResponseEntity.ok().body(ranges);
    }

    private PokerRange extraireRange(RequeteRange requete) {
        String typeRange = requete.getTypeRange().toUpperCase();

        if (typeRange.equals("PREFLOP")) {
            return new PreflopRange(requete.getCombos());
        }

        else if (typeRange.equals("POSTFLOP")) {
            return new PostflopRange(requete.getCombos());
        }

        return null;
    }
}
