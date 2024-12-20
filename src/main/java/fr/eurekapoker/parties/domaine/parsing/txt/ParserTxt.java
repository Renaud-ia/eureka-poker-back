package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.ParserModele;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartie;

import java.math.BigDecimal;
import java.util.ArrayList;

public abstract class ParserTxt extends ParserModele {
    protected final String[] lignesFichier;
    protected final InterpreteurLigne interpreteurLigne;
    protected final ExtracteurLigne extracteurLigne;
    protected final BuilderInfosPartie builderInfosPartie;
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

    protected abstract void extraireLigne(int indexLigne) throws ErreurImport;

    protected void ajouterJoueur(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        InfosJoueur infosJoueur = extracteurLigne.extraireStackJoueur(lignesFichier[indexLigne]);
        observateurParser.ajouterJoueur(infosJoueur);
    }

    protected void ajouterBlindeOuAnte(int indexLigne) throws ErreurLectureFichier, ErreurRegex {
        BlindeOuAnte blindeOuAnte = extracteurLigne.extraireBlindeOuAnte(lignesFichier[indexLigne]);
        String nomJoueur = blindeOuAnte.getNomJoueur();

        if (blindeOuAnte.isBlinde()) {
            observateurParser.ajouterBlinde(nomJoueur, blindeOuAnte.obtMontant());
        }

        else if (blindeOuAnte.isAnte()) {
            observateurParser.ajouterAnte(nomJoueur, blindeOuAnte.obtMontant());
        }
    }

    protected void ajouterInfoshero(int indexLigne) throws ErreurRegex {
        InfosHero infosHero = extracteurLigne.extraireInfosHero(lignesFichier[indexLigne]);
        observateurParser.ajouterHero(infosHero.obtNomHero(), infosHero.obtCartesHero());
    }

    protected void creerNouveauTour(int indexLigne) throws ErreurRegex, ErreurLectureFichier {
        NouveauTour nouveauTour = extracteurLigne.extraireNouveauTour(lignesFichier[indexLigne], new ArrayList<>());
        observateurParser.ajouterTour(nouveauTour);
    }

    protected void ajouterAction(int indexLigne) throws ErreurRegex, ErreurLectureFichier, JoueurNonExistant {
        ActionPokerJoueur actionPoker = extracteurLigne.extraireAction(lignesFichier[indexLigne]);
        observateurParser.ajouterAction(actionPoker);
    }



}
