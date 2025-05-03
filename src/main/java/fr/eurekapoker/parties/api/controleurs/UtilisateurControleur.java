package fr.eurekapoker.parties.api.controleurs;

import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceUtilisateur;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("utilisateur")
@CrossOrigin(origins = "http://localhost:3000")
public class UtilisateurControleur extends BaseControleur {
    private final ServiceUtilisateur serviceUtilisateur;

    @Autowired
    public UtilisateurControleur(ServiceUtilisateur serviceUtilisateur) {
        this.serviceUtilisateur = serviceUtilisateur;
    }

    @PatchMapping(value = "/{tokenDeSession}")
    public ResponseEntity<ResumePartieDto> associerUtilisateurSession(
            @PathVariable String tokenDeSession,
            @RequestHeader Map<String, String> headers,
            HttpServletResponse response
    ) throws ErreurModificationPartie {
        UtilisateurIdentifie utilisateurIdentifie = this.extraireUtilisateurIdentifie(headers, tokenDeSession, response);

        this.serviceUtilisateur.associerUtilisateurSession(utilisateurIdentifie);

        return ResponseEntity.noContent().build();
    }
}

