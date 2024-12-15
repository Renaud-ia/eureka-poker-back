package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurPmu;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartiePmu;

public class ParserPmu extends ParserTxt {
    public ParserPmu(ObservateurParser observateurParser, String[] lignesFichier) {
        super(observateurParser, lignesFichier, new InterpreteurPmu(), new ExtracteurPmu(), new BuilderInfosPartiePmu());
    }

    @Override
    protected void extraireLigne(int indexLigne) throws ErreurImport {

    }

    @Override
    public boolean peutLireFichier() {
        if (lignesFichier.length == 0) return false;
        String pattern = "Game #[0-9]+ starts\\.";

        return lignesFichier[0].matches(pattern);
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return null;
    }


}
