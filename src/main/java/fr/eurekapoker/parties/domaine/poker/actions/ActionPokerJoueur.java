package fr.eurekapoker.parties.domaine.poker.actions;

import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;

public class ActionPokerJoueur extends ActionPoker {
    private final String nomJoueur;
    public ActionPokerJoueur(String nomJoueur, TypeAction typeAction) {
        super(typeAction);
        this.nomJoueur = nomJoueur;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }
}
