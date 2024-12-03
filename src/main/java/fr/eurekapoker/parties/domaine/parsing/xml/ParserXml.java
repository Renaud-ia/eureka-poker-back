package fr.eurekapoker.parties.domaine.parsing.xml;

import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.ParserModele;
import org.w3c.dom.Document;


public abstract class ParserXml extends ParserModele {
    protected final Document document;
    public ParserXml(ObservateurParser observateurParser, Document document) {
        super(observateurParser);
        this.document = document;
    }
}
