package fr.eurekapoker.parties.application.api;

import fr.eurekapoker.parties.application.api.dto.*;
import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerAvecBet;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker.RoundPoker;
import fr.eurekapoker.parties.domaine.poker.moteur.MoteurJeu;

import java.math.BigDecimal;
import java.util.*;

// todo ajouter des tests
public class ConvertisseurPersistanceVersApi {
    private final PartiePersistanceDto partiePersistanceDto;
    private int numeroVillain;
    private final String nomHero;
    private final boolean joueursAnonymes;
    private HashMap<String, String> nomsAnonymes;
    private final MoteurJeu moteurJeu;
    public ConvertisseurPersistanceVersApi(PartiePersistanceDto partiePersistanceDto) {
        this.partiePersistanceDto = partiePersistanceDto;
        this.numeroVillain = 1;
        this.nomHero = partiePersistanceDto.obtNomHero();
        this.joueursAnonymes = partiePersistanceDto.obtJoueursAnonymes();
        this.nomsAnonymes = new HashMap<>();
        this.moteurJeu = new MoteurJeu();
    }

    public ContenuPartieDto obtContenuPartieDto() throws ErreurLectureFichier {
        String nomHero = this.joueursAnonymes ? "Hero" : partiePersistanceDto.obtNomHero();
        ContenuPartieDto contenuPartieDto = new ContenuPartieDto(
                partiePersistanceDto.obtIdUnique(),
                GenerateurNomPartie.genererNomPartie(partiePersistanceDto),
                partiePersistanceDto.obtNomRoom(),
                nomHero,
                partiePersistanceDto.obtNombreSieges(),
                partiePersistanceDto.obtNombreMains(),
                partiePersistanceDto.obtStackEnEuros()
        );

        for (MainPersistenceDto mainPersistenceDto : partiePersistanceDto.obtMains()) {
            this.moteurJeu.reinitialiser();
            contenuPartieDto.ajouterMain(convertirMainDtoVersApi(
                    mainPersistenceDto
            ));
        }

        return contenuPartieDto;
    }

    private ContenuMainDto convertirMainDtoVersApi(MainPersistenceDto mainPersistenceDto) throws ErreurLectureFichier {
        List<JoueurDto> joueurs = extraireJoueursDepuisMain(mainPersistenceDto);
        List<ContenuTourDto> tours = extraireToursDepuisMain(mainPersistenceDto);
        HashMap<String, BigDecimal> antes = extraireAnteDepuisMain(mainPersistenceDto);
        HashMap<String, BigDecimal> blindes = extraireBlindesDepuisMain(mainPersistenceDto);

        ContenuMainDto contenuMainDto = new ContenuMainDto(
                mainPersistenceDto.obtIdentifiantGenere(),
                mainPersistenceDto.obtPositionDealer(),
                mainPersistenceDto.obtMontantBB(),
                joueurs,
                tours,
                blindes,
                antes
        );

        return contenuMainDto;
    }

    private List<JoueurDto> extraireJoueursDepuisMain(MainPersistenceDto mainPersistenceDto) {
        List<JoueurDto> joueursExtraits = new ArrayList<>();

        // MEME SI C'EST UN SET IL SEMBLE Y AVOIR UN ORDRE INVERSE
        Set<JoueurPersistenceDto> joueursSet = mainPersistenceDto.obtJoueursPresents();
        List<JoueurPersistenceDto> joueursList = new ArrayList<>(joueursSet);
        Collections.reverse(joueursList);

        for (JoueurPersistenceDto joueurPersistenceDto : joueursList) {
            String nomJoueur = joueurPersistenceDto.obtNomJoueur();

            if (joueursAnonymes) {
                if (Objects.equals(nomHero, nomJoueur)) nomsAnonymes.putIfAbsent(nomJoueur, "Hero");
                else {
                    nomsAnonymes.computeIfAbsent(nomJoueur, key -> "Villain" + numeroVillain++);
                }
            }

            String nomAnonyme = getNomJoueurAnonyme(nomJoueur);

            JoueurDto joueurDto = new JoueurDto(
                    nomAnonyme,
                    mainPersistenceDto.obtStack(nomJoueur),
                    extraireCartes(mainPersistenceDto.obtComboAsString(nomJoueur)),
                    mainPersistenceDto.obtSiege(joueurPersistenceDto),
                    mainPersistenceDto.obtAnte(nomJoueur),
                    mainPersistenceDto.obtBlinde(nomJoueur),
                    mainPersistenceDto.obtGains(nomJoueur),
                    mainPersistenceDto.estDealer(joueurPersistenceDto),
                    mainPersistenceDto.obtBounty(nomJoueur)
            );
            joueursExtraits.add(joueurDto);

            moteurJeu.ajouterJoueur(nomJoueur, mainPersistenceDto.obtStack(nomJoueur), mainPersistenceDto.obtBounty(nomJoueur));
            moteurJeu.ajouterBlinde(nomJoueur, mainPersistenceDto.obtBlinde(nomJoueur));
            moteurJeu.ajouterAnte(nomJoueur, mainPersistenceDto.obtAnte(nomJoueur));
        }

        return joueursExtraits;
    }

