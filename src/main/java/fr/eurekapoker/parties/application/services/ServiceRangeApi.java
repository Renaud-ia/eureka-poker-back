package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceRange;

import java.util.HashMap;

public interface ServiceRangeApi {
    public void changerRangeEnregistree(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idAction,
            PokerRange pokerRange
    ) throws ModificationNonAutorisee;

    public HashMap<ServiceRange.MethodeGeneration, PokerRange> recupererRanges(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idAction
    ) throws ModificationNonAutorisee;
}
