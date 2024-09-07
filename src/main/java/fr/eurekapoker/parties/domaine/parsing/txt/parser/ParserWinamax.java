package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurWinamax;
import fr.eurekapoker.parties.domaine.poker.RoomPoker;

import javax.swing.text.html.parser.Parser;

public class ParserWinamax extends ParserTxt {
    public ParserWinamax(String[] lignesFichier) {
        super(lignesFichier, new InterpreteurWinamax(), new ExtracteurWinamax());
    }

    @Override
    public boolean peutLireFichier(String[] lignesFichier) {
        // todo
        return false;
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.WINAMAX;
    }
}
