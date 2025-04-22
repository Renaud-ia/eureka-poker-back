package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;

public interface ModifierRange {
    public void changerRangeEnregistree(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idRange,
            PokerRange pokerRange
    ) throws ModificationNonAutorisee;
}
