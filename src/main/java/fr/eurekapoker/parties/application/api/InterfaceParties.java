package fr.eurekapoker.parties.application.api;

import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;

public interface InterfaceParties {
    ResumePartieDto ajouterPartie(String contenuPartie, ParametresImport parametresImport) throws ErreurAjoutPartie;
    /**
     * permet d'accéder à une partie ou main
     * @param idPartie l'UUID unique de la partie/main
     * @param indexPremiereMain index de la première main souhaitée
     * @param nombreMains nombre de mains qu'on veut récupérer (=batch)
     * @return un DTO permettant de rejouer la partie
     */
    ContenuPartieDto consulterMainsParties(String idPartie, int indexPremiereMain, int nombreMains)
            throws ErreurConsultationPartie, ErreurLectureFichier;
}
