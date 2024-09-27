package fr.eurekapoker.parties.domaine.exceptions;

public abstract class ErreurImport extends Exception {
    public ErreurImport(String message) {
        super(message);
    }
}
