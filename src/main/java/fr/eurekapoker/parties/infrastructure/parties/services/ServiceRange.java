package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PostflopRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PreflopRange;
import fr.eurekapoker.parties.infrastructure.PersistanceRangeBDD;
import fr.eurekapoker.parties.infrastructure.parties.entites.ActionJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.PokerRangeJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.RangeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ServiceRange {
    @Autowired
    private RangeRepository rangeRepository;

    @Transactional
    public PokerRangeJpa sauvegarderNouvelleRange(PokerRange pokerRange, ActionJpa actionJpa) {
        PokerRangeJpa pokerRangeJpa = this.mapperRange(pokerRange, actionJpa);
        this.rangeRepository.save(pokerRangeJpa);

        return pokerRangeJpa;
    }

    private PokerRangeJpa mapperRange(PokerRange pokerRange, ActionJpa actionJpa) {
        PokerRangeJpa pokerRangeJpa = PokerRangeJpa.builder()
                .dateCreation(LocalDateTime.now())
                .derniereModification(LocalDateTime.now())
                .combos(pokerRange.obtenirCombos())
                .methodeGeneration(MethodeGeneration.SAISIE_USER.toString())
                .build();

        if (pokerRange instanceof PreflopRange) {
            pokerRangeJpa.setTypeRange(PersistanceRangeBDD.TypeRange.PREFLOP.toString());
        }

        else if (pokerRange instanceof PostflopRange) {
            pokerRangeJpa.setTypeRange(PersistanceRangeBDD.TypeRange.POSTFLOP.toString());
        }

        else throw new IllegalArgumentException("Type de range non pris en charge");

        pokerRangeJpa.ajouterAction(actionJpa);

        return pokerRangeJpa;
    }

    public enum MethodeGeneration {
        SAISIE_USER,
    }
}
