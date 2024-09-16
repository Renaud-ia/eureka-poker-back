package fr.eurekapoker.parties.application.api;

import java.util.HashMap;
import java.util.List;

public class ContenuPartieDto {
    private final String idUniquePartie;
    private final String nomPartie;
    private final int nombreSieges;
    private final int nombreMains;
    private final List<ContenuMainDto> mainsExtraites;
    public ContenuPartieDto(String idUniquePartie,
                            String nomPartie,
                            int nombreSieges,
                            int nombreMains,
                            List<ContenuMainDto> mainsExtraites) {
        this.idUniquePartie = idUniquePartie;
        this.nomPartie = nomPartie;
        this.nombreSieges = nombreSieges;
        this.nombreMains = nombreMains;
        this.mainsExtraites = mainsExtraites;
    }

    public String getIdUniquePartie() {
        return idUniquePartie;
    }

    public String getNomPartie() {
        return nomPartie;
    }

    public int getNombreSieges() {
        return nombreSieges;
    }

    public int getNombreMains() {
        return nombreMains;
    }

    public List<ContenuMainDto> getMainsExtraites() {
        return mainsExtraites;
    }
}
