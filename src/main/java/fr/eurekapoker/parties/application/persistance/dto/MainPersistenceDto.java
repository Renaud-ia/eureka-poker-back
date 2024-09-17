package fr.eurekapoker.parties.application.persistance.dto;

import java.util.HashMap;
import java.util.List;

public class MainPersistenceDto {
    private final String idUniqueGenere;
    private final long idParse;
    private final int indexMain;
    private final String nomHero;
    private final String joueurAffiche;
    private final List<JoueurPersistenceDto> joueursPresents;
    private final List<TourPersistanceDto> tours;
    private final HashMap<JoueurPersistenceDto, String> cartesJoueursString;
    private final HashMap<JoueurPersistenceDto, Integer> cartesJoueursInt;
    public MainPersistenceDto(String idUniqueGenere,
                              long idParse,
                              int indexMain,
                              String nomHero,
                              String joueurAffiche,
                              List<JoueurPersistenceDto> joueursPresents,
                              List<TourPersistanceDto> tours,
                              HashMap<JoueurPersistenceDto, String> cartesJoueursString,
                              HashMap<JoueurPersistenceDto, Integer> cartesJoueursInt) {
        this.idUniqueGenere = idUniqueGenere;
        this.idParse = idParse;
        this.indexMain = indexMain;
        this.nomHero = nomHero;
        this.joueurAffiche = joueurAffiche;
        this.joueursPresents = joueursPresents;
        this.tours = tours;
        this.cartesJoueursString = cartesJoueursString;
        this.cartesJoueursInt = cartesJoueursInt;
    }
}
