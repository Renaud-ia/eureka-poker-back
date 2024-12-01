package fr.eurekapoker.parties.domaine.parsing.xml.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * connait où sont tous les éléments dans le fichier XML
 * connait l'emplacement des infos mais les extrait de manière brute
 * raise des erreurs si le format n'est pas comme attendu
 */
public class ExtracteurBetclic extends ExtracteurXml {
    public ExtracteurBetclic(Document document) {
        super(document);
    }

    public int extraireNombreSieges() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");

        return extraireEntierBalise(generalElement, "tablesize");
    }
}
