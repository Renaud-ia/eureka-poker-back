package fr.eurekapoker.parties.application.persistance;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceRange;

import java.util.HashMap;
import java.util.List;

public interface PersistanceRange {
    public UtilisateurAuthentifie getProprietaireRange(String idAction);
    public void modifierRangeJoueur(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idAction,
            PokerRange pokerRange) throws ModificationNonAutorisee;

    List<PokerRange> getRangePrecedentes(String idAction);
    public HashMap<ServiceRange.MethodeGeneration, PokerRange> getRanges(String idAction);
}
