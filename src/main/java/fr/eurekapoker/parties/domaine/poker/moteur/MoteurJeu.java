package fr.eurekapoker.parties.domaine.poker.moteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.mains.TourPoker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Objects;

/**
 * calcule le pot et le pot bounty
 */
public class MoteurJeu {
    private EncodageSituation encodageSituation;
    private final HashMap<String, BigDecimal> stackDepart;
    private final HashMap<String, BigDecimal> investi;
    private final HashMap<String, BigDecimal> bountyDepart;
    private BigDecimal pot;
    private BigDecimal potBounty;
    public MoteurJeu() {
        this.stackDepart = new HashMap<>();
        this.investi = new HashMap<>();
        this.bountyDepart = new HashMap<>();
        this.pot = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
    }

    public void nouveauRound(TourPoker.RoundPoker round) {
        this.encodageSituation = new EncodageSituation(stackDepart.size(), round);
    }

    public void ajouterJoueur(String nomJoueur, BigDecimal stackDepart, BigDecimal bounty) {
        this.stackDepart.put(nomJoueur, stackDepart);
        if (bounty == null) bounty = new BigDecimal(0);
        this.bountyDepart.put(nomJoueur, bounty);
        this.investi.put(nomJoueur, new BigDecimal(0));
    }

    public void ajouterJoueurManquant() throws ErreurLectureFichier {
        try {
            this.encodageSituation.ajouterAction(ActionPoker.TypeAction.FOLD);
        }
        catch (Exception e) {
            throw new ErreurLectureFichier("Trop d'actions pour être encodé");
        }
    }

    public void ajouterAction(ActionPokerJoueur actionPoker) throws ErreurLectureFichier {
        try {
            this.encodageSituation.ajouterAction(actionPoker.getTypeAction());
        }
        catch (Exception e) {
            throw new ErreurLectureFichier("Trop d'actions pour être encodé");
        }
        this.pot = this.pot.add(actionPoker.obtMontantAction());
        this.incrementerMontantInvesti(actionPoker.getNomJoueur(), actionPoker.obtMontantAction());
    }

    public void ajouterBlinde(String nomJoueur, BigDecimal montant) {
        this.incrementerMontantInvesti(nomJoueur, montant);
        this.pot = this.pot.add(montant).setScale(2, RoundingMode.HALF_UP);
    }

    public void ajouterAnte(String nomJoueur, BigDecimal montant) {
        this.incrementerMontantInvesti(nomJoueur, montant);
        this.pot = this.pot.add(montant);
    }

    private void actualiserPotBounty() {
        this.potBounty = new BigDecimal(0);

        for (String joueurAvecInvestissement: this.investi.keySet()) {
            BigDecimal bountyDepart = this.bountyDepart.getOrDefault(joueurAvecInvestissement, new BigDecimal(0));
            BigDecimal stackDepart = this.stackDepart.get(joueurAvecInvestissement).setScale(2, RoundingMode.HALF_UP);
            BigDecimal montantInvesti = this.investi.get(joueurAvecInvestissement).setScale(2, RoundingMode.HALF_UP);
            this.potBounty = this.potBounty.add(
                    bountyDepart
                    .multiply(montantInvesti.divide(stackDepart, RoundingMode.HALF_UP)))
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }

    private void incrementerMontantInvesti(String nomJoueur, BigDecimal montantInvesti) {
        this.investi.put(nomJoueur,
                this.investi.getOrDefault(
                        nomJoueur,
                        new BigDecimal(0)).add(montantInvesti).setScale(2, RoundingMode.HALF_UP));
    }

    public long obtIdentifiantSituation() {
        return this.encodageSituation.toLong();
    }

    public BigDecimal obtPot() {
        return pot;
    }

    public BigDecimal obtPotBounty() {
        this.actualiserPotBounty();
        return potBounty;
    }

    public BigDecimal obtStackEffectif(String nomJoueur) {
        BigDecimal stackJoueur = stackDepart.get(nomJoueur).subtract(investi.get(nomJoueur));
        BigDecimal stackEffectif = new BigDecimal(0);

        for (String nomAdversaire : stackDepart.keySet()) {
            if (Objects.equals(nomAdversaire, nomJoueur)) continue;
            BigDecimal stackAdversaire = stackDepart.get(nomAdversaire).subtract(investi.get(nomAdversaire));
            if (stackAdversaire.compareTo(stackEffectif) > 0) stackEffectif = stackAdversaire;
        }

        if (stackEffectif.compareTo(stackJoueur) > 0) {
            stackEffectif = stackJoueur;
        }

        return stackEffectif;
    }

    public boolean seraAllIn(String nomJoueur, BigDecimal montantAction) {
        BigDecimal stackRestant = stackDepart.get(nomJoueur).subtract(investi.get(nomJoueur));
        return stackRestant.compareTo(montantAction) <= 0;
    }
}
