package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.imports.ConstructeurPersistence;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.domaine.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.poker.moteur.MoteurJeu;
import fr.eurekapoker.parties.infrastructure.parties.PersistancePartiesBDD;

public class FabriqueDependancesImpl implements FabriqueDependances {
    private final FabriqueDomainServicesImport fabriqueDomainServicesImport;
    public FabriqueDependancesImpl() {
        this.fabriqueDomainServicesImport = new FabriqueDomainServicesImport();
    }
    @Override
    public PersistanceParties obtPersistanceParties() {
        return new PersistancePartiesBDD();
    }

    @Override
    public PersistanceFichiers obtPersistanceFichiers() {
        return null;
    }

    public ConstructeurPersistence obtConstructeurPersistance() {
        return new ConstructeurPersistenceDto(new MoteurJeu());
    }

    @Override
    public DomaineServiceImport obtDomaineServiceImport(String contenuFichier) throws ErreurImport {
        return fabriqueDomainServicesImport.obtService(obtConstructeurPersistance(), contenuFichier);
    }
}
