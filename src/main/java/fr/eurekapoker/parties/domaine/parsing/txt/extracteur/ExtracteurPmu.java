package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.pmu.InfosMainPmu;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.ResultatJoueurWinamax;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerAvecBet;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtracteurPmu extends ExtracteurLigne {

    private static final Pattern patternIdMain =
            Pattern.compile("\\*\\*\\*\\*\\*\\sHand\\sHistory\\sfor\\sGame\\s(?<idMain>\\d+)\\s");

    public long extraireIdMain(String ligne) throws ErreurImport {
        Matcher matcher = this.matcherRegex(patternIdMain, ligne);

        try {
            return Long.parseLong(matcher.group("idMain"));
        }

        catch (Exception e) {
            throw new ErreurRegex("Id main non parsé");
        }
    }

    private static final Pattern patternInfosMain =
            Pattern.compile("(?<blindesEuros>[\\d.\\u20AC/]+\\sEUR\\s)?" +
                    "NL\\sTexas\\sHold'em\\s" +
                    "(?<buyIn>[\\d.\\u20AC]+\\sEUR\\sBuy-in\\s+)?" +
                    "(Trny:\\d+\\s)?" +
                    "(Level:\\d+\\s)?" +
                    "(\\sBlinds-Antes\\([.\\d\\sK\\-/]+\\)\\s)?" +
                    "\\-\\s(?<date>.+)"
            );

    public InfosMainPmu extraireInfosMain(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternInfosMain, ligne);

        double buyIn = 0;

        if (matcher.group("blindesEuros") != null) {
            String blindesEuros = matcher.group("blindesEuros");
            blindesEuros = blindesEuros.replace("EUR", "").replace("€", "");
            String[] parts = blindesEuros.split("/");

            String secondValue = parts[1];

            buyIn = Float.parseFloat(secondValue) * 100;
        }

        else {
            String buyInString = matcher.group("buyIn");
            buyInString = buyInString
                    .replace("EUR", "")
                    .replace("€", "")
                    .replace("Buy-in", "");

            buyIn = Float.parseFloat(buyInString);
        }

        LocalDateTime dateExtraite = extraireDate(matcher.group("date"));

        return new InfosMainPmu(
            FormatPoker.Variante.HOLDEM_NO_LIMIT,
                buyIn,
                dateExtraite
        );
    }

    private LocalDateTime extraireDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, HH:mm:ss z yyyy", Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);
        return zonedDateTime.toLocalDateTime();
    }

    private static final Pattern patternInfosTable =
            Pattern.compile("Table\\s" +
                    "([\\u20AC.\\d]+\\s)?" +
                    "(?<nomTable>[.\\u20AC\\-\\s\\w]+)" +
                    "\\s\\([\\w\\s]+\\)");

    @Override
    public InfosTable extraireInfosTable(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternInfosTable, ligne);

        String nomTable = supprimerDernierEspace(matcher.group("nomTable"));

        return new InfosTable(nomTable);
    }

    private String supprimerDernierEspace(String nom) {
        if (nom.endsWith(" ")) {
            return nom.substring(0, nom.length() - 1);
        }

        return nom;
    }

    private static final Pattern patternNombreJoueurs =
            Pattern.compile("Total number of players : "+
                    "\\d+/(?<nombreJoueurs>\\d+)"
            );

    public int extraireNombreJoueurs(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternNombreJoueurs, ligne);

        return Integer.parseInt(matcher.group("nombreJoueurs"));
    }

    private static final Pattern patternInfosJoueurs =
            Pattern.compile("Seat\\s(?<siege>\\d+):\\s" +
                    "(?<nomJoueur>[\\w\\s]+)\\s" +
                    "\\((?<stack>[\\s,.\\w\\u20AC]+)\\)"
            );

    @Override
    public InfosJoueur extraireStackJoueur(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternInfosJoueurs, ligne);

        return new InfosJoueur(
                matcher.group("nomJoueur"),
                Double.parseDouble(formatterStringValeur(matcher.group("stack"))),
                Integer.parseInt(matcher.group("siege"))
        );
    }



    private static final Pattern patternBigBlinde =
            Pattern.compile(
                        "Blinds" +
                            "(-Antes)?" +
                            "\\(\\d+/(?<montantBB>\\d+)([\\s\\d\\-]+)?\\)"
            );

    public BigDecimal extraireBigBlinde(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternBigBlinde, ligne);
        float value = Float.parseFloat(matcher.group("montantBB"));
        return new BigDecimal(value);
    }

    private static final Pattern patternBlindeOuAnte =
            Pattern.compile(
                    "(?<nomJoueur>[\\w\\s]+)\\s" +
                            "posts\\s" +
                            "(?<typeBlinde>\\w+)\\s" +
                            "(blind\\s)?" +
                            "\\[(?<montant>[,.\\w\\s\\u20AC]+)]"
            );

    @Override
    public BlindeOuAnte extraireBlindeOuAnte(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternBlindeOuAnte, ligne);

        BlindeOuAnte.TypeTaxe typeTaxe;
        String typeBlindeString = matcher.group("typeBlinde");
        if (Objects.equals(typeBlindeString, "small") || Objects.equals(typeBlindeString, "big")) {
            typeTaxe = BlindeOuAnte.TypeTaxe.BLINDE;
        }
        else if (Objects.equals(typeBlindeString, "ante")) {
            typeTaxe = BlindeOuAnte.TypeTaxe.ANTE;
        }
        else throw new ErreurRegex("Type de taxe inconnue");

        String montantString = formatterStringValeur(matcher.group("montant"));
        float montant = Float.parseFloat(montantString);

        return new BlindeOuAnte(
                matcher.group("nomJoueur"),
                typeTaxe,
                montant
        );
    }

    private static final Pattern patternInfosHero =
            Pattern.compile(
                    "Dealt\\sto\\s" +
                            "(?<nomHero>[\\w\\s]+)\\s" +
                            "\\[(?<cartesHero>[\\w\\s]+)]");

    @Override
    public InfosHero extraireInfosHero(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternInfosHero, ligne);

        List<CartePoker> cartesHero = extraireCartesString(matcher.group("cartesHero"));

        return new InfosHero(
                matcher.group("nomHero"),
                cartesHero
        );
    }

    private static final Pattern patternAction = Pattern.compile(
            "(?<nomJoueur>[\\w\\s]+)\\s"+
                    "(?<action>bets|raises|calls|folds|checks|is all-In)" +
                    "(\\s+\\[(?<montant>[,.\\w\\s\\u20AC]+)])?"
    );

    @Override
    public ActionPokerJoueur extraireAction(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternAction, ligne);

        boolean betTotal = false;

        String montantString = matcher.group("montant");
        float montant = montantString != null ? Float.parseFloat(formatterStringValeur(montantString)) : 0;

        String nomAction = matcher.group("action");
        ActionPoker.TypeAction typeAction;

        if (Objects.equals(nomAction, "folds")) {
            typeAction = ActionPoker.TypeAction.FOLD;
            betTotal = true;
        }

        else if (Objects.equals(nomAction, "checks")) {
            typeAction = ActionPoker.TypeAction.CHECK;
            betTotal = true;
        }

        else if (Objects.equals(nomAction, "calls")) {
            typeAction = ActionPoker.TypeAction.CALL;
        }

        else if (Objects.equals(nomAction, "bets") || Objects.equals(nomAction, "raises") || Objects.equals(nomAction, "is all-In") ) {
            typeAction = ActionPoker.TypeAction.RAISE;
        }

        else throw new ErreurRegex("Type d'action inconnue");

        return new ActionPokerAvecBet(
                matcher.group("nomJoueur"),
                typeAction,
                montant,
                betTotal
        );
    }

    private static final Pattern patternNouveauTour =
            Pattern.compile("\\*\\*\\sDealing\\s" +
                            "(?<nomTour>[\\w\\s]+)\\s" +
                            "\\*\\*" +
                            "(\\s\\[(?<cartesTour>[\\w\\s,]+)])?"
            );

    @Override
    public NouveauTour extraireNouveauTour(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternNouveauTour, ligne);

        TourPoker.RoundPoker roundPoker = TourPoker.RoundPoker.PREFLOP;
        String nomTour = matcher.group("nomTour");

        if (Objects.equals(nomTour, "Flop")) {
            roundPoker = TourPoker.RoundPoker.FLOP;
        }

        else if (Objects.equals(nomTour, "Turn")) {
            roundPoker = TourPoker.RoundPoker.TURN;
        }

        else if (Objects.equals(nomTour, "River")) {
            roundPoker = TourPoker.RoundPoker.RIVER;
        }

        List<CartePoker> cartesExtraites = new ArrayList<>();
        if (matcher.group("cartesTour") != null) {
            cartesExtraites = extraireCartesString(matcher.group("cartesTour"));
        }

        return new NouveauTour(roundPoker, cartesExtraites);
    }

    private static final Pattern patternCartesJoueur =
            Pattern.compile(
                    "(?<nomJoueur>[\\w\\s]+)\\s" +
                            "shows\\s" +
                            "\\[(?<cartesTour>[\\w\\s,]+)]"
            );

    public CartesJoueur extraireCartes(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternCartesJoueur, ligne);

        List<CartePoker> cartesExtraites = extraireCartesString(matcher.group("cartesTour"));

        return new CartesJoueur(
            matcher.group("nomJoueur"),
                cartesExtraites
        );
    }

    private static final Pattern patternResultat =
            Pattern.compile("(?<nomJoueur>[\\w\\s]+)\\swins\\s" +
                            "(\\u20AC)?(?<montant>[\\d.]+)\\s" +
                            "(chips|EUR)");


    @Override
    public ResultatJoueur extraireResultat(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternResultat, ligne);

        float montant = Float.parseFloat(matcher.group("montant"));

        return new ResultatJoueur(
                matcher.group("nomJoueur"),
                montant
        );
    }



    private static String formatterStringValeur(String stack) {
        return stack
                .replace("EUR", "")
                .replace(",", "")
                .replace("€", "");
    }

    private List<CartePoker> extraireCartesString(String cartesAsString) {
        cartesAsString = cartesAsString.replaceAll(",", "");
        List<CartePoker> cartesExtraites = new ArrayList<>();
        List<Character> carteUnique = new ArrayList<>();
        for (int i = 0; i < cartesAsString.length(); i++) {
            char c = cartesAsString.charAt(i);
            if (c != ' ') {
                carteUnique.add(c);
            }

            if (carteUnique.size() == 2) {
                cartesExtraites.add(new CartePoker(carteUnique.get(0), carteUnique.get(1)));
                carteUnique.clear();
            }
        }

        return cartesExtraites;
    }

}
