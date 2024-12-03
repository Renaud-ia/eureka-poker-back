package fr.eurekapoker.parties.domaine.parsing.xml.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.dto.BlindeOuAnte;
import fr.eurekapoker.parties.domaine.parsing.dto.CartesJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosJoueurBetclic;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerAvecBet;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * connait où sont tous les éléments dans le fichier XML
 * connait l'emplacement des infos mais les extrait de manière brute
 * raise des erreurs si le format n'est pas comme attendu
 */
public class ExtracteurBetclic extends ExtracteurXml {
    public ExtracteurBetclic(Document document) {
        super(document);
    }

    public int extraireNombreSieges() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireEntierBalise(generalElement, "tablesize");
    }

    public String obtenirTypeJeu() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "gametype", true);
    }

    public String obtenirNomTournoi() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "tournamentname", false);
    }

    public String extraireNomPartie() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "tablename", true);
    }

    public String extraireBigBlind() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "bigblind", true);
    }

    public String extraireTotalBuyIn() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "totalbuyin", true);
    }

    public String extraireDate() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "startdate", true);
    }

    public String extraireNomHero() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "nickname", true);
    }

    public NodeList extraireMains() throws ErreurLectureFichier {
        NodeList mains = document.getElementsByTagName("game");

        if (mains.getLength() < 1) {
            throw new ErreurLectureFichier("Aucune main à extraire");
        }

        return mains;
    }

    public long extraireIdMain(Element gameElement) {
        return Long.parseLong(gameElement.getAttribute("gamecode"));
    }

    public BigDecimal extraireMontantBB(Element mainElement) throws ErreurLectureFichier {
        NodeList bigBlindNodes = mainElement.getElementsByTagName("bigblind");
        if (bigBlindNodes.getLength() == 1) {
            Node bigBlindNode = bigBlindNodes.item(0);
            String bigBlindValue = bigBlindNode.getTextContent();

            return new BigDecimal(bigBlindValue);
        }

        throw new ErreurLectureFichier("Big blind non trouvée");
    }

    public NodeList extraireJoueurs() throws ErreurLectureFichier {
        NodeList joueurs = document.getElementsByTagName("players");

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


    public NouveauTour extraireInfoTour(Element tour) {
        TourPoker.RoundPoker round = convertirTour(Integer.parseInt(tour.getAttribute("no")));
        Element cartesBoard = (Element) tour.getElementsByTagName("cards").item(0);
        List<CartePoker> cartesExtraites = convertirNomCartes(cartesBoard.getTextContent());

        return new NouveauTour(round, cartesExtraites);
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

    public CartesJoueur extraireCartes(Element carteElement) {
        String nomJoueurCarte = carteElement.getAttribute("player");
        List<CartePoker> cartesExtraites = convertirNomCartes(carteElement.getTextContent());

        return new CartesJoueur(nomJoueurCarte, cartesExtraites);
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
            case 3:
            case 4:
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
