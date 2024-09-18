package fr.eurekapoker.parties.application;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.eurekapoker.parties.application.api.dto.ContenuPartieDto;
import fr.eurekapoker.parties.application.api.InterfaceParties;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.exceptions.ErreurAjoutPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurModificationPartie;
import fr.eurekapoker.parties.application.exceptions.ErreurParsing;
import fr.eurekapoker.parties.application.persistance.PersistanceFichiers;
import fr.eurekapoker.parties.application.persistance.dto.JoueurPersistenceDto;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.domaine.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.FabriqueDomainServicesImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;

public class InterfacePartiesImpl implements InterfaceParties {
    private static final Logger logger = LoggerFactory.getLogger(InterfacePartiesImpl.class);
    private final PersistanceParties persistanceParties;
    private final PersistanceFichiers persistanceFichiers;
    public InterfacePartiesImpl(PersistanceParties persistanceParties, PersistanceFichiers persistanceFichiers) {
        this.persistanceParties = persistanceParties;
        this.persistanceFichiers = persistanceFichiers;
    }
    @Override
    public ResumePartieDto ajouterPartie(String contenuPartie) throws ErreurAjoutPartie {
        ObservateurParser observateurParser = parserPartie(contenuPartie);
        logger.info("Partie persistée avec UUID:" + observateurParser.getIdUniquePartie());
        enregistrerFichier(contenuPartie, observateurParser.getIdUniquePartie());
        logger.info("Données fichiers sauvegardées avec UUID:" + observateurParser.getIdUniquePartie());

        return observateurParser.obtResumePartie();
    }

    private ObservateurParser parserPartie(String contenuPartie) throws ErreurAjoutPartie {
        ObservateurParser observateurParser = new ObservateurParserImpl();
        FabriqueDomainServicesImport fabriqueDomainServicesImport = new FabriqueDomainServicesImport();
        DomaineServiceImport domaineServiceImport;

        // todo logger les erreurs et ajuster le niveau de détail
        try {
            domaineServiceImport = fabriqueDomainServicesImport.obtService(observateurParser, contenuPartie);
        }
        catch (ErreurImport erreurImport) {
            logger.error("Une erreur est survenue pendant la création du parser: " + erreurImport);
            throw new ErreurAjoutPartie("Impossible de trouver un parser");
        }

        try {
            domaineServiceImport.lancerImport();
            persistanceParties.ajouterPartie(observateurParser.obtPartie());
        }
        catch (ErreurImport erreurImport) {
            logger.error("Une erreur est survenue pendant le parsing: " + erreurImport);
            throw new ErreurParsing("Une erreur est survenue pendant le parsing");
        }

        return observateurParser;
    }

    private void enregistrerFichier(String contenuPartie, String idUniqueGenere) {
        this.persistanceFichiers.enregistrerFichier(contenuPartie, idUniqueGenere);
    }

    @Override
    public ContenuPartieDto consulterMainsParties(String idPartie, int indexPremiereMain, int nombreMains)
            throws ErreurConsultationPartie {
        try {
            ContenuPartieDto contenuPartieDto =
                    persistanceParties.recupererPartie(idPartie, indexPremiereMain, nombreMains);
            logger.info("Contenu de la partie récupérée pour: " + idPartie);
            return contenuPartieDto;
        }
        catch (Exception e) {
            logger.error("Une erreur est survenue pendant la récupération de: " + idPartie);
            throw new ErreurConsultationPartie("Impossible de récupérer la partie: " + idPartie);
        }
    }

    // todo cette fonctions devraient être réservés au créateur
    @Override
    public void rendreAnonymeJoueurDansPartie(String idPartie, String nomJoueur)
            throws ErreurModificationPartie {
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(nomJoueur);
        persistanceParties.rendreAnonymeJoueurDansPartie(idPartie, joueurPersistenceDto);
    }

    // todo cette fonction devrait être activable une seule fois
    @Override
    public void definirJoueurCentreDansPartie(String idPartie, String nomJoueur)
            throws ErreurModificationPartie {
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(nomJoueur);
        persistanceParties.definirJoueurCentreDansPartie(idPartie, joueurPersistenceDto);
    }
}
