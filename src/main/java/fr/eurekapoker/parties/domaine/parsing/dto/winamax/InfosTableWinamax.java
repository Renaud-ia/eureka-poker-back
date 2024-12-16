package fr.eurekapoker.parties.domaine.parsing.dto.winamax;

import fr.eurekapoker.parties.domaine.parsing.dto.InfosTable;

public class InfosTableWinamax extends InfosTable {
    private final int nombreJoueurs;
    private final int positionDealer;
    public InfosTableWinamax(String nomTable, int nombreJoueurs, int positionDealer) {
        super(nomTable);
        this.nombreJoueurs = nombreJoueurs;
        this.positionDealer = positionDealer;
    }

    public int obtNombreJoueurs() {
        return nombreJoueurs;
    }

    public int obtPositionDealer() {
        return positionDealer;
    }


}
