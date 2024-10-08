package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.infrastructure.parties.entites.*;
import fr.eurekapoker.parties.infrastructure.parties.repositories.JoueurRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.PartieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ServiceAjoutPartie {
    private PartieRepository partieRepository;
    private JoueurRepository joueurRepository;
    private String nomRoom;

    @Autowired
    public ServiceAjoutPartie(PartieRepository partieRepository, JoueurRepository joueurRepository) {
        this.partieRepository = partieRepository;
        this.joueurRepository = joueurRepository;
    }

    @Transactional
    public void persisterPartie(PartiePersistanceDto partiePersistanceDto) {
        PartieJpa nouvellePartie = PartieJpa.builder()
                .identifiantGenere(partiePersistanceDto.obtIdUnique())
                .joueursAnonymes(partiePersistanceDto.obtJoueursAnonymes())
                .nomRoom(partiePersistanceDto.obtNomRoom())
                .varianteJeu(partiePersistanceDto.obtVariante())
                .typeTable(partiePersistanceDto.obtTypeTable())
                .formatSpecialRoom(partiePersistanceDto.obtFormatSpecialRoom())
                .identifiantParse(partiePersistanceDto.obtIdParse())
                .nomPartie(partiePersistanceDto.obtNomPartie())
                .buyIn(partiePersistanceDto.obtBuyIn())
                .nomHero(partiePersistanceDto.obtNomHero())
                .nombreSieges(partiePersistanceDto.obtNombreSieges())
                .stackEnEuros(partiePersistanceDto.obtStackEnEuros())
                .nombreMains(partiePersistanceDto.obtNombreMains())
                .dateJeu(partiePersistanceDto.obtDate())
                .dateSauvegarde(LocalDateTime.now())
                .mainsJpa(new ArrayList<>())
                .build();

        this.nomRoom = partiePersistanceDto.obtNomRoom();
        ajouterMainsPartie(nouvellePartie, partiePersistanceDto.obtMains());

        partieRepository.save(nouvellePartie);
    }

    private void ajouterMainsPartie(PartieJpa nouvellePartie,
                                    List<MainPersistenceDto> mainPersistenceDtos) {
        int indexMain = 1;
        for (MainPersistenceDto mainDto: mainPersistenceDtos) {
            MainJpa nouvelleMain = MainJpa.builder()
                    .identifiantGenere(mainDto.obtIdentifiantGenere())
                    .identifiantParse(mainDto.obtIdParse())
                    .montantBB(mainDto.obtMontantBB())
                    .indexMain(indexMain++)
                    .partieJpa(nouvellePartie)
                    .infosJoueurJpa(new ArrayList<>())
                    .toursJpa(new ArrayList<>())
                    .positionDealer(mainDto.obtPositionDealer())
                    .build();

            HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap = ajouterJoueursMain(nouvelleMain, mainDto);
            ajouterTours(nouvelleMain, mainDto, infosJoueurJpaHashMap);

            nouvellePartie.ajouterMain(nouvelleMain);
        }
    }

    private HashMap<String, InfosJoueurJpa> ajouterJoueursMain(
            MainJpa nouvelleMain,
            MainPersistenceDto mainDto
    ) {
        HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap = new HashMap<>();
        for (JoueurPersistenceDto joueurPersistenceDto : mainDto.obtJoueursPresents()) {
            JoueurJpa nouveauJoueur = chargerOuSauvegarderJoueur(joueurPersistenceDto.obtNomJoueur(), this.nomRoom);

            String nomJoueur = joueurPersistenceDto.obtNomJoueur();

            InfosJoueurJpa infosJoueurJpa = InfosJoueurJpa.builder()
                    .joueurJpa(nouveauJoueur)
                    .mainJpa(nouvelleMain)
                    .siege(mainDto.obtSiege(joueurPersistenceDto))
                    .stack(mainDto.obtStack(nomJoueur))
                    .bounty(mainDto.obtBounty(nomJoueur))
                    .comboJoueurString(mainDto.obtComboAsString(nomJoueur))
                    .comboJoueurInt(mainDto.obtComboAsInt(nomJoueur))
                    .antePayee(mainDto.obtAnte(nomJoueur))
                    .blindePayee(mainDto.obtBlinde(nomJoueur))
                    .gains(mainDto.obtGains(nomJoueur))
                    .build();

            infosJoueurJpaHashMap.put(nomJoueur, infosJoueurJpa);

            nouvelleMain.ajouterInfosJoueur(infosJoueurJpa);
        }

        return infosJoueurJpaHashMap;
    }

    private void ajouterTours(
            MainJpa nouvelleMain,
            MainPersistenceDto mainDto,
            HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap
    ) {
        for (TourPersistanceDto tourPersistanceDto : mainDto.obtTours()) {
            TourJpa tourJpa = TourJpa.builder()
                    .mainJpa(nouvelleMain)
                    .nomTour(tourPersistanceDto.obtNomTour())
                    .boardString(tourPersistanceDto.obtBoardAsString())
                    .boardLong(tourPersistanceDto.obtBoardAsLong())
                    .actionsJpas(new ArrayList<>())
                    .build();

            ajouterActions(tourJpa, mainDto, tourPersistanceDto, infosJoueurJpaHashMap);

            nouvelleMain.ajouterTour(tourJpa);
        }
    }

    private void ajouterActions(
            TourJpa tourJpa, MainPersistenceDto mainDto,
            TourPersistanceDto tourDto,
            HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap
    ) {
        for (ActionPersistanceDto actionPersistanceDto: tourDto.obtActions()) {
            InfosJoueurJpa infosJoueurJpa = infosJoueurJpaHashMap.get(actionPersistanceDto.obtNomJoueur());

            ActionJpa actionJpa = ActionJpa.builder()
                    .infosJoueurJpa(infosJoueurJpa)
                    .tourJpa(tourJpa)
                    .nomAction(actionPersistanceDto.obtNomAction())
                    .montantAction(actionPersistanceDto.obtMontant())
                    .identifiantSituation(actionPersistanceDto.obtIdSituation())
                    .valueAction(mainDto.obtValueParActionJoueur(actionPersistanceDto.obtNomJoueur()))
                    .pot(actionPersistanceDto.obtPot())
                    .potBounty(actionPersistanceDto.obtPotBounty())
                    .stackEffectif(actionPersistanceDto.obtStackEffectif())
                    .allIn(actionPersistanceDto.estAllIn())
                    .numeroAction(actionPersistanceDto.getNumeroAction())
                    .build();

            tourJpa.ajouterAction(actionJpa);
        }
    }

    private JoueurJpa chargerOuSauvegarderJoueur(String nomJoueur, String nomRoom) {
        JoueurJpa joueurJpa = joueurRepository.findByNomJoueurAndNomRoom(nomJoueur, nomRoom);
        if (joueurJpa == null) {
            joueurJpa = JoueurJpa.builder()
                    .nomJoueur(nomJoueur)
                    .nomRoom(nomRoom)
                    .joueurAnonyme(false)
                    .build();

            joueurRepository.save(joueurJpa);
        }

        return joueurJpa;
    }
}
