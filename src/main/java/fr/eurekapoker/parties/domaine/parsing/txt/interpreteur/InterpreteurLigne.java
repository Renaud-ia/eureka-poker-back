package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

/**
    Indique quels informations est présente dans la ligne
 */
public interface InterpreteurLigne {
    boolean estFormat(String ligne);

    boolean estNouvelleMain(String ligne);

    boolean estJoueur(String ligne);

    boolean estNouveauTour(String ligne);

    boolean estGain(String ligne);

    boolean estAction(String ligne);

    boolean estBlindeAnte(String ligne);
}
