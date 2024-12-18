package fr.eurekapoker.parties.api;

import fr.eurekapoker.parties.application.FabriqueDependances;
import fr.eurekapoker.parties.application.InterfacePartiesImpl;
import fr.eurekapoker.parties.application.api.InterfaceParties;
import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO : VARIABILISER LE CORS
@RestController
@RequestMapping("parties")
@CrossOrigin(origins = "http://localhost:3000")
public class PartieControleur {
    private final static Logger logger = LoggerFactory.getLogger(PartieControleur.class);
    private final static int MAX_FENETRE_CONSULTATION = 10000;
    private final InterfaceParties interfaceParties;
    @Autowired
    public PartieControleur(FabriqueDependances fabriqueDependances) {
        this.interfaceParties = new InterfacePartiesImpl(fabriqueDependances);
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
            @RequestParam(required = false) String joueursAnonymes
            ) throws ErreurAjoutPartie {
        boolean requeteJoueursAnonymes;
        if (joueursAnonymes == null) {
            requeteJoueursAnonymes = false;
        }
        else {
            requeteJoueursAnonymes = joueursAnonymes.equals("on");
        }

        if (fichierUpload != null && !fichierUpload.isEmpty()) {
            try {
                contenuPartie = new String(fichierUpload.getBytes(), StandardCharsets.UTF_8);
            }

            catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        else if (contenuPartie == null || contenuPartie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ParametresImport parametresImport = new ParametresImport(requeteJoueursAnonymes);

        ResumePartieDto resumePartieDto = this.interfaceParties.ajouterPartie(
                contenuPartie,
                parametresImport
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resumePartieDto);
    }
}
