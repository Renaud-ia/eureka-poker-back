package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.infrastructure.parties.entites.*;
import fr.eurekapoker.parties.application.exceptions.PartieNonTrouvee;
import fr.eurekapoker.parties.infrastructure.parties.repositories.MainRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.PartieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

@Service
public class ServiceConsultationPartie {
    @Autowired
    private PartieRepository partieRepository;
    @Autowired
    private MainRepository mainRepository;
    public PartiePersistanceDto recupererMains(
            UtilisateurIdentifie utilisateurIdentifie,
            String idUniqueGenere,
            int premiereMain,
            int deniereMain)
            throws PartieNonTrouvee {
        PartieJpa partieJpa =
                partieRepository.trouverParIdGenereAvecIndexMains(
                        idUniqueGenere,
                        premiereMain,
                        deniereMain);

        if (partieJpa != null) return convertirPartieJpaVersDto(utilisateurIdentifie, partieJpa);

        throw new PartieNonTrouvee("La partie n'a pas été trouvée avec id:" + idUniqueGenere);
    }

    private PartiePersistanceDto convertirPartieJpaVersDto(UtilisateurIdentifie utilisateurIdentifie, PartieJpa partieJpa) {
        UtilisateurJpa proprietairePartie = partieJpa.getUtilisateur();

        boolean estProprietaire = false;
        if (utilisateurIdentifie.getUtilisateurAuthentifie() != null) {
            estProprietaire = Objects.equals(
                    proprietairePartie.getMailUtilisateur(),
                    utilisateurIdentifie.getUtilisateurAuthentifie().getEmailUtilisateur()
            );
        }
        PartiePersistanceDto partiePersistanceDto = new PartiePersistanceDto(
                partieJpa.getIdentifiantGenere(),
                partieJpa.getIdentifiantParse(),
                partieJpa.getJoueursAnonymes(),
                partieJpa.getNomRoom(),
                partieJpa.getNomHero(),
                partieJpa.getVarianteJeu(),
                partieJpa.getTypeTable(),
                partieJpa.getFormatSpecialRoom(),
                partieJpa.isStackEnEuros(),
                partieJpa.getDateJeu(),
                partieJpa.getNomPartie(),
                partieJpa.getBuyIn(),
                partieJpa.getNombreSieges(),
                partieJpa.getNombreMains(),
                estProprietaire
        );

        for (MainJpa mainJpa : partieJpa.getMainsJpa()) {
            partiePersistanceDto.ajouterMain(convertirMainJpaVersDto(mainJpa));
        }
        return partiePersistanceDto;
    }

    private MainPersistenceDto convertirMainJpaVersDto(MainJpa mainJpa) {
        MainPersistenceDto mainPersistenceDto = new MainPersistenceDto(
                mainJpa.getIdentifiantParse(),
                mainJpa.getIndexMain()
        );

        mainPersistenceDto.ajouterMontantBB(mainJpa.getMontantBB());

        for (InfosJoueurJpa infosJoueurJpa : mainJpa.getInfosJoueurJpa()) {
            ajouterJoueurMainPersistenceDto(mainPersistenceDto, infosJoueurJpa);
        }

        LinkedList<TourPersistanceDto> tours = this.extraireToursMain(mainJpa);

        for (TourPersistanceDto tourPersistanceDto: tours) {
            mainPersistenceDto.ajouterTour(tourPersistanceDto);
        }

        mainPersistenceDto.ajouterPositionDealer(mainJpa.getPositionDealer());

        return mainPersistenceDto;
    }

    private LinkedList<TourPersistanceDto> extraireToursMain(MainJpa mainJpa) {
        HashMap<String, TourPersistanceDto> toursExtraits = new HashMap<>();

        for (ActionJpa actionJpa : mainJpa.getActions()) {
            String nomTour = actionJpa.getNomTour();

            TourPersistanceDto tour = toursExtraits.get(nomTour);

            if (tour == null) {
                tour = convertirActionJpaVersTourDto(actionJpa);
                toursExtraits.put(nomTour, tour);
            }

            if (actionJpa.getNomAction() != null) {
                ActionPersistanceDto action = convertirActionJpaVersDto(actionJpa);

                tour.ajouterAction(action);
            }
        }

        return new LinkedList<>(toursExtraits.values());
    }

    private void ajouterJoueurMainPersistenceDto(MainPersistenceDto mainPersistenceDto, InfosJoueurJpa infosJoueurJpa) {
        JoueurJpa joueurJpa = infosJoueurJpa.getJoueurJpa();
        String nomJoueur = joueurJpa.getNomJoueur();
        JoueurPersistenceDto joueurPersistenceDto = new JoueurPersistenceDto(
                joueurJpa.getIdGenere(),
                joueurJpa.getNomJoueur(),
                joueurJpa.getNotesJoueur()
        );
        mainPersistenceDto.ajouterJoueur(joueurPersistenceDto, infosJoueurJpa.getSiege());
        mainPersistenceDto.ajouterBlinde(nomJoueur, infosJoueurJpa.getBlindePayee());
        mainPersistenceDto.ajouterBounty(nomJoueur, infosJoueurJpa.getBounty());
        mainPersistenceDto.ajouterStackDepart(nomJoueur, infosJoueurJpa.getStack());
        mainPersistenceDto.ajouterAnte(nomJoueur, infosJoueurJpa.getAntePayee());
        mainPersistenceDto.ajouterCartes(nomJoueur, infosJoueurJpa.getComboJoueurInt(), infosJoueurJpa.getComboJoueurString());
        mainPersistenceDto.ajouterGains(nomJoueur, infosJoueurJpa.getGains());
    }

    private TourPersistanceDto convertirActionJpaVersTourDto(ActionJpa actionJpa) {
        return new TourPersistanceDto(
                actionJpa.getNomTour(),
                actionJpa.getBoardString(),
                actionJpa.getBoardLong()
        );
    }

    private ActionPersistanceDto convertirActionJpaVersDto(ActionJpa actionJpa) {
        ActionPersistanceDto actionPersistanceDto = new ActionPersistanceDto(
                actionJpa.getIdGenere(),
                actionJpa.getInfosJoueurJpa().getJoueurJpa().getNomJoueur(),
                actionJpa.getNomAction(),
                actionJpa.getIdentifiantSituation(),
                actionJpa.getMontantAction(),
                actionJpa.getNumeroAction()
        );

        return actionPersistanceDto;
    }
}
