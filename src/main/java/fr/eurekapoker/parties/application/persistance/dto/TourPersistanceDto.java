package fr.eurekapoker.parties.application.persistance.dto;

import java.util.ArrayList;
import java.util.List;


public class TourPersistanceDto {
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

    public void ajouterAction(ActionPersistanceDto actionPoker) {
        this.actions.add(actionPoker);
    }

    public String obtNomTour() {
        return nomTour;
    }

    public String obtBoardAsString() {
        return stringBoard;
    }

    public long obtBoardAsLong() {
        return longBoard;
    }

    public List<ActionPersistanceDto> obtActions() {
        return actions;
    }
}
