package fr.eurekapoker.parties.application.persistance.dto;

import java.util.HashMap;

public class TourPersistanceDto {
    private final String nomTour;
    private final String stringBoard;
    private final long longBoard;
    private final HashMap<ActionPersistanceDto, String> actions;

    public TourPersistanceDto(String nomTour, String stringBoard, long longBoard) {
        this.nomTour = nomTour;
        this.stringBoard = stringBoard;
        this.longBoard = longBoard;
        this.actions = new HashMap<>();
    }

    public void ajouterAction(String nomJoueur, ActionPersistanceDto actionPoker) {
        this.actions.put(actionPoker, nomJoueur);
    }
}
