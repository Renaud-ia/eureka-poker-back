package fr.eurekapoker.parties.application.persistance;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;

import java.util.List;

public interface PersistanceRange {
    public UtilisateurAuthentifie getProprietaireRange(String idRange);
    public void modifierRangeJoueur(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idRange,
            PokerRange pokerRange) throws ModificationNonAutorisee;

    List<PokerRange> getRangePrecedentes(String idRange);
}
