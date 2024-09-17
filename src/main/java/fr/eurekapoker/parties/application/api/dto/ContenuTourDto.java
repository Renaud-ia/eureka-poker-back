package fr.eurekapoker.parties.application.api.dto;

import java.util.List;

public class ContenuTourDto {
    private final String nomTour;
    private final List<String> board;
    private final List<ActionDto> actions;

    public ContenuTourDto(String nomTour, List<String> board, List<ActionDto> actions) {
        this.nomTour = nomTour;
        this.board = board;
        this.actions = actions;
    }

    public String getNomTour() {
        return nomTour;
    }

    public List<String> getBoard() {
        return board;
    }

    public List<ActionDto> getActions() {
        return actions;
    }
}
