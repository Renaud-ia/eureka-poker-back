package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.api.ConvertisseurPersistanceVersApi;
import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.imports.ConstructeurPersistence;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.InterfaceParties;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurParsing;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.domaine.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;

public class InterfacePartiesImpl implements InterfaceParties {
    private static final Logger logger = LoggerFactory.getLogger(InterfacePartiesImpl.class);
    private final FabriqueDependances fabriqueDependances;
    private final PersistanceParties persistanceParties;
    private final PersistanceFichiers persistanceFichiers;
    public InterfacePartiesImpl(FabriqueDependances fabriqueDependances) {
        this.fabriqueDependances = fabriqueDependances;
        this.persistanceParties = fabriqueDependances.obtPersistanceParties();
        this.persistanceFichiers = fabriqueDependances.obtPersistanceFichiers();
    }
    @Override
    public ResumePartieDto ajouterPartie(String contenuPartie, ParametresImport parametresImport) throws ErreurAjoutPartie {
        enregistrerFichier(contenuPartie);
        logger.info("Données fichiers sauvegardées");
        ConstructeurPersistence constructeurPersistenceDto = parserPartie(contenuPartie, parametresImport);
        logger.info("Partie persistée avec UUID:{}", constructeurPersistenceDto.getIdUniquePartie());

        return constructeurPersistenceDto.obtResumePartie();
    }

    private ConstructeurPersistence parserPartie(String contenuPartie, ParametresImport parametresImport) throws ErreurAjoutPartie {
        ConstructeurPersistence constructeurPersistence = fabriqueDependances.obtConstructeurPersistance(parametresImport);
        DomaineServiceImport domaineServiceImport;

        try {
            domaineServiceImport = fabriqueDependances.obtDomaineServiceImport(contenuPartie, constructeurPersistence);
        }
        catch (ErreurImport erreurImport) {
            logger.error("Une erreur est survenue pendant la création du parser: {}", String.valueOf(erreurImport));
            throw new ErreurAjoutPartie("Impossible de trouver un parser");
        }

        try {
            domaineServiceImport.lancerImport();
            persistanceParties.ajouterPartie(constructeurPersistence.obtPartie());
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
            throws ErreurConsultationPartie, ErreurLectureFichier {

        PartiePersistanceDto partiePersistanceDto =
                    persistanceParties.recupererPartie(idPartie, indexPremiereMain, nombreMains);
        logger.info("Contenu de la partie récupérée pour: {}", idPartie);

        return this.convertirDtoPersistanceEnApi(partiePersistanceDto);
    }

    private ContenuPartieDto convertirDtoPersistanceEnApi(PartiePersistanceDto partiePersistanceDto) throws ErreurLectureFichier {
        // todo OPTIMISATION => on fait deux tours sur la même structure de données => observateur de persistance ? (=15 ms)
        ConvertisseurPersistanceVersApi covertisseur =
                this.fabriqueDependances.obtConvertisseurPersistanceVersApi(partiePersistanceDto);
        return covertisseur.obtContenuPartieDto();
    }
}
