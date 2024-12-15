package fr.eurekapoker.parties.domaine.parsing.xml.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.FormatNonPrisEnCharge;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.InfosJoueurBetclic;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.InfosTourBetclic;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.NomIdPartieBetclic;
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
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParserBetclic extends ParserXml {
    private String nomHero;
    private final ExtracteurBetclic extracteurBetclic;
    private final List<CartePoker> cartesBoard;
    public ParserBetclic(ObservateurParser observateurParser, Document document, ExtracteurBetclic extracteurBetclic) {
        super(observateurParser, document);
        this.extracteurBetclic = extracteurBetclic;
        this.cartesBoard = new ArrayList<>();
    }

    @Override
    protected void extraireMains() throws ErreurImport {
        InfosPartiePoker infosPartiePoker = extraireInfosPartie();
        observateurParser.fixInfosPartie(infosPartiePoker);
        this.nomHero = extracteurBetclic.extraireNomHero(this.document);

        NodeList mains = extracteurBetclic.extraireMains(this.document);
        for (int i = 0; i < mains.getLength(); i++) {
            Element mainElement = (Element) mains.item(i);
            long idMain = extracteurBetclic.extraireIdMain(mainElement);
            MainPoker mainPoker = new MainPoker(idMain);
            BigDecimal montantBB = extracteurBetclic.extraireMontantBB(this.document, mainElement);
            this.observateurParser.ajouterMain(mainPoker, montantBB);
            this.extraireJoueurs(mainElement);
            this.extraireTours(mainElement);

            this.observateurParser.mainTerminee();
            this.reinitialiserMain();
        }
    }

    private void reinitialiserMain() {
        this.cartesBoard.clear();
    }

    private void extraireJoueurs(Element mainElement) throws ErreurLectureFichier {
        NodeList noeudsJoueurs = extracteurBetclic.extraireJoueurs(mainElement);
        for (int i = 0; i < noeudsJoueurs.getLength(); i++) {
            Element joueurElement = (Element) noeudsJoueurs.item(i);
            InfosJoueurBetclic infosJoueur = extracteurBetclic.extraireInfoJoueurs(joueurElement);
            this.observateurParser.ajouterJoueur(infosJoueur);

            if (infosJoueur.obtDealer()) this.observateurParser.ajouterPositionDealer(infosJoueur.obtSiege());
            this.observateurParser.ajouterGains(infosJoueur.obtJoueur(), infosJoueur.obtGains());
        }
    }

    private void extraireTours(Element mainElement) throws ErreurLectureFichier {
        NodeList tourElements = extracteurBetclic.extraireTours(mainElement);

        for (int i = 0; i < tourElements.getLength(); i++) {
            Element tourElement = (Element) tourElements.item(i);
            InfosTourBetclic infosTour = extracteurBetclic.extraireInfoTour(tourElement);
            this.cartesBoard.addAll(infosTour.obtCartesExtraites());

            NouveauTour nouveauTour = new NouveauTour(infosTour.obtRoundPoker(), this.cartesBoard);
            TourPoker.RoundPoker roundPoker = nouveauTour.obtRound();

            if (roundPoker == TourPoker.RoundPoker.BLINDES) {
                extraireBlindesAnte(tourElement);
                continue;
            }

            this.observateurParser.ajouterTour(nouveauTour);
            if (roundPoker == TourPoker.RoundPoker.PREFLOP) {
                extraireCartesJoueurs(tourElement);
            }

            extraireActions(tourElement);
        }
    }

    private void extraireBlindesAnte(Element tourElement) throws ErreurLectureFichier {
        NodeList actionsElements = extracteurBetclic.extraireBlindesouAntes(tourElement);

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
        NodeList cartesElements = extracteurBetclic.extraireNoeudsCartes(tourElement);

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
        NodeList actionsElements = extracteurBetclic.extraireListeActions(tourElement);

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
        LocalDateTime dateTournoi = extracteurBetclic.extraireDate(this.document);
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
        BigDecimal buyIn;

        if (typeTable == FormatPoker.TypeTable.CASH_GAME) {
            buyIn = extracteurBetclic.extraireBigBlind(this.document);
        }

        else {
            buyIn = extracteurBetclic.extraireTotalBuyIn(this.document);
        }

        builderInfosPartie.fixBuyIn(buyIn);
    }

    private void extraireNombreSieges(BuilderInfosPartieBetclic builderInfosPartie)
            throws ErreurLectureFichier {
        int nombreSieges = extracteurBetclic.extraireNombreSieges(this.document);
        builderInfosPartie.fixNombreJoueurs(nombreSieges);
    }

    private void extraireIdPartie(BuilderInfosPartieBetclic builderInfosPartie) throws ErreurLectureFichier {
        NomIdPartieBetclic nomPartieAvecId = extracteurBetclic.extraireNomIdPartie(this.document);
        builderInfosPartie.fixNomPartie(nomPartieAvecId.obtNomPartie());
        builderInfosPartie.fixNumeroTable(nomPartieAvecId.obtIdPartie());
    }

    private FormatPoker.TypeTable extraireFormatPoker(BuilderInfosPartieBetclic builderInfosPartie)
            throws FormatNonPrisEnCharge, ErreurLectureFichier {
        FormatPoker.Variante variantePoker = extracteurBetclic.obtenirTypeJeu(this.document);

        FormatPoker.TypeTable typeTable = extracteurBetclic.obtenirTypeTable(this.document);
        builderInfosPartie.fixFormatPoker(new FormatPoker(variantePoker, typeTable));

        return typeTable;
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
