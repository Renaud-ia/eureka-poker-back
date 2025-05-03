package fr.eurekapoker.parties.application.api.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ContenuMainDto {
    private final int siegeDealer;
    private final BigDecimal montantBB;
    private final List<JoueurDto> joueurs;
    private final List<ContenuTourDto> tours;
    private final HashMap<String, BigDecimal> antes;
    private final HashMap<String, BigDecimal> blindes;
    private final BigDecimal potInitial;
    public ContenuMainDto(int siegeDealer,
                          BigDecimal montantBB,
                          List<JoueurDto> joueurs,
                          List<ContenuTourDto> tours,
                          HashMap<String, BigDecimal> antes,
                          HashMap<String, BigDecimal> blindes) {
        this.siegeDealer = siegeDealer;
        this.montantBB = montantBB;
        this.joueurs = joueurs;
        this.tours = this.trierTours(tours);
        this.antes = antes;
        this.blindes = blindes;

        float valuePotInitial = 0;
        for (BigDecimal blinde : blindes.values()) {
            valuePotInitial += blinde.floatValue();
        }
        for (BigDecimal ante: antes.values()) {
            valuePotInitial += ante.floatValue();
        }
        this.potInitial = new BigDecimal(valuePotInitial).setScale(2, RoundingMode.HALF_UP);
    }

    private List<ContenuTourDto> trierTours(List<ContenuTourDto> tours) {
        Map<String, Integer> ordre = Map.of(
                "PREFLOP", 1,
                "FLOP", 2,
                "TURN", 3,
                "RIVER", 4
        );

        return tours.stream()
                .sorted(Comparator.comparingInt(tour -> ordre.getOrDefault(tour.getNomTour(), Integer.MAX_VALUE)))
                .collect(Collectors.toList());
    }
}
