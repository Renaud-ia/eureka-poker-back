package fr.eurekapoker.parties.domaine.parsing.dto;

import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.util.List;

public class InfosHero {
    private final String nomHero;
    private final List<CartePoker> cartes;

    public InfosHero(String nomHero, List<CartePoker> cartes) {
        this.nomHero = nomHero;
        this.cartes = cartes;
    }

    public String obtNomHero() {
        return nomHero;
    }

    public List<CartePoker> obtCartesHero() {
        return cartes;
    }
}
