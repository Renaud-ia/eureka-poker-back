package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;

public class InterpreteurPmu implements InterpreteurLigne{
    private TourPoker.RoundPoker tourActuel;
    private EndroitFichier endroitActuel;

    @Override
    public void lireLigne(String ligne) {
    }

    @Override
    public boolean estNouvelleMain() {
        return this.endroitActuel == EndroitFichier.NOUVELLE_MAIN;
    }

    @Override
    public boolean estFormat() {
        return false;
    }

    @Override
    public boolean estCartesHero() {
        return false;
    }

    @Override
    public boolean estJoueur() {
        return false;
    }

    @Override
    public boolean estNouveauTour() {
        return false;
    }

    @Override
    public boolean estResultat() {
        return false;
    }

    @Override
    public boolean estAction() {
        return false;
    }

    @Override
    public boolean estBlindeAnte() {
        return false;
    }

    @Override
    public boolean ligneSansInfo() {
        return false;
    }

    public boolean estInfosTable() {
        return false;
    }

    public boolean estPositionDealer() {
        return false;
    }

    public boolean estNombreJoueurs() {
        return false;
    }

    public boolean estMontantBlindes() {
        return false;
    }

    public boolean estCartesJoueur() {
        return false;
    }

    private enum EndroitFichier {
        NOUVELLE_MAIN,
        INFOS_TABLE,
        POSITION_JOUEURS,
        SECTION_ANTE_BLINDS,
        BLINDES_ANTE,
        CARTES_HERO,
        NOUVEAU_TOUR,
        ACTION,
        SHOWDOWN,
        GAINS,
        BILAN_POT,
        FIN_MAIN,
        NON_CHERCHE
    }
}
