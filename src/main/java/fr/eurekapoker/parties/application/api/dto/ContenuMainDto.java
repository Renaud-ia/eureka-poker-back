package fr.eurekapoker.parties.application.api.dto;

import java.util.List;

public class ContenuMainDto {
    private final String idUniqueMain;
    private final int siegeDealer;
    private final List<JoueurDto> joueurs;
    private final List<ContenuTourDto> tours;
    public ContenuMainDto(String idUniqueMain,
                          int siegeDealer,
                          List<JoueurDto> joueurs,
                          List<ContenuTourDto> tours) {
        this.idUniqueMain = idUniqueMain;
        this.siegeDealer = siegeDealer;
        this.joueurs = joueurs;
        this.tours = tours;
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

    public List<ContenuTourDto> getTours() {
        return tours;
    }
}
