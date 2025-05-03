package fr.eurekapoker.parties.api;

import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurAuthentification;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Une erreur est survenue")
    @ExceptionHandler(ErreurConsultationPartie.class)
    public void ErreurConsultation() {
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Une erreur est survenue")
    @ExceptionHandler(ErreurAjoutPartie.class)
    public void ErreurAjout() {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ErreurRegex.class)
    public void ErreurRegex() {}

    // todo gérer toutes les erreurs
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Vous n'avez pas les droits pour cette modification")
    @ExceptionHandler(ModificationNonAutorisee.class)
    public void NonAutorise() {
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Cette session a déjà un utilisateur")
    @ExceptionHandler(ErreurAuthentification.class)
    public void DejaAssocie() {
    }
}