    private List<ContenuTourDto> extraireToursDepuisMain(MainPersistenceDto mainPersistenceDto) throws ErreurLectureFichier {
        List<ContenuTourDto> toursExtraits = new ArrayList<>();
        for (TourPersistanceDto tourPersistanceDto : mainPersistenceDto.obtTours()) {
            this.moteurJeu.nouveauRound(RoundPoker.valueOf(tourPersistanceDto.obtNomTour()));
            ContenuTourDto contenuTourDto = new ContenuTourDto(
                    tourPersistanceDto.obtNomTour(),
                    extraireCartes(tourPersistanceDto.obtBoardAsString())
            );

            for (ActionPersistanceDto actionPersistanceDto: tourPersistanceDto.obtActions()) {
                contenuTourDto.ajouterAction(convertirActionVersApi(actionPersistanceDto));
            }

            contenuTourDto.getActions().sort(Comparator.comparingInt(ActionDto::getNumeroAction));

            toursExtraits.add(contenuTourDto);
        }

        return toursExtraits;
    }



    private HashMap<String, BigDecimal> extraireAnteDepuisMain(MainPersistenceDto mainPersistenceDto) {
        HashMap<String, BigDecimal> antesExtraites = new HashMap<>();
        for (JoueurPersistenceDto joueurPersistenceDto : mainPersistenceDto.obtJoueursPresents()) {
            String nomJoueur = getNomJoueurAnonyme(joueurPersistenceDto.obtNomJoueur());
            antesExtraites.put(nomJoueur, mainPersistenceDto.obtAnte(nomJoueur));
        }

        return antesExtraites;
    }

    private HashMap<String, BigDecimal> extraireBlindesDepuisMain(MainPersistenceDto mainPersistenceDto) {
        HashMap<String, BigDecimal> blindesExtraites = new HashMap<>();
        for (JoueurPersistenceDto joueurPersistenceDto : mainPersistenceDto.obtJoueursPresents()) {
            String nomJoueur = getNomJoueurAnonyme(joueurPersistenceDto.obtNomJoueur());
            blindesExtraites.put(nomJoueur, mainPersistenceDto.obtBlinde(nomJoueur));
        }

        return blindesExtraites;
    }

    private ActionDto convertirActionVersApi(ActionPersistanceDto actionPersistanceDto) throws ErreurLectureFichier {
        String nomJoueur = actionPersistanceDto.obtNomJoueur();
        ActionPokerAvecBet actionPokerJoueur = new ActionPokerAvecBet(
                nomJoueur,
                ActionPoker.TypeAction.valueOf(actionPersistanceDto.obtNomAction()),
                actionPersistanceDto.obtMontant().floatValue(),
                true
        );
        moteurJeu.ajouterAction(actionPokerJoueur);
        ActionDto actionDto = new ActionDto(
                getNomJoueurAnonyme(nomJoueur),
                actionPersistanceDto.obtNomAction(),
                actionPersistanceDto.obtMontant(),
                actionPersistanceDto.getNumeroAction(),
                moteurJeu.obtStackActuel(nomJoueur),
                moteurJeu.obtMontantInvesti(nomJoueur),
                moteurJeu.seraAllIn(nomJoueur, actionPersistanceDto.obtMontant()),
                moteurJeu.obtPot()
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

    private String getNomJoueurAnonyme(String nomJoueur) {
        return nomsAnonymes.getOrDefault(nomJoueur, nomJoueur);
    }

}

