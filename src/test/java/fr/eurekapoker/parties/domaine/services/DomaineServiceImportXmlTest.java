package fr.eurekapoker.parties.domaine.services;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.RoomNonPriseEnCharge;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.xml.ParserXml;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DomaineServiceImportXmlTest {
    @Mock
    private ParserXml mockParserXml;

    @Mock
    private ObservateurParser mockObservateurParser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void parseFichierXmlValide() throws URISyntaxException, IOException, ErreurImport {
        String contenuXml = lireContenuFichier("5359260503.xml");

        Assertions.assertDoesNotThrow(() -> {
            new DomaineServiceImportXml(mockObservateurParser, contenuXml);
        });
    }

    private void assertDoesNotThrow(Class<ErreurLectureFichier> erreurLectureFichierClass, Object o) {
    }

    @Test
    void refuseFichierXmlInvalide() throws ErreurLectureFichier, RoomNonPriseEnCharge {
        String contenuXml = "<hhfeuazhfehfezfhu";
        assertThrows(ErreurLectureFichier.class, () -> {
            // Lancer le service avec le contenu XML invalide
            new DomaineServiceImportXml(mockObservateurParser, contenuXml);
        });
    }

    private String lireContenuFichier(String nomFichier) throws URISyntaxException, IOException {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/betclic/" + nomFichier)).toURI());
        return Files.readString(cheminRepertoire);
    }
}
