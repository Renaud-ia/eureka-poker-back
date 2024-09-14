package fr.eurekapoker.parties.domaine;

import fr.eurekapoker.parties.domaine.exceptions.RoomNonPriseEnCharge;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;

import java.util.List;

class DomaineServiceImportXml implements DomaineServiceImport {
    public DomaineServiceImportXml(String lignesFichier) throws RoomNonPriseEnCharge {
        throw new RoomNonPriseEnCharge("Les formats XML ne sont pas pris en charge");
    }
    @Override
    public void lancerImport() {

    }

    @Override
    public FormatPoker obtFormatPoker() {
        return null;
    }

    @Override
    public List<MainPoker> obtMainsExtraites() {
        return null;
    }

    @Override
    public List<JoueurPoker> obtJoueursInitiaux() {
        return null;
    }
}
