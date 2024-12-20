package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class ParserTxtModele {
    @Mock
    protected ObservateurParser observateurParser;

    protected abstract ParserTxt fabriqueParserTxt(ObservateurParser observateurParser, String[] lignesFichier);

    protected boolean peutLireFichiersAutresQue(List<String> listeNomsRepertoire) throws Exception {
        for(String nomRepertoire: listeNomsRepertoire) {
            if (peutLireFichiersAutresQue(nomRepertoire, listeNomsRepertoire)) return true;
        }

        return false;
    }

    protected boolean peutLireFichiersAutresQue(String nomRepertoire, List<String> repertoiresExclus) throws Exception {
        List<String> repertoiresAutreRooms = listerRepertoiresAutreQue(nomRepertoire);

        for (String repertoireRoom: repertoiresAutreRooms) {
            if (repertoiresExclus.contains(repertoireRoom)) continue;
            if (peutLireLesFichiers(repertoireRoom)) return true;
        }

        return false;
    }



    private List<String> listerRepertoiresAutreQue(String repertoireExclu) throws Exception {
        String baseDir = "/parsing/";
        Path parsingPath = Paths.get(Objects.requireNonNull(getClass().getResource(baseDir)).toURI());

        if (!Files.isDirectory(parsingPath)) {
            throw new IllegalArgumentException("Le chemin spécifié n'est pas un répertoire : " + baseDir);
        }

        try (Stream<Path> paths = Files.list(parsingPath)) {
            return paths
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> !name.equals(repertoireExclu))
                    .collect(Collectors.toList());
        }
    }


    protected boolean peutLireLesFichiers(String nomRepertoire) throws IOException, URISyntaxException {
        Path cheminRepertoire = Paths.get(Objects.requireNonNull(getClass().getResource("/parsing/" + nomRepertoire)).toURI());

        assertTrue(Files.exists(cheminRepertoire),
                "Le répertoire parsing/winamax n'existe pas dans les ressources.");

        try (DirectoryStream<Path> fichiers = Files.newDirectoryStream(cheminRepertoire)) {
            for (Path fichier : fichiers) {
                if (Files.isRegularFile(fichier)) {
                    String[] lignesFichier = lireFichierSousFormeDeTableau(fichier);

                    ParserTxt parserTxt = fabriqueParserTxt(observateurParser, lignesFichier);
                    if (!parserTxt.peutLireFichier()) return false;
                }
            }
        }

        return true;
    }

    private String[] lireFichierSousFormeDeTableau(Path fichier) throws IOException {
        List<String> lignes = Files.readAllLines(fichier);

        return lignes.toArray(new String[0]);
    }
}
