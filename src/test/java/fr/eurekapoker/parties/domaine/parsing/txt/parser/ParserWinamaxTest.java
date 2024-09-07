package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserWinamaxTest {
    @Test
    void peutLireLesFichiersWinamax() throws IOException, URISyntaxException {
        Path cheminRepertoire = Paths.get(getClass().getResource("/parsing/winamax").toURI());


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

        assertNotEquals(0, nFichiersTestes, "Aucun fichier de test");
    }

    private String[] lireFichierSousFormeDeTableau(Path fichier) throws IOException {
        // Lire toutes les lignes du fichier dans une liste de chaînes
        List<String> lignes = Files.readAllLines(fichier);

        // Convertir la liste en un tableau de chaînes (String[])
        return lignes.toArray(new String[0]);
    }
}
