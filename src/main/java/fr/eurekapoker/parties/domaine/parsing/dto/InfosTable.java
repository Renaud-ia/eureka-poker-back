package fr.eurekapoker.parties.domaine.parsing.dto;

public class InfosTable {
    private final String nomTable;

    public InfosTable(String nomTable) {
        this.nomTable = nomTable;
    }

    public String obtNomTable() {
        return nomTable;
    }
}
