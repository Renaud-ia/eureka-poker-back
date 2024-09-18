package fr.eurekapoker.parties.application.api.dto;

import java.util.List;

public class ResumePartieDto {
    private final List<String> joueursInitiaux;
    private final String nomHero;

    public ResumePartieDto(List<String> joueursInitiaux, String nomHero) {
        this.joueursInitiaux = joueursInitiaux;
        this.nomHero = nomHero;
    }

    public List<String> getJoueursInitiaux() {
        return joueursInitiaux;
    }

    public final String getNomHero() {
        return nomHero;
    }
}
