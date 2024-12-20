package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.InfosMainWinamax;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.InfosTableWinamax;
import fr.eurekapoker.parties.domaine.parsing.dto.winamax.ResultatJoueurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurWinamax;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartieWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.infos.InfosPartiePoker;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParserWinamaxTest extends ParserTxtModele {

    @Mock
    private InterpreteurWinamax interpreteurWinamax;
    @Mock
    private ExtracteurWinamax extracteurWinamax;
    @Mock
    private BuilderInfosPartieWinamax builderInfosPartieWinamax;
    @Spy
    private ArrayList<MainPoker> mainsExtraitesSpy = new ArrayList<>();
    @Mock
    private InfosTableWinamax infosTableMock;
    @Mock
    private BlindeOuAnte blindeOuAnteMock;
    private final String fauxNomJoueur = "Fake joueur";
    @Mock
    private JoueurPoker joueurPokerMock;
    private List<JoueurPoker> joueurs;
    private InfosJoueur infosJoueur;
    @Mock
    private TourPoker tourPokerMock;
    private List<TourPoker> tourPokers;
    @Mock
    private ResultatJoueurWinamax resultatJoueurMock;
    @Mock
    private InfosMainWinamax infosMain;
    @InjectMocks
    private ParserWinamax parserWinamax;
    private String[] lignesFichier;
    @Mock
    private InfosPartiePoker mockInfosPartiePoker;
    @BeforeEach
    void initialisation() throws ErreurImport {
        MockitoAnnotations.openMocks(this);

        lignesFichier = new String[]{
                "Ligne Test",
        };

        parserWinamax = spy(
                new ParserWinamax(
                        observateurParser,
                        lignesFichier,
                        interpreteurWinamax,
                        extracteurWinamax,
                        builderInfosPartieWinamax
                ));

        joueurs = new ArrayList<>();
        joueurs.add(joueurPokerMock);

        tourPokers = new ArrayList<>();
        tourPokers.add(tourPokerMock);

        when(joueurPokerMock.obtNom()).thenReturn(fauxNomJoueur);
        when(resultatJoueurMock.getNomJoueur()).thenReturn(fauxNomJoueur);

        infosJoueur = new InfosJoueur(joueurPokerMock.obtNom(), 0, 0);
        when(extracteurWinamax.extraireStackJoueur(anyString())).thenReturn(infosJoueur);

        when(infosMain.obtIdentifiantMain()).thenReturn(0L);
        when(infosMain.obtMontantBb()).thenReturn(new BigDecimal(20));
        when(extracteurWinamax.extraireInfosMain(anyString())).thenReturn(infosMain);
    }

    @Test
    void nouvelleMainAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estNouvelleMain()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verify(observateurParser, times(1)).mainTerminee();
        verify(extracteurWinamax).extraireInfosMain(lignesFichier[0]);
        verify(observateurParser).ajouterMain(any(MainPoker.class));
        verify(observateurParser).ajouterMontantBB(any());

        verify(builderInfosPartieWinamax).donneesIncompletes();
    }

    @Test
    void infosPartieAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estFormat()).thenReturn(Boolean.TRUE);
        when(extracteurWinamax.extraireInfosTable(anyString())).thenReturn(infosTableMock);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireInfosTable(lignesFichier[0]);

        verify(builderInfosPartieWinamax).donneesIncompletes();
    }

    @Test
    void ligneJoueurAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estJoueur()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireStackJoueur(lignesFichier[0]);
        verify(observateurParser).ajouterJoueur(any(InfosJoueur.class));
    }

    @Test
    void ligneBlindeAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(blindeOuAnteMock.isBlinde()).thenReturn(true);
        when(interpreteurWinamax.estBlindeAnte()).thenReturn(Boolean.TRUE);
        when(extracteurWinamax.extraireBlindeOuAnte(lignesFichier[0])).thenReturn(blindeOuAnteMock);
        when(blindeOuAnteMock.getNomJoueur()).thenReturn(fauxNomJoueur);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireBlindeOuAnte(lignesFichier[0]);
        verify(observateurParser).ajouterBlinde(eq(fauxNomJoueur), any());
    }

    @Test
    void ligneAnteAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(blindeOuAnteMock.isAnte()).thenReturn(true);
        when(interpreteurWinamax.estBlindeAnte()).thenReturn(Boolean.TRUE);
        when(extracteurWinamax.extraireBlindeOuAnte(lignesFichier[0])).thenReturn(blindeOuAnteMock);
        when(blindeOuAnteMock.getNomJoueur()).thenReturn(fauxNomJoueur);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireBlindeOuAnte(lignesFichier[0]);
        verify(observateurParser).ajouterAnte(eq(fauxNomJoueur), any());
    }

    @Test
    void ligneActionAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estAction()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireAction(lignesFichier[0]);
        verify(observateurParser).ajouterAction(any());
    }

    @Test
    void ligneResultatAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estResultat()).thenReturn(Boolean.TRUE);
        when(extracteurWinamax.extraireResultat(lignesFichier[0])).thenReturn(resultatJoueurMock);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireResultat(lignesFichier[0]);
        verify(observateurParser).ajouterGains(eq(fauxNomJoueur), any());
        verify(observateurParser).ajouterCartes(eq(fauxNomJoueur), any());
    }

    @Test
    void testLigneNouveauTourAppelleBonnesMethodes() throws ErreurImport {
        when(interpreteurWinamax.estNouveauTour()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireNouveauTour(lignesFichier[0], new ArrayList<>());
        verify(observateurParser).ajouterTour(any());
    }

    @Test
    void ligneSansInfoNappellePasExtracteur() throws ErreurImport {
        when(interpreteurWinamax.ligneSansInfo()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verifyNoInteractions(extracteurWinamax);
        verify(observateurParser).mainTerminee();
    }

    @Test
    void peutLireUniquementLesFichiersWinamax() throws Exception {
        assertTrue(peutLireLesFichiers("winamax"));
        assertFalse(peutLireFichiersAutresQue("winamax"));
    }

    @Override
    protected ParserTxt fabriqueParserTxt(ObservateurParser observateurParser, String[] lignesFichier) {
        return new ParserWinamax(observateurParser, lignesFichier);
    }
}
