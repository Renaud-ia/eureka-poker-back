package fr.eurekapoker.parties.infrastructure.auth;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.application.auth.UtilisateurAuthentifie;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import fr.eurekapoker.parties.application.persistance.PersistanceUtilisateur;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${audience_request_token_google}")
    private String audienceRequestTokenGoogle;

    private final JwtDecoder jwtDecoder;
    private final PersistanceUtilisateur persistanceUtilisateur;

    public AuthServiceImpl(JwtDecoder jwtDecoder, PersistanceUtilisateur persistanceUtilisateur) {
        this.jwtDecoder = jwtDecoder;
        this.persistanceUtilisateur = persistanceUtilisateur;
    }

    @Override
    public UtilisateurIdentifie getUtilisateurIdentifie(String authHeader, String idSession) {
        UtilisateurAuthentifie utilisateurAuthentifie = extraireUserFromHeaders(authHeader);

        return new UtilisateurIdentifie(utilisateurAuthentifie, idSession);
    }

    public UtilisateurAuthentifie extraireUserFromHeaders(String authHeader) {
        if (authHeader == null) return null;

        if (!authHeader.startsWith("Bearer ")) {
            throw  new TokenInvalid("Le bearer n'a pas le format attendu");
        }

        try {
            String token = authHeader.substring(7); // sans "Bearer "
            Jwt jwt = jwtDecoder.decode(token);

            if (!jwt.getAudience().contains(audienceRequestTokenGoogle)) {
                throw new TokenInvalid("Token non destiné à cette application");
            }

            String email = jwt.getClaimAsString("email");
            if (email == null) throw new RuntimeException("Email manquant dans le token");

            return persistanceUtilisateur
                    .findOrCreateByEmail(email);

        } catch (JwtException e) {
            throw  new TokenInvalid(e.getMessage());
        }
    }

}
