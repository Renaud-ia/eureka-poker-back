package fr.eurekapoker.parties.domaine;

import fr.eurekapoker.parties.domaine.exceptions.ErreurLectureFichier;
import fr.eurekapoker.parties.domaine.exceptions.RoomNonPriseEnCharge;
import fr.eurekapoker.parties.domaine.poker.parties.FormatPoker;
import fr.eurekapoker.parties.domaine.poker.parties.InfosPartiePoker;
import fr.eurekapoker.parties.domaine.poker.parties.JoueurPoker;
import fr.eurekapoker.parties.domaine.poker.mains.MainPoker;

import java.util.List;

public class DomaineServiceImportXml implements DomaineServiceImport {
    public DomaineServiceImportXml(String lignesFichier) throws RoomNonPriseEnCharge {
        throw new RoomNonPriseEnCharge("Les formats XML ne sont pas pris en charge");
    }
    @Override
    public void lancerImport() {

    }
}
