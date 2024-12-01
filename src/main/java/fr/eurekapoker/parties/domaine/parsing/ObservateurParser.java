package fr.eurekapoker.parties.domaine.parsing;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosJoueur;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.parties.infos.InfosPartiePoker;

import java.math.BigDecimal;
import java.util.List;

public interface ObservateurParser {
    void fixInfosPartie(InfosPartiePoker infosPartiePoker);

    void ajouterMain(MainPoker mainPoker, BigDecimal bigDecimal) throws ErreurLectureFichier;

    void ajouterJoueur(InfosJoueur infosJoueur) throws ErreurLectureFichier;

    void ajouterBlinde(String nomJoueur, BigDecimal bigDecimal) throws ErreurLectureFichier;

    void ajouterAnte(String nomJoueur, BigDecimal bigDecimal) throws ErreurLectureFichier;

    void ajouterHero(String nomHero, List<CartePoker> cartesHero);

    void ajouterTour(NouveauTour nouveauTour) throws ErreurLectureFichier;

    void ajouterAction(ActionPokerJoueur actionPoker) throws ErreurLectureFichier;
    void mainTerminee();

    void ajouterGains(String nomJoueur, BigDecimal bigDecimal);

    void ajouterCartes(String nomJoueur, List<CartePoker> cartePokers);
    void ajouterPositionDealer(int positionDealer);

    void partieTerminee();
}
