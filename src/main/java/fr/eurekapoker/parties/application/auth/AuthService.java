package fr.eurekapoker.parties.application.auth;

public interface AuthService {
    public boolean userEstIdentifie();
    public boolean estUtilisateur(UtilisateurIdentifie utilisateurIdentifie);
    public UtilisateurIdentifie getUtilisateur();
}
