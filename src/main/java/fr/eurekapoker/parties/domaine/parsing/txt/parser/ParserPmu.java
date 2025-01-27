package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.CartesJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosTable;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.parsing.dto.ResultatJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.pmu.InfosMainPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurPmu;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartie;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ParserPmu extends ParserTxt {
    private boolean erreurLectureMain;
    private final InterpreteurPmu interpreteurPmu;
    private final ExtracteurPmu extracteurPmu;
    private final List<CartePoker> board;
    public ParserPmu(ObservateurParser observateurParser,
                         String[] lignesFichier,
                         InterpreteurLigne interpreteurPmu,
                         ExtracteurLigne extracteurPmu,
                         BuilderInfosPartie builderInfosPartie) {
        super(observateurParser, lignesFichier, interpreteurPmu, extracteurPmu, builderInfosPartie);
        this.interpreteurPmu = (InterpreteurPmu) interpreteurPmu;
        this.extracteurPmu = (ExtracteurPmu) extracteurPmu;
        this.erreurLectureMain = false;
        this.board = new ArrayList<>();
    }

    @Override
    protected void extraireLigne(int indexLigne) throws ErreurImport {
        String ligne = lignesFichier[indexLigne];
        interpreteurLigne.lireLigne(ligne);

        if (interpreteurLigne.ligneSansInfo()) return;

        if (interpreteurLigne.estNouvelleMain()) creerNouvelleMain(indexLigne);
        // on veut sauter la main si elle est erronée
        else if (erreurLectureMain) return;
        else if (interpreteurPmu.estNumeroPartie()) extraireNumeroPartie(indexLigne);
        else if (interpreteurLigne.estFormat()) extraireInfosPartie(indexLigne);
        else if (interpreteurPmu.estInfosTable()) extraireInfosTable(indexLigne);
        else if (interpreteurPmu.estNombreJoueurs()) extraireNombreJoueurs(indexLigne);
        else if (interpreteurPmu.estMontantBlindes()) extraireMontantBlindes(indexLigne);
        else if (interpreteurLigne.estJoueur()) ajouterJoueur(indexLigne);
        else if (interpreteurPmu.estPositionDealer()) ajouterDealer(indexLigne);
        else if (interpreteurLigne.estNouveauTour()) creerNouveauTour(indexLigne);
        else if (interpreteurLigne.estBlindeAnte()) ajouterBlindeOuAnte(indexLigne);
        else if (interpreteurLigne.estAction()) ajouterAction(indexLigne);
        else if (interpreteurLigne.estResultat()) ajouterResultat(indexLigne);
        else if (interpreteurPmu.estCartesJoueur()) ajouterCartesJoueur(indexLigne);
        else if (interpreteurLigne.estCartesHero()) ajouterInfoshero(indexLigne);
    }

    private void extraireNumeroPartie(int indexLigne) throws ErreurRegex {
        long numeroPartie = extracteurPmu.extraireNumeroPartie(lignesFichier[indexLigne]);

        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixNumeroTable(numeroPartie);
        }
    }

    private void creerNouvelleMain(int indexLigne) throws ErreurImport {
        board.clear();
        if (indexLigne > 0 && !erreurLectureMain) observateurParser.mainTerminee();

        long idMain = extracteurPmu.extraireIdMain(lignesFichier[indexLigne]);
        MainPoker mainPoker = new MainPoker(idMain);
        observateurParser.ajouterMain(mainPoker);
        this.erreurLectureMain = false;
    }

    private void extraireInfosPartie(int indexLigne) throws ErreurRegex {
        InfosMainPmu infosMainPmu = extracteurPmu.extraireInfosMain(lignesFichier[indexLigne]);

        FormatPoker formatPoker = new FormatPoker(infosMainPmu.obtVariante(), infosMainPmu.obtTypeJeu());

        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixFormatPoker(formatPoker);
            builderInfosPartie.fixBuyIn(infosMainPmu.obtBuyIn());
            builderInfosPartie.fixDate(infosMainPmu.obtDate());
        }

        if (formatPoker.obtTypeTable() == FormatPoker.TypeTable.CASH_GAME) {
            this.observateurParser.ajouterMontantBB(infosMainPmu.obtBuyIn());
        }
    }

    private void extraireInfosTable(int indexLigne) throws ErreurRegex {
        InfosTable infosTable = extracteurPmu.extraireInfosTable(lignesFichier[indexLigne]);

        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixNomPartie(infosTable.obtNomTable());
        }
    }

    private void extraireNombreJoueurs(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        int nombreJoueurs = extracteurPmu.extraireNombreJoueurs(lignesFichier[indexLigne]);

        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixNombreJoueurs(nombreJoueurs);
        }

        this.observateurParser.fixInfosPartie(builderInfosPartie.build());
    }

    private void extraireMontantBlindes(int indexLigne) throws ErreurRegex {
        BigDecimal montantBB = extracteurPmu.extraireBigBlinde(lignesFichier[indexLigne]);

        observateurParser.ajouterMontantBB(montantBB);
    }

    private void ajouterDealer(int indexLigne) throws ErreurRegex {
        int siegeDealer = extracteurPmu.extraireSiegeDealer(lignesFichier[indexLigne]);

        observateurParser.ajouterPositionDealer(siegeDealer);
    }

    private void ajouterResultat(int indexLigne) throws ErreurRegex {
        ResultatJoueur resultatJoueur = extracteurPmu.extraireResultat(lignesFichier[indexLigne]);

        observateurParser.ajouterGains(resultatJoueur.getNomJoueur(), resultatJoueur.obtMontantGagne());
    }

    private void ajouterCartesJoueur(int indexLigne) throws ErreurRegex {
        CartesJoueur cartesJoueur = extracteurPmu.extraireCartes(lignesFichier[indexLigne]);

        observateurParser.ajouterCartes(cartesJoueur.obtNomJoueur(), cartesJoueur.obtCartes());
    }

    @Override
    protected void creerNouveauTour(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        NouveauTour nouveauTour = extracteurLigne.extraireNouveauTour(lignesFichier[indexLigne], board);
        observateurParser.ajouterTour(nouveauTour);
    }

    @Override
    public boolean peutLireFichier() {
        if (lignesFichier.length == 0) return false;

        String pattern = "(Game #\\d+ starts\\.|#Game No : \\d+\\s*)";

        Boolean condition = lignesFichier[0].replaceAll("\r", "").matches(pattern);
        return condition;
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.PARTY_GAMING;
    }

    @Override
    protected void ajouterAction(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        try {
            ActionPokerJoueur actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
            observateurParser.ajouterAction(actionPoker);
        }

        // BUG DE PMU => obligé de supprimer la main
        catch (JoueurNonExistant joueurNonExistant) {
            this.observateurParser.supprimerDerniereMain();
            this.erreurLectureMain = true;
        }

    }
}
