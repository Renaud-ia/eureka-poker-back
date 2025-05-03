package fr.eurekapoker.parties.application.imports;

import fr.eurekapoker.parties.application.api.dto.ParametresImport;
import fr.eurekapoker.parties.application.api.dto.ResumePartieDto;
import fr.eurekapoker.parties.application.persistance.dto.*;
import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.JoueurNonExistant;
import fr.eurekapoker.parties.domaine.parsing.dto.NouveauTour;
import fr.eurekapoker.parties.domaine.parsing.dto.InfosJoueur;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.BoardPoker;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;
import fr.eurekapoker.parties.domaine.poker.cartes.ComboReel;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;
import fr.eurekapoker.parties.domaine.poker.moteur.MoteurJeu;
import fr.eurekapoker.parties.domaine.poker.parties.infos.InfosPartiePoker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * fait le lien entre le parsing et la persistance en créant les DTO appropriés
 * génère les UUID
 * calcule la value des actions
 */
public class ConstructeurPersistenceDto implements ConstructeurPersistence {
    private final ParametresImport parametresImport;
    private final PartiePersistanceDto partiePersistanceDto;
    private final MoteurJeu moteurJeu;
    private int indexMain;
    private MainPersistenceDto derniereMain;
    private TourPersistanceDto dernierTour;
    private final HashMap<String, Integer> nombreActionsParJoueur;
    private int numeroAction;

    public ConstructeurPersistenceDto(ParametresImport parametresImport) {
        this.moteurJeu = new MoteurJeu();
        this.parametresImport = parametresImport;
        this.partiePersistanceDto = new PartiePersistanceDto();

        this.indexMain = 0;
        this.numeroAction = 0;
        this.nombreActionsParJoueur = new HashMap<>();
    }

    public ConstructeurPersistenceDto(ParametresImport parametresImport, MoteurJeu moteurJeu) {
        this.moteurJeu = moteurJeu;
        this.parametresImport = parametresImport;
        this.partiePersistanceDto = new PartiePersistanceDto();

        this.indexMain = 0;
        this.numeroAction = 0;
        this.nombreActionsParJoueur = new HashMap<>();
    }

    @Override
    public void fixInfosPartie(InfosPartiePoker infosPartiePoker) {
        UUID idUnique = UUID.randomUUID();

        partiePersistanceDto.fixerValeurs(
                idUnique.toString(),
                infosPartiePoker.getIdParse(),
                parametresImport.getJoueursAnonymes(),
                infosPartiePoker.getNomRoom(),
                infosPartiePoker.getFormatPoker(),
                infosPartiePoker.getTypeJeu(),
                infosPartiePoker.getFormatSpecialRoom(),
                infosPartiePoker.getStackEnEuros(),
                infosPartiePoker.getDate(),
                infosPartiePoker.getNomPartie(),
                infosPartiePoker.getBuyIn(),
                infosPartiePoker.getNombreSieges()
        );
    }

    @Override
    public void ajouterMain(MainPoker mainPoker) throws ErreurLectureFichier {
        MainPersistenceDto mainPersistenceDto = new MainPersistenceDto(
                mainPoker.obtIdParse(),
                indexMain++
        );
        partiePersistanceDto.ajouterMain(mainPersistenceDto);
        derniereMain = mainPersistenceDto;
        this.numeroAction = 0;
    }

    @Override
    public void supprimerDerniereMain() {
        partiePersistanceDto.supprimerDerniereMain();
        this.derniereMain = null;
    }

    @Override
    public void ajouterMontantBB(BigDecimal montantBB) {
        this.derniereMain.ajouterMontantBB(montantBB);
    }

    @Override
    public void ajouterJoueur(InfosJoueur infosJoueur) {
        String nomJoueur = infosJoueur.obtJoueur();

        JoueurPersistenceDto nouveauJoueur = new JoueurPersistenceDto(
                null,
                nomJoueur,
                "");
        this.derniereMain.ajouterJoueur(nouveauJoueur, infosJoueur.obtSiege());
        this.derniereMain.ajouterStackDepart(nomJoueur, infosJoueur.obtStack());
        this.derniereMain.ajouterBounty(nomJoueur, infosJoueur.obtBounty());
        this.nombreActionsParJoueur.put(nomJoueur, 0);
        this.moteurJeu.ajouterJoueur(nomJoueur, infosJoueur.obtStack(), infosJoueur.obtBounty());
    }

    @Override
    public void ajouterBlinde(String nomJoueur, BigDecimal montant) {
        this.derniereMain.ajouterBlinde(nomJoueur, montant);
        this.moteurJeu.ajouterBlinde(nomJoueur, montant);
    }

