package fr.eurekapoker.parties.domaine.parsing.dto;

public class InfosTableWinamax extends InfosTable {
    private final String nomTable;
    private final int nombreJoueurs;
    public InfosTableWinamax(String nomTable, int nombreJoueurs) {
        super();
        this.nomTable = nomTable;
        this.nombreJoueurs = nombreJoueurs;
    }

    public String obtNomTable() {
        return nomTable;
    }

    public int obtNombreJoueurs() {
        return nombreJoueurs;
    }
}
