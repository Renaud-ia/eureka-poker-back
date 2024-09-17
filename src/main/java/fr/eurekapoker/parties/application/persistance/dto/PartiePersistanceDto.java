package fr.eurekapoker.parties.application.persistance.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PartiePersistanceDto {
    private final String idUniqueGenere;
    private final long idParse;
    private final String nomRoom;
    private final String formatPoker;
    private final String typeJeu;
    private final LocalDateTime date;
    private final String nomPartie;
    private final int nombreSieges;
    private final int nombreMains;
    private final List<MainPersistenceDto> mainPersistence;

    public PartiePersistanceDto(String idUniqueGenere,
                                long idParse,
                                String nomRoom,
                                String formatPoker,
                                String typeJeu,
                                LocalDateTime date,
                                String nomPartie,
                                int nombreSieges,
                                int nombreMains,
                                List<MainPersistenceDto> mainPersistence) {
        this.idUniqueGenere = idUniqueGenere;
        this.idParse = idParse;
        this.nomRoom = nomRoom;
        this.formatPoker = formatPoker;
        this.typeJeu = typeJeu;
        this.date = date;
        this.nomPartie = nomPartie;
        this.nombreSieges = nombreSieges;
        this.nombreMains = nombreMains;
        this.mainPersistence = mainPersistence;
    }
}
