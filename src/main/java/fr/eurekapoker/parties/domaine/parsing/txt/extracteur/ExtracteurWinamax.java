package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.exceptions.FormatNonPrisEnCharge;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerAvecBet;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtracteurWinamax implements ExtracteurLigne {
    private static final Pattern patternPremiereLigne = Pattern.compile(
            "Winamax\\sPoker\\s-\\s" +
                    "(?<nomTournoi>(.(?!buyIn|- HandId))+)\\s" +
                    "(buyIn:\\s(?<buyInMTT>[\\d+\\s\\u20AC€.]+))?" +
                    "((.(?!HandId))+\\s)" +
                    "(HandId:\\s#(?<numeroTournoi>[\\d-]+))" +
                    "(\\s-\\s(?<nomVariante>[\\sa-zA-Z]+))" +
                    "(\\((?<valeursBlindes>[\\d/\\u20AC€.]+)\\))\\s-\\s" +
                    "((?<dateTournoi>(\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2})) UTC$)"
    );

    private static final Pattern patternInfosTable = Pattern.compile(
            "Table:\\s'(?<nomTable>.+)'\\s" +
                    "(?<nombreJoueurs>[0-9]+)-max" +
                    "\\s(.+)\\s" +
                    "Seat\\s#(?<positionDealer>[0-9]+)\\s.+"
    );

    private static final Pattern patternAction = Pattern.compile(
            "(?<playName>.+)\\s"+
                    "(?<action>bets|raises|calls|folds|checks)" +
                    "(\\s(?<bet>[\\d.]+))?[\\u20AC€]*"+
                    "(\\sto\\s(?<bet2>[\\d.]+))?[\\u20AC€]*"+
                    "(?<allIn>(.+all-in))?"
    );
    private static final Pattern patternInfoJoueur = Pattern.compile(
            "Seat\\s(?<seat>\\d+):" +
                    "\\s(?<playName>.+)\\s" +
                    "\\((?<stack>[\\d.]+)(\\u20AC)?" +
                    // si le bounty n'est pas en €, on n'en veut pas car pas exploitable
                    // donc il est hors du groupe de capture
                    "(,\\s((?<bounty>[\\d.]+)\\u20AC\\s(bounty)|[\\d.]+\\s(bounty))?)?\\)"
    );
    @Override
    public InfosMainWinamax extraireInfosMain(String ligne) throws ErreurImport {
        // todo : à refactoriser
        Matcher matcher = matcherRegex(patternPremiereLigne, ligne);

        // on trouve le format
        FormatPoker.TypeTable pokerFormat;
        String nomTournoi = matcher.group("nomTournoi");
        if (nomTournoi == null) throw new ErreurRegex("Aucun nom de tournoi trouvé");

        else if (nomTournoi.contains("CashGame")) {
            pokerFormat = FormatPoker.TypeTable.CASH_GAME;
        }
        // attention Expresso contient aussi Tournament
        else if (nomTournoi.contains("Expresso")) {
            pokerFormat = FormatPoker.TypeTable.SPIN;
        }

        else if (nomTournoi.contains("Tournament")) {
            pokerFormat = FormatPoker.TypeTable.MTT;
        }

        else {
            pokerFormat = FormatPoker.TypeTable.INCONNU;
        }

        // on trouve la variante
        FormatPoker.Variante variantePoker;
        String nomVariante = matcher.group("nomVariante");
        if (nomVariante == null) throw new ErreurRegex("Aucun nom de variante trouvé");
        if (nomVariante.contains("Holdem no limit")) {
            variantePoker = FormatPoker.Variante.HOLDEM_NO_LIMIT;
        }

        else {
            variantePoker = FormatPoker.Variante.INCONNU;
        }

        // on convertit le buy in et on récupère le rake
        // la procédure est différente selon MTT/SPIN ou Cash-Game
        // récupération manuelle ante/rake désactivé car relou
        float ante = 0;
        float rake = 0;
        float buyIn = 0;

        if (matcher.group("buyInMTT") != null) {
            String[] buyInParties = matcher.group("buyInMTT").split("\\+");
            for (String partieBuyIn : buyInParties) {
                buyIn += Float.parseFloat(partieBuyIn.replaceAll("[^\\d.]", ""));
            }

            rake = Float.parseFloat(buyInParties[1].replace("€", "")) / buyIn;

            if (pokerFormat == FormatPoker.TypeTable.MTT) {
                if (matcher.group("valeursBlindes") != null) {
                    String[] partiesBlindes = matcher.group("valeursBlindes").split("/");
                    // attention il y a des tournois sans Ante(starting block par ex)
                    if (partiesBlindes.length != 3) throw new FormatNonPrisEnCharge("Tournoi gratuit probablement");

                    ante = Float.parseFloat(partiesBlindes[0]);
                }

                else throw new ErreurRegex("Pas de blindes trouvées");
            }

            // en Spin, on n'a pas d'ANTE avec WINAMAX
            else {
                ante = 0f;
            }
        }

        // si cash-game
        else if (matcher.group("valeursBlindes") != null)  {
            String[] partiesBlindes = matcher.group("valeursBlindes").split("/");
            String montantBB = partiesBlindes[partiesBlindes.length - 1].replaceAll("[^\\d.]", "");
            buyIn = Float.parseFloat(montantBB);
        }

        else throw new ErreurRegex("Le buy in n'a pas été trouvée");


        // on récupère le numéro de main et de table
        long numeroTable;
        long numeroMain;
        if (matcher.group("numeroTournoi") == null) throw new ErreurRegex("Numéro de tournoi non trouvé");
        String[] idTournois = matcher.group("numeroTournoi").split("-");
        if (idTournois.length != 3) throw new ErreurRegex("Format numéro tournoi non conforme");
        numeroTable = Long.parseLong(idTournois[0]);
        numeroMain = Long.parseLong(idTournois[2]);

        // on récupère la date
        String dateTimeStr = matcher.group("dateTournoi");
        if (dateTimeStr == null) throw new ErreurRegex("Date non trouvée");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(dateTimeStr, formatter);


        return new InfosMainWinamax(
                variantePoker,
                pokerFormat,
                buyIn,
                date,
                numeroTable,
                numeroMain,
                ante,
                rake
        );
    }
    @Override
    public InfosTableWinamax extraireInfosTable(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternInfosTable, ligne);

        return new InfosTableWinamax(
                matcher.group("nomTable"),
                Integer.parseInt(matcher.group("nombreJoueurs")),
                Integer.parseInt(matcher.group("positionDealer"))
        );
    }

    private static final Pattern patternNomTour = Pattern.compile(
            "\\*\\*\\*\\s(?<nomTour>.+)\\s\\*\\*\\*");

    @Override
    public NouveauTour extraireNouveauTour(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternNomTour, ligne);
        String nomRound = matcher.group("nomTour");
        List<CartePoker> board = extraireCartes(ligne);
        if (board == null) board = new ArrayList<>();

        if (Objects.equals(nomRound, "PRE-FLOP")) return new NouveauTour(TourPoker.RoundPoker.PREFLOP, board);
        if (Objects.equals(nomRound, "FLOP")) return new NouveauTour(TourPoker.RoundPoker.FLOP, board);
        if (Objects.equals(nomRound, "TURN")) return new NouveauTour(TourPoker.RoundPoker.TURN, board);
        if (Objects.equals(nomRound, "RIVER")) return new NouveauTour(TourPoker.RoundPoker.RIVER, board);

        throw new ErreurRegex("Nom tour inconnu: " + nomRound);
    }



    @Override
    public InfosJoueur extraireStackJoueur(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternInfoJoueur, ligne);

        String joueurPoker = matcher.group("playName");

        String bountyString = (matcher.group("bounty"));
        if (bountyString == null) {
            return new InfosJoueur(
                    joueurPoker,
                    Double.parseDouble(matcher.group("stack")),
                    Integer.parseInt(matcher.group("seat")
                    )
            );
        }

        return new InfosJoueur(
                joueurPoker,
                Double.parseDouble(matcher.group("stack")),
                Float.parseFloat(bountyString),
                Integer.parseInt(matcher.group("seat"))
        );
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
            return new ActionPokerAvecBet(
                    nomJoueur, action, montantAction, totalBet
            );
        }

        throw new ErreurRegex("Nom de l'action inconnue dans :" + ligne);
    }

    private static final Pattern patternNomGain = Pattern.compile(
            "Seat\\s\\d:\\s(?<playName>(?:(?!\\sshowed\\s|\\swon\\s|[()]).)*)\\s.+");

    private static final Pattern patternGains = Pattern.compile("\\swon\\s(?<gains>[\\d.]+)");

    @Override
    public ResultatJoueur extraireResultat(String ligne) throws ErreurRegex {
        List<CartePoker> cartes = extraireCartes(ligne);
        if (cartes == null) cartes = new ArrayList<>();

        Matcher matcherNom = matcherRegex(patternNomGain, ligne);
        String nomJoueur = matcherNom.group("playName");

        Matcher matcherGains = patternGains.matcher(ligne);
        float gains;
        if (!matcherGains.find()) {
            gains = 0;
        }
        else {
            gains = Float.parseFloat(matcherGains.group("gains"));
        }

        return new ResultatJoueur(nomJoueur, gains, cartes);
    }

    private static final Pattern patternBlindesAntes = Pattern.compile(
            "(?<playName>.+)\\sposts\\s((?<blind>\\S*)\\s)(blind\\s)?(?<value>[\\d.]+)");

    @Override
    public BlindeOuAnte extraireBlindeOuAnte(String ligne) throws ErreurRegex {
        Matcher matcher = matcherRegex(patternBlindesAntes, ligne);
        String nomJoueur = matcher.group("playName");
        float montant = Float.parseFloat(matcher.group("value"));
        if (Objects.equals(matcher.group("blind"), "ante")) {
            return new BlindeOuAnte(nomJoueur, BlindeOuAnte.TypeTaxe.ANTE, montant);
        }
        else return new BlindeOuAnte(nomJoueur, BlindeOuAnte.TypeTaxe.BLINDE, montant);
    }

    private static final Pattern patternCartes = Pattern.compile(
            "\\[(?<cards>\\w{2}[\\s\\w{2}]*)](\\[(?<newCard>\\w{2})])?");

    @Override
    public List<CartePoker> extraireCartes(String ligne) throws ErreurRegex {
        Matcher matcher = patternCartes.matcher(ligne);
        if (!matcher.find()) {
            return null;
        }

        List<CartePoker> cartesTrouvees = new ArrayList<>();

        String[] cartesString = matcher.group("cards").split(" ");
        for (String carte : cartesString) {
            if (carte.length() != 2) throw new ErreurRegex("Le format de la carte n'est pas bon");
            CartePoker objetCarte = new CartePoker(carte.charAt(0), carte.charAt(1));
            cartesTrouvees.add(objetCarte);
        }

        String nouvelleCarte = matcher.group("newCard");
        if (nouvelleCarte != null) cartesTrouvees.add(new CartePoker(nouvelleCarte.charAt(0), nouvelleCarte.charAt(1)));

        return cartesTrouvees;
    }

    private static final Pattern patternNomHero = Pattern.compile(
            "Dealt\\sto\\s(?<nomHero>.+)\\s\\[.+");
    @Override
    public InfosHero extraireInfosHero(String ligne) throws ErreurRegex {
        Matcher nomHeroMatcher = matcherRegex(patternNomHero, ligne);
        String nomHero = nomHeroMatcher.group("nomHero");
        List<CartePoker> cartePokers = extraireCartes(ligne);

        return new InfosHero(nomHero, cartePokers);
    }

    private Matcher matcherRegex(Pattern pattern, String ligne) throws ErreurRegex {
        Matcher matcher = pattern.matcher(ligne);
        if (!matcher.find()) {
            throw new ErreurRegex("Regex non trouvé dans : " + ligne);
        }
        return matcher;
    }

}
