package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.application.persistance.PersistanceRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;

import java.util.List;

public class PersistanceRangeBDD implements PersistanceRange {
    @Override
    public UtilisateurAuthentifie getProprietaireRange(String idRange) {
        return null;
    }

    @Override
    public void modifierRangeJoueur(UtilisateurAuthentifie utilisateurAuthentifie, String idRange, PokerRange pokerRange) throws ModificationNonAutorisee {

    }

    @Override
    public List<PokerRange> getRangePrecedentes(String idRange) {
        return List.of();
    }
}
