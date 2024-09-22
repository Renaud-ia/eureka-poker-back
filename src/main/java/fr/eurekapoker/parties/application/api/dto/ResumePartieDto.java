package fr.eurekapoker.parties.application.api.dto;

import lombok.Setter;

import java.util.List;

@Setter
public class ResumePartieDto {
    private final String idUnique;
    private final List<String> joueursInitiaux;
    private final String nomHero;

    public ResumePartieDto(String idUnique, List<String> joueursInitiaux, String nomHero) {
        this.idUnique = idUnique;
        this.joueursInitiaux = joueursInitiaux;
        this.nomHero = nomHero;
    }

    public List<String> getJoueursInitiaux() {
        return joueursInitiaux;
    }

    public final String getNomHero() {
        return nomHero;
    }

    public String getIdUnique() {
        return idUnique;
    }
}
