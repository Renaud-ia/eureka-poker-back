package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.BuilderInfosPartie;
import fr.eurekapoker.parties.domaine.poker.parties.BuilderInfosPartieWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;

public class ParserWinamax extends ParserTxt {
    public ParserWinamax(String[] lignesFichier,
                         InterpreteurWinamax interpreteurWinamax,
                         ExtracteurWinamax extracteurWinamax,
                         BuilderInfosPartie builderInfosPartie) {
        super(lignesFichier, interpreteurWinamax, extracteurWinamax, builderInfosPartie);
    }
    public ParserWinamax(String[] lignesFichier) {
        super(lignesFichier, new InterpreteurWinamax(), new ExtracteurWinamax(), new BuilderInfosPartieWinamax());
    }

    @Override
    public boolean peutLireFichier() {
        return lignesFichier[0].startsWith("Winamax Poker");
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.WINAMAX;
    }
}
