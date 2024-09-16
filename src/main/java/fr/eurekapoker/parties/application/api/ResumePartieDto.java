package fr.eurekapoker.parties.application.api;

import java.util.List;

public class ResumePartieDto {
    private final List<String> joueursInitiaux;
    private final String nomHero;

    public ResumePartieDto(List<String> joueursInitiaux, String nomHero, String nomHero1) {
        this.joueursInitiaux = joueursInitiaux;
        this.nomHero = nomHero1;
    }

    public List<String> getJoueursInitiaux() {
        return joueursInitiaux;
    }

    public final String getNomHero() {
        return nomHero;
    }
}
