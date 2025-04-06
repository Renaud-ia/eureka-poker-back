package fr.eurekapoker.parties.domaine.services;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurInconnue;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.txt.FabriqueParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;

import java.util.List;

/**
    Implémentation du service du domaine pour l'import de fichiers txt
    Trouve l'extracteur approprié
 */
public class DomaineServiceImportTxt implements DomaineServiceImport {
    private final ParserTxt parserTxt;
    public DomaineServiceImportTxt(ObservateurParser observateurParser, String fichierBrut)
            throws ErreurImport {
        this(observateurParser, fichierBrut.split("\n"));
    }

    public DomaineServiceImportTxt(ObservateurParser observateurParser, String[] lignesFichier)
            throws ErreurImport {
        try {
            this.parserTxt = FabriqueParserTxt.trouverParser(observateurParser, lignesFichier);
        }
        catch (Exception e) {
            throw new ErreurInconnue(e.getMessage());
        }
    }

    @Override
    public void lancerImport() throws ErreurImport {
        this.parserTxt.lancerImport();
    }
}
