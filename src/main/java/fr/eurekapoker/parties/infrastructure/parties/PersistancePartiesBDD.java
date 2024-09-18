package fr.eurekapoker.parties.infrastructure.parties;

import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.application.persistance.dto.JoueurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;

public class PersistancePartiesBDD implements PersistanceParties {
    @Override
    public String ajouterPartie(PartiePersistanceDto partiePersistanceDto) {
        return null;
    }

    @Override
    public PartiePersistanceDto recupererPartie(String idPartie, int indexMin, int indexMax) {
        return null;
    }

    @Override
    public void rendreAnonymeJoueurDansPartie(String idPartie, JoueurPersistenceDto joueurPersistenceDto) {

    }

    @Override
    public void definirJoueurCentreDansPartie(String idPartie, JoueurPersistenceDto joueurPersistenceDto) {

    }
}
