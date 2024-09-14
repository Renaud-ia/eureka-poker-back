package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.parsing.dto.BlindeOuAnte;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosMainWinamax;
import fr.eurekapoker.parties.domaine.parsing.dto.ResultatJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.StackJoueur;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurWinamax;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.BuilderInfosPartieWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParserWinamaxTest {
    @Mock
    private InterpreteurWinamax interpreteurWinamax;
    @Mock
    private ExtracteurWinamax extracteurWinamax;
    @Spy
    private ArrayList<MainPoker> mainsExtraitesSpy = new ArrayList<>();
    @Mock
    private MainPoker mainPokerMock;
    @Mock
    private BlindeOuAnte blindeOuAnteMock;
    private final String fauxNomJoueur = "Fake joueur";
    @Mock
    private JoueurPoker joueurPokerMock;
    private List<JoueurPoker> joueurs;
    private StackJoueur stackJoueur;
    @Mock
    private TourPoker tourPokerMock;
    private List<TourPoker> tourPokers;
    @Mock
    private ResultatJoueur resultatJoueurMock;
    @Mock
    private InfosMainWinamax infosMain;
    @InjectMocks
    private ParserWinamax parserWinamax;
    @Mock
    private BuilderInfosPartieWinamax builderInfosPartieWinamax;
    private String[] lignesFichier;
    @BeforeEach
    void initialisation() throws ErreurImport {
        MockitoAnnotations.openMocks(this);

        lignesFichier = new String[]{
                "Ligne Test",
        };

        parserWinamax = spy(new ParserWinamax(lignesFichier, interpreteurWinamax, extracteurWinamax, builderInfosPartieWinamax));

        mainsExtraitesSpy.add(mainPokerMock);

        doReturn(mainsExtraitesSpy).when(parserWinamax).obtMains();
        joueurs = new ArrayList<>();
        joueurs.add(joueurPokerMock);
        when(mainPokerMock.obtJoueurs()).thenReturn(joueurs);

        tourPokers = new ArrayList<>();
        tourPokers.add(tourPokerMock);
        when(mainPokerMock.obtTours()).thenReturn(tourPokers);

        when(joueurPokerMock.obtNom()).thenReturn(fauxNomJoueur);
        when(resultatJoueurMock.getNomJoueur()).thenReturn(fauxNomJoueur);

        stackJoueur = new StackJoueur(joueurPokerMock.obtNom(), 0, 0);
        when(extracteurWinamax.extraireStackJoueur(anyString())).thenReturn(stackJoueur);

        when(infosMain.obtIdentifiantMain()).thenReturn(0L);
        when(extracteurWinamax.extraireInfosMain(anyString())).thenReturn(infosMain);
    }

    @Test
    void nouvelleMainAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estNouvelleMain()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireInfosMain(lignesFichier[0]);
    }

    @Test
    void infosTableAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estFormat()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        //verify(extracteurWinamax).extraireInfosTable(lignesFichier[0]);

        //todo
    }

    @Test
    void ligneJoueurAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estJoueur()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireStackJoueur(lignesFichier[0]);
    }

    @Test
    void ligneBlindeAnteAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estBlindeAnte()).thenReturn(Boolean.TRUE);
        when(extracteurWinamax.extraireBlindeOuAnte(lignesFichier[0])).thenReturn(blindeOuAnteMock);
        when(blindeOuAnteMock.getNomJoueur()).thenReturn(fauxNomJoueur);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireBlindeOuAnte(lignesFichier[0]);
    }

    @Test
    void ligneActionAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estAction()).thenReturn(Boolean.TRUE);
        when(extracteurWinamax.extraireBlindeOuAnte(lignesFichier[0])).thenReturn(blindeOuAnteMock);
        when(blindeOuAnteMock.getNomJoueur()).thenReturn(fauxNomJoueur);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireAction(lignesFichier[0]);
    }

    @Test
    void ligneResultatAppelleBonneMethodeExtracteur() throws ErreurImport {
        when(interpreteurWinamax.estResultat()).thenReturn(Boolean.TRUE);
        when(extracteurWinamax.extraireResultat(lignesFichier[0])).thenReturn(resultatJoueurMock);
        when(blindeOuAnteMock.getNomJoueur()).thenReturn(fauxNomJoueur);

        parserWinamax.lancerImport();
        verify(extracteurWinamax).extraireResultat(lignesFichier[0]);
    }

    @Test
    void ligneSansInfoNappellePasExtracteur() throws ErreurImport {
        when(interpreteurWinamax.ligneSansInfo()).thenReturn(Boolean.TRUE);

        parserWinamax.lancerImport();
        verifyNoInteractions(extracteurWinamax);
    }

    @Test
    void peutLireLesFichiersWinamax() throws IOException, URISyntaxException {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/winamax")).toURI());


        assertTrue(Files.exists(cheminRepertoire),
                "Le répertoire parsing/winamax n'existe pas dans les ressources.");

        int nFichiersTestes = 0;

        try (DirectoryStream<Path> fichiers = Files.newDirectoryStream(cheminRepertoire)) {
            for (Path fichier : fichiers) {
                if (Files.isRegularFile(fichier)) {
                    String[] lignesFichier = lireFichierSousFormeDeTableau(fichier);

                    ParserWinamax parserWinamax = new ParserWinamax(lignesFichier);
                    assertTrue(parserWinamax.peutLireFichier(), "Fichier non reconnu :" + fichier);
                }
                nFichiersTestes++;
            }
        }

        assertTrue(nFichiersTestes >= 3, "Pas assez de fichiers de tests");
    }

    private String[] lireFichierSousFormeDeTableau(Path fichier) throws IOException {
        // Lire toutes les lignes du fichier dans une liste de chaînes
        List<String> lignes = Files.readAllLines(fichier);

        // Convertir la liste en un tableau de chaînes (String[])
        return lignes.toArray(new String[0]);
    }


    @Test
    void nePeutPasLireLesFichiersNonWinamax() throws IOException, URISyntaxException {
        List<Path> chemins = new ArrayList<>();
        // todo ajout de nouvelles roooms => vérifier que c'est faux pour toutes les autres rooms
        //Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/winamax")).toURI());

        for (Path cheminRepertoire: chemins) {
            assertTrue(Files.exists(cheminRepertoire),
                    "Le répertoire parsing/winamax n'existe pas dans les ressources.");

            int nFichiersTestes = 0;

            try (DirectoryStream<Path> fichiers = Files.newDirectoryStream(cheminRepertoire)) {
                for (Path fichier : fichiers) {
                    if (Files.isRegularFile(fichier)) {
                        String[] lignesFichier = lireFichierSousFormeDeTableau(fichier);

                        ParserWinamax parserWinamax = new ParserWinamax(lignesFichier);
                        assertTrue(parserWinamax.peutLireFichier(), "Fichier non reconnu :" + fichier);
                    }
                    nFichiersTestes++;
                }
            }

            assertTrue(nFichiersTestes >= 3, "Pas assez de fichiers de tests");
        }
    }
}
