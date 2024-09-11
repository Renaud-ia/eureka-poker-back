package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.poker.*;

import java.util.Objects;
import java.util.regex.Matcher;
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
    public ActionPokerJoueur extraireAction(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternAction, ligne);

        String nomJoueur = matcher.group("playName");
        String nomAction = matcher.group("action");
        boolean totalBet = true;

        // actions sans bet size
        if (Objects.equals(nomAction, "folds")) {
            return new ActionPokerJoueur(nomJoueur, ActionPoker.TypeAction.FOLD);
        }

        if (Objects.equals(nomAction, "checks")) {
            return new ActionPokerJoueur(nomJoueur, ActionPoker.TypeAction.CHECK);
        }

        // actions avec bet size
        if (Objects.equals(nomAction, "calls")) {
            // call on a juste le montant de la complétion
            totalBet = false;
            float montantCall = Float.parseFloat(matcher.group("bet"));

            return new ActionPokerAvecBet(
                    nomJoueur,
                    ActionPoker.TypeAction.CALL,
                    montantCall,
                    totalBet
            );
        }

        if (Objects.equals(nomAction, "bets")) {
            float montantBet = Float.parseFloat(matcher.group("bet"));
            return new ActionPokerAvecBet(
                    nomJoueur,
                    ActionPoker.TypeAction.RAISE,
                    montantBet,
                    totalBet
            );
        }

        if (Objects.equals(nomAction, "raises")) {
            float montantAction = Float.parseFloat(matcher.group("bet2"));
            // BUG WINAMAX, affiche parfois "raises to [bet2]" plutôt que "raises [bet1] to [bet2]"
            if (matcher.group("bet") == null) {
                totalBet = false;
            }
            ActionPoker.TypeAction action = ActionPoker.TypeAction.RAISE;
            if (matcher.group("allIn") != null) {
                action = ActionPoker.TypeAction.RAISE_ALL_IN;
            }
            return new ActionPokerAvecBet(
                    nomJoueur, action, montantAction, totalBet
            );
        }

        throw new ErreurRegex("Nom de l'action inconnue dans :" + ligne);
    }
    @Override
    public ResultatJoueur extraireResultat(String ligne) throws ErreurRegex {
        return null;
    }
    @Override
    public BlindeOuAnte extraireBlindeOuAnte(String ligne) throws ErreurRegex {
        return null;
    }

    private Matcher matcherRegex(Pattern pattern, String ligne) throws ErreurRegex {
        Matcher matcher = pattern.matcher(ligne);
        if (!matcher.find()) {
            throw new ErreurRegex("Regex non trouvé dans : " + ligne);
        }
        return matcher;
    }

}
