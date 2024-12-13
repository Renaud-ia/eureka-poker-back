package fr.eurekapoker.parties.domaine.parsing.txt.parser;

import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.txt.ParserTxt;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartie;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartieWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;

public class ParserWinamax extends ParserTxt {
    public ParserWinamax(ObservateurParser observateurParser,
                         String[] lignesFichier,
                         InterpreteurLigne interpreteurWinamax,
                         ExtracteurLigne extracteurWinamax,
                         BuilderInfosPartie builderInfosPartie) {
        super(observateurParser, lignesFichier, interpreteurWinamax, extracteurWinamax, builderInfosPartie);
    }
    public ParserWinamax(ObservateurParser observateurParser, String[] lignesFichier) {
        super(observateurParser,
                lignesFichier,
                new InterpreteurWinamax(),
                new ExtracteurWinamax(),
                new BuilderInfosPartieWinamax()
        );
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
