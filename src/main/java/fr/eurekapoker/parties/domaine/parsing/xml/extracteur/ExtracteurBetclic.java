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

    public String obtenirTypeJeu() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "gametype", true);
    }

    public String obtenirNomTournoi() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "tournamentname", false);
    }

    public String extraireNomPartie() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "tablename", true);
    }

    public String extraireBigBlind() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "bigblind", true);
    }

    public String extraireTotalBuyIn() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "totalbuyin", true);
    }

    public String extraireDate() throws ErreurLectureFichier {
        Element generalElement = extraireElement("general");
        return extraireTexteBalise(generalElement, "startdate", true);
    }
}
