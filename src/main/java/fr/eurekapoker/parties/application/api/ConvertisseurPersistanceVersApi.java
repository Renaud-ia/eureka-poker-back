package fr.eurekapoker.parties.application.api;

import fr.eurekapoker.parties.application.api.dto.*;
import fr.eurekapoker.parties.application.persistance.dto.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

// todo ajouter des tests
public class ConvertisseurPersistanceVersApi {
    private final PartiePersistanceDto partiePersistanceDto;
    private int numeroVillain;
    public ConvertisseurPersistanceVersApi(PartiePersistanceDto partiePersistanceDto) {
        this.partiePersistanceDto = partiePersistanceDto;
        this.numeroVillain = 1;
    }

    public ContenuPartieDto obtContenuPartieDto() {
        ContenuPartieDto contenuPartieDto = new ContenuPartieDto(
                partiePersistanceDto.obtIdUnique(),
                GenerateurNomPartie.genererNomPartie(partiePersistanceDto),
                partiePersistanceDto.obtNomRoom(),
                partiePersistanceDto.obtNombreSieges(),
                partiePersistanceDto.obtNombreMains()
        );

        for (MainPersistenceDto mainPersistenceDto : partiePersistanceDto.obtMains()) {
            contenuPartieDto.ajouterMain(convertirMainDtoVersApi(
                    mainPersistenceDto,
                    partiePersistanceDto.obtNomHero(),
                    partiePersistanceDto.obtJoueursAnonymes())
            );
        }

        return contenuPartieDto;
    }

    private ContenuMainDto convertirMainDtoVersApi(MainPersistenceDto mainPersistenceDto, String nomHero, boolean joueursAnonymes) {
        List<JoueurDto> joueurs = extraireJoueursDepuisMain(mainPersistenceDto, nomHero, joueursAnonymes);
        List<ContenuTourDto> tours = extraireToursDepuisMain(mainPersistenceDto);
        HashMap<String, BigDecimal> antes = extraireAnteDepuisMain(mainPersistenceDto);
        HashMap<String, BigDecimal> blindes = extraireBlindesDepuisMain(mainPersistenceDto);

        ContenuMainDto contenuMainDto = new ContenuMainDto(
                mainPersistenceDto.obtIdentifiantGenere(),
                mainPersistenceDto.obtPositionDealer(),
                joueurs,
                tours,
                blindes,
                antes
        );

        return contenuMainDto;
    }

    private List<JoueurDto> extraireJoueursDepuisMain(MainPersistenceDto mainPersistenceDto,
                                                      String nomHero,
                                                      boolean joueursAnonymes) {
        List<JoueurDto> joueursExtraits = new ArrayList<>();

        for (JoueurPersistenceDto joueurPersistenceDto: mainPersistenceDto.obtJoueursPresents()) {
            String nomJoueur = joueurPersistenceDto.obtNomJoueur();

            if (joueursAnonymes) {
                if (Objects.equals(nomHero, nomJoueur)) nomJoueur = "Hero";
                else nomJoueur = "Villain" + numeroVillain++;
            }

            JoueurDto joueurDto = new JoueurDto(
                    nomJoueur,
                    mainPersistenceDto.obtStack(nomJoueur),
                    extraireCartes(mainPersistenceDto.obtComboAsString(nomJoueur)),
                    mainPersistenceDto.obtSiege(joueurPersistenceDto),
                    mainPersistenceDto.obtAnte(nomJoueur),
                    mainPersistenceDto.obtBlinde(nomJoueur)
            );

            joueursExtraits.add(joueurDto);
        }

        return joueursExtraits;
    }

    private List<ContenuTourDto> extraireToursDepuisMain(MainPersistenceDto mainPersistenceDto) {
        List<ContenuTourDto> toursExtraits = new ArrayList<>();
        for (TourPersistanceDto tourPersistanceDto : mainPersistenceDto.obtTours()) {
            ContenuTourDto contenuTourDto = new ContenuTourDto(
                    tourPersistanceDto.obtNomTour(),
                    extraireCartes(tourPersistanceDto.obtBoardAsString())
            );

            for (ActionPersistanceDto actionPersistanceDto: tourPersistanceDto.obtActions()) {
                contenuTourDto.ajouterAction(convertirActionVersApi(actionPersistanceDto));
            }

            toursExtraits.add(contenuTourDto);
        }

        return toursExtraits;
    }



    private HashMap<String, BigDecimal> extraireAnteDepuisMain(MainPersistenceDto mainPersistenceDto) {
        HashMap<String, BigDecimal> antesExtraites = new HashMap<>();
        for (JoueurPersistenceDto joueurPersistenceDto : mainPersistenceDto.obtJoueursPresents()) {
            String nomJoueur = joueurPersistenceDto.obtNomJoueur();
            antesExtraites.put(nomJoueur, mainPersistenceDto.obtAnte(nomJoueur));
        }

        return antesExtraites;
    }

    private HashMap<String, BigDecimal> extraireBlindesDepuisMain(MainPersistenceDto mainPersistenceDto) {
        HashMap<String, BigDecimal> blindesExtraites = new HashMap<>();
        for (JoueurPersistenceDto joueurPersistenceDto : mainPersistenceDto.obtJoueursPresents()) {
            String nomJoueur = joueurPersistenceDto.obtNomJoueur();
            blindesExtraites.put(nomJoueur, mainPersistenceDto.obtBlinde(nomJoueur));
        }

        return blindesExtraites;
    }

    private ActionDto convertirActionVersApi(ActionPersistanceDto actionPersistanceDto) {
        ActionDto actionDto = new ActionDto(
                actionPersistanceDto.obtNomJoueur(),
                actionPersistanceDto.obtNomAction(),
                actionPersistanceDto.obtMontant()
        );

        return actionDto;
    }

    private List<String> extraireCartes(String comboString) {
        List<String> cartesAsString = new ArrayList<>();
        if (comboString.isEmpty()) {
            return cartesAsString; // Retourne une liste vide
        }
        for (int i = 0; i < comboString.length(); i += 2) {
            String carte = String.valueOf(comboString.charAt(i)) + comboString.charAt(i+1);
            cartesAsString.add(carte);
        }

        return cartesAsString;
    }

}

