package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.api.ConvertisseurPersistanceVersApi;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistence;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.services.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;

public interface FabriqueDependances {
    PersistanceParties obtPersistanceParties();
    PersistanceFichiers obtPersistanceFichiers();
    ConstructeurPersistence obtConstructeurPersistance(ParametresImport parametresImport);
    public DomaineServiceImport obtDomaineServiceImport(String contenuFichier, ConstructeurPersistence constructeurPersistence)
            throws ErreurImport;
    ConvertisseurPersistanceVersApi obtConvertisseurPersistanceVersApi(PartiePersistanceDto partiePersistanceDto);
}
