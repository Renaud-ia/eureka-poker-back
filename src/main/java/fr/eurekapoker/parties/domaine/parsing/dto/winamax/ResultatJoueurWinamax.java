package fr.eurekapoker.parties.domaine.parsing.dto.winamax;

import fr.eurekapoker.parties.domaine.parsing.dto.ResultatJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.math.BigDecimal;
import java.util.List;

public class ResultatJoueurWinamax extends ResultatJoueur {
    private final List<CartePoker> cartes;
    public ResultatJoueurWinamax(String nomJoueur, float gains, List<CartePoker> cartes) {
        super(nomJoueur, gains);
        this.cartes = cartes;
    }
    public List<CartePoker> obtCartesJoueur() {
        return cartes;
    }
}
