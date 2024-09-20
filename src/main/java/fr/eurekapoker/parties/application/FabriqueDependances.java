package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.imports.ConstructeurPersistence;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.domaine.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;

public interface FabriqueDependances {
    PersistanceParties obtPersistanceParties();
    PersistanceFichiers obtPersistanceFichiers();
    ConstructeurPersistence obtConstructeurPersistance();
    DomaineServiceImport obtDomaineServiceImport(String contenuFichier) throws ErreurImport;

}
