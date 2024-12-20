package fr.eurekapoker.parties.domaine.parsing.txt;

import fr.eurekapoker.parties.domaine.exceptions.FormatNonPrisEnCharge;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.extracteur.ExtracteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurLigne;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.interpreteur.InterpreteurWinamax;
import fr.eurekapoker.parties.domaine.parsing.txt.parser.ParserPmu;
import fr.eurekapoker.parties.domaine.parsing.txt.parser.ParserWinamax;
import fr.eurekapoker.parties.domaine.poker.parties.*;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartie;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartiePmu;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartieWinamax;

import java.util.HashMap;

/**
 * fabrique des parsers txt
 * crée le parser approprié et lui injecte les bonnes dépendances
 */
public class FabriqueParserTxt {
    private static final HashMap<RoomPoker, Class<? extends ParserTxt>> parsersDisponibles = new HashMap<>();

    static {
        parsersDisponibles.put(RoomPoker.WINAMAX, ParserWinamax.class);
        parsersDisponibles.put(RoomPoker.PARTY_GAMING, ParserPmu.class);
    }
    public static ParserTxt trouverParser(ObservateurParser observateurParser, String[] lignesFichier)
            throws Exception {
        for (RoomPoker roomPoker : parsersDisponibles.keySet()) {
            Class<? extends ParserTxt> parserTxtClass = parsersDisponibles.get(roomPoker);
            InterpreteurLigne interpreteurLigne = obtInterpreteurLigne(roomPoker);
            ExtracteurLigne extracteurLigne = obtParserLigne(roomPoker);
            BuilderInfosPartie builderInfosPartie = obtBuilderInfosPartie(roomPoker);

            ParserTxt parserTxt = parserTxtClass
                    .getDeclaredConstructor(
                            ObservateurParser.class,
                            String[].class,
                            InterpreteurLigne.class,
                            ExtracteurLigne.class,
                            BuilderInfosPartie.class
                            )
                    .newInstance(
                            observateurParser,
                            lignesFichier,
                            interpreteurLigne,
                            extracteurLigne,
                            builderInfosPartie);

            if (parserTxt.peutLireFichier()) {
                return parserTxt;
            }
        }

        throw new FormatNonPrisEnCharge("Impossible de trouver un parser");
    }

    private static InterpreteurLigne obtInterpreteurLigne(RoomPoker roomPoker) throws FormatNonPrisEnCharge {
        if (roomPoker == RoomPoker.WINAMAX) {
            return new InterpreteurWinamax();
        }

        if (roomPoker == RoomPoker.PARTY_GAMING) {
            return new InterpreteurPmu();
        }

        throw new FormatNonPrisEnCharge("Impossible de trouver un interpréteur pour:" + roomPoker);
    }

    private static ExtracteurLigne obtParserLigne(RoomPoker roomPoker) throws FormatNonPrisEnCharge {
        if (roomPoker == RoomPoker.WINAMAX) {
            return new ExtracteurWinamax();
        }

        if (roomPoker == RoomPoker.PARTY_GAMING) {
            return new ExtracteurPmu();
        }

        throw new FormatNonPrisEnCharge("Impossible de trouver un interpréteur pour:" + roomPoker);
    }

    private static BuilderInfosPartie obtBuilderInfosPartie(RoomPoker roomPoker) throws FormatNonPrisEnCharge {
        if (roomPoker == RoomPoker.WINAMAX) {
            return new BuilderInfosPartieWinamax();
        }

        if (roomPoker == RoomPoker.PARTY_GAMING) {
            return new BuilderInfosPartiePmu();
        }

        throw new FormatNonPrisEnCharge("Impossible de trouver un builder infos partie pour:" + roomPoker);
    }
}
