package fr.eurekapoker.parties.application.api.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class ContenuMainDto {
    private final String idUniqueMain;
    private final int siegeDealer;
    private final List<JoueurDto> joueurs;
    private final List<ContenuTourDto> tours;
    private final HashMap<String, BigDecimal> antes;
    private final HashMap<String, BigDecimal> blindes;
    public ContenuMainDto(String idUniqueMain,
                          int siegeDealer,
                          List<JoueurDto> joueurs,
                          List<ContenuTourDto> tours,
                          HashMap<String, BigDecimal> antes,
                          HashMap<String, BigDecimal> blindes) {
        this.idUniqueMain = idUniqueMain;
        this.siegeDealer = siegeDealer;
        this.joueurs = joueurs;
        this.tours = tours;
        this.antes = antes;
        this.blindes = blindes;
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
