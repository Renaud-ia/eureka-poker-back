package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.ParserModele;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.poker.*;

import java.util.Objects;

public abstract class ParserTxt extends ParserModele {
    protected final String[] lignesFichier;
    private final InterpreteurLigne interpreteurLigne;
    private final ExtracteurLigne extracteurLigne;

    public ParserTxt(String[] lignesFichier,
                     InterpreteurLigne interpreteurLigne,
                     ExtracteurLigne extracteurLigne) {
        super();
        this.lignesFichier = lignesFichier;
        this.interpreteurLigne = interpreteurLigne;
        this.extracteurLigne = extracteurLigne;
    }

    @Override
    protected void extraireMains() throws ErreurImport {
        for (int indexLigne = 0; indexLigne < lignesFichier.length; indexLigne++) {
            extraireLigne(indexLigne);
        }

        calculerLaValueDesActions();
    }

    private void extraireLigne(int indexLigne) throws ErreurImport {
        String ligne = lignesFichier[indexLigne];
        interpreteurLigne.lireLigne(ligne);

        if (interpreteurLigne.ligneSansInfo()) return;

        if (interpreteurLigne.estFormat()) extraireFormat(indexLigne);
        else if (interpreteurLigne.estNouvelleMain()) creerNouvelleMain(indexLigne);
        else if (interpreteurLigne.estJoueur()) ajouterJoueur(indexLigne);
        else if (interpreteurLigne.estNouveauTour()) creerNouveauTour(indexLigne);
        else if (interpreteurLigne.estBlindeAnte()) ajouterBlindeOuAnte(indexLigne);
        else if (interpreteurLigne.estAction()) ajouterAction(indexLigne);
        else if (interpreteurLigne.estResultat()) ajouterResultat(indexLigne);
    }

    private void extraireFormat(int indexLigne) throws ErreurRegex {
        this.formatPoker =  extracteurLigne.extraireFormat(lignesFichier[indexLigne]);
    }

    private void creerNouvelleMain(int indexLigne) throws ErreurRegex {
        MainPoker mainPoker = extracteurLigne.extraireMain(lignesFichier[indexLigne]);
        this.mainsExtraites.add(mainPoker);
    }

    private void ajouterJoueur(int indexLigne) throws ErreurRegex {
        JoueurPoker joueurPoker = extracteurLigne.extraireJoueur(lignesFichier[indexLigne]);
        MainPoker mainActuelle = obtMains().getLast();
        mainActuelle.ajouterJoueur(joueurPoker);
    }

    private void creerNouveauTour(int indexLigne) throws ErreurRegex {
        TourPoker tourPoker = extracteurLigne.extraireTour(lignesFichier[indexLigne]);
        MainPoker mainActuelle = obtMains().getLast();
        mainActuelle.ajouterTour(tourPoker);
    }

    private void ajouterAction(int indexLigne) throws ErreurRegex {
        ActionPoker actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
        this.ajouterActionAvecMontantDejaInvesti(actionPoker);
    }

    private void ajouterResultat(int indexLigne) throws ErreurLectureFichier, ErreurRegex {
        ResultatJoueur resultatJoueur = extracteurLigne.extraireResultat(lignesFichier[indexLigne]);
        String nomJoueur = resultatJoueur.getNomJoueur();

        MainPoker mainActuelle = obtMains().getLast();
        JoueurPoker joueurPoker = retrouverJoueurParNom(mainActuelle, nomJoueur);

        joueurPoker.ajouterResultat(resultatJoueur);
    }

    private JoueurPoker retrouverJoueurParNom(MainPoker mainPoker, String nomJoueur) throws ErreurLectureFichier {
        for (JoueurPoker joueur : mainPoker.obtJoueurs()) {
            if (Objects.equals(joueur.obtNom(), nomJoueur)) return joueur;
        }

        throw new ErreurLectureFichier("Le joueur n'a pas été trouvée");
    }

    private void ajouterBlindeOuAnte(int indexLigne) throws ErreurLectureFichier, ErreurRegex {
        BlindeOuAnte blindeOuAnte = extracteurLigne.extraireBlindeOuAnte(lignesFichier[indexLigne]);
        MainPoker mainActuelle = obtMains().getLast();
        JoueurPoker joueurPoker = retrouverJoueurParNom(mainActuelle, blindeOuAnte.getNomJoueur());

        if (blindeOuAnte.isBlinde()) {
            mainActuelle.ajouterBlinde(joueurPoker, blindeOuAnte.obtMontant());
        }

        else if (blindeOuAnte.isAnte()) {
            mainActuelle.ajouterAnte(joueurPoker, blindeOuAnte.obtMontant());
        }
    }

}
