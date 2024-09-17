package fr.eurekapoker.parties.application.persistance.dto;

import java.util.List;

public class TourPersistanceDto {
    private final String nomTour;
    private final String stringBoard;
    private final long longBoard;
    private final List<ActionPersistanceDto> actions;

    public TourPersistanceDto(String nomTour, String stringBoard, long longBoard, List<ActionPersistanceDto> actions) {
        this.nomTour = nomTour;
        this.stringBoard = stringBoard;
        this.longBoard = longBoard;
        this.actions = actions;
    }
}
