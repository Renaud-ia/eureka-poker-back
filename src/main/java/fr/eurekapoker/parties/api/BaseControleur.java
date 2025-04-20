package fr.eurekapoker.parties.api;

import fr.eurekapoker.parties.application.auth.AuthService;
import fr.eurekapoker.parties.application.auth.UtilisateurIdentifie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

public abstract class BaseControleur {
    @Autowired
    protected AuthService authService;

    protected UtilisateurIdentifie extraireUtilisateurIdentifie(
            Map<String, String> headers,
            String tokenDeSession,
            HttpServletResponse response
    ) {
        tokenDeSession = this.extraireOucreerTokenDeSession(tokenDeSession, response);

        return this.authService.getUtilisateurIdentifie(headers.get("authorization"), tokenDeSession);
    }

    private String extraireOucreerTokenDeSession(String tokenDeSession, HttpServletResponse response) {
        if (tokenDeSession == null || tokenDeSession.isEmpty()) {
            tokenDeSession = UUID.randomUUID().toString();

            ResponseCookie cookie = ResponseCookie.from("tokenDeSession", tokenDeSession)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(3))
                    .sameSite("Lax")
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        return tokenDeSession;
    }
}
