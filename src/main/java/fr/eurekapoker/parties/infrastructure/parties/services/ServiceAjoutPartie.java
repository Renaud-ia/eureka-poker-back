package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PostflopRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PreflopRange;
import fr.eurekapoker.parties.infrastructure.parties.entites.*;
import fr.eurekapoker.parties.infrastructure.parties.repositories.JoueurRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.PartieRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ServiceAjoutPartie {
    private PartieRepository partieRepository;
    private JoueurRepository joueurRepository;
    private UtilisateurRepository utilisateurRepository;
    private ServiceUtilisateur serviceUtilisateur;
    private String nomRoom;

    @Autowired
    public ServiceAjoutPartie(
            PartieRepository partieRepository,
            JoueurRepository joueurRepository,
            UtilisateurRepository utilisateurRepository,
            ServiceUtilisateur serviceUtilisateur) {
        this.partieRepository = partieRepository;
        this.joueurRepository = joueurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.serviceUtilisateur = serviceUtilisateur;
    }

    @Transactional
    public void persisterPartie(UtilisateurIdentifie utilisateurIdentifie, PartiePersistanceDto partiePersistanceDto) {
        UtilisateurJpa utilisateurJpa = this.serviceUtilisateur.trouverOuCreerUtilisateur(utilisateurIdentifie);

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
                .utilisateur(utilisateurJpa)
                .mainsJpa(new ArrayList<>())
                .build();

        utilisateurJpa.ajouterPartie(nouvellePartie);

        this.nomRoom = partiePersistanceDto.obtNomRoom();
        ajouterMainsPartie(nouvellePartie, utilisateurJpa, partiePersistanceDto.obtMains());
        partieRepository.save(nouvellePartie);
    }

    private void ajouterMainsPartie(PartieJpa nouvellePartie,
                                    UtilisateurJpa utilisateurJpa,
                                    List<MainPersistenceDto> mainPersistenceDtos) {
        int indexMain = 1;
        for (MainPersistenceDto mainDto: mainPersistenceDtos) {
            MainJpa nouvelleMain = MainJpa.builder()
                    .identifiantParse(mainDto.obtIdParse())
                    .montantBB(mainDto.obtMontantBB())
                    .indexMain(indexMain++)
                    .partieJpa(nouvellePartie)
                    .infosJoueurJpa(new ArrayList<>())
                    .positionDealer(mainDto.obtPositionDealer())
                    .build();

            HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap = ajouterJoueursMain(nouvelleMain, utilisateurJpa, mainDto);
            ajouterTours(nouvelleMain, utilisateurJpa, mainDto, infosJoueurJpaHashMap);

            nouvellePartie.ajouterMain(nouvelleMain);
        }
    }

    private HashMap<String, InfosJoueurJpa> ajouterJoueursMain(
            MainJpa nouvelleMain,
            UtilisateurJpa utilisateurJpa,
            MainPersistenceDto mainDto
    ) {
        HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap = new HashMap<>();
        for (JoueurPersistenceDto joueurPersistenceDto : mainDto.obtJoueursPresents()) {
            JoueurJpa nouveauJoueur = chargerOuSauvegarderJoueur(utilisateurJpa, joueurPersistenceDto.obtNomJoueur(), this.nomRoom);

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
            UtilisateurJpa utilisateurJpa, MainPersistenceDto mainDto,
            HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap
    ) {
        for (TourPersistanceDto tourPersistanceDto : mainDto.obtTours()) {
            ajouterActions(nouvelleMain, utilisateurJpa, mainDto, tourPersistanceDto, infosJoueurJpaHashMap);
        }
    }

    private void ajouterActions(
            MainJpa mainJpa,
            UtilisateurJpa utilisateurJpa,
            MainPersistenceDto mainDto,
            TourPersistanceDto tourDto,
            HashMap<String, InfosJoueurJpa> infosJoueurJpaHashMap
    ) {
        for (ActionPersistanceDto actionPersistanceDto: tourDto.obtActions()) {
            InfosJoueurJpa infosJoueurJpa = infosJoueurJpaHashMap.get(actionPersistanceDto.obtNomJoueur());

            ActionJpa actionJpa = ActionJpa.builder()
                    .infosJoueurJpa(infosJoueurJpa)
                    .nomAction(actionPersistanceDto.obtNomAction())
                    .montantAction(actionPersistanceDto.obtMontant())
                    .identifiantSituation(actionPersistanceDto.obtIdSituation())
                    .valueAction(mainDto.obtValueParActionJoueur(actionPersistanceDto.obtNomJoueur()))
                    .pot(actionPersistanceDto.obtPot())
                    .potBounty(actionPersistanceDto.obtPotBounty())
                    .stackEffectif(actionPersistanceDto.obtStackEffectif())
                    .allIn(actionPersistanceDto.estAllIn())
                    .numeroAction(actionPersistanceDto.getNumeroAction())
                    .utilisateur(utilisateurJpa)
                    .main(mainJpa)
                    .nomTour(tourDto.obtNomTour())
                    .boardLong(tourDto.obtBoardAsLong())
                    .boardString(tourDto.obtBoardAsString())
                    .build();

            mainJpa.ajouterAction(actionJpa);
            utilisateurJpa.ajouterAction(actionJpa);

            PokerRange pokerRange;
            if (Objects.equals(tourDto.obtNomTour(), TourPoker.RoundPoker.PREFLOP.toString())) {
                pokerRange = new PreflopRange();
            }
            else {
                pokerRange = new PostflopRange();
            }
            pokerRange.initialiser();

            PokerRangeJpa pokerRangeJpa = mapperRange(pokerRange, actionJpa);

            actionJpa.ajouterRange(pokerRangeJpa);
        }
    }

    private PokerRangeJpa mapperRange(PokerRange pokerRange, ActionJpa actionJpa) {
        PokerRangeJpa pokerRangeJpa = PokerRangeJpa.builder()
                .idGenere(UUID.randomUUID().toString())
                .build();

        pokerRangeJpa.ajouterAction(actionJpa);

        for (String nomCombo: pokerRange.obtenirCombos().keySet()) {
            float frequence = pokerRange.obtenirCombos().get(nomCombo);

            ComboJpa comboJpa = ComboJpa.builder()
                    .nomCombo(nomCombo)
                    .frequence(frequence)
                    .range(pokerRangeJpa)
                    .build();

            pokerRangeJpa.ajouterCombo(comboJpa);
        }

        return pokerRangeJpa;
    }

    private JoueurJpa chargerOuSauvegarderJoueur(UtilisateurJpa utilisateurJpa, String nomJoueur, String nomRoom) {
        JoueurJpa joueurJpa = joueurRepository.findByUtilisateurAndNomJoueurAndNomRoom(utilisateurJpa, nomJoueur, nomRoom);
        if (joueurJpa == null) {
            joueurJpa = JoueurJpa.builder()
                    .idGenere(UUID.randomUUID().toString())
                    .nomJoueur(nomJoueur)
                    .nomRoom(nomRoom)
                    .joueurAnonyme(false)
                    .utilisateur(utilisateurJpa)
                    .build();

            joueurRepository.save(joueurJpa);

            utilisateurJpa.ajouterJoueur(joueurJpa);
        }

        return joueurJpa;
    }
}
