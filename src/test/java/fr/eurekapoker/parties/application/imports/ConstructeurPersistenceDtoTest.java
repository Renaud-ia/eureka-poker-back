package fr.eurekapoker.parties.application.imports;

import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerAvecBet;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.moteur.MoteurJeu;
import fr.eurekapoker.parties.domaine.poker.parties.infos.InfosPartiePoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConstructeurPersistenceDtoTest {
    private ConstructeurPersistenceDto constructeur;

    @Mock
    private MoteurJeu moteurJeuMock;

    @Mock
    private ParametresImport parametresImportMock;

    @Mock
    InfosPartiePoker infosPartiePokerMock;

    int nombreJoueurs;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        constructeur = new ConstructeurPersistenceDto(moteurJeuMock, parametresImportMock);

        nombreJoueurs = 6;
    }

    @Test
    void testEnchainementAjoutPartieEtMain() throws ErreurLectureFichier, ErreurLectureFichier {
        // 1. Simuler la création d'une partie
        when(infosPartiePokerMock.getIdParse()).thenReturn(new Random().nextLong());
        when(infosPartiePokerMock.getNomRoom()).thenReturn("Fake room");
        when(infosPartiePokerMock.getFormatPoker()).thenReturn("Fake format poker");
        when(infosPartiePokerMock.getTypeJeu()).thenReturn("Fake Type Jeu");
        when(infosPartiePokerMock.getDate()).thenReturn(LocalDateTime.now());
        when(infosPartiePokerMock.getNomPartie()).thenReturn("Fake nom partie");
        when(infosPartiePokerMock.getNombreSieges()).thenReturn(nombreJoueurs);

        when(moteurJeuMock.obtAnteJoueur(anyString())).thenReturn(new BigDecimal(0));

        InfosPartiePoker infosPartiePoker = infosPartiePokerMock;
        constructeur.fixInfosPartie(infosPartiePoker);
        assertEquals(nombreJoueurs, constructeur.obtPartie().obtNombreSieges());

        // 2. Simuler l'ajout d'une main à la partie
        MainPoker nouvelleMain = new MainPoker(
                new Random().nextLong()
        );
        constructeur.ajouterMain(nouvelleMain, new BigDecimal("0.05"));
        assertEquals(1, constructeur.obtPartie().obtMains().size());


        // 3. Simuler l'ajout d'un joueur
        String nomJoueur = "Fake joueur";
        InfosJoueur infosJoueur = new InfosJoueur(
                nomJoueur,
                20000,
                3
        );
        constructeur.ajouterJoueur(infosJoueur);
        assertEquals(1, constructeur.obtPartie().obtMains().getLast().obtNombreJoueurs());
        assertTrue(constructeur.obtPartie().obtMains().getLast().obtNomsJoueursPresents().contains(nomJoueur));
        verify(moteurJeuMock).ajouterJoueur(nomJoueur, infosJoueur.obtStack(), infosJoueur.obtBounty());

        // 4. Simuler l'ajout de blindes/ante
        BigDecimal montantFloatAnte = new BigDecimal("2.23");
        constructeur.ajouterBlinde(nomJoueur, montantFloatAnte);
        verify(moteurJeuMock).ajouterBlinde(nomJoueur, montantFloatAnte);

        constructeur.ajouterAnte(nomJoueur, montantFloatAnte);
        verify(moteurJeuMock).ajouterAnte(nomJoueur, montantFloatAnte);

        // 5. Simuler l'ajout d'un tour et vérifier que encodage est appelé
        TourPoker.RoundPoker roundPoker = TourPoker.RoundPoker.PREFLOP;
        NouveauTour nouveauTour = new NouveauTour(
                roundPoker,
                new ArrayList<>()
        );
        constructeur.ajouterTour(nouveauTour);
        verify(moteurJeuMock).nouveauRound(roundPoker);
        verify(moteurJeuMock, times(nombreJoueurs - 1)).ajouterJoueurManquant();

        // 6. Simuler deux actions de poker
        int nombreActions = 2;
        ActionPokerJoueur actionPokerJoueur = new ActionPokerAvecBet(
                nomJoueur,
                ActionPoker.TypeAction.RAISE,
                1000,
                true
        );
        constructeur.ajouterAction(actionPokerJoueur);

        InOrder inOrder = inOrder(moteurJeuMock);
        inOrder.verify(moteurJeuMock).obtIdentifiantSituation();
        inOrder.verify(moteurJeuMock).ajouterAction(actionPokerJoueur);

        constructeur.ajouterAction(actionPokerJoueur);

        // 8. Ajouter un résultat
        BigDecimal montantGains = new BigDecimal("665.98");
        constructeur.ajouterGains(nomJoueur, montantGains);

        // 9. Terminer la main et vérifier les calculs de value
        constructeur.mainTerminee();
        assertEquals(
                montantGains.divide(new BigDecimal(nombreActions), RoundingMode.HALF_UP),
                constructeur.obtPartie().obtMains().getLast().obtValueParActionJoueur(nomJoueur)
        );
    }

    @Test
    void throwErreurLectureFichierSiPartieNonInitialiseeQuandCreationAction() {
        ActionPokerJoueur action = new ActionPokerJoueur(
                "Fake joueur",
                ActionPoker.TypeAction.FOLD
        );
        assertThrows(ErreurLectureFichier.class, () -> constructeur.ajouterAction(action));
    }
}
