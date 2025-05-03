package fr.eurekapoker.parties.infrastructure;

import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.exceptions.ErreurConsultationRange;
import fr.eurekapoker.parties.application.exceptions.ModificationNonAutorisee;
import fr.eurekapoker.parties.application.persistance.PersistanceRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PokerRange;
import fr.eurekapoker.parties.domaine.poker.ranges.PreflopRange;
import fr.eurekapoker.parties.infrastructure.parties.entites.ActionJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.PokerRangeJpa;
import fr.eurekapoker.parties.infrastructure.parties.entites.UtilisateurJpa;
import fr.eurekapoker.parties.infrastructure.parties.repositories.ActionRepository;
import fr.eurekapoker.parties.infrastructure.parties.repositories.RangeRepository;
import fr.eurekapoker.parties.infrastructure.parties.services.ServiceRange;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class PersistanceRangeBDD implements PersistanceRange {
    @Autowired
    private RangeRepository rangeRepository;

    @Autowired
    private ActionRepository actionRepository;

    @Override
    public UtilisateurAuthentifie getProprietaireRange(String idAction) {
        ActionJpa actionJpa = this.actionRepository.findByIdGenere(idAction);
        UtilisateurJpa proprietaire = actionJpa.getUtilisateur();

        return new UtilisateurAuthentifie(
                proprietaire.getIdGenere(),
                proprietaire.getMailUtilisateur(),
                proprietaire.isMailVerifie(),
                UtilisateurAuthentifie.StatutMembre.valueOf(proprietaire.getStatutMembre()),
                proprietaire.getNomFamille(),
                proprietaire.getPrenom()
        );
    }

    @Transactional
    @Override
    public void modifierRangeJoueur(
            UtilisateurAuthentifie utilisateurAuthentifie,
            String idAction,
            PokerRange pokerRange
    ) throws ModificationNonAutorisee {
        PokerRangeJpa pokerRangeJpa = this.rangeRepository.trouverParIdActionEtMethodeGeneration(
                idAction,
                ServiceRange.MethodeGeneration.SAISIE_USER.toString()
        );

        pokerRangeJpa.setCombos(pokerRange.obtenirCombos());

        this.rangeRepository.save(pokerRangeJpa);
    }

    @Transactional
    @Override
    public List<PokerRange> getRangePrecedentes(String idAction) {
        List<PokerRange> rangesPrecedentes = new ArrayList<>();

        List<ActionJpa> actionsPrecedentes = this.actionRepository.trouverActionsPrecedentes(idAction);

        for (ActionJpa action: actionsPrecedentes) {
            List<PokerRangeJpa> ranges = action.getRanges();

            List<PokerRangeJpa> rangesUsers = new ArrayList<>();

            for (PokerRangeJpa rangeJpa : ranges) {
                if (Objects.equals(rangeJpa.getMethodeGeneration(), ServiceRange.MethodeGeneration.SAISIE_USER.toString())) {
                    rangesUsers.add(rangeJpa);
                }
            }

            rangesPrecedentes.add(extraireRangeJpa(rangesUsers.getFirst()));
        }

        return rangesPrecedentes;
    }

    @Transactional
    @Override
    public HashMap<ServiceRange.MethodeGeneration, PokerRange> getRanges(String idAction) {
        HashMap<ServiceRange.MethodeGeneration, PokerRange> ranges = new HashMap<>();
        ActionJpa actionJpa = this.actionRepository.findByIdGenere(idAction);

        for (PokerRangeJpa pokerRangeJpa: actionJpa.getRanges()) {
            try {
                ranges.put(
                        ServiceRange.MethodeGeneration.valueOf(pokerRangeJpa.getMethodeGeneration()),
                        extraireRangeJpa(pokerRangeJpa)
                );
            }
            catch (IllegalArgumentException e) {
                throw new ErreurConsultationRange("Le type de range stockée est inconnu" + pokerRangeJpa.getMethodeGeneration());
            }
        }

        return ranges;
    }


    public PokerRange extraireRangeJpa(PokerRangeJpa pokerRangeJpa) {
        if (Objects.equals(pokerRangeJpa.getTypeRange(), TypeRange.PREFLOP.toString())) {
            return new PreflopRange(new HashMap<>(pokerRangeJpa.getCombos()));
        }

        else if (Objects.equals(pokerRangeJpa.getTypeRange(), TypeRange.PREFLOP.toString())) {
            return new PreflopRange(new HashMap<>(pokerRangeJpa.getCombos()));
        }

        else throw new ErreurConsultationRange("Le type de range stockée est inconnu" + pokerRangeJpa.getTypeRange());
    }

    public enum TypeRange {
        PREFLOP,
        POSTFLOP
    }

}
