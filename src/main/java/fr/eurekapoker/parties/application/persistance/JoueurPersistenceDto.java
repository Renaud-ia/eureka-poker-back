package fr.eurekapoker.parties.application.persistance;

public class JoueurPersistenceDto {
    private final String nomJoueur;
    private final String room;
    public JoueurPersistenceDto(String nomJoueur, String room) {
        this.nomJoueur = nomJoueur;
        this.room = room;
    }
}
