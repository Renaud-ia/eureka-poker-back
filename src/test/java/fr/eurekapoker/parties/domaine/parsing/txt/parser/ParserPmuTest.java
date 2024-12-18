package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurPmu;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartiePmu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserPmuTest extends ParserTxtModele {
    @Test
    void peutLireUniquementLesFichiersWinamax() throws Exception {
        assertTrue(peutLireLesFichiers("pmu"));
        assertFalse(peutLireFichiersAutresQue("pmu"));
    }

    @Override
    protected ParserTxt fabriqueParserTxt(ObservateurParser observateurParser, String[] lignesFichier) {
        return new ParserPmu(observateurParser, lignesFichier, new InterpreteurPmu(), new ExtracteurPmu(), new BuilderInfosPartiePmu());
    }
}
