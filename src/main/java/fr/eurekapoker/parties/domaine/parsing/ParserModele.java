package fr.eurekapoker.parties.domaine.parsing;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.*;

import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * centralise les procédures communes aux différents parsers
 * calculer la value des actions
 */
public abstract class ParserModele {
    private final HashMap<JoueurPoker, Float> montantDejaInvesti;
    protected final List<MainPoker> mainsExtraites;
    protected FormatPoker formatPoker;
    protected ParserModele() {
        this.mainsExtraites = new ArrayList<>();
        this.montantDejaInvesti = new HashMap<>();
    }
    protected void calculerLaValueDesActions() {
        // todo
    }

    protected void ajouterActionAvecMontantDejaInvesti(ActionPoker actionPoker) {
        TourPoker tourActuel = obtMains().getLast().obtTours().getLast();
        tourActuel.ajouterAction(actionPoker);
    }

    public void lancerImport() throws ErreurImport {
        extraireMains();
        calculerLaValueDesActions();
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
