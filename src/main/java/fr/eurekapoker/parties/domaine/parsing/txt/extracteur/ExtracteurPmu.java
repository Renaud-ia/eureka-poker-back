package fr.eurekapoker.parties.domaine.parsing.txt.extracteur;

import fr.eurekapoker.parties.domaine.exceptions.ErreurImport;
import fr.eurekapoker.parties.domaine.exceptions.ErreurRegex;
import fr.eurekapoker.parties.domaine.parsing.dto.*;
import fr.eurekapoker.parties.domaine.parsing.dto.pmu.InfosMainPmu;
import fr.eurekapoker.parties.domaine.poker.actions.ActionPokerJoueur;
import fr.eurekapoker.parties.domaine.poker.cartes.CartePoker;

import java.math.BigDecimal;
import java.util.List;

public class ExtracteurPmu implements ExtracteurLigne {
    @Override
    public InfosTable extraireInfosTable(String ligne) throws ErreurRegex {
        return null;
    }

    public long extraireIdMain(String ligne) throws ErreurImport {
        return 0L;
    }

    @Override
    public InfosMainPmu extraireInfosMain(String ligne) throws ErreurImport {
        return null;
    }

    @Override
    public InfosJoueur extraireStackJoueur(String ligne) throws ErreurRegex {
        return null;
    }

    @Override
    public NouveauTour extraireNouveauTour(String ligne) throws ErreurRegex {
        return null;
    }

    @Override
    public ActionPokerJoueur extraireAction(String ligne) throws ErreurRegex {
        return null;
    }

    @Override
    public ResultatJoueur extraireResultat(String ligne) throws ErreurRegex {
        return null;
    }

    @Override
    public BlindeOuAnte extraireBlindeOuAnte(String s) throws ErreurRegex {
        return null;
    }

    @Override
    public List<CartePoker> extraireCartes(String ligne) throws ErreurRegex {
        return List.of();
    }

    @Override
    public InfosHero extraireInfosHero(String ligne) throws ErreurRegex {
        return null;
    }

    public int extraireNombreJoueurs() {
        return 0;
    }

    public BigDecimal extraireBigBlinde(String ligne) {
        return new BigDecimal(0);
    }
}
