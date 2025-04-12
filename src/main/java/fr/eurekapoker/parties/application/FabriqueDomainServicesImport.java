package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.domaine.services.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.services.DomaineServiceImportTxt;
import fr.eurekapoker.parties.domaine.services.DomaineServiceImportXml;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;

import java.io.File;

public class FabriqueDomainServicesImport {
    public static DomaineServiceImport obtService(ObservateurParser observateurParser, String lignesFichier)
            throws ErreurImport {
        if (lignesFichier.startsWith("<")) return new DomaineServiceImportXml(observateurParser, lignesFichier);

        return new DomaineServiceImportTxt(observateurParser, lignesFichier);
    }
}
