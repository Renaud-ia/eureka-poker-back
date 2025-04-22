package fr.eurekapoker.parties.application.services;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.application.persistance.PersistanceRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.domaine.services.ManipulationRanges;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ModifierRangeImpl implements ModifierRange {
    private final PersistanceRange persistanceRange;

    @Autowired
    public ModifierRangeImpl(PersistanceRange persistanceRange) {
        this.persistanceRange = persistanceRange;
    }


    @Override
    public void changerRangeEnregistree(UtilisateurAuthentifie utilisateurAuthentifie, String idRange, PokerRange pokerRange) throws ModificationNonAutorisee {
        UtilisateurAuthentifie proprietairePartie = this.persistanceRange.getProprietaireRange(idRange);

        if (!utilisateurAuthentifie.equals(proprietairePartie)) {
            throw new ModificationNonAutorisee("La partie n'appartient pas à cet utilisateur");
        }

        List<PokerRange> rangesPrecedentes = this.persistanceRange.getRangePrecedentes(idRange);
        rangesPrecedentes.add(pokerRange);

        ManipulationRanges manipulationRanges = new ManipulationRanges();
        List<PokerRange> rangesCorrigees = manipulationRanges.controleDiminutionFrequence(rangesPrecedentes);

        this.persistanceRange.modifierRangeJoueur(
                proprietairePartie,
                idRange,
                rangesCorrigees.getLast());
    }
}
