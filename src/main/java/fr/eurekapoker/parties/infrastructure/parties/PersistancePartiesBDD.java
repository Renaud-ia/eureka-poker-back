package fr.eurekapoker.parties.infrastructure.parties;

import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.application.persistance.dto.JoueurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceAjoutPartie;
import org.springframework.beans.factory.annotation.Autowired;

public class PersistancePartiesBDD implements PersistanceParties {
    @Autowired
    ServiceAjoutPartie serviceAjoutPartie;
    @Override
    public void ajouterPartie(PartiePersistanceDto partiePersistanceDto) {
        serviceAjoutPartie.persisterPartie(partiePersistanceDto);
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
