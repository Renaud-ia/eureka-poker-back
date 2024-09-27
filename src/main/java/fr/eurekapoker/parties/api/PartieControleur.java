package fr.eurekapoker.parties.api;

import fr.eurekapoker.parties.application.FabriqueDependances;
import fr.eurekapoker.parties.application.InterfacePartiesImpl;
import fr.eurekapoker.parties.application.api.InterfaceParties;
import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// TODO : VARIABILISER LE CORS
@RestController
@RequestMapping("parties")
@CrossOrigin(origins = "http://localhost:3000")
public class PartieControleur {
    private final static int MAX_FENETRE_CONSULTATION = 10;
    private final InterfaceParties interfaceParties;
    @Autowired
    public PartieControleur(FabriqueDependances fabriqueDependances) {
        this.interfaceParties = new InterfacePartiesImpl(fabriqueDependances);
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Bienvenue");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ContenuPartieDto> consulterPartie(
            @PathVariable String id,
            @RequestParam int indexPremiereMain,
            @RequestParam int fenetreConsultation) throws ErreurConsultationPartie {
        fenetreConsultation = Math.max(fenetreConsultation, MAX_FENETRE_CONSULTATION);
        ContenuPartieDto contenuPartie =
                this.interfaceParties.consulterMainsParties(id, indexPremiereMain, fenetreConsultation);
        return ResponseEntity.ok(contenuPartie);

    }

    @PostMapping(value = "/creer")
    public ResponseEntity<ResumePartieDto> ajouterPartie(
            @RequestBody RequeteImport requeteImport
            ) throws ErreurAjoutPartie {
        ResumePartieDto resumePartieDto = this.interfaceParties.ajouterPartie(
                requeteImport.getContenuPartie(),
                requeteImport.getParametresImport()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(resumePartieDto);
    }
}
