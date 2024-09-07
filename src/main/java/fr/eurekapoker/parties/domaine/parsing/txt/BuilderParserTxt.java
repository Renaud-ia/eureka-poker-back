package fr.eurekapoker.parties.domaine.parsing.txt;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BuilderParserTxt {
    private static final List<Class<ParserTxt>> parsersDisponibles = new ArrayList<>();
    public static ParserTxt trouverParser(String[] lignesFichier) throws Exception {
        for (Class<ParserTxt> parserTxtClass : parsersDisponibles) {
            ParserTxt parserTxt = parserTxtClass
                    .getDeclaredConstructor(String[].class)
                    .newInstance((Object) lignesFichier);

            if (parserTxt.peutLireFichier()) {
                return parserTxt;
            }
        }

        return null;
    }
}
