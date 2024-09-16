package fr.eurekapoker.parties.application.api;

import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;

public interface InterfaceParties {
    ResumePartieDto ajouterPartie(String contenuPartie) throws ErreurAjoutPartie;
    /**
     * permet d'accéder à une partie ou main
     * @param idPartie l'UUID unique de la partie/main
     * @param indexPremiereMain index de la première main souhaitée
     * @param nombreMains nombre de mains qu'on veut récupérer (=batch)
     * @return un DTO permettant de rejouer la partie
     */
    ContenuPartieDto consulterMainsParties(String idPartie, int indexPremiereMain, int nombreMains)
            throws ErreurConsultationPartie;
    void rendreAnonymeJoueurDansPartie(String idPartie, String nomJoueur, String room) throws ErreurModificationPartie;
    void definirHeroDansPartie(String idPartie, String nomJoueur, String room) throws ErreurModificationPartie;
}
