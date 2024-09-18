package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.ParserModele;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.BoardPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.BuilderInfosPartie;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;

import java.util.List;

public abstract class ParserTxt extends ParserModele {
    protected final String[] lignesFichier;
    private final InterpreteurLigne interpreteurLigne;
    private final ExtracteurLigne extracteurLigne;
    private final BuilderInfosPartie builderInfosPartie;
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
        this.infosPartiePoker = this.builderInfosPartie.build();
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
        else if (interpreteurLigne.estCartesHero()) ajouterCarteshero(indexLigne);
    }

    private void creerNouvelleMain(int indexLigne) throws ErreurImport {
        observateurParser.mainTerminee();
        InfosMain infosMain = extracteurLigne.extraireInfosMain(lignesFichier[indexLigne]);
        MainPoker mainPoker = new MainPoker(infosMain.obtIdentifiantMain());
        observateurParser.ajouterMain(mainPoker);

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
            observateurParser.fixInfosPartie(this.obtInfosPartie());
        }
    }

    private void ajouterJoueur(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        StackJoueur stackJoueur = extracteurLigne.extraireStackJoueur(lignesFichier[indexLigne]);
        observateurParser.ajouterJoueur(stackJoueur);
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

    private void ajouterCarteshero(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        List<CartePoker> cartesHero = extracteurLigne.extraireCartes(lignesFichier[indexLigne]);
        observateurParser.ajouterCartesHero(cartesHero);
    }

    private void creerNouveauTour(int indexLigne) throws ErreurRegex {
        NouveauTour nouveauTour = extracteurLigne.extraireNouveauTour(lignesFichier[indexLigne]);
        observateurParser.ajouterTour(nouveauTour);
    }

    private void ajouterAction(int indexLigne) throws ErreurRegex {
        ActionPokerJoueur actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
        observateurParser.ajouterAction(actionPoker);
    }

    private void ajouterResultat(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        ResultatJoueur resultatJoueur = extracteurLigne.extraireResultat(lignesFichier[indexLigne]);
        String nomJoueur = resultatJoueur.getNomJoueur();
        observateurParser.ajouterGains(nomJoueur, resultatJoueur.obtMontantGagne());
        observateurParser.ajouterCartes(nomJoueur, resultatJoueur.obtCartesJoueur());
    }

}
