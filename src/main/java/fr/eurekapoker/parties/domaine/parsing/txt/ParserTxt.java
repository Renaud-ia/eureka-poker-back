package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.ParserModele;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
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
    public ParserTxt(String[] lignesFichier,
                     InterpreteurLigne interpreteurLigne,
                     ExtracteurLigne extracteurLigne,
                     BuilderInfosPartie builderInfosPartie) {
        super();
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

        calculerLaValueDesActionsDerniereMain();
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
        calculerLaValueDesActionsDerniereMain();
        InfosMain infosMain = extracteurLigne.extraireInfosMain(lignesFichier[indexLigne]);
        MainPoker mainPoker = new MainPoker(infosMain.obtIdentifiantMain());
        this.obtMains().add(mainPoker);

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
        }
    }

    private void ajouterJoueur(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        StackJoueur stackJoueur = extracteurLigne.extraireStackJoueur(lignesFichier[indexLigne]);
        String nomJoueur = stackJoueur.obtJoueur();
        MainPoker mainActuelle = this.obtMains().getLast();
        mainActuelle.ajouterJoueur(nomJoueur);
        mainActuelle.ajouterStackDepart(nomJoueur, stackJoueur.obtStack());
        if (stackJoueur.aBounty()) {
            mainActuelle.ajouterBounty(nomJoueur, stackJoueur.obtBounty());
        }
    }

    private void ajouterBlindeOuAnte(int indexLigne) throws ErreurLectureFichier, ErreurRegex {
        BlindeOuAnte blindeOuAnte = extracteurLigne.extraireBlindeOuAnte(lignesFichier[indexLigne]);
        MainPoker mainActuelle = obtMains().getLast();
        String nomJoueur = blindeOuAnte.getNomJoueur();

        if (blindeOuAnte.isBlinde()) {
            mainActuelle.ajouterBlinde(nomJoueur, blindeOuAnte.obtMontant());
        }

        else if (blindeOuAnte.isAnte()) {
            mainActuelle.ajouterAnte(nomJoueur, blindeOuAnte.obtMontant());
        }
    }

    private void ajouterCarteshero(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        List<CartePoker> cartesHero = extracteurLigne.extraireCartes(lignesFichier[indexLigne]);
        MainPoker mainActuelle = obtMains().getLast();
        mainActuelle.ajouterCartesHero(cartesHero);
    }

    private void creerNouveauTour(int indexLigne) throws ErreurRegex {
        List<CartePoker> cartesBoard = extracteurLigne.extraireBoardTour(lignesFichier[indexLigne]);
        BoardPoker board = new BoardPoker(cartesBoard);
        TourPoker nouveauTour = new TourPoker(board);
        MainPoker mainActuelle = obtMains().getLast();

        // IMPORTANT : on notifie qu'il y a des joueurs en moins sur la table
        int nombreJoueursFormat = this.obtInfosPartie().obtNombreJoueurs();
        int nombreJoueursTable = mainActuelle.obtJoueurs().size();

        while(nombreJoueursTable++ < nombreJoueursFormat) {
            nouveauTour.ajouterJoueurNonPresent();
        }

        mainActuelle.ajouterTour(nouveauTour);
    }

    private void ajouterAction(int indexLigne) throws ErreurRegex {
        ActionPoker actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
        TourPoker tourActuel = obtMains().getLast().obtTours().getLast();
        tourActuel.ajouterAction(actionPoker);
    }

    private void ajouterResultat(int indexLigne) throws ErreurRegex {
        ResultatJoueur resultatJoueur = extracteurLigne.extraireResultat(lignesFichier[indexLigne]);
        String nomJoueur = resultatJoueur.getNomJoueur();

        MainPoker mainActuelle = obtMains().getLast();

        mainActuelle.ajouterGains(nomJoueur, resultatJoueur.obtMontantGagne());
        mainActuelle.ajouterCartes(nomJoueur, resultatJoueur.obtCartesJoueur());
    }

    private void calculerLaValueDesActionsDerniereMain() {
        if (this.obtMains().size() > 1) {
            MainPoker derniereMain = this.obtMains().getLast();
            derniereMain.calculerLaValueDesActions();
        }
    }

}
