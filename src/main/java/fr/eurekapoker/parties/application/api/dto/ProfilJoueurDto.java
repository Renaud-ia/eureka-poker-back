package fr.eurekapoker.parties.application.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfilJoueurDto {
    private final String idUnique;
    private final String nomJoueur;
    private final String notes;
}
