package fr.eurekapoker.parties.domaine.parsing;

import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.persistance.dto.PartiePersistanceDto;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.parsing.dto.ResultatJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.StackJoueur;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.InfosPartiePoker;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ObservateurParser {
    void fixInfosPartie(InfosPartiePoker infosPartiePoker);

    void ajouterMain(MainPoker mainPoker) throws ErreurLectureFichier;

    void ajouterJoueur(StackJoueur stackJoueur) throws ErreurLectureFichier;

    void ajouterBlinde(String nomJoueur, BigDecimal bigDecimal) throws ErreurLectureFichier;

    void ajouterAnte(String nomJoueur, BigDecimal bigDecimal) throws ErreurLectureFichier;

    void ajouterCartesHero(List<CartePoker> cartesHero);

    void ajouterTour(NouveauTour nouveauTour);

    void ajouterAction(ActionPokerJoueur actionPoker);
    void mainTerminee();
    ResumePartieDto obtResumePartie();
    String getIdUniquePartie();

    void ajouterGains(String nomJoueur, BigDecimal bigDecimal);

    void ajouterCartes(String nomJoueur, List<CartePoker> cartePokers);

    PartiePersistanceDto obtPartie();
}
