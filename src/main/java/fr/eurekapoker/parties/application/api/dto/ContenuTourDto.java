package fr.eurekapoker.parties.application.api.dto;

import java.util.ArrayList;
import java.util.List;

public class ContenuTourDto {
    private final String nomTour;
    private final List<String> board;
    private final List<ActionDto> actions;

    public ContenuTourDto(String nomTour, List<String> board) {
        this.nomTour = nomTour;
        this.board = board;
        this.actions = new ArrayList<>();
    }

    public String getNomTour() {
        return nomTour;
    }

    public List<String> getBoard() {
        return board;
    }

    public void ajouterAction(ActionDto actionDto) {
        this.actions.add(actionDto);
    }

    public List<ActionDto> getActions() {
        return actions;
    }
}
