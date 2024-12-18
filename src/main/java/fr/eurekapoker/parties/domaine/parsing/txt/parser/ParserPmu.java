package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.CartesJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosTable;
import fr.eurekapoker.parties.domaine.parsing.dto.ResultatJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.pmu.InfosMainPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurPmu;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartie;

import java.math.BigDecimal;

public class ParserPmu extends ParserTxt {
    private final InterpreteurPmu interpreteurPmu;
    private final ExtracteurPmu extracteurPmu;
    public ParserPmu(ObservateurParser observateurParser,
                         String[] lignesFichier,
                         InterpreteurLigne interpreteurPmu,
                         ExtracteurLigne extracteurPmu,
                         BuilderInfosPartie builderInfosPartie) {
        super(observateurParser, lignesFichier, interpreteurPmu, extracteurPmu, builderInfosPartie);
        this.interpreteurPmu = (InterpreteurPmu) interpreteurPmu;
        this.extracteurPmu = (ExtracteurPmu) extracteurPmu;
    }

    @Override
    protected void extraireLigne(int indexLigne) throws ErreurImport {
        String ligne = lignesFichier[indexLigne];
        interpreteurLigne.lireLigne(ligne);

        if (interpreteurLigne.ligneSansInfo()) return;

        if (interpreteurPmu.estNumeroPartie()) extraireNumeroPartie(indexLigne);
        else if (interpreteurLigne.estNouvelleMain()) creerNouvelleMain(indexLigne);
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
        if (indexLigne > 0) observateurParser.mainTerminee();

        long idMain = extracteurPmu.extraireIdMain(lignesFichier[indexLigne]);
        MainPoker mainPoker = new MainPoker(idMain);
        observateurParser.ajouterMain(mainPoker);
    }

    private void extraireInfosPartie(int indexLigne) throws ErreurRegex {
        InfosMainPmu infosMainPmu = extracteurPmu.extraireInfosMain(lignesFichier[indexLigne]);

        FormatPoker formatPoker = new FormatPoker(infosMainPmu.obtVariante(), infosMainPmu.obtTypeJeu());

        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixFormatPoker(formatPoker);
            builderInfosPartie.fixBuyIn(infosMainPmu.obtBuyIn());
            builderInfosPartie.fixDate(infosMainPmu.obtDate());
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
    public boolean peutLireFichier() {
        if (lignesFichier.length == 0) return false;
        String pattern = "Game #\\d+ starts\\.";

        return lignesFichier[0].replaceAll("\r", "").matches(pattern);
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.PMU;
    }
}
