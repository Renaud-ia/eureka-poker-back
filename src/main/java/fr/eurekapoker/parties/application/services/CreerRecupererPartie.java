package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;

public interface CreerRecupererPartie {
    public ResumePartieDto ajouterPartie(
            UtilisateurIdentifie utilisateurIdentifie,
            String contenuPartie,
            ParametresImport parametresImport
    ) throws ErreurAjoutPartie;

    public ContenuPartieDto consulterMainsParties(
            UtilisateurIdentifie utilisateurIdentifie,
            String idPartie,
            int indexPremiereMain,
            int nombreMains
    ) throws ErreurConsultationPartie, ErreurLectureFichier, JoueurNonExistant;
}
