package fr.eurekapoker.parties.domaine.parsing.dto;

public class NomIdPartieBetclic {
    private final String nomPartie;
    private final long idPartie;

    public NomIdPartieBetclic(String nomPartie, long idPartie) {
        this.nomPartie = nomPartie;
        this.idPartie = idPartie;
    }

    public String obtNomPartie() {
        return nomPartie;
    }

    public long obtIdPartie() {
        return idPartie;
    }
}
