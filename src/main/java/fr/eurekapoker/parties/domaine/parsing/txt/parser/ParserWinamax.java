package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosMain;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosTable;
import fr.eurekapoker.parties.domaine.parsing.dto.ResultatJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.InfosMainWinamax;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.InfosTableWinamax;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.ResultatJoueurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurWinamax;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartie;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartieWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;

public class ParserWinamax extends ParserTxt {
    private final ExtracteurWinamax extracteurWinamax;
    public ParserWinamax(ObservateurParser observateurParser,
                         String[] lignesFichier,
                         InterpreteurLigne interpreteurWinamax,
                         ExtracteurLigne extracteurWinamax,
                         BuilderInfosPartie builderInfosPartie) {
        super(observateurParser, lignesFichier, interpreteurWinamax, extracteurWinamax, builderInfosPartie);
        this.extracteurWinamax = (ExtracteurWinamax) extracteurWinamax;
    }
    public ParserWinamax(ObservateurParser observateurParser, String[] lignesFichier) {
        super(observateurParser,
                lignesFichier,
                new InterpreteurWinamax(),
                new ExtracteurWinamax(),
                new BuilderInfosPartieWinamax()
        );
        this.extracteurWinamax = new ExtracteurWinamax();
    }

    protected void extraireLigne(int indexLigne) throws ErreurImport {
        String ligne = lignesFichier[indexLigne];
        interpreteurLigne.lireLigne(ligne);

        if (interpreteurLigne.ligneSansInfo()) return;

        if (interpreteurLigne.estNouvelleMain()) creerNouvelleMain(indexLigne);
        else if (interpreteurLigne.estFormat()) extraireInfosPartie(indexLigne);
        else if (interpreteurLigne.estJoueur()) ajouterJoueur(indexLigne);
        else if (interpreteurLigne.estNouveauTour()) creerNouveauTour(indexLigne);
        else if (interpreteurLigne.estBlindeAnte()) ajouterBlindeOuAnte(indexLigne);
        else if (interpreteurLigne.estAction()) ajouterAction(indexLigne);
        else if (interpreteurLigne.estResultat()) ajouterResultat(indexLigne);
        else if (interpreteurLigne.estCartesHero()) ajouterInfoshero(indexLigne);
    }

    private void creerNouvelleMain(int indexLigne) throws ErreurImport {
        if (indexLigne > 0) observateurParser.mainTerminee();
        InfosMainWinamax infosMain = extracteurWinamax.extraireInfosMain(lignesFichier[indexLigne]);
        MainPoker mainPoker = new MainPoker(infosMain.obtIdentifiantMain());
        observateurParser.ajouterMain(mainPoker, infosMain.obtMontantBb());

        FormatPoker formatPoker = new FormatPoker(infosMain.obtVariante(), infosMain.obtTypeTable());

        // on ne l'initialise qu'une seule fois
        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixFormatPoker(formatPoker);
            builderInfosPartie.fixNumeroTable(infosMain.obtNumeroTable());
            builderInfosPartie.fixDate(infosMain.obtDate());
            builderInfosPartie.fixBuyIn(infosMain.obtBuyIn());
            builderInfosPartie.fixAnte(infosMain.obtAnte());
            builderInfosPartie.fixRake(infosMain.obtRake());
        }
    }

    private void extraireInfosPartie(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        InfosTableWinamax infosTable = extracteurWinamax.extraireInfosTable(lignesFichier[indexLigne]);
        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixNombreJoueurs(infosTable.obtNombreJoueurs());
            builderInfosPartie.fixNomPartie(infosTable.obtNomTable());
            observateurParser.fixInfosPartie(builderInfosPartie.build());
        }

        int positionDealer = infosTable.obtPositionDealer();
        observateurParser.ajouterPositionDealer(positionDealer);
    }

    @Override
    public boolean peutLireFichier() {
        return lignesFichier[0].startsWith("Winamax Poker");
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.WINAMAX;
    }

    protected void ajouterResultat(int indexLigne) throws ErreurRegex {
        ResultatJoueurWinamax resultatJoueur = extracteurWinamax.extraireResultat(lignesFichier[indexLigne]);
        String nomJoueur = resultatJoueur.getNomJoueur();
        observateurParser.ajouterGains(nomJoueur, resultatJoueur.obtMontantGagne());
        observateurParser.ajouterCartes(nomJoueur, resultatJoueur.obtCartesJoueur());
    }
}
