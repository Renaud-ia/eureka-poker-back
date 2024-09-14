package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.util.List;

public interface ExtracteurLigne {
    InfosTable extraireInfosTable(String ligne) throws ErreurRegex;
    InfosMain extraireInfosMain(String ligne) throws ErreurImport;

    StackJoueur extraireStackJoueur(String ligne) throws ErreurRegex;

    List<CartePoker> extraireBoardTour(String ligne) throws ErreurRegex;

    ActionPokerJoueur extraireAction(String ligne) throws ErreurRegex;

    ResultatJoueur extraireResultat(String ligne) throws ErreurRegex;

    BlindeOuAnte extraireBlindeOuAnte(String s) throws ErreurRegex;

    List<CartePoker> extraireCartes(String ligne) throws ErreurRegex;
}
