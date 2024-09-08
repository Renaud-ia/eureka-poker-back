package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.poker.*;

public interface ExtracteurLigne {
    // todo
    FormatPoker extraireFormat(String ligne) throws ErreurRegex;
    MainPoker extraireMain(String ligne) throws ErreurRegex;

    JoueurPoker extraireJoueur(String ligne) throws ErreurRegex;

    TourPoker extraireTour(String ligne) throws ErreurRegex;

    ActionPoker extraireAction(String ligne) throws ErreurRegex;

    ResultatJoueur extraireResultat(String ligne) throws ErreurRegex;

    BlindeOuAnte extraireBlindeOuAnte(String s) throws ErreurRegex;
}
