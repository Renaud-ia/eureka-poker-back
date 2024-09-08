package fr.eurekapoker.parties.domaine;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurInconnue;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.RoomNonPriseEnCharge;
import fr.eurekapoker.parties.domaine.parsing.txt.BuilderParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.poker.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.MainPoker;

import java.util.List;

/**
    Implémentation du service du domaine pour l'import de fichiers txt
    Trouve l'extracteur approprié
 */
class DomaineServiceImportTxt implements DomaineServiceImport {
    private final ParserTxt parserTxt;
    private List<MainPoker> mainsExtraites;
    public DomaineServiceImportTxt(String fichierBrut) throws ErreurImport {
        this(fichierBrut.split("\n"));
    }

    public DomaineServiceImportTxt(String[] lignesFichier) throws ErreurImport {
        try {
            this.parserTxt = BuilderParserTxt.trouverParser(lignesFichier);
        }
        catch (Exception e) {
            throw new ErreurInconnue(e.getMessage());
        }

        if (parserTxt == null) {
            throw new RoomNonPriseEnCharge("Impossible de trouver un parser");
        }
    }

    @Override
    public void lancerImport() throws ErreurImport {
        this.parserTxt.lancerImport();
        this.mainsExtraites = parserTxt.obtMains();
    }

    @Override
    public FormatPoker obtFormatPoker() throws ErreurLectureFichier {
        return parserTxt.obtFormatPoker();
    }

    @Override
    public List<MainPoker> obtMainsExtraites() {
        return this.mainsExtraites;
    }

    @Override
    public List<JoueurPoker> obtJoueursInitiaux() {
        return this.mainsExtraites.getFirst().obtJoueurs();
    }
}
