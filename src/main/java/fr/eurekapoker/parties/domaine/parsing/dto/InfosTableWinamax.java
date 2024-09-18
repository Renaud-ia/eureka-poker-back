package fr.eurekapoker.parties.domaine.parsing.dto;

public class InfosTableWinamax extends InfosTable {
    private final String nomTable;
    private final int nombreJoueurs;
    private final int positionDealer;
    public InfosTableWinamax(String nomTable, int nombreJoueurs, int positionDealer) {
        super();
        this.nomTable = nomTable;
        this.nombreJoueurs = nombreJoueurs;
        this.positionDealer = positionDealer;
    }

    public String obtNomTable() {
        return nomTable;
    }

    public int obtNombreJoueurs() {
        return nombreJoueurs;
    }

    @Override
    public int obtPositionDealer() {
        return positionDealer;
    }
}
