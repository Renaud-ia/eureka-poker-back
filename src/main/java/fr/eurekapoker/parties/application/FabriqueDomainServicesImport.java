package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.domaine.DomaineServiceImport;
import fr.eurekapoker.parties.domaine.DomaineServiceImportTxt;
import fr.eurekapoker.parties.domaine.DomaineServiceImportXml;
import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;

import java.io.File;

public class FabriqueDomainServicesImport {
    public DomaineServiceImport obtService(ObservateurParser observateurParser, String lignesFichier)
            throws ErreurImport {
        if (lignesFichier.startsWith("<")) return new DomaineServiceImportXml(lignesFichier);

        return new DomaineServiceImportTxt(observateurParser, lignesFichier);
    }

    public DomaineServiceImport obtService(ObservateurParser observateurParser, File fichier) {
        //todo en vérifiant le type d'entrée qui sera fourni
        return null;
    }
}
