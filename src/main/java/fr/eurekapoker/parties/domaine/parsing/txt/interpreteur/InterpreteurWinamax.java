package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

import fr.eurekapoker.parties.domaine.poker.TourPoker;

public class InterpreteurWinamax implements InterpreteurLigne {
    private TourPoker.RoundPoker tourActuel;
    private EndroitFichier endroitActuel;

    public InterpreteurWinamax() {
        tourActuel = TourPoker.RoundPoker.PREFLOP;
    }

    @Override
    public void lireLigne(String ligne) {
        if (ligne.startsWith("Winamax Poker")) {
            endroitActuel = EndroitFichier.NOUVELLE_MAIN;
            tourActuel = TourPoker.RoundPoker.PREFLOP;
        }

        else if (ligne.startsWith("Table")) {
            endroitActuel = EndroitFichier.INFOS_TABLE;
        }


        else if (ligne.startsWith("Seat")) {
            if (ligne.contains(" won ")) {
                endroitActuel = EndroitFichier.GAINS;
            }
            else if (ligne.contains(" lost ")) {
                endroitActuel = EndroitFichier.NON_CHERCHE;
            }
            else {
                endroitActuel = EndroitFichier.POSITION_JOUEURS;
            }
        }

        else if (ligne.startsWith("***")) {
            if (ligne.startsWith("*** SUMMARY ***")) {
                endroitActuel = EndroitFichier.FIN_MAIN;
                tourActuel = null;
            }
            else if (ligne.startsWith("*** SHOW DOWN ***")) {
                endroitActuel = EndroitFichier.SHOWDOWN;
                tourActuel = null;
            }

            else if (ligne.startsWith("*** ANTE/BLINDS ***")) {
                endroitActuel = EndroitFichier.SECTION_ANTE_BLINDS;
            }

            else if (endroitActuel == EndroitFichier.CARTES_HERO || endroitActuel == EndroitFichier.BLINDES_ANTE) {
                tourActuel = TourPoker.RoundPoker.PREFLOP;
                endroitActuel = EndroitFichier.NOUVEAU_TOUR;
            }

            else {
                tourActuel = tourActuel.suivant();
                endroitActuel = EndroitFichier.NOUVEAU_TOUR;
            }
        }

        else if (endroitActuel == EndroitFichier.NOUVEAU_TOUR || endroitActuel == EndroitFichier.ACTION) {
            if (ligne.contains("collected") || ligne.contains("shows")) {
                endroitActuel = EndroitFichier.NON_CHERCHE;
            }
            else {
                endroitActuel = EndroitFichier.ACTION;
            }
        }

        else if (endroitActuel == EndroitFichier.SECTION_ANTE_BLINDS || endroitActuel == EndroitFichier.BLINDES_ANTE) {
            if (ligne.startsWith("Dealt to")) {
                endroitActuel = EndroitFichier.CARTES_HERO;
            }
            else {
                endroitActuel = EndroitFichier.BLINDES_ANTE;
            }
        }

        else if (ligne.startsWith("Total pot")) {
            endroitActuel = EndroitFichier.BILAN_POT;
        }

        else {
            endroitActuel = EndroitFichier.NON_CHERCHE;
        }
    }

    @Override
    public boolean estFormat() {
        return estInfosTable();
    }

    @Override
    public boolean estNouvelleMain() {
        return endroitActuel == EndroitFichier.NOUVELLE_MAIN;
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
        return endroitActuel == EndroitFichier.GAINS;
    }

    @Override
    public boolean estAction() {
        return endroitActuel == EndroitFichier.ACTION;
    }

    @Override
    public boolean estBlindeAnte() {
        return endroitActuel == EndroitFichier.BLINDES_ANTE;
    }

    public boolean estInfosTable() {
        return  endroitActuel == EndroitFichier.INFOS_TABLE;
    }

    public boolean ligneSansInfo() {
        return endroitActuel == EndroitFichier.NON_CHERCHE
                || endroitActuel == EndroitFichier.SECTION_ANTE_BLINDS
                || endroitActuel == EndroitFichier.BILAN_POT
                || endroitActuel == EndroitFichier.FIN_MAIN;
    }

    public boolean estCartesHero() {
        return endroitActuel == EndroitFichier.CARTES_HERO;
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
