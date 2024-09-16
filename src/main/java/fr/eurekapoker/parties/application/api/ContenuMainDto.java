package fr.eurekapoker.parties.application.api;

import java.util.List;

public class ContenuMainDto {
    private final String idUniqueMain;
    private final int siegeDealer;
    private final List<JoueurDto> joueurs;
    private final List<ActionDto> actions;
    public ContenuMainDto(String idUniqueMain,
                          int siegeDealer,
                          List<JoueurDto> joueurs,
                          List<ActionDto> actions) {
        this.idUniqueMain = idUniqueMain;
        this.siegeDealer = siegeDealer;
        this.joueurs = joueurs;
        this.actions = actions;
    }

    public String getIdUniqueMain() {
        return idUniqueMain;
    }
    public int getSiegeDealer() {
        return siegeDealer;
    }

    public List<JoueurDto> getJoueurs() {
        return joueurs;
    }

    public final List<ActionDto> getActions() {
        return actions;
    }
}
