package fr.eurekapoker.parties.domaine.parsing.xml.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.xml.ParserXml;
import fr.eurekapoker.parties.domaine.parsing.xml.extracteur.ExtracteurBetclic;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.InfosPartiePoker;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;
import org.w3c.dom.Document;

public class ParserBetclic extends ParserXml {
    private final ExtracteurBetclic extracteurBetclic;
    public ParserBetclic(ObservateurParser observateurParser, Document document, ExtracteurBetclic extracteurBetclic) {
        super(observateurParser, document);
        this.extracteurBetclic = extracteurBetclic;
    }

    @Override
    protected void extraireMains() throws ErreurImport {
        InfosPartiePoker infosPartiePoker = extraireInfosPartie();
        observateurParser.fixInfosPartie(infosPartiePoker);

    }

    private InfosPartiePoker extraireInfosPartie() throws ErreurLectureFichier {
        FormatPoker formatPoker = detecterFormat();

        int nombreSieges = extracteurBetclic.extraireNombreSieges();
        return null;
    }

    private FormatPoker detecterFormat() {
        return null;
    }

    @Override
    public boolean peutLireFichier() {
        return true;
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.BETCLIC;
    }
}
