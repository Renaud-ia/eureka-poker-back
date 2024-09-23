package fr.eurekapoker.parties.application.api.dto;

import java.util.ArrayList;
import java.util.List;

public class ContenuPartieDto {
    private final String idUniquePartie;
    private final String nomPartie;
    private final String nomRoom;
    private final int nombreSieges;
    private final int nombreMains;
    private final List<ContenuMainDto> mainsExtraites;
    public ContenuPartieDto(String idUniquePartie,
                            String nomPartie,
                            String nomRoom,
                            int nombreSieges,
                            int nombreMains) {
        this.idUniquePartie = idUniquePartie;
        this.nomPartie = nomPartie;
        this.nomRoom = nomRoom;
        this.nombreSieges = nombreSieges;
        this.nombreMains = nombreMains;
        this.mainsExtraites = new ArrayList<>();
    }

    public String getIdUniquePartie() {
        return idUniquePartie;
    }

    public String getNomRoom() {
        return nomRoom;
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

    public void ajouterMain(ContenuMainDto contenuMainDto) {
        this.mainsExtraites.add(contenuMainDto);
    }

    public List<ContenuMainDto> getMainsExtraites() {
        return mainsExtraites;
    }
}
