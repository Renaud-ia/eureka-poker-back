package fr.eurekapoker.parties.domaine.parsing;

import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosJoueur;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.parties.InfosPartiePoker;

import java.math.BigDecimal;
import java.util.List;

public interface ObservateurParser {
    void fixInfosPartie(InfosPartiePoker infosPartiePoker);

    void ajouterMain(MainPoker mainPoker) throws ErreurLectureFichier;

    void ajouterJoueur(InfosJoueur infosJoueur) throws ErreurLectureFichier;

    void ajouterBlinde(String nomJoueur, BigDecimal bigDecimal) throws ErreurLectureFichier;

    void ajouterAnte(String nomJoueur, BigDecimal bigDecimal) throws ErreurLectureFichier;

    void ajouterHero(String nomHero, List<CartePoker> cartesHero);

    void ajouterTour(NouveauTour nouveauTour);

    void ajouterAction(ActionPokerJoueur actionPoker);
    void mainTerminee();
    ResumePartieDto obtResumePartie();
    String getIdUniquePartie();

    void ajouterGains(String nomJoueur, BigDecimal bigDecimal);

    void ajouterCartes(String nomJoueur, List<CartePoker> cartePokers);

    PartiePersistanceDto obtPartie();
    void ajouterPositionDealer(int positionDealer);
}
