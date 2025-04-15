package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.application.persistance.dto.JoueurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceAjoutPartie;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceConsultationPartie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersistancePartiesBDD implements PersistanceParties {
    @Autowired
    ServiceAjoutPartie serviceAjoutPartie;
    @Autowired
    ServiceConsultationPartie serviceConsultationPartie;
    @Override
    public void ajouterPartie(UtilisateurIdentifie utilisateurIdentifie, PartiePersistanceDto partiePersistanceDto) {
        serviceAjoutPartie.persisterPartie(utilisateurIdentifie, partiePersistanceDto);
    }
    @Override
    public PartiePersistanceDto recupererPartie(String idPartie, int indexMin, int fenetre) throws ErreurConsultationPartie {
        return serviceConsultationPartie.recupererMains(idPartie, indexMin, indexMin + fenetre);
    }
}
