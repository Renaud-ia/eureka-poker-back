package fr.eurekapoker.parties.application.auth;

public class UtilisateurIdentifie {
    private final UtilisateurAuthentifie utilisateurAuthentifie;
    private final String identifiantSession;

    public UtilisateurIdentifie(UtilisateurAuthentifie utilisateurAuthentifie, String identifiantSession) {
        this.utilisateurAuthentifie = utilisateurAuthentifie;
        this.identifiantSession = identifiantSession;
    }
}
