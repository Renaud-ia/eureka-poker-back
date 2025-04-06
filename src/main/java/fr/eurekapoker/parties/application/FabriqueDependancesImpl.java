package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.api.ConvertisseurPersistanceVersApi;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistence;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.services.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.poker.moteur.MoteurJeu;
import fr.eurekapoker.parties.infrastructure.PersistancePartiesBDD;
import fr.eurekapoker.parties.infrastructure.PersistanceS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FabriqueDependancesImpl implements FabriqueDependances {
    private final FabriqueDomainServicesImport fabriqueDomainServicesImport;
    private final PersistancePartiesBDD persistancePartiesBDD;
    @Autowired
    public FabriqueDependancesImpl(
            PersistancePartiesBDD persistancePartiesBDD
    ) {
        this.persistancePartiesBDD = persistancePartiesBDD;
        this.fabriqueDomainServicesImport = new FabriqueDomainServicesImport();
    }
    @Override
    public PersistanceParties obtPersistanceParties() {
        return persistancePartiesBDD;
    }

    @Override
    public PersistanceFichiers obtPersistanceFichiers() {
        return new PersistanceS3();
    }

    public ConstructeurPersistence obtConstructeurPersistance(ParametresImport parametresImport) {
        return new ConstructeurPersistenceDto(new MoteurJeu(), parametresImport);
    }

    @Override
    public DomaineServiceImport obtDomaineServiceImport(String contenuFichier, ConstructeurPersistence constructeurPersistence)
            throws ErreurImport {
        return fabriqueDomainServicesImport.obtService(constructeurPersistence, contenuFichier);
    }

    @Override
    public ConvertisseurPersistanceVersApi obtConvertisseurPersistanceVersApi(PartiePersistanceDto partiePersistanceDto) {
        return new ConvertisseurPersistanceVersApi(partiePersistanceDto);
    }
}
