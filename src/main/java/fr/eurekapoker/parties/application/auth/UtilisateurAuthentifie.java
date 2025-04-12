package fr.eurekapoker.parties.application.auth;

public class UtilisateurAuthentifie {
    private final String idUtilisateur;
    private final String nomUtilisateur;
    private final StatutMembre statutMembre;

    public UtilisateurAuthentifie(String idUtilisateur, String nomUtilisateur, StatutMembre statutMembre) {
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.statutMembre = statutMembre;
    }

    public enum StatutMembre {
        UTILISATEUR,
        ADMIN,
    }
}
