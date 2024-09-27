package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

/**
    Indique quels informations est présente dans la ligne
 */
public interface InterpreteurLigne {
    void lireLigne(String ligne);
    boolean estFormat();
    boolean estNouvelleMain();
    public boolean estCartesHero();
    boolean estJoueur();
    boolean estNouveauTour();
    boolean estResultat();
    boolean estAction();
    boolean estBlindeAnte();
    boolean ligneSansInfo();
}
