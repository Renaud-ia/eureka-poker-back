package fr.eurekapoker.parties.api;

import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.auth.AuthService;
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
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO : VARIABILISER LE CORS
@RestController
@RequestMapping("parties")
@CrossOrigin(origins = "http://localhost:3000")
public class PartieControleur {
    private final static Logger logger = LoggerFactory.getLogger(PartieControleur.class);
    private final static int MAX_FENETRE_CONSULTATION = 10000;
    private final CreerRecupererPartie interfaceParties;
    private final AuthService authService;
    @Autowired
    public PartieControleur(AuthService authService, CreerRecupererPartie creerRecupererPartie) {
        this.authService = authService;
        this.interfaceParties = creerRecupererPartie;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContenuPartieDto> consulterPartie(
            @PathVariable String id,
            @RequestParam int indexMain,
            @RequestParam int fenetreConsultation) throws ErreurConsultationPartie, ErreurLectureFichier, JoueurNonExistant {
        fenetreConsultation = Math.max(fenetreConsultation, MAX_FENETRE_CONSULTATION);
        ContenuPartieDto contenuPartie =
                this.interfaceParties.consulterMainsParties(id, indexMain, fenetreConsultation);
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
        tokenDeSession = this.extraireOucreerTokenDeSession(tokenDeSession, response);
        UtilisateurIdentifie utilisateurIdentifie = this.authService.getUtilisateurIdentifie(headers.get("authorization"), tokenDeSession);

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

    private String extraireOucreerTokenDeSession(String tokenDeSession, HttpServletResponse response) {
        if (tokenDeSession == null || tokenDeSession.isEmpty()) {
            tokenDeSession = UUID.randomUUID().toString();

            ResponseCookie cookie = ResponseCookie.from("tokenDeSession", tokenDeSession)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(3))
                    .sameSite("Lax")
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        return tokenDeSession;
    }
}
