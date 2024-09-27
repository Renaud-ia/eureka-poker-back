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
        INCONNU;

        @Override
        public String toString() {
            switch (this) {
                case CASH_GAME:
                    return "Cash-Game";
                case SPIN:
                    return "Spin&Go";
                case MTT:
                    return "MTT";
                case INCONNU:
                    return "Inconnu";
                default:
                    return super.toString(); // En cas d'erreur, mais normalement jamais atteint
            }
        }
    }


}
