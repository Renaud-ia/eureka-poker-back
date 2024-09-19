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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ServiceAjoutPartie {
    private PartieRepository partieRepository;
    private JoueurRepository joueurRepository;
    private String nomRoom;
    private final ConcurrentHashMap<String, InfosJoueurJpa> joueursEnregistres;

    @Autowired
    public ServiceAjoutPartie(PartieRepository partieRepository, JoueurRepository joueurRepository) {
        this.partieRepository = partieRepository;
        this.joueurRepository = joueurRepository;
        this.joueursEnregistres = new ConcurrentHashMap<>();
    }

    @Transactional
    public void persisterPartie(PartiePersistanceDto partiePersistanceDto) {
        PartieJpa nouvellePartie = PartieJpa.builder()
                .identifiantGenere(partiePersistanceDto.obtIdUnique())
                .nomRoom(partiePersistanceDto.obtNomRoom())
                .varianteJeu(partiePersistanceDto.obtVariante())
                .typeTable(partiePersistanceDto.obtTypeTable())
                .identifiantParse(partiePersistanceDto.obtIdParse())
                .nomPartie(partiePersistanceDto.obtNomPartie())
                .nomHero(partiePersistanceDto.obtNomHero())
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
                    .indexMain(indexMain++)
                    .partieJpa(nouvellePartie)
                    .infosJoueurJpa(new ArrayList<>())
                    .toursJpa(new ArrayList<>())
                    .build();

            ajouterJoueursMain(nouvelleMain, mainDto);
            ajouterTours(nouvelleMain, mainDto);

            nouvellePartie.ajouterMain(nouvelleMain);
        }
    }

    private void ajouterJoueursMain(MainJpa nouvelleMain, MainPersistenceDto mainDto) {
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
                    .build();

            joueursEnregistres.put(nomJoueur, infosJoueurJpa);

            nouvelleMain.ajouterInfosJoueur(infosJoueurJpa);
        }
    }

    private void ajouterTours(MainJpa nouvelleMain, MainPersistenceDto mainDto) {
        for (TourPersistanceDto tourPersistanceDto : mainDto.obtTours()) {
            TourJpa tourJpa = TourJpa.builder()
                    .mainJpa(nouvelleMain)
                    .nomTour(tourPersistanceDto.obtNomTour())
                    .boardString(tourPersistanceDto.obtBoardAsString())
                    .boardLong(tourPersistanceDto.obtBoardAsLong())
                    .build();

            ajouterActions(tourJpa, mainDto, tourPersistanceDto);

            nouvelleMain.ajouterTour(tourJpa);
        }
    }

    private void ajouterActions(TourJpa tourJpa, MainPersistenceDto mainDto, TourPersistanceDto tourDto) {
        for (ActionPersistanceDto actionPersistanceDto: tourDto.obtActions()) {
            InfosJoueurJpa infosJoueurJpa = joueursEnregistres.get(actionPersistanceDto.obtNomJoueur());

            ActionJpa actionJpa = ActionJpa.builder()
                    .infosJoueurJpa(infosJoueurJpa)
                    .nomAction(actionPersistanceDto.obtNomAction())
                    .montantAction(actionPersistanceDto.obtMontant())
                    .identifiantSituation(actionPersistanceDto.obtIdSituation())
                    .value(mainDto.obtValueParActionJoueur(actionPersistanceDto.obtNomJoueur()))
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
