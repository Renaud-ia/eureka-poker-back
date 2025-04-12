package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.FabriqueDomainServicesImport;
import fr.eurekapoker.parties.application.api.ConvertisseurPersistanceVersApi;
import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurParsing;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistence;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;
import fr.eurekapoker.parties.domaine.services.DomaineServiceImport;
import fr.eurekapoker.parties.infrastructure.PersistancePartiesBDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreerRecupererPartieImpl implements CreerRecupererPartie {
    private static final Logger logger = LoggerFactory.getLogger(CreerRecupererPartieImpl.class);
    private final PersistancePartiesBDD persistancePartiesBDD;
    private final PersistanceFichiers persistanceFichiers;

    @Autowired
    public CreerRecupererPartieImpl(
            PersistancePartiesBDD persistancePartiesBDD,
            PersistanceFichiers persistanceFichiers
    ) {
        this.persistancePartiesBDD = persistancePartiesBDD;
        this.persistanceFichiers = persistanceFichiers;
    }
    @Override
    public ResumePartieDto ajouterPartie(
            String contenuPartie,
            ParametresImport parametresImport
    ) throws ErreurAjoutPartie {
        enregistrerFichier(contenuPartie);
        logger.info("Données fichiers sauvegardées");
        ConstructeurPersistence constructeurPersistenceDto = parserPartie(contenuPartie, parametresImport);
        logger.info("Partie persistée avec UUID:{}", constructeurPersistenceDto.getIdUniquePartie());

        return constructeurPersistenceDto.obtResumePartie();
    }

    private ConstructeurPersistence parserPartie(String contenuPartie, ParametresImport parametresImport) throws ErreurAjoutPartie {
        ConstructeurPersistence constructeurPersistence = new ConstructeurPersistenceDto(parametresImport);
        DomaineServiceImport domaineServiceImport;

        try {
            domaineServiceImport = FabriqueDomainServicesImport.obtService(constructeurPersistence, contenuPartie);
        }
        catch (ErreurImport erreurImport) {
            logger.error("Une erreur est survenue pendant la création du parser: {}", String.valueOf(erreurImport));
            throw new ErreurAjoutPartie("Impossible de trouver un parser");
        }

        try {
            domaineServiceImport.lancerImport();
            this.persistancePartiesBDD.ajouterPartie(constructeurPersistence.obtPartie());
        }
        catch (ErreurImport erreurImport) {
            logger.error("Une erreur est survenue pendant le parsing: {}", String.valueOf(erreurImport));
            throw new ErreurParsing("Une erreur est survenue pendant le parsing");
        }

        return constructeurPersistence;
    }

    private void enregistrerFichier(String contenuPartie) {
        this.persistanceFichiers.enregistrerFichier(contenuPartie);
    }

    @Override
    public ContenuPartieDto consulterMainsParties(String idPartie, int indexPremiereMain, int nombreMains)
            throws ErreurConsultationPartie, ErreurLectureFichier, JoueurNonExistant {

        PartiePersistanceDto partiePersistanceDto =
                persistancePartiesBDD.recupererPartie(idPartie, indexPremiereMain, nombreMains);
        logger.info("Contenu de la partie récupérée pour: {}", idPartie);

        return this.convertirDtoPersistanceEnApi(partiePersistanceDto);
    }

    private ContenuPartieDto convertirDtoPersistanceEnApi(PartiePersistanceDto partiePersistanceDto) throws ErreurLectureFichier, JoueurNonExistant {
        // todo OPTIMISATION => on fait deux tours sur la même structure de données => observateur de persistance ? (=15 ms)
        ConvertisseurPersistanceVersApi covertisseur =
                new ConvertisseurPersistanceVersApi(partiePersistanceDto);
        return covertisseur.obtContenuPartieDto();
    }
}
