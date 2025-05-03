package fr.eurekapoker.parties.application.exceptions;

public class ErreurAuthentification extends RuntimeException {
    public ErreurAuthentification(String message) {
        super(message);
    }
}
