package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.poker.*;

public interface ExtracteurLigne {
    // todo
    FormatPoker extraireFormat(String ligne);
    MainPoker extraireMain(String ligne);

    JoueurPoker extraireJoueur(String ligne);

    TourPoker extraireTour(String ligne);

    ActionPoker extraireAction(String s);
}
