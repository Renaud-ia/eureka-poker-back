package fr.eurekapoker.parties.integration;

import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationPartie;
import fr.eurekapoker.parties.application.exceptions.PartieNonTrouvee;
import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.domaine.poker.cartes.BoardPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.cartes.ComboReel;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceAjoutPartie;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceConsultationPartie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AjoutEtRecuperationPartieBddTest {
    private Random random;
    @Autowired
    private ServiceAjoutPartie serviceAjoutPartie;
    @Autowired
    ServiceConsultationPartie serviceConsultationPartie;
    private PartiePersistanceDto partiePersistanceDto;

    @Mock
    private ParametresImport parametresImport;

    @BeforeEach
    void initialisation() {
        MockitoAnnotations.openMocks(this);
        when(parametresImport.getJoueursAnonymes()).thenReturn(false);

        random = new Random();
        partiePersistanceDto = new PartiePersistanceDto();
        partiePersistanceDto.fixerValeurs(
                UUID.randomUUID().toString(),
                random.nextLong(),
                parametresImport.getJoueursAnonymes(),
                "Fake room",
                "Fake variante",
                "Fake typeJeu",
                "Fake format special",
                false,
                LocalDateTime.of(2022, 12, 9, random.nextInt(24), random.nextInt(60), random.nextInt(60)),
                "Fake nom partie",
                new BigDecimal("2"),
                6
        );
        partiePersistanceDto.fixNomHero("Fake hero");

        // ajouter une main
        MainPersistenceDto mainPersistenceDto = new MainPersistenceDto(
                UUID.randomUUID().toString(),
                random.nextLong(),
                new BigDecimal(20), 1
        );
        partiePersistanceDto.ajouterMain(mainPersistenceDto);

        // ajouter un joueur
        String nomFauxJoueur = "FakeJoueur";
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(nomFauxJoueur);
        mainPersistenceDto.ajouterJoueur(joueurPersistenceDto, 2);
        mainPersistenceDto.ajouterStackDepart(nomFauxJoueur, new BigDecimal("1256.76"));
        mainPersistenceDto.ajouterAnte(nomFauxJoueur, new BigDecimal("43.43"));
        mainPersistenceDto.ajouterBlinde(nomFauxJoueur, new BigDecimal("23.35"));
        mainPersistenceDto.ajouterBounty(nomFauxJoueur, new BigDecimal("12.67"));
        mainPersistenceDto.ajouterGains(nomFauxJoueur, new BigDecimal("123.76"));

        ComboReel comboFakeJoueur = new ComboReel(
                new ArrayList<>(Arrays.asList(
                        new CartePoker('A', 's'),
                        new CartePoker('K', 'd')
                ))
        );
        mainPersistenceDto.ajouterCartes(nomFauxJoueur, comboFakeJoueur.toInt(), comboFakeJoueur.toString());
        mainPersistenceDto.ajouterPositionDealer(6);

        // ajouter des tours
        BoardPoker boardPoker = new BoardPoker(
                new ArrayList<>(Arrays.asList(
                        new CartePoker('5', 's'),
                        new CartePoker('6', 'd'),
                        new CartePoker('6', 'c')
                ))
        );
        TourPersistanceDto tourPersistanceDto = new TourPersistanceDto(
            "PREFLOP",
                boardPoker.toString(),
                boardPoker.asLong()
        );
        mainPersistenceDto.ajouterTour(tourPersistanceDto);

        // ajouter des actions
        ActionPersistanceDto actionPersistanceDto = new ActionPersistanceDto(
                nomFauxJoueur,
                "Raise",
                random.nextLong(),
                new BigDecimal("25.56"),
                new BigDecimal("3.45"),
                new BigDecimal("10.4"),
                new BigDecimal("888.32"),
                false
        );
        tourPersistanceDto.ajouterAction(actionPersistanceDto);
        mainPersistenceDto.fixNombreActionsDuJoueur(nomFauxJoueur, 1);
    }

    @Test
    void onRecupereLaMemePartieApresPersistance() throws ErreurConsultationPartie {
        serviceAjoutPartie.persisterPartie(partiePersistanceDto);

        PartiePersistanceDto partieRecuperee = serviceConsultationPartie.recupererMains(
                partiePersistanceDto.obtIdUnique(),
                1,
                100
        );

        assertTrue(partiesSontLesMemes(partiePersistanceDto, partieRecuperee));
    }

    @Test
    void renvoieErreurSiIdNexistePas() {
        assertThrows(PartieNonTrouvee.class, () -> serviceConsultationPartie.recupererMains(UUID.randomUUID().toString(), 0, 10));
    }

    private boolean partiesSontLesMemes(PartiePersistanceDto partiePersistanceDto, PartiePersistanceDto partieRecuperee) {
        if (infosPartieSontDifferentes(partiePersistanceDto, partieRecuperee)) return false;

        for (int indexMain = 0; indexMain < partiePersistanceDto.obtNombreMains(); indexMain++) {
            MainPersistenceDto mainOriginale = partiePersistanceDto.obtMains().get(indexMain);
            MainPersistenceDto mainRecuperee = partieRecuperee.obtMains().get(indexMain);

            if (infosMainsSontDifferentes(mainOriginale, mainRecuperee)) return false;
        }

        return true;
    }

    private boolean infosPartieSontDifferentes(
            PartiePersistanceDto partiePersistanceDto,
            PartiePersistanceDto partieRecuperee) {
        if (!Objects.equals(partiePersistanceDto.obtIdUnique(), partieRecuperee.obtIdUnique())) return true;
        if (partiePersistanceDto.obtIdParse() != partieRecuperee.obtIdParse()) return true;
        if (!Objects.equals(partiePersistanceDto.obtNomRoom(), partieRecuperee.obtNomRoom())) return true;
        if (!Objects.equals(partiePersistanceDto.obtVariante(), partieRecuperee.obtVariante())) return true;
        if (!Objects.equals(partiePersistanceDto.obtTypeTable(), partieRecuperee.obtTypeTable())) return true;
        if (!partiePersistanceDto.obtDate().withNano(0).isEqual(partieRecuperee.obtDate().withNano(0))) return true;
        if (!Objects.equals(partiePersistanceDto.obtNomPartie(), partieRecuperee.obtNomPartie())) return true;
        if (partiePersistanceDto.obtNombreSieges() != partieRecuperee.obtNombreSieges()) return true;
        if (partiePersistanceDto.obtNombreMains() != partieRecuperee.obtNombreMains()) return true;

        return false;
    }

    private boolean infosMainsSontDifferentes(MainPersistenceDto mainOriginale, MainPersistenceDto mainRecuperee) {
        if (!Objects.equals(mainOriginale.obtIdentifiantGenere(), mainRecuperee.obtIdentifiantGenere())) return true;
        if (mainOriginale.obtIdParse() != mainRecuperee.obtIdParse()) return true;
        if (mainOriginale.obtPositionDealer() != mainRecuperee.obtPositionDealer()) return true;

        if (joueursSontDifferents(mainOriginale, mainRecuperee)) return true;

        if (toursSontDifferents(mainOriginale, mainRecuperee)) return true;

        return false;
    }

    private boolean joueursSontDifferents(MainPersistenceDto mainOriginale, MainPersistenceDto mainRecuperee) {
        // vérifier les joueurs
        for (JoueurPersistenceDto joueurPersistenceDto: mainOriginale.obtJoueursPresents()) {
            String nomJoueur = joueurPersistenceDto.obtNomJoueur();
            if (mainOriginale.obtSiege(joueurPersistenceDto) != mainRecuperee.obtSiege(joueurPersistenceDto)) return true;

            if (mainOriginale.obtAnte(nomJoueur).compareTo(mainRecuperee.obtAnte(nomJoueur)) != 0) return true;
            if (mainOriginale.obtBlinde(nomJoueur).compareTo(mainRecuperee.obtBlinde(nomJoueur)) != 0) return true;

            if (mainOriginale.obtBounty(nomJoueur).compareTo(mainRecuperee.obtBounty(nomJoueur)) != 0) return true;
            if (mainOriginale.obtStack(nomJoueur).compareTo(mainRecuperee.obtStack(nomJoueur)) != 0) return true;
            if (mainOriginale.obtGains(nomJoueur).compareTo(mainRecuperee.obtGains(nomJoueur)) != 0) return true;

            if (!Objects.equals(mainOriginale.obtComboAsString(nomJoueur), mainRecuperee.obtComboAsString(nomJoueur))) return true;
            if (mainOriginale.obtComboAsInt(nomJoueur) != mainRecuperee.obtComboAsInt(nomJoueur)) return true;
        }

        return false;
    }

    private boolean toursSontDifferents(MainPersistenceDto mainOriginale, MainPersistenceDto mainRecuperee) {
        for (int indexTour = 0; indexTour < mainOriginale.obtTours().size(); indexTour++) {
            TourPersistanceDto tourOriginal = mainOriginale.obtTours().get(indexTour);
            TourPersistanceDto tourRecupere = mainRecuperee.obtTours().get(indexTour);

            if (!Objects.equals(tourOriginal.obtNomTour(), tourRecupere.obtNomTour())) return true;
            if (!Objects.equals(tourOriginal.obtBoardAsString(), tourRecupere.obtBoardAsString())) return true;
            if (tourOriginal.obtBoardAsLong() != tourRecupere.obtBoardAsLong()) return true;

            for (int indexAction = 0; indexAction < tourOriginal.obtActions().size(); indexAction++) {
                ActionPersistanceDto actionOriginale = tourOriginal.obtActions().get(indexAction);
                ActionPersistanceDto actionRecuperee = tourRecupere.obtActions().get(indexAction);

                if (actionsSontDifferentes(actionOriginale, actionRecuperee)) return true;
            }
        }

        return false;
    }

    private boolean actionsSontDifferentes(ActionPersistanceDto actionOriginale, ActionPersistanceDto actionRecuperee) {
        if (!Objects.equals(actionOriginale.obtNomJoueur(), actionRecuperee.obtNomJoueur())) return true;
        if (!Objects.equals(actionOriginale.obtNomAction(), actionRecuperee.obtNomAction())) return true;
        if (actionOriginale.obtIdSituation() != actionRecuperee.obtIdSituation()) return true;
        if (!Objects.equals(actionOriginale.obtMontant(), actionRecuperee.obtMontant())) return true;

        return false;
    }
}
