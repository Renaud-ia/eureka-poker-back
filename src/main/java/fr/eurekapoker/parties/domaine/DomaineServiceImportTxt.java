package fr.eurekapoker.parties.domaine;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurInconnue;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.RoomNonPriseEnCharge;
import fr.eurekapoker.parties.domaine.parsing.txt.FabriqueParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.InfosPartiePoker;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;

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
            this.parserTxt = FabriqueParserTxt.trouverParser(lignesFichier);
        }
        catch (Exception e) {
            throw new ErreurInconnue(e.getMessage());
        }
    }

    @Override
    public void lancerImport() throws ErreurImport {
        this.parserTxt.lancerImport();
        this.mainsExtraites = parserTxt.obtMains();
    }

    @Override
    public InfosPartiePoker obtInfosPartie() {
        return parserTxt.obtInfosPartie();
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