    @Override
    public void ajouterAnte(String nomJoueur, BigDecimal montant) {
        this.derniereMain.ajouterAnte(nomJoueur, montant);
        this.moteurJeu.ajouterAnte(nomJoueur, montant);
    }

    @Override
    public void ajouterHero(String nomHero, List<CartePoker> cartesHero) {
        String comboAsString = convertirCartesString(cartesHero);
        ComboReel comboReel = new ComboReel(cartesHero);
        this.partiePersistanceDto.fixNomHero(nomHero);
        this.derniereMain.ajouterInfosHero(nomHero, comboReel.toInt(), comboAsString);
    }

    @Override
    public void ajouterTour(NouveauTour tourParse) throws ErreurLectureFichier {
        BoardPoker boardPoker = new BoardPoker(tourParse.obtCartesExtraites());
        TourPersistanceDto nouveauTour = new TourPersistanceDto(
                tourParse.obtRound().toString(),
                boardPoker.toString(),
                boardPoker.asLong()
        );

        // IMPORTANT : on notifie qu'il y a des joueurs en moins sur la table
        int nombreJoueursFormat = this.partiePersistanceDto.obtNombreSieges();
        int nombreJoueursTable = this.derniereMain.obtNombreJoueurs();

        this.moteurJeu.nouveauRound(tourParse.obtRound());
        while(nombreJoueursTable++ < nombreJoueursFormat) {
            this.moteurJeu.ajouterJoueurManquant();
        }

        this.derniereMain.ajouterTour(nouveauTour);
        this.dernierTour = nouveauTour;
    }

    @Override
    public void ajouterAction(ActionPokerJoueur actionPoker) throws ErreurLectureFichier, JoueurNonExistant {
        if (this.dernierTour == null) throw new ErreurLectureFichier("Aucune main existante");

        BigDecimal montantTotalAction = actionPoker.obtMontantAction();

        if (actionPoker.montantPositif() && !actionPoker.estMontantTotal()) {
            montantTotalAction = montantTotalAction
                        .add(this.moteurJeu.obtMontantInvestiCeTour(actionPoker.getNomJoueur()));
        }

        ActionPersistanceDto nouvelleAction = new ActionPersistanceDto(
                actionPoker.getNomJoueur(),
                actionPoker.getTypeAction().toString(),
                this.moteurJeu.obtIdentifiantSituation(),
                montantTotalAction,
                moteurJeu.obtPot(),
                moteurJeu.obtPotBounty(),
                moteurJeu.obtStackEffectif(actionPoker.getNomJoueur()),
                moteurJeu.seraAllIn(actionPoker.getNomJoueur(), montantTotalAction),
                this.numeroAction++
        );
        this.dernierTour.ajouterAction(nouvelleAction);

        // on incrémente le nombre d'actions du joueur
        this.nombreActionsParJoueur.put(actionPoker.getNomJoueur(),
                nombreActionsParJoueur.get(actionPoker.getNomJoueur()) + 1);
        // doit rester à la fin car change le calcul
        this.moteurJeu.ajouterAction(actionPoker);
    }

    @Override
    public void ajouterGains(String nomJoueur, BigDecimal montant) {
        this.derniereMain.ajouterGains(nomJoueur, montant);
    }

    @Override
    public void ajouterCartes(String nomJoueur, List<CartePoker> cartePokers) {
        String cartesAsString = convertirCartesString(cartePokers);
        ComboReel comboReel = new ComboReel(cartePokers);
        this.derniereMain.ajouterCartes(nomJoueur, comboReel.toInt(), cartesAsString);
    }

    private String convertirCartesString(List<CartePoker> cartePokers) {
        StringBuilder cartesAsString = new StringBuilder();

        for (CartePoker cartePoker: cartePokers) {
            cartesAsString.append(cartePoker.toString());
        }

        return cartesAsString.toString();
    }

    @Override
    public PartiePersistanceDto obtPartie() {
        return partiePersistanceDto;
    }

    @Override
    public void ajouterPositionDealer(int positionDealer) {
        this.derniereMain.ajouterPositionDealer(positionDealer);
    }

    @Override
    public void partieTerminee() {
        this.partiePersistanceDto.partieTerminee();
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
                this.partiePersistanceDto.obtIdUnique(),
                this.partiePersistanceDto.obtMains().getFirst().obtNomsJoueursPresents(),
                this.derniereMain.obtNomHero(),
                this.partiePersistanceDto.obtNomRoom()
        );
    }

    private void calculerLaValueDesActions() {
        for (String nomJoueur: nombreActionsParJoueur.keySet()) {
            int nombreActions = nombreActionsParJoueur.get(nomJoueur);
            derniereMain.fixNombreActionsDuJoueur(nomJoueur, nombreActions);
        }
    }
}
