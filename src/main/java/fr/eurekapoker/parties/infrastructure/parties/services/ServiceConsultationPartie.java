package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.infrastructure.parties.entites.*;
import fr.eurekapoker.parties.application.exceptions.PartieNonTrouvee;
import fr.eurekapoker.parties.infrastructure.parties.repositories.MainRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.PartieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceConsultationPartie {
    @Autowired
    private PartieRepository partieRepository;
    @Autowired
    private MainRepository mainRepository;
    public PartiePersistanceDto recupererMains(String idUniqueGenere, int premiereMain, int deniereMain)
            throws PartieNonTrouvee {
        PartieJpa partieJpa =
                partieRepository.trouverParIdGenereAvecIndexMains(
                        idUniqueGenere,
                        premiereMain,
                        deniereMain);

        if (partieJpa != null) return convertirPartieJpaVersDto(partieJpa);

        throw new PartieNonTrouvee("La partie n'a pas été trouvée avec id:" + idUniqueGenere);
    }

    private PartiePersistanceDto convertirPartieJpaVersDto(PartieJpa partieJpa) {
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
                partieJpa.getNombreMains()
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

        for (TourJpa tourJpa: mainJpa.getToursJpa()) {
            mainPersistenceDto.ajouterTour(convertirTourJpaVersDto(tourJpa));
        }

        mainPersistenceDto.ajouterPositionDealer(mainJpa.getPositionDealer());

        return mainPersistenceDto;
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

    private TourPersistanceDto convertirTourJpaVersDto(TourJpa tourJpa) {
        TourPersistanceDto tourPersistanceDto = new TourPersistanceDto(
                tourJpa.getNomTour(),
                tourJpa.getBoardString(),
                tourJpa.getBoardLong()
        );

        for (ActionJpa actionJpa: tourJpa.getActionsJpas()) {
            tourPersistanceDto.ajouterAction(convertirActionJpaVersDto(actionJpa));
        }
        return tourPersistanceDto;
    }

    private ActionPersistanceDto convertirActionJpaVersDto(ActionJpa actionJpa) {
        ActionPersistanceDto actionPersistanceDto = new ActionPersistanceDto(
                actionJpa.getInfosJoueurJpa().getJoueurJpa().getNomJoueur(),
                actionJpa.getNomAction(),
                actionJpa.getIdentifiantSituation(),
                actionJpa.getMontantAction(),
                actionJpa.getNumeroAction()
        );

        return actionPersistanceDto;
    }
}
