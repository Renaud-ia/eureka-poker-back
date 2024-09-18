package fr.eurekapoker.parties.domaine.poker.parties;

public class FormatPoker {
    private final Variante variante;
    private final TypeTable typeTable;
    public FormatPoker(Variante variante, TypeTable typeTable) {
        this.variante = variante;
        this.typeTable = typeTable;
    }

    public TypeTable obtTypeTable() {
        return typeTable;
    }

    public String obtVariante() {
        return variante.toString();
    }

    public enum Variante {
        HOLDEM_NO_LIMIT,
        INCONNU
    }
    public enum TypeTable {
        CASH_GAME,
        SPIN,
        MTT,
        INCONNU
    }


}
