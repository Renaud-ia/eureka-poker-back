package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;

public class InterpreteurPmu implements InterpreteurLigne{
    private TourPoker.RoundPoker tourActuel;
    private EndroitFichier endroitActuel;

    public InterpreteurPmu() {
        this.endroitActuel = EndroitFichier.NON_CHERCHE;
        this.tourActuel = TourPoker.RoundPoker.BLINDES;
    }

    @Override
    public void lireLigne(String ligne) {
        this.endroitActuel = trouverEndroitFichier(ligne);
    }

    private EndroitFichier trouverEndroitFichier(String ligne) {
        ligne = ligne.replaceAll("\n", "");

        if (ligne.startsWith("#Game")) {
            return EndroitFichier.NUMERO_PARTIE;
        }

        if (ligne.startsWith("***** Hand History for Game")) {
            return EndroitFichier.NOUVELLE_MAIN;
        }

        if (ligne.startsWith("NL Texas Hold'em") || ligne.startsWith("€")) {
            return EndroitFichier.FORMAT;
        }

        if (ligne.startsWith("Table") && !ligne.contains("Closed")) {
            return EndroitFichier.INFOS_TABLE;
        }

        if (ligne.replaceAll("\r", "").endsWith("button")) {
            return EndroitFichier.POSITION_DEALER;
        }

        if (ligne.startsWith("Total number")) {
            return EndroitFichier.NOMBRE_JOUEURS;
        }

        if (ligne.startsWith("Seat")) {
            return EndroitFichier.POSITION_JOUEURS;
        }

        if (ligne.startsWith("Blinds")) {
            this.tourActuel = TourPoker.RoundPoker.BLINDES;
            return EndroitFichier.SECTION_ANTE_BLINDS;
        }

        if (ligne.contains(" posts ")) {
            return EndroitFichier.BLINDES_ANTE;
        }

        if (ligne.startsWith("** Dealing down cards **")) {
            this.tourActuel = TourPoker.RoundPoker.PREFLOP;
            return EndroitFichier.NOUVEAU_TOUR;
        }

        if (ligne.startsWith("** Dealing")) {
            this.tourActuel.suivant();
            return EndroitFichier.NOUVEAU_TOUR;
        }

        if (ligne.startsWith("Dealt to")) {
            return EndroitFichier.CARTES_HERO;
        }

        if (ligne.contains(" folds")
                || ligne.contains(" calls")
                || ligne.contains(" checks")
                || ligne.contains(" bets")
                || ligne.contains(" raises")
                || ligne.contains(" all-In")
        ) {
            return EndroitFichier.ACTION;
        }

        if (ligne.contains(" wins ")) {
            return EndroitFichier.GAINS;
        }

        if (ligne.contains(" shows ")) {
            return EndroitFichier.CARTES;
        }

        return EndroitFichier.NON_CHERCHE;
    }

    public boolean estNumeroPartie() {
        return this.endroitActuel == EndroitFichier.NUMERO_PARTIE;
    }

    @Override
    public boolean estNouvelleMain() {
        return this.endroitActuel == EndroitFichier.NOUVELLE_MAIN;
    }

    @Override
    public boolean estFormat() {
        return this.endroitActuel == EndroitFichier.FORMAT;
    }

    @Override
    public boolean estCartesHero() {
        return endroitActuel == EndroitFichier.CARTES_HERO;
    }

    @Override
    public boolean estJoueur() {
        return endroitActuel == EndroitFichier.POSITION_JOUEURS;
    }

    @Override
    public boolean estNouveauTour() {
        return endroitActuel == EndroitFichier.NOUVEAU_TOUR;
    }

    @Override
    public boolean estResultat() {
        return this.endroitActuel == EndroitFichier.GAINS;
    }

    @Override
    public boolean estAction() {
        return this.endroitActuel == EndroitFichier.ACTION;
    }

    @Override
    public boolean estBlindeAnte() {
        return this.endroitActuel == EndroitFichier.BLINDES_ANTE;
    }

    @Override
    public boolean ligneSansInfo() {
        return endroitActuel == EndroitFichier.NON_CHERCHE;
    }

    public boolean estInfosTable() {
        return endroitActuel == EndroitFichier.INFOS_TABLE;
    }

    public boolean estPositionDealer() {
        return endroitActuel == EndroitFichier.POSITION_DEALER;
    }

    public boolean estNombreJoueurs() {
        return endroitActuel == EndroitFichier.NOMBRE_JOUEURS;
    }

    public boolean estMontantBlindes() {
        return endroitActuel == EndroitFichier.SECTION_ANTE_BLINDS;
    }

    public boolean estCartesJoueur() {
        return this.endroitActuel == EndroitFichier.CARTES;
    }

    private enum EndroitFichier {
        NUMERO_PARTIE,
        NOUVELLE_MAIN,
        FORMAT,
        INFOS_TABLE,
        POSITION_DEALER,
        NOMBRE_JOUEURS,
        POSITION_JOUEURS,
        SECTION_ANTE_BLINDS,
        BLINDES_ANTE,
        CARTES_HERO,
        NOUVEAU_TOUR,
        ACTION,
        SHOWDOWN,
        GAINS,
        CARTES,
        BILAN_POT,
        FIN_MAIN,
        NON_CHERCHE
    }
}
