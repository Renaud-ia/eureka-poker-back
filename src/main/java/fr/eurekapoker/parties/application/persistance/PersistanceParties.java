package fr.eurekapoker.parties.application.persistance;

import java.util.List;

public interface PersistanceParties {
    String ajouterOuRecupererPartie(PartiePersistanceDto partiePersistanceDto);
    boolean creerMainDansPartie(MainPersistenceDto mainPersistenceDto);
    List<MainPersistenceDto> recupererMains(String idPartie, int indexMin, int indexMax);
    long ajouterOuRecupererJoueur(JoueurPersistenceDto joueurPersistenceDto);
    void rendreAnonymeJoueurDansPartie(String idPartie, long idJoueur);
    void definirHeroDansPartie(String idPartie, long idJoueur);
}
