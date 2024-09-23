package fr.eurekapoker.parties.application.api.dto;

import lombok.Setter;

import java.util.List;

@Setter
public class ResumePartieDto {
    private final String idUniquePartie;
    private final List<String> joueursInitiaux;
    private final String nomHero;

    public ResumePartieDto(String idUnique, List<String> joueursInitiaux, String nomHero) {
        this.idUniquePartie = idUnique;
        this.joueursInitiaux = joueursInitiaux;
        this.nomHero = nomHero;
    }

    public List<String> getJoueursInitiaux() {
        return joueursInitiaux;
    }

    public final String getNomHero() {
        return nomHero;
    }

    public String getIdUniquePartie() {
        return idUniquePartie;
    }
}
