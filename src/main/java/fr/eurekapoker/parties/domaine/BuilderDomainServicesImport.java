package fr.eurekapoker.parties.domaine;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import java.io.File;

public class BuilderDomainServicesImport {
    DomaineServiceImport obtService(String lignesFichier) throws ErreurImport {
        if (lignesFichier.startsWith("<")) return new DomaineServiceImportXml(lignesFichier);

        return new DomaineServiceImportTxt(lignesFichier);
    }

    DomaineServiceImport obtService(File fichier) {
        //todo en vérifiant le type qui sera fourni
        return null;
    }
}
