package fr.eurekapoker.parties.domaine.parsing;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * centralise les procédures communes aux différents parsers
 */
public abstract class ParserModele {
    protected final ObservateurParser observateurParser;
    private final HashMap<JoueurPoker, Float> montantDejaInvesti;
    protected final List<MainPoker> mainsExtraites;
    protected FormatPoker formatPoker;
    protected ParserModele(ObservateurParser observateurParser) {
        this.observateurParser = observateurParser;
        this.mainsExtraites = new ArrayList<>();
        this.montantDejaInvesti = new HashMap<>();
    }

    public void lancerImport() throws ErreurImport {
        extraireMains();
    }

    protected abstract void extraireMains() throws ErreurImport;

    public FormatPoker obtFormatPoker() throws ErreurLectureFichier {
        if (formatPoker == null) throw new ErreurLectureFichier("Le format n'a pas été récupéré");
        return formatPoker;
    }

    public List<MainPoker> obtMains() {
        return mainsExtraites;
    }
    public abstract boolean peutLireFichier();
    public abstract RoomPoker obtRoomPoker();
}
