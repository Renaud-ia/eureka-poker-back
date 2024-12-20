package fr.eurekapoker.parties.domaine;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.RoomNonPriseEnCharge;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.xml.ParserXml;
import fr.eurekapoker.parties.domaine.parsing.xml.extracteur.ExtracteurBetclic;
import fr.eurekapoker.parties.domaine.parsing.xml.parser.ParserBetclic;
import fr.eurekapoker.parties.domaine.parsing.xml.parser.ParserIPoker;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DomaineServiceImportXml implements DomaineServiceImport {
    private final ParserXml parserXml;
    public DomaineServiceImportXml(ObservateurParser observateurParser, String lignesFichier)
            throws RoomNonPriseEnCharge, ErreurLectureFichier {

        try {
            DocumentBuilderFactory factor = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factor.newDocumentBuilder();

            InputStream inputStream = new ByteArrayInputStream(lignesFichier.getBytes());

            Document document = builder.parse(inputStream);
            ExtracteurBetclic extracteurBetclic = new ExtracteurBetclic();
            this.parserXml = new ParserIPoker(observateurParser, document, extracteurBetclic);
        }

        catch (ParserConfigurationException | java.io.IOException | SAXException erreurLecture) {
            {
                throw new ErreurLectureFichier("Impossible de charger le fichier XML");
            }
        }

    }

    @Override
    public void lancerImport() throws ErreurImport {
        this.parserXml.lancerImport();
    }
}
