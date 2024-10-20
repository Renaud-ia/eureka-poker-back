package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreteurWinamaxTest {
    @Test
    void interpreteBienPartieExpresso() {
        InterpreteurWinamax interpreteurWinamax = new InterpreteurWinamax();

        String ligne = "Winamax Poker - Tournament \"Expresso\" buyIn: 0.46€ + 0.04€ level: 1 - HandId: #2759101941731557377-1-1678437143 - Holdem no limit (10/20) - 2023/03/10 08:32:23 UTC\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estNouvelleMain(), "Nouvelle main non détectée");

        ligne = "Table: 'Expresso(642403481)#0' 3-max (real money) Seat #1 is the button\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estInfosTable(), "Ligne infos table non trouvée");

        ligne = "Seat 1: 2O2L (500)\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estJoueur(), "Ligne joueur non détecté");
        ligne = "Seat 2: jveudlamone (500)\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estJoueur(), "Ligne joueur non détecté");

        ligne = "*** ANTE/BLINDS ***\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "jveudlamone posts small blind 10\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estBlindeAnte(), "Ligne blinde/ante non détectée");
        ligne = "L3mu posts big blind 20\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estBlindeAnte(), "Ligne blinde/ante non détectée");

        ligne = "Dealt to jveudlamone [Ks Td]\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estCartesHero(), "Ligne carte hero non détectée");

        ligne = "*** PRE-FLOP *** \n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estNouveauTour());

        ligne = "2O2L raises 90 to 110\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estAction(), "Ligne action non trouvée");
        ligne = "jveudlamone folds\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estAction(), "Ligne action non trouvée");
        ligne = "L3mu folds\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estAction(), "Ligne action non trouvée");

        ligne = "2O2L collected 140 from pot\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "*** SUMMARY ***\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());
        ligne = "Total pot 140 | No rake\n";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "Seat 1: 2O2L (button) won 140";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estResultat(), "Ligne résultat non trouvé");

        ligne = "Seat 3: bolchevik33 (small blind) showed [Kc Qc] and won 6200 with One pair : Queens";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estResultat(), "Ligne résultat non trouvé");

        ligne = "Seat 6: wina6710 showed [4c As] and lost with One pair : 4";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estResultat(), "Ligne résultat non trouvé");
    }

    @Test
    void interpreteBienColorado() {
        InterpreteurWinamax interpreteurWinamax = new InterpreteurWinamax();

        String ligne = "Winamax Poker - Go Fast \"Colorado\" - HandId: #14870085-987907-1599323058 - Holdem no limit (0.01€/0.02€) - 2020/09/05 16:24:18 UTC";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estNouvelleMain(), "Nouvelle main non détectée");

        ligne = "Table: 'Colorado' 6-max (real money) Seat #6 is the button";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estInfosTable(), "Ligne infos table non trouvée");

        ligne = "Seat 1: benj603 (2.09€)";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estJoueur(), "Ligne joueur non détecté");

        ligne = "*** ANTE/BLINDS ***";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "benj603 posts small blind 0.01€";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estBlindeAnte(), "Ligne blinde/ante non détectée");

        ligne = "Dealt to bolchevik33 [4c Ks]";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estCartesHero(), "Ligne carte hero non détectée");

        ligne = "*** PRE-FLOP *** ";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estNouveauTour());

        ligne = "Malber raises 0.04€ to 0.06€";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estAction(), "Ligne action non trouvée");

        ligne = "*** FLOP *** [9h Tc Ad]";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estNouveauTour());

        ligne = "bolchevik33 bets 0.09€";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estAction(), "Ligne action non trouvée");

        ligne = "*** TURN *** [9h Tc Ad][9s]";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estNouveauTour());

        ligne = "Malber calls 0.21€";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estAction(), "Ligne action non trouvée");

        ligne = "*** RIVER *** [9h Tc Ad 9s][6s]";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estNouveauTour());

        ligne = "bolchevik33 collected 1.42€ from pot";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "*** SUMMARY ***";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "Total pot 1.42€ | Rake 0.04€";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "Board: [9h Tc Ad 9s 6s]";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.ligneSansInfo());

        ligne = "Seat 2: bolchevik33 (big blind) won 1.42€";
        interpreteurWinamax.lireLigne(ligne);
        assertTrue(interpreteurWinamax.estResultat(), "Ligne résultat non trouvé");
    }
}
