package fr.eurekapoker.parties.infrastructure.auth;

public class TokenInvalid extends RuntimeException {
    public TokenInvalid(String message) {
        super(message);
    }
}
