package fr.eurekapoker.parties.domaine.parsing.xml.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.FormatNonPrisEnCharge;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.xml.ParserXml;
import fr.eurekapoker.parties.domaine.parsing.xml.extracteur.ExtracteurBetclic;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartieBetclic;
import fr.eurekapoker.parties.domaine.poker.parties.infos.InfosPartiePoker;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserBetclic extends ParserXml {
    private String nomHero;
    private final ExtracteurBetclic extracteurBetclic;
    public ParserBetclic(ObservateurParser observateurParser, Document document, ExtracteurBetclic extracteurBetclic) {
        super(observateurParser, document);
        this.extracteurBetclic = extracteurBetclic;
    }

    @Override
    protected void extraireMains() throws ErreurImport {
        InfosPartiePoker infosPartiePoker = extraireInfosPartie();
        observateurParser.fixInfosPartie(infosPartiePoker);
        this.nomHero = extracteurBetclic.extraireNomHero();

        NodeList mains = extracteurBetclic.extraireMains();
        for (int i = 0; i < mains.getLength(); i++) {
            Element mainElement = (Element) mains.item(i);
            long idMain = extracteurBetclic.extraireIdMain(mainElement);
            MainPoker mainPoker = new MainPoker(idMain);
            BigDecimal montantBB = extracteurBetclic.extraireMontantBB(mainElement);
            this.observateurParser.ajouterMain(mainPoker, montantBB);
            this.extraireJoueurs(mainElement);
            this.extraireTours(mainElement);
        }
    }

    private void extraireJoueurs(Element mainElement) throws ErreurLectureFichier {
        NodeList noeudsJoueurs = extracteurBetclic.extraireJoueurs();
        for (int i = 0; i < noeudsJoueurs.getLength(); i++) {
            Element joueurElement = (Element) noeudsJoueurs.item(i);
            InfosJoueurBetclic infosJoueur = extracteurBetclic.extraireInfoJoueurs(joueurElement);
            this.observateurParser.ajouterJoueur(infosJoueur);

            if (infosJoueur.obtDealer()) this.observateurParser.ajouterPositionDealer(infosJoueur.obtSiege());
            this.observateurParser.ajouterGains(infosJoueur.obtJoueur(), infosJoueur.obtGains());
        }
    }

    private void extraireTours(Element mainElement) throws ErreurLectureFichier {
        NodeList tourElements = mainElement.getElementsByTagName("round");

        for (int i = 0; i < tourElements.getLength(); i++) {
            Element tourElement = (Element) tourElements.item(i);
            NouveauTour nouveauTour = extracteurBetclic.extraireInfoTour(tourElement);
            this.observateurParser.ajouterTour(nouveauTour);

            TourPoker.RoundPoker roundPoker = nouveauTour.obtRound();

            if (roundPoker == TourPoker.RoundPoker.BLINDES) {
                extraireBlindesAnte(tourElement);
                continue;
            }

            if (roundPoker == TourPoker.RoundPoker.PREFLOP) {
                extraireCartesJoueurs(tourElement);
            }

            extraireActions(tourElement);
        }
    }

    private void extraireBlindesAnte(Element tourElement) throws ErreurLectureFichier {
        NodeList actionsElements = tourElement.getElementsByTagName("action");

        for (int i = 0; i < actionsElements.getLength(); i++) {
            Element actionElement = (Element) actionsElements.item(i);
            BlindeOuAnte blindeOuAnte = extracteurBetclic.extraireBlindeOuAnte(actionElement);
            if (blindeOuAnte.isBlinde()) {
                this.observateurParser.ajouterBlinde(blindeOuAnte.getNomJoueur(), blindeOuAnte.obtMontant());
            }
            if (blindeOuAnte.isAnte()) {
                this.observateurParser.ajouterAnte(blindeOuAnte.getNomJoueur(), blindeOuAnte.obtMontant());
            }
        }
    }

    private void extraireCartesJoueurs(Element tourElement) {
        NodeList cartesElements = tourElement.getElementsByTagName("cards");

        for (int i = 0; i < cartesElements.getLength(); i++) {
            Element carteElement = (Element) cartesElements.item(i);
            CartesJoueur cartes = extracteurBetclic.extraireCartes(carteElement);

            this.observateurParser.ajouterCartes(cartes.obtNomJoueur(), cartes.obtCartes());

            if (Objects.equals(cartes.obtNomJoueur(), this.nomHero)) {
                this.observateurParser.ajouterHero(cartes.obtNomJoueur(), cartes.obtCartes());
            }
        }
    }

    private void extraireActions(Element tourElement) throws ErreurLectureFichier {
        NodeList actionsElements = tourElement.getElementsByTagName("action");

        for (int i = 0; i < actionsElements.getLength(); i++) {
            Element actionElement = (Element) actionsElements.item(i);
            ActionPokerJoueur actionPokerJoueur = extracteurBetclic.extraireAction(actionElement);
            this.observateurParser.ajouterAction(actionPokerJoueur);
        }
    }

    private InfosPartiePoker extraireInfosPartie() throws ErreurLectureFichier, FormatNonPrisEnCharge {
        BuilderInfosPartieBetclic builderInfosPartie = new BuilderInfosPartieBetclic();

        FormatPoker.TypeTable typeTable = this.extraireFormatPoker(builderInfosPartie);

        this.extraireIdPartie(builderInfosPartie);
        this.extraireNombreSieges(builderInfosPartie);
        this.extraireDate(builderInfosPartie);

        this.extraireAnte(builderInfosPartie, typeTable);
        this.extraireRake(builderInfosPartie, typeTable);

        this.extraireBuyIn(builderInfosPartie, typeTable);

        return builderInfosPartie.build();
    }

    private void extraireDate(BuilderInfosPartieBetclic builderInfosPartie)
            throws ErreurLectureFichier {
        String dateString = extracteurBetclic.extraireDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTournoi = LocalDateTime.parse(dateString, formatter);

        builderInfosPartie.fixDate(dateTournoi);
    }

    private void extraireRake(
            BuilderInfosPartieBetclic builderInfosPartie,
            FormatPoker.TypeTable typeTable) {
    }

    private void extraireAnte(
            BuilderInfosPartieBetclic builderInfosPartie,
            FormatPoker.TypeTable typeTable) {
    }

    private void extraireBuyIn(
            BuilderInfosPartieBetclic builderInfosPartie,
            FormatPoker.TypeTable typeTable
    ) throws ErreurLectureFichier {
        String buyIn;

        if (typeTable == FormatPoker.TypeTable.CASH_GAME) {
            buyIn = extracteurBetclic.extraireBigBlind();
        }

        else {
            buyIn = extracteurBetclic.extraireTotalBuyIn();
        }

        builderInfosPartie.fixBuyIn(corrigerMontantEuros(buyIn));
    }

    private BigDecimal corrigerMontantEuros(String buyIn) {
        String buyInCorrige = buyIn.replace(",", ".").replaceAll("[^\\d.]", "");
        return new BigDecimal(buyInCorrige);
    }

    private void extraireNombreSieges(BuilderInfosPartieBetclic builderInfosPartie)
            throws ErreurLectureFichier {
        int nombreSieges = extracteurBetclic.extraireNombreSieges();
        builderInfosPartie.fixNombreJoueurs(nombreSieges);
    }

    private void extraireIdPartie(BuilderInfosPartieBetclic builderInfosPartie) throws ErreurLectureFichier {
        String nomPartieAvecId = extracteurBetclic.extraireNomPartie();
        Pattern regexIdNom = Pattern.compile("(?<nomPartie>.+),\\s(?<idTournoi>\\d+)");
        Matcher matcher = regexIdNom.matcher(nomPartieAvecId);

        if (!(matcher.find())) throw new ErreurLectureFichier("Nom et id de partie non trouvé");

        long idTournoi = Long.parseLong(matcher.group("idTournoi"));
        String nomPartie = matcher.group("nomPartie");

        builderInfosPartie.fixNumeroTable(idTournoi);
        builderInfosPartie.fixNomPartie(nomPartie);
    }

    private FormatPoker.TypeTable extraireFormatPoker(BuilderInfosPartieBetclic builderInfosPartie)
            throws FormatNonPrisEnCharge, ErreurLectureFichier {
        FormatPoker.Variante variantePoker;
        String gameType = extracteurBetclic.obtenirTypeJeu();
        if (gameType.contains("Holdem NL")) {
            variantePoker = FormatPoker.Variante.HOLDEM_NO_LIMIT;
        }
        else throw new FormatNonPrisEnCharge("Holdem no limit non détecté");

        FormatPoker.TypeTable typeTable;
        String nomTournoi = extracteurBetclic.obtenirNomTournoi();
        if (nomTournoi == null) {
            typeTable = FormatPoker.TypeTable.CASH_GAME;
        }

        else {
            if (nomTournoi.contains("Twister")) {
                typeTable = FormatPoker.TypeTable.SPIN;
            }
            // attention il y aussi les sit'n'go
            // todo vérifier un jour que c'est bien ça qui apparaît
            else if (nomTournoi.contains("Sit'n'Go")) {
                throw new FormatNonPrisEnCharge("Sit'n'Go non twister");
            }
            else {
                typeTable = FormatPoker.TypeTable.MTT;
            }
        }

        builderInfosPartie.fixFormatPoker(new FormatPoker(variantePoker, typeTable));

        return typeTable;
    }

    private FormatPoker detecterFormat() {
        return null;
    }

    @Override
    public boolean peutLireFichier() {
        return true;
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.BETCLIC;
    }
}
