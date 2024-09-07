package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.poker.*;

import java.util.ArrayList;
import java.util.List;

public abstract class ParserTxt {
    private final String[] lignesFichier;
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
            lireLigne(indexLigne);
        }
    }

    private void lireLigne(int indexLigne) throws ErreurImport {
        String ligne = lignesFichier[indexLigne];
        if (interpreteurLigne.estFormat(ligne)) extraireFormat(indexLigne);
        else if (interpreteurLigne.estNouvelleMain(ligne)) creerNouvelleMain(indexLigne);
        else if (interpreteurLigne.estJoueur(ligne)) ajouterJoueur(indexLigne);
        else if (interpreteurLigne.estNouveauTour(ligne)) creerNouveauTour(indexLigne);
        else if (interpreteurLigne.estBlindeAnte(ligne)) ajouterBlindeOuAnte(indexLigne);
        else if (interpreteurLigne.estAction(ligne)) ajouterAction(indexLigne);
        else if (interpreteurLigne.estGain(ligne)) ajouterGain(indexLigne);
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
        MainPoker mainActuelle = mainsExtraites.getLast();
        mainActuelle.ajouterJoueur(joueurPoker);
    }

    private void creerNouveauTour(int indexLigne) {
        TourPoker tourPoker = extracteurLigne.extraireTour(lignesFichier[indexLigne]);
        MainPoker mainActuelle = mainsExtraites.getLast();
        mainActuelle.ajouterTour(tourPoker);
    }

    private void ajouterAction(int indexLigne) {
        ActionPoker actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
        TourPoker tourActuel = mainsExtraites.getLast().obtTours().getLast();
        tourActuel.ajouterAction(actionPoker);
    }

    private void ajouterGain(int indexLigne) {
    }

    private void ajouterBlindeOuAnte(int indexLigne) {
    }

    public List<MainPoker> obtMains() {
        return mainsExtraites;
    }

    public FormatPoker obtFormatPoker() throws ErreurLectureFichier {
        if (formatPoker == null) throw new ErreurLectureFichier("Le format n'a pas été récupéré");
        return formatPoker;
    }

    public abstract boolean peutLireFichier(String[] lignesFichier);
    public abstract RoomPoker obtRoomPoker();
}
