package fr.eurekapoker.parties.application.persistance;

import fr.eurekapoker.parties.application.persistance.dto.JoueurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.dto.MainPersistenceDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;

import java.util.List;

public interface PersistanceParties {
    String ajouterPartie(PartiePersistanceDto partiePersistanceDto);
    List<MainPersistenceDto> recupererMains(String idPartie, int indexMin, int indexMax);
    void rendreAnonymeJoueurDansPartie(String idPartie, JoueurPersistenceDto joueurPersistenceDto);
    void definirHeroDansPartie(String idPartie, JoueurPersistenceDto joueurPersistenceDto);
}
