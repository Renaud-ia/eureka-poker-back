package fr.eurekapoker.parties.api;

import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RequeteImport {
    private String contenuPartie;
    private ParametresImport parametresImport;
}
