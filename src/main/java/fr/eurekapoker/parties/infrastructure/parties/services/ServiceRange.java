package fr.eurekapoker.parties.infrastructure.parties.services;

import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.infrastructure.parties.entites.ActionJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.PokerRangeJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.RangeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                .idGenere(UUID.randomUUID().toString())
                .combos(pokerRange.obtenirCombos())
                .build();

        pokerRangeJpa.ajouterAction(actionJpa);

        return pokerRangeJpa;
    }
}
