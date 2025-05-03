package fr.eurekapoker.parties.application.auth;

public interface AuthService {
    public UtilisateurIdentifie getUtilisateurIdentifie(String authHeader, String idSession);
}
