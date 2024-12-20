package fr.eurekapoker.parties.domaine.parsing.txt.interpreteur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreteurPmuTest {
    private InterpreteurPmu interpreteurPmu;

    @BeforeEach
    void initialisation() {
        this.interpreteurPmu = new InterpreteurPmu();
    }
    @Test
    void interpreteBienSpin() {
        String ligne = "#Game No : 24771887244 \n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNumeroPartie());

        ligne = "***** Hand History for Game 24771887244 *****\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNouvelleMain());

        ligne = "NL Texas Hold'em €2 EUR Buy-in  - Saturday, December 14, 13:45:56 CET 2024\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estFormat());

        ligne = "Table 2€ SPINS (402535347) Table #1 (Real Money)\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estInfosTable());

        ligne = "Seat 3 is the button\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estPositionDealer());

        ligne = "Total number of players : 3/3 \n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNombreJoueurs());

        ligne = "Seat 3: SACPALSA ( 500 )\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estJoueur());

        ligne = "Seat 3: SACPALSA ( 500 )\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estJoueur());

        ligne = "Seat 2: Sbg72. ( 500 )\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estJoueur());

        ligne = "Seat 1: rendslargent33 ( 500 )\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estJoueur());

        ligne = "Trny: 402535347 Level: 1 \n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.ligneSansInfo());

        ligne = "Blinds(10/20)\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estMontantBlindes());

        ligne = "Blinds-Antes(400/800 -100)";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estMontantBlindes());

        ligne = "rendslargent33 posts small blind [10].\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estBlindeAnte());

        ligne = "Sbg72. posts big blind [20].\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estBlindeAnte());

        ligne = "** Dealing down cards **\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNouveauTour());

        ligne = "Dealt to rendslargent33 [  Js 2h ]\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estCartesHero());

        ligne = "SACPALSA calls [20]\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estAction());

        ligne = "rendslargent33 folds\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estAction());

        ligne = "Your time bank will be activated in 6 secs. If you do not want it to be used, please act now.";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.ligneSansInfo());

        ligne = "Sbg72. checks\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estAction());

        ligne = "** Dealing Flop ** [ Kc, 5s, 5d ]\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNouveauTour());

        ligne = "Sbg72. checks\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estAction());

        ligne = "** Dealing Turn ** [ 4c ]\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNouveauTour());

        ligne = "Sbg72. checks\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estAction());

        ligne = "** Dealing River ** [ 3s ]\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNouveauTour());

        ligne = "SACPALSA bets [310]\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estAction());

        ligne = "rendslargent33 is all-In  [50,500]";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estAction());

        ligne = "SACPALSA does not show cards.\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.ligneSansInfo());

        ligne = "SACPALSA shows [ 2s, 8d ]three of a kind.";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estCartesJoueur());

        ligne = "SACPALSA wins 360 chips\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estResultat());

        ligne = "Player Sbg72. finished in 3.";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.ligneSansInfo());

        ligne = "Game #24771888099 starts.\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.ligneSansInfo());

        ligne = "\n";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.ligneSansInfo());

        ligne = "#Game No : 24771888099";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNumeroPartie());

        ligne = "***** Hand History for Game 24771888099 *****";
        interpreteurPmu.lireLigne(ligne);
        assertTrue(interpreteurPmu.estNouvelleMain());
    }
}
