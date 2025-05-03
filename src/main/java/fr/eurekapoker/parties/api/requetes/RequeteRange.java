package fr.eurekapoker.parties.api.requetes;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
public class RequeteRange {
    private HashMap<String, Float> combos;
    private String typeRange;
}
