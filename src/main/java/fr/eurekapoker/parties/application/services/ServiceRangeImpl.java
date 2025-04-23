package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.application.persistance.PersistanceRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.domaine.services.ManipulationRanges;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ServiceRangeImpl implements ServiceRangeApi {
    private final PersistanceRange persistanceRange;

    @Autowired
    public ServiceRangeImpl(PersistanceRange persistanceRange) {
        this.persistanceRange = persistanceRange;
    }


    @Override
    public void changerRangeEnregistree(UtilisateurAuthentifie utilisateurAuthentifie, String idAction, PokerRange pokerRange) throws ModificationNonAutorisee {
        UtilisateurAuthentifie proprietairePartie = verifierProprietaire(utilisateurAuthentifie, idAction);
        List<PokerRange> rangesPrecedentes = this.persistanceRange.getRangePrecedentes(idAction);
        rangesPrecedentes.add(pokerRange);

        ManipulationRanges manipulationRanges = new ManipulationRanges();
        List<PokerRange> rangesCorrigees = manipulationRanges.controleDiminutionFrequence(rangesPrecedentes);

        this.persistanceRange.modifierRangeJoueur(
                proprietairePartie,
                idAction,
                rangesCorrigees.getLast());
    }

    @Override
    public HashMap<ServiceRange.MethodeGeneration, PokerRange> recupererRanges(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idAction) throws ModificationNonAutorisee {
        verifierProprietaire(utilisateurAuthentifie, idAction);

        return this.persistanceRange.getRanges(idAction);
    }

    private UtilisateurAuthentifie verifierProprietaire(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idAction)
            throws ModificationNonAutorisee {
        UtilisateurAuthentifie proprietairePartie = this.persistanceRange.getProprietaireRange(idAction);

        if (!utilisateurAuthentifie.equals(proprietairePartie)) {
            throw new ModificationNonAutorisee("La partie n'appartient pas à cet utilisateur");
        }

        return proprietairePartie;
    }
}
