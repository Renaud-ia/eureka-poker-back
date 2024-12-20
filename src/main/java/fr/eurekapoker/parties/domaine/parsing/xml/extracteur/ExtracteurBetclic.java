package fr.eurekapoker.parties.domaine.parsing.xml.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.FormatNonPrisEnCharge;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.InfosJoueurBetclic;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.InfosTourBetclic;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.NomIdPartieBetclic;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerAvecBet;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * connait où sont tous les éléments dans le fichier XML
 * connait l'emplacement des infos mais les extrait de manière brute
 * raise des erreurs si le format n'est pas comme attendu
 */
public class ExtracteurBetclic extends ExtracteurXml {
    public int extraireNombreSieges(Document document) throws ErreurLectureFichier {
        Element generalElement = extraireElement(document, "general");
        return extraireEntierBalise(generalElement, "tablesize");
    }

    public FormatPoker.Variante obtenirTypeJeu(Document document) throws ErreurLectureFichier, FormatNonPrisEnCharge {
        Element generalElement = extraireElement(document,"general");
        String texteTypeJeu = extraireTexteBalise(generalElement, "gametype", true);
        if (texteTypeJeu.contains("Holdem NL")) {
            return FormatPoker.Variante.HOLDEM_NO_LIMIT;
        }
        else throw new FormatNonPrisEnCharge("Holdem no limit non détecté");
    }

    public FormatPoker.TypeTable obtenirTypeTable(Document document) throws ErreurLectureFichier, FormatNonPrisEnCharge {
        Element generalElement = extraireElement(document,"general");
        String nomTournoi = extraireTexteBalise(generalElement, "tournamentname", false);

        if (nomTournoi == null) {
            return FormatPoker.TypeTable.CASH_GAME;
        }

        if (nomTournoi.contains("Twister")) {
                return FormatPoker.TypeTable.SPIN;
        }
        // attention il y aussi les sit'n'go
        // TODO vérifier un jour que c'est bien ça qui apparaît
        // TODO être plus restritif sur MTT ??
        if (!nomTournoi.contains("Sit'n'Go")) {
            return FormatPoker.TypeTable.MTT;

        }
        else {
            throw new FormatNonPrisEnCharge("Sit'n'Go non twister");
        }
    }

    public NomIdPartieBetclic extraireNomIdPartie(Document document) throws ErreurLectureFichier {
        Element generalElement = extraireElement(document,"general");
        String nomPartieAvecId = extraireTexteBalise(generalElement, "tablename", true);

        Pattern regexIdNom = Pattern.compile("(?<nomPartie>.+),\\s(?<idTournoi>\\d+)");
        Matcher matcher = regexIdNom.matcher(nomPartieAvecId);

        if (!(matcher.find())) throw new ErreurLectureFichier("Nom et id de partie non trouvé");

        long idTournoi = Long.parseLong(matcher.group("idTournoi"));
        String nomPartie = matcher.group("nomPartie");

        return new NomIdPartieBetclic(nomPartie, idTournoi);
    }

    public BigDecimal extraireBigBlind(Document document) throws ErreurLectureFichier {
        Element generalElement = extraireElement(document,"general");
        String textBigBlinde = extraireTexteBalise(generalElement, "bigblind", true);

        return new BigDecimal(corrigerString(textBigBlinde));
    }

    public BigDecimal extraireTotalBuyIn(Document document) throws ErreurLectureFichier {
        Element generalElement = extraireElement(document, "general");
        String texteBuyIn = extraireTexteBalise(generalElement, "totalbuyin", true);

        return new BigDecimal(corrigerString(texteBuyIn));
    }

