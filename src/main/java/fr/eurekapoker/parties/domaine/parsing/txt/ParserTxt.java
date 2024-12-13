package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.ParserModele;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartie;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.math.BigDecimal;

public abstract class ParserTxt extends ParserModele {
    protected final String[] lignesFichier;
    private final InterpreteurLigne interpreteurLigne;
    private final ExtracteurLigne extracteurLigne;
    private final BuilderInfosPartie builderInfosPartie;
    private BigDecimal montantBbActuel;
    public ParserTxt(ObservateurParser observateurParser,
                     String[] lignesFichier,
                     InterpreteurLigne interpreteurLigne,
                     ExtracteurLigne extracteurLigne,
                     BuilderInfosPartie builderInfosPartie) {
        super(observateurParser);
        this.lignesFichier = lignesFichier;
        this.interpreteurLigne = interpreteurLigne;
        this.extracteurLigne = extracteurLigne;
        this.builderInfosPartie = builderInfosPartie;
    }

    @Override
    protected void extraireMains() throws ErreurImport {
        for (int indexLigne = 0; indexLigne < lignesFichier.length; indexLigne++) {
            extraireLigne(indexLigne);
        }

        observateurParser.mainTerminee();
        observateurParser.partieTerminee();
    }

    private void extraireLigne(int indexLigne) throws ErreurImport {
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
        InfosMain infosMain = extracteurLigne.extraireInfosMain(lignesFichier[indexLigne]);
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
        InfosTable infosTable = extracteurLigne.extraireInfosTable(lignesFichier[indexLigne]);
        if (builderInfosPartie.donneesIncompletes()) {
            builderInfosPartie.fixNombreJoueurs(infosTable.obtNombreJoueurs());
            builderInfosPartie.fixNomPartie(infosTable.obtNomTable());
            observateurParser.fixInfosPartie(builderInfosPartie.build());
        }

        int positionDealer = infosTable.obtPositionDealer();
        observateurParser.ajouterPositionDealer(positionDealer);
    }

    private void ajouterJoueur(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        InfosJoueur infosJoueur = extracteurLigne.extraireStackJoueur(lignesFichier[indexLigne]);
        observateurParser.ajouterJoueur(infosJoueur);
    }

    private void ajouterBlindeOuAnte(int indexLigne) throws ErreurLectureFichier, ErreurRegex {
        BlindeOuAnte blindeOuAnte = extracteurLigne.extraireBlindeOuAnte(lignesFichier[indexLigne]);
        String nomJoueur = blindeOuAnte.getNomJoueur();

        if (blindeOuAnte.isBlinde()) {
            observateurParser.ajouterBlinde(nomJoueur, blindeOuAnte.obtMontant());
        }

        else if (blindeOuAnte.isAnte()) {
            observateurParser.ajouterAnte(nomJoueur, blindeOuAnte.obtMontant());
        }
    }

    private void ajouterInfoshero(int indexLigne) throws ErreurRegex {
        InfosHero infosHero = extracteurLigne.extraireInfosHero(lignesFichier[indexLigne]);
        observateurParser.ajouterHero(infosHero.obtNomHero(), infosHero.obtCartesHero());
    }

    private void creerNouveauTour(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        NouveauTour nouveauTour = extracteurLigne.extraireNouveauTour(lignesFichier[indexLigne]);
        observateurParser.ajouterTour(nouveauTour);
    }

    private void ajouterAction(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        ActionPokerJoueur actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
        observateurParser.ajouterAction(actionPoker);
    }

    private void ajouterResultat(int indexLigne) throws ErreurRegex {
        ResultatJoueur resultatJoueur = extracteurLigne.extraireResultat(lignesFichier[indexLigne]);
        String nomJoueur = resultatJoueur.getNomJoueur();
        observateurParser.ajouterGains(nomJoueur, resultatJoueur.obtMontantGagne());
        observateurParser.ajouterCartes(nomJoueur, resultatJoueur.obtCartesJoueur());
    }

}
