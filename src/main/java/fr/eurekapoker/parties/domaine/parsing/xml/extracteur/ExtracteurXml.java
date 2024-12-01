package fr.eurekapoker.parties.domaine.parsing.xml.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class ExtracteurXml {
    protected final Document document;
    public ExtracteurXml(Document document) {
        this.document = document;
    }

    protected Element extraireElement(String nomElement) throws ErreurLectureFichier {
        NodeList listeNoeuds = document.getElementsByTagName(nomElement);
        if (listeNoeuds.getLength() <= 0 || listeNoeuds.getLength() > 1) {
            throw new ErreurLectureFichier("L'élément n'est pas unique':" + nomElement);
        }
        return (Element) listeNoeuds.item(0);
    }

    protected String extraireTexteBalise(Element element, String nomBalise, boolean erreurSiNonExistant) throws ErreurLectureFichier {
        NodeList listeNoeuds = element.getElementsByTagName(nomBalise);
        if (listeNoeuds.getLength() == 0) {
            if (erreurSiNonExistant) throw new ErreurLectureFichier("La balise n'existe pas : " + nomBalise);
            else return null;
        }

        if (listeNoeuds.getLength() > 1) {
            throw new ErreurLectureFichier("La balise n'est pas unique:" + nomBalise);
        }

        return listeNoeuds.item(0).getTextContent();
    }

    protected int extraireEntierBalise(Element element, String nomBalise) throws ErreurLectureFichier {
        NodeList listeNoeuds = element.getElementsByTagName(nomBalise);
        if (listeNoeuds.getLength() == 0) {
            throw new ErreurLectureFichier("La balise n'existe pas : " + nomBalise);
        }

        if (listeNoeuds.getLength() > 1) {
            throw new ErreurLectureFichier("La balise n'est pas unique:" + nomBalise);
        }

        return Integer.parseInt(listeNoeuds.item(0).getTextContent().trim());
    }

    protected NodeList extraireBalisesMultiples(Element element, String nomBalise) throws ErreurLectureFichier {
        NodeList listeNoeuds = element.getElementsByTagName(nomBalise);
        if (listeNoeuds.getLength() == 0) {
            throw new ErreurLectureFichier("La balise n'existe pas : " + nomBalise);
        }

        return listeNoeuds;

    }
}
