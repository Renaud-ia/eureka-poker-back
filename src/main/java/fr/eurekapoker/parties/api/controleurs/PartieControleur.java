package fr.eurekapoker.parties.api.controleurs;

import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.services.CreerRecupererPartie;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO : VARIABILISER LE CORS
@RestController
@RequestMapping("parties")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PartieControleur extends BaseControleur {
    private final static Logger logger = LoggerFactory.getLogger(PartieControleur.class);
    private final static int MAX_FENETRE_CONSULTATION = 10000;
    private final CreerRecupererPartie interfaceParties;

    @Autowired
    public PartieControleur(CreerRecupererPartie creerRecupererPartie) {
        this.interfaceParties = creerRecupererPartie;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContenuPartieDto> consulterPartie(
            @PathVariable String id,
            @RequestParam int indexMain,
            @RequestParam int fenetreConsultation,
            @CookieValue(name = "tokenSession", required = false) String tokenDeSession,
            @RequestHeader Map<String, String> headers,
            HttpServletResponse response
    ) throws ErreurConsultationPartie, ErreurLectureFichier, JoueurNonExistant {
        UtilisateurIdentifie utilisateurIdentifie = this.extraireUtilisateurIdentifie(headers, tokenDeSession, response);
        fenetreConsultation = Math.max(fenetreConsultation, MAX_FENETRE_CONSULTATION);
        ContenuPartieDto contenuPartie =
                this.interfaceParties.consulterMainsParties(utilisateurIdentifie, id, indexMain, fenetreConsultation);
        return ResponseEntity.ok(contenuPartie);

    }

    @PostMapping(value = "/", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResumePartieDto> ajouterPartie(
            @RequestPart(required = false) String contenuPartie,
            @RequestParam(required = false) MultipartFile fichierUpload,
            @RequestParam(required = false) String joueursAnonymes,
            @CookieValue(name = "tokenSession", required = false) String tokenDeSession,
            @RequestHeader Map<String, String> headers,
            HttpServletResponse response
            ) throws ErreurAjoutPartie {

        UtilisateurIdentifie utilisateurIdentifie = this.extraireUtilisateurIdentifie(headers, tokenDeSession, response);

        boolean requeteJoueursAnonymes = this.extraireValeurJoueursAnonymes(joueursAnonymes);
        String contenuUpload = this.extraireContenuUpload(contenuPartie, fichierUpload);
        if (contenuUpload == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ParametresImport parametresImport = new ParametresImport(requeteJoueursAnonymes);

        ResumePartieDto resumePartieDto = this.interfaceParties.ajouterPartie(
                utilisateurIdentifie,
                contenuUpload,
                parametresImport
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resumePartieDto);
    }



    private String extraireContenuUpload(String contenuPartie, MultipartFile fichierUpload) {
        if (fichierUpload != null && !fichierUpload.isEmpty()) {
            try {
                return new String(fichierUpload.getBytes(), StandardCharsets.UTF_8);
            }

            catch (IOException e) {
                logger.error("Impossible d'ouvir le fichier fourni");
                return null;
            }
        }

        if (contenuPartie == null || contenuPartie.isEmpty()) {
            logger.error("Aucun fichier ou texte dans la requête");
            return null;
        }

        return contenuPartie;
    }

    private boolean extraireValeurJoueursAnonymes(String joueursAnonymes) {
        if (joueursAnonymes == null) {
            return false;
        }
        return joueursAnonymes.equals("on");
    }


}
