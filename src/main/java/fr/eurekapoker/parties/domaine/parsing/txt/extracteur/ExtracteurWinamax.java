package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.poker.*;

import java.util.regex.Pattern;

public class ExtracteurWinamax implements ExtracteurLigne {
    private static final Pattern patternAction = Pattern.compile(
            "(?<playName>.+)\\s"+
                    "(?<action>bets|raises|calls|folds|checks)" +
                    "(\\s(?<bet>[\\d.]+))?[\\u20AC€]*"+
                    "(\\sto\\s(?<bet2>[\\d.]+))?[\\u20AC€]*"+
                    "(?<allIn>(.+all-in))?"
    );
    @Override
    public MainPoker extraireMain(String ligne) throws ErreurRegex {
        return null;
    }
    @Override
    public FormatPoker extraireFormat(String ligne) throws ErreurRegex {
        return null;
    }
    @Override
    public JoueurPoker extraireJoueur(String ligne) throws ErreurRegex {
        return null;
    }
    @Override
    public TourPoker extraireTour(String ligne) throws ErreurRegex {
        return null;
    }
    @Override
    public ActionPoker extraireAction(String ligne) throws ErreurRegex {
        return null;
    }
    @Override
    public ResultatJoueur extraireResultat(String ligne) throws ErreurRegex {
        return null;
    }
    @Override
    public BlindeOuAnte extraireBlindeOuAnte(String ligne) throws ErreurRegex {
        return null;
    }
}
