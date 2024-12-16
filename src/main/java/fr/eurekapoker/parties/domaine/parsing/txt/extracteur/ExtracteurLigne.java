package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.InfosMainWinamax;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ExtracteurLigne {
    public abstract InfosTable extraireInfosTable(String ligne) throws ErreurRegex;

    public abstract InfosJoueur extraireStackJoueur(String ligne) throws ErreurRegex;

    public abstract NouveauTour extraireNouveauTour(String ligne) throws ErreurRegex;

    public abstract ActionPokerJoueur extraireAction(String ligne) throws ErreurRegex;

    public abstract ResultatJoueur extraireResultat(String ligne) throws ErreurRegex;

    public abstract BlindeOuAnte extraireBlindeOuAnte(String s) throws ErreurRegex;

    public abstract InfosHero extraireInfosHero(String ligne) throws ErreurRegex;

    public abstract InfosMain extraireInfosMain(String ligne) throws ErreurImport;

    protected Matcher matcherRegex(Pattern pattern, String ligne) throws ErreurRegex {
        Matcher matcher = pattern.matcher(ligne);
        if (!matcher.find()) {
            throw new ErreurRegex("Regex non trouvé dans :" + ligne);
        }
        return matcher;
    }
}
