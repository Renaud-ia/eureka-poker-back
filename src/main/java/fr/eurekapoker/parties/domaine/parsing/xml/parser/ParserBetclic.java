package fr.eurekapoker.parties.domaine.parsing.xml.parser;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.FormatNonPrisEnCharge;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.InfosJoueurBetclic;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.InfosTourBetclic;
import fr.eurekapoker.parties.domaine.parsing.dto.betclic.NomIdPartieBetclic;
import fr.eurekapoker.parties.domaine.parsing.xml.ParserXml;
import fr.eurekapoker.parties.domaine.parsing.xml.extracteur.ExtracteurBetclic;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.builders.BuilderInfosPartieBetclic;
import fr.eurekapoker.parties.domaine.poker.parties.infos.InfosPartiePoker;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParserBetclic extends ParserIPoker {
    public ParserBetclic(ObservateurParser observateurParser, Document document, ExtracteurBetclic extracteurBetclic) {
        super(observateurParser, document, extracteurBetclic);
    }

    @Override
    public RoomPoker obtRoomPoker() {
        return RoomPoker.BETCLIC;
    }
}
