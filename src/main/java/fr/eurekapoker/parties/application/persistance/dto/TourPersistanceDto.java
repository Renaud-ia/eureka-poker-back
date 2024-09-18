package fr.eurekapoker.parties.application.persistance.dto;

import java.util.ArrayList;
import java.util.List;

public class TourPersistanceDto {
    // todo coder les méthodes
    // todo coder les vérifications
    private final String nomTour;
    private final String stringBoard;
    private final long longBoard;
    private final List<ActionPersistanceDto> actions;

    public TourPersistanceDto(String nomTour, String stringBoard, long longBoard) {
        this.nomTour = nomTour;
        this.stringBoard = stringBoard;
        this.longBoard = longBoard;
        this.actions = new ArrayList<>();
    }

    public void ajouterJoueurNonPresent() {
    }

    public void ajouterAction(String nomJoueur, ActionPersistanceDto actionPoker) {
    }
}
