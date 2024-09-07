package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.poker.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ParserTxt {
    protected final String[] lignesFichier;
    private final List<MainPoker> mainsExtraites;
    private final InterpreteurLigne interpreteurLigne;
    private final ExtracteurLigne extracteurLigne;
    private FormatPoker formatPoker;
    public ParserTxt(String[] lignesFichier,
                     InterpreteurLigne interpreteurLigne,
                     ExtracteurLigne extracteurLigne) {
        this.lignesFichier = lignesFichier;
        this.mainsExtraites = new ArrayList<>();
        this.interpreteurLigne = interpreteurLigne;
        this.extracteurLigne = extracteurLigne;
    }
    public void extraireMains() throws ErreurImport {
        for (int indexLigne = 0; indexLigne < lignesFichier.length; indexLigne++) {
            extraireLigne(indexLigne);
        }
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

    private void extraireFormat(int indexLigne) {
        this.formatPoker =  extracteurLigne.extraireFormat(lignesFichier[indexLigne]);
    }

    private void creerNouvelleMain(int indexLigne) {
        MainPoker mainPoker = extracteurLigne.extraireMain(lignesFichier[indexLigne]);
        this.mainsExtraites.add(mainPoker);
    }

    private void ajouterJoueur(int indexLigne) {
        JoueurPoker joueurPoker = extracteurLigne.extraireJoueur(lignesFichier[indexLigne]);
        MainPoker mainActuelle = obtMains().getLast();
        mainActuelle.ajouterJoueur(joueurPoker);
    }

    private void creerNouveauTour(int indexLigne) {
        TourPoker tourPoker = extracteurLigne.extraireTour(lignesFichier[indexLigne]);
        MainPoker mainActuelle = obtMains().getLast();
        mainActuelle.ajouterTour(tourPoker);
    }

    private void ajouterAction(int indexLigne) {
        ActionPoker actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
        TourPoker tourActuel = obtMains().getLast().obtTours().getLast();
        tourActuel.ajouterAction(actionPoker);
    }

    private void ajouterResultat(int indexLigne) throws ErreurLectureFichier {
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

    private void ajouterBlindeOuAnte(int indexLigne) throws ErreurLectureFichier {
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

    public List<MainPoker> obtMains() {
        return mainsExtraites;
    }

    public FormatPoker obtFormatPoker() throws ErreurLectureFichier {
        if (formatPoker == null) throw new ErreurLectureFichier("Le format n'a pas été récupéré");
        return formatPoker;
    }

    public abstract boolean peutLireFichier();
    public abstract RoomPoker obtRoomPoker();
}
