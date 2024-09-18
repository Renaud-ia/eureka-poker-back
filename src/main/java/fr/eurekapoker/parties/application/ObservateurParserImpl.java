package fr.eurekapoker.parties.application;

import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.persistance.PersistanceParties;
import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.parsing.ObservateurParser;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.parsing.dto.ResultatJoueur;
import fr.eurekapoker.parties.domaine.parsing.dto.StackJoueur;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.BoardPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.cartes.ComboReel;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;
import fr.eurekapoker.parties.domaine.poker.moteur.EncodageSituation;
import fr.eurekapoker.parties.domaine.poker.parties.InfosPartiePoker;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.parties.RoomPoker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * fait le lien entre le parsing et la persistance en créant les DTO appropriés
 * génère les UUID
 */
public class ObservateurParserImpl implements ObservateurParser {
    private PartiePersistanceDto partiePersistanceDto;
    private final EncodageSituation encodageSituation;
    private int indexMain;
    private MainPersistenceDto derniereMain;
    private TourPersistanceDto dernierTour;
    public ObservateurParserImpl() {
        this.indexMain = 0;
        this.encodageSituation = new EncodageSituation();
    }

    @Override
    public void fixInfosPartie(InfosPartiePoker infosPartiePoker) {
        UUID idUnique = UUID.randomUUID();

        partiePersistanceDto = new PartiePersistanceDto(
                idUnique.toString(),
                infosPartiePoker.getIdParse(),
                infosPartiePoker.getNomRoom(),
                infosPartiePoker.getFormatPoker(),
                infosPartiePoker.getTypeJeu(),
                infosPartiePoker.getDate(),
                infosPartiePoker.getNomPartie(),
                infosPartiePoker.getNombreSieges()
        );
    }

    @Override
    public void ajouterMain(MainPoker mainPoker) throws ErreurLectureFichier {
        if (partiePersistanceDto == null) throw new ErreurLectureFichier("La partie n'a pas été fixée");
        UUID idUniqueGenere = UUID.randomUUID();
        MainPersistenceDto mainPersistenceDto = new MainPersistenceDto(
                idUniqueGenere.toString(),
                mainPoker.obtIdParse(),
                indexMain++
        );
        partiePersistanceDto.ajouterMain(mainPersistenceDto);
        derniereMain = mainPersistenceDto;
    }

    @Override
    public void ajouterJoueur(StackJoueur stackJoueur) {
        // todo il manque le siège
        String nomJoueur = stackJoueur.obtJoueur();

        JoueurPersistenceDto nouveauJoueur = new JoueurPersistenceDto(
                nomJoueur
        );
        this.derniereMain.ajouterJoueur(nouveauJoueur);
    }

    @Override
    public void ajouterBlinde(String nomJoueur, BigDecimal montant) {
        this.derniereMain.ajouterBlinde(nomJoueur, montant);
    }

    @Override
    public void ajouterAnte(String nomJoueur, BigDecimal montant) {
        this.derniereMain.ajouterAnte(nomJoueur, montant);
    }

    @Override
    public void ajouterCartesHero(List<CartePoker> cartesHero) {
        ComboReel comboReel = new ComboReel(cartesHero);
        this.derniereMain.ajouterComboHero(comboReel.toInt(), comboReel.toString());
    }

    @Override
    public void ajouterTour(NouveauTour tourParse) {
        BoardPoker boardPoker = new BoardPoker(tourParse.obtCartesExtraites());
        TourPersistanceDto nouveauTour = new TourPersistanceDto(
                tourParse.obtRound().toString(),
                boardPoker.toString(),
                boardPoker.asLong()
        );
        // IMPORTANT : on notifie qu'il y a des joueurs en moins sur la table
        int nombreJoueursFormat = this.partiePersistanceDto.obtNombreSieges();
        int nombreJoueursTable = this.derniereMain.obtNombreJoueurs();

        while(nombreJoueursTable++ < nombreJoueursFormat) {
            encodageSituation.ajouterAction(ActionPoker.TypeAction.FOLD);
        }

        this.derniereMain.ajouterTour(nouveauTour);
        this.dernierTour = nouveauTour;
    }

    @Override
    public void ajouterAction(ActionPokerJoueur actionPoker) {
        this.encodageSituation.ajouterAction(actionPoker.getTypeAction());

        ActionPersistanceDto nouvelleAction = new ActionPersistanceDto(
                actionPoker.getTypeAction().toString(),
                this.encodageSituation.obtIdentifiantSituation(),
                actionPoker.obtMontantAction()
        );
        this.dernierTour.ajouterAction(actionPoker.getNomJoueur(), nouvelleAction);
    }

    @Override
    public void ajouterGains(String nomJoueur, BigDecimal montant) {
        this.derniereMain.ajouterGains(nomJoueur, montant);
    }

    @Override
    public void ajouterCartes(String nomJoueur, List<CartePoker> cartePokers) {
        ComboReel comboReel = new ComboReel(cartePokers);
        this.derniereMain.ajouterCartes(nomJoueur, comboReel.toInt(), comboReel.toString());
    }

    @Override
    public PartiePersistanceDto obtPartie() {
        return partiePersistanceDto;
    }

    @Override
    public String getIdUniquePartie() {
        return partiePersistanceDto.obtIdUnique();
    }

    @Override
    public void mainTerminee() {
        calculerLaValueDesActions();
    }

    @Override
    public ResumePartieDto obtResumePartie() {
        return new ResumePartieDto(
                this.partiePersistanceDto.obtMains().getFirst().obtNomsJoueursPresents(),
                this.derniereMain.obtNomHero()
        );
    }

    private void calculerLaValueDesActions() {

    }
}
