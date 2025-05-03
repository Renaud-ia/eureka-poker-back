package fr.eurekapoker.parties.application.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ContenuPartieDto {
    private final String idUniquePartie;
    private final String nomPartie;
    private final String nomRoom;
    private final int nombreSieges;
    private final int nombreMains;
    private final String nomHero;
    private final boolean stackEnEuros;
    private final boolean estProprietaire;
    private final List<ContenuMainDto> mainsExtraites;
    @Getter
    @Setter
    private List<ProfilJoueurDto> profilsJoueurs;
    public ContenuPartieDto(String idUniquePartie,
                            String nomPartie,
                            String nomRoom,
                            String nomHero,
                            int nombreSieges,
                            int nombreMains,
                            boolean stackEnEuros,
                            boolean estProprietaire
    ) {
        this.idUniquePartie = idUniquePartie;
        this.nomPartie = nomPartie;
        this.nomRoom = nomRoom;
        this.nomHero = nomHero;
        this.nombreSieges = nombreSieges;
        this.nombreMains = nombreMains;
        this.stackEnEuros = stackEnEuros;
        this.estProprietaire = estProprietaire;
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

    public String getNomHero() {
        return nomHero;
    }

    public boolean getStackEnEuros() {
        return stackEnEuros;
    }

    public void ajouterMain(ContenuMainDto contenuMainDto) {
        this.mainsExtraites.add(contenuMainDto);
    }

    public boolean getEstProprietaire() {
        return estProprietaire;
    }

    public List<ContenuMainDto> getMainsExtraites() {
        return mainsExtraites;
    }
}
