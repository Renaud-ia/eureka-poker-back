package fr.eurekapoker.parties.infrastructure.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@Profile("!test")
public class JwtConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public JwtDecoder jwtDecoder() {
        // Cette méthode télécharge automatiquement les JWK via /.well-known/openid-configuration
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }
}