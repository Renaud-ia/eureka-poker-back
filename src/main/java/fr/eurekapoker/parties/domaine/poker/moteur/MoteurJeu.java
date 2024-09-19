package fr.eurekapoker.parties.domaine.poker.moteur;

import fr.eurekapoker.parties.domaine.poker.actions.ActionPoker;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

/**
 * calcule le pot et le pot bounty
 */
public class MoteurJeu {
    private final EncodageSituation encodageSituation;
    private final HashMap<String, BigDecimal> stackDepart;
    private final HashMap<String, BigDecimal> investi;
    private final HashMap<String, BigDecimal> bountyDepart;
    private BigDecimal pot;
    private BigDecimal potBounty;
    public MoteurJeu() {
        this.encodageSituation = new EncodageSituation();
        this.stackDepart = new HashMap<>();
        this.investi = new HashMap<>();
        this.bountyDepart = new HashMap<>();
        this.pot = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
    }

    public void ajouterJoueur(String nomJoueur, BigDecimal stackDepart, BigDecimal bounty) {
        this.stackDepart.put(nomJoueur, stackDepart);
        this.bountyDepart.put(nomJoueur, bounty);
    }
    public void ajouterJoueurManquant() {
        this.encodageSituation.ajouterAction(ActionPoker.TypeAction.FOLD);
    }

    public void ajouterAction(ActionPokerJoueur actionPoker) {
        this.encodageSituation.ajouterAction(actionPoker.getTypeAction());
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
        return this.encodageSituation.obtIdSituation();
    }

    public BigDecimal obtPot() {
        return pot;
    }

    public BigDecimal obtPotBounty() {
        this.actualiserPotBounty();
        return potBounty;
    }


}