    public LocalDateTime extraireDate(Document document) throws ErreurLectureFichier {
        Element generalElement = extraireElement(document, "general");
        String dateString = extraireTexteBalise(generalElement, "startdate", true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }

    public String extraireNomHero(Document document) throws ErreurLectureFichier {
        Element generalElement = extraireElement(document, "general");
        return extraireTexteBalise(generalElement, "nickname", true);
    }

    public NodeList extraireMains(Document document) throws ErreurLectureFichier {
        NodeList mains = document.getElementsByTagName("game");

        if (mains.getLength() < 1) {
            throw new ErreurLectureFichier("Aucune main à extraire");
        }

        return mains;
    }

    public long extraireIdMain(Element gameElement) {
        return Long.parseLong(gameElement.getAttribute("gamecode"));
    }

    public BigDecimal extraireMontantBB(Document document, Element mainElement)
            throws ErreurLectureFichier, FormatNonPrisEnCharge {
        FormatPoker.TypeTable typeTable = this.obtenirTypeTable(document);
        if (typeTable == FormatPoker.TypeTable.CASH_GAME) {
            return extraireMontantBBGeneral(document);
        }

        return extraireMontantBBMain(mainElement);
    }

    private BigDecimal extraireMontantBBMain(Element mainElement) throws ErreurLectureFichier {
        NodeList bigBlindNodes = mainElement.getElementsByTagName("bigblind");
        if (bigBlindNodes.getLength() == 1) {
            Node bigBlindNode = bigBlindNodes.item(0);
            String bigBlindValue = bigBlindNode.getTextContent();

            return new BigDecimal(bigBlindValue);
        }

        throw new ErreurLectureFichier("Big blind non trouvée");
    }

    private BigDecimal extraireMontantBBGeneral(Document document) throws ErreurLectureFichier {
        return extraireBigBlind(document);
    }

    public NodeList extraireJoueurs(Element mainElement) throws ErreurLectureFichier {
        NodeList joueurs = mainElement.getElementsByTagName("player");

        if (joueurs.getLength() < 1) {
            throw new ErreurLectureFichier("Aucun joueur trouvé");
        }

        return joueurs;
    }

    public InfosJoueurBetclic extraireInfoJoueurs(Element joueurElement) {
        return new InfosJoueurBetclic(
                joueurElement.getAttribute("name"),
                Double.parseDouble(corrigerString(joueurElement.getAttribute("chips"))),
                Integer.parseInt(joueurElement.getAttribute("seat")),
                Integer.parseInt(joueurElement.getAttribute("dealer")) == 1,
                Double.parseDouble(corrigerString(joueurElement.getAttribute("win")))
         );
    }

    public NodeList extraireTours(Element mainElement) {
        return mainElement.getElementsByTagName("round");
    }

    public InfosTourBetclic extraireInfoTour(Element tour) {
        TourPoker.RoundPoker round = convertirTour(Integer.parseInt(tour.getAttribute("no")));
        Element cartesBoard = null;
        NodeList cartesElements = tour.getElementsByTagName("cards");
        for (int i = 0; i < cartesElements.getLength(); i++) {
            Element elementCarte = (Element) cartesElements.item(i);
            if (!elementCarte.getAttribute("type").equals("Pocket")) {
                cartesBoard = elementCarte;
                break;
            }
        }
        List<CartePoker> cartesExtraites = new ArrayList<>();
        if (cartesBoard != null) cartesExtraites = convertirNomCartes(cartesBoard.getTextContent());

        return new InfosTourBetclic(round, cartesExtraites);
    }

    private List<CartePoker> convertirNomCartes(String nomCartes) {
        String[] splittedCards = nomCartes.split("\\s+");
        List<CartePoker> cartes = new ArrayList<>();

        for (String splittedCard : splittedCards) {
            String strCards = splittedCard.trim();
            strCards = strCards.replace("10", "T");

            if ("X".equals(strCards)) {
                return new ArrayList<>();
            }
            cartes.add(new CartePoker(strCards.charAt(1), Character.toLowerCase(strCards.charAt(0))));
        }

        return cartes;
    }

    private TourPoker.RoundPoker convertirTour(int nTour) {
        return switch (nTour) {
            case 0 -> TourPoker.RoundPoker.BLINDES;
            case 1 -> TourPoker.RoundPoker.PREFLOP;
            case 2 -> TourPoker.RoundPoker.FLOP;
            case 3 -> TourPoker.RoundPoker.TURN;
            case 4 -> TourPoker.RoundPoker.RIVER;
            default -> throw new IllegalArgumentException("Round inconnu d'index : " + nTour);
        };
    }

    public NodeList extraireBlindesouAntes(Element tourElement) {
        return tourElement.getElementsByTagName("action");
    }

    public BlindeOuAnte extraireBlindeOuAnte(Element actionElement) {
        String nomJoueur = actionElement.getAttribute("player");
        int actionType = Integer.parseInt(actionElement.getAttribute("type"));
        float valeurAnte = Float.parseFloat(corrigerString(actionElement.getAttribute("sum")));

        if (actionType == 15) {
            return new BlindeOuAnte(nomJoueur, BlindeOuAnte.TypeTaxe.ANTE, valeurAnte);
        }

        return new BlindeOuAnte(nomJoueur, BlindeOuAnte.TypeTaxe.BLINDE, valeurAnte);
    }

    private String corrigerString(String stringOriginale) {
        return stringOriginale.replace(",", ".").replaceAll("[^\\d.]", "");
    }

    public NodeList extraireNoeudsCartes(Element tourElement) {
        return tourElement.getElementsByTagName("cards");
    }

    public CartesJoueur extraireCartes(Element carteElement) {
        String nomJoueurCarte = carteElement.getAttribute("player");
        List<CartePoker> cartesExtraites = convertirNomCartes(carteElement.getTextContent());

        return new CartesJoueur(nomJoueurCarte, cartesExtraites);
    }

    public NodeList extraireListeActions(Element tourElement) {
        return tourElement.getElementsByTagName("action");
    }

    public ActionPokerJoueur extraireAction(Element actionElement) {
        String nomJoueur = actionElement.getAttribute("player");
        int typeAction = Integer.parseInt(actionElement.getAttribute("type"));
        float montantBet = Float.parseFloat(corrigerString(actionElement.getAttribute("sum")));

        boolean totalBet = false;
        ActionPoker.TypeAction action;

        switch (typeAction) {
            case 0:
                action = ActionPoker.TypeAction.FOLD;
                break;
            case 4:
                action = ActionPoker.TypeAction.CHECK;
                break;
            case 3:
            case 7:
                action = ActionPoker.TypeAction.CALL;
                break;
            case 5:
            case 23:
                action = ActionPoker.TypeAction.RAISE;
                totalBet = true;
                break;
            default:
                throw new IllegalArgumentException("Action non reconnue d'index : " + typeAction);

        }
        return new ActionPokerAvecBet(nomJoueur, action, montantBet, totalBet);
    }
}
